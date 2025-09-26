package com.tonthepduybao.api.service.debt;

import com.tonthepduybao.api.app.exception.model.ExistenceException;
import com.tonthepduybao.api.app.exception.model.SystemException;
import com.tonthepduybao.api.app.helper.MessageHelper;
import com.tonthepduybao.api.app.utils.DataBuilder;
import com.tonthepduybao.api.app.utils.ExcelUtils;
import com.tonthepduybao.api.app.utils.TimeUtils;
import com.tonthepduybao.api.app.utils.Utils;
import com.tonthepduybao.api.entity.*;
import com.tonthepduybao.api.entity.enumeration.ECustomerType;
import com.tonthepduybao.api.entity.enumeration.EDebtStatus;
import com.tonthepduybao.api.entity.enumeration.EType;
import com.tonthepduybao.api.mapper.DebtMapper;
import com.tonthepduybao.api.mapper.dataset.DebtDataset;
import com.tonthepduybao.api.model.CommonData;
import com.tonthepduybao.api.model.PagingWrapper;
import com.tonthepduybao.api.model.debt.*;
import com.tonthepduybao.api.repository.*;
import com.tonthepduybao.api.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * DebtServiceImpl
 *
 * @author khal
 * @since 2023/07/09
 */
@Service
@RequiredArgsConstructor
public class DebtServiceImpl implements DebtService {

    private final int START_ROW = 7;
    private final int MAX_ROW = 506;

    private final MessageHelper messageHelper;

    private final DebtMapper debtMapper;

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final CustomerRepository customerRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyDetailRepository propertyDetailRepository;
    private final DebtRepository debtRepository;
    private final DebtDetailRepository debtDetailRepository;
    private final DebtPropertyRepository debtPropertyRepository;
    private final DebtDetailPropertyDetailRepository debtDetailPropertyDetailRepository;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(final DebtForm form) {
        String id = form.getId();
        if (debtRepository.existsById(id))
            throw new ExistenceException(List.of("Mã công nợ", id));

        Long currentUserId = SecurityUtils.getCurrentUserId(true);
        String now = TimeUtils.nowStr();
        Customer customer = getCustomer(form.getCustomerId());

        // Save debt
        Debt debt = Debt.builder()
                .id(id)
                .date(form.getDate())
                .status(EDebtStatus.ACTIVE)
                .type(EType.valueOf(form.getType()))
                .customer(customer)
                .createdBy(currentUserId)
                .createdAt(now)
                .updatedBy(currentUserId)
                .updatedAt(now)
                .build();
        Debt savedDebt = debtRepository.saveAndFlush(debt);

        // Save debt details
        saveDebtDetail(form.getItems(), Collections.emptyList(), savedDebt);

        // Save debt properties
        saveDebtProperties(form.getPropertyIds(), savedDebt);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public List<String> createFromFile(final MultipartFile file) {
        List<String> errors = new ArrayList<>();

        Pattern DATE_PATTERN = Pattern.compile("^\\d{8}$");
        Pattern LONG_VALUE_PATTERN = Pattern.compile("^\\d+(__).*$");
        Pattern STRING_VALUE_PATTERN = Pattern.compile("^(\\d|\\w)+(__).*$");

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row0 = sheet.getRow(0);
            String id = ExcelUtils.getCellValue(workbook, row0, 1);
            if (!StringUtils.hasLength(id))
                errors.add("[Cell B1] Mã công nợ là trường bắt buộc");
            else if (debtRepository.existsById(id))
                errors.add("[Cell B1] Mã công nợ [" + id + "] đã tồn tại");

            XSSFRow row1 = sheet.getRow(1);
            String date = ExcelUtils.getCellValue(workbook, row1, 1);
            if (!DATE_PATTERN.matcher(date).matches())
                errors.add("[Cell B2] Ngày tạo công nợ là trường bắt buộc và có định dạng YYYYMMDD");

            XSSFRow row2 = sheet.getRow(2);
            String customerIdValue = ExcelUtils.getCellValue(workbook, row2, 1);
            Long customerId = null;
            if (LONG_VALUE_PATTERN.matcher(customerIdValue).matches()) {

                try {
                    customerId = Long.parseLong(customerIdValue.split("__")[0]);
                } catch (NumberFormatException e) {
                    errors.add("[Cell B4] Nhà cung cấp không chính xác, vui lòng không sửa giá trị thuộc tính mặc định.");
                }
            } else errors.add("[Cell B4] Nhà cung cấp không chính xác, vui lòng không sửa giá trị thuộc tính mặc định.");

            XSSFRow row3 = sheet.getRow(3);
            EType type = null;
            try {
                type = EType.valueOf(ExcelUtils.getCellValue(workbook, row3, 1));
            } catch (IllegalArgumentException e) {
                errors.add("[Cell B3] Danh mục không chính xác, vui lòng không sửa giá trị sau khi chọn.");
            }

            XSSFRow row4 = sheet.getRow(4);
            String propertyValue = ExcelUtils.getCellValue(workbook, row4, 1);
            List<Long> propertyIds = new ArrayList<>();
            if (!StringUtils.hasLength(propertyValue)) {
                errors.add("[Cell B5] Thuộc tính không chính xác, vui lòng không sửa giá trị thuộc tính mặc định.");
            } else {
                propertyIds = Stream.of(ExcelUtils.getCellValue(workbook, row4, 1).split(","))
                        .map(Long::parseLong)
                        .toList();
            }

            if (Objects.nonNull(type) && !CollectionUtils.isEmpty(propertyIds)) {
                List<DebtDetailForm> debtDetailForms = new ArrayList<>();

                for (var rowIndex = START_ROW; rowIndex < MAX_ROW; rowIndex++) {
                    XSSFRow bodyRow = sheet.getRow(rowIndex);

                    int colIndex = 1;

                    String branchValue = ExcelUtils.getCellValue(workbook, bodyRow, colIndex++);
                    if (!LONG_VALUE_PATTERN.matcher(branchValue).matches())
                        continue;
                    Long branch = Long.valueOf(branchValue.split("__")[0]);

                    String name = ExcelUtils.getCellValue(workbook, bodyRow, colIndex++);
                    if (!StringUtils.hasLength(name))
                        continue;

                    String note = ExcelUtils.getCellValue(workbook, bodyRow, colIndex++);

                    Map<Long, Long> properties = new HashMap<>();
                    for (final Long propId : propertyIds) {
                        String propertyDetailValue = ExcelUtils.getCellValue(workbook, bodyRow, colIndex++);
                        if (LONG_VALUE_PATTERN.matcher(propertyDetailValue).matches()) {
                            Long propertyDetailId = Long.valueOf(propertyDetailValue.split("__")[0]);
                            properties.put(propId, propertyDetailId);
                        }
                    }

                    String weightValue = ExcelUtils.getCellValue(workbook, bodyRow, colIndex++);
                    double weight = StringUtils.hasLength(weightValue)
                            ? new BigDecimal(weightValue).setScale(2, RoundingMode.FLOOR).doubleValue()
                            : 0;

                    String quantityValue = ExcelUtils.getCellValue(workbook, bodyRow, colIndex++);
                    int quantity = StringUtils.hasLength(quantityValue) ? Integer.parseInt(quantityValue) : 0;

                    String avgProportionValue = ExcelUtils.getCellValue(workbook, bodyRow, colIndex++);
                    double avgProportion = StringUtils.hasLength(avgProportionValue)
                            ? new BigDecimal(avgProportionValue).setScale(2, RoundingMode.FLOOR).doubleValue()
                            : 0;

                    String unitPriceValue = ExcelUtils.getCellValue(workbook, bodyRow, colIndex++);
                    BigDecimal unitPrice = StringUtils.hasLength(unitPriceValue) ? new BigDecimal(unitPriceValue) : BigDecimal.ZERO;

                    String totalUnitPriceValue = ExcelUtils.getCellValue(workbook, bodyRow, colIndex++);
                    BigDecimal totalUnitPrice = StringUtils.hasLength(totalUnitPriceValue) ? new BigDecimal(totalUnitPriceValue) : BigDecimal.ZERO;

                    String totalPriceValue = ExcelUtils.getCellValue(workbook, bodyRow, colIndex);
                    BigDecimal totalPrice = StringUtils.hasLength(totalPriceValue) ? new BigDecimal(totalPriceValue) : BigDecimal.ZERO;

                    DebtDetailForm debtDetailForm = DebtDetailForm.builder()
                            .id(null)
                            .name(name)
                            .note(note)
                            .properties(properties)
                            .weight(weight)
                            .quantity(quantity)
                            .avgProportion(avgProportion)
                            .unitPrice(unitPrice)
                            .totalPrice(totalPrice)
                            .totalUnitPrice(totalUnitPrice)
                            .branch(branch)
                            .build();
                    debtDetailForms.add(debtDetailForm);
                }

                if (CollectionUtils.isEmpty(debtDetailForms))
                    errors.add("Có thể dữ liệu của sản phẩm bị lỗi hoặc bạn chưa thêm sản phẩm nào, vui lòng kiểm tra lại danh sách sản phẩm của bạn.");

                if (CollectionUtils.isEmpty(errors)) {
                    DebtForm debtForm = DebtForm.builder()
                            .id(id)
                            .date(date)
                            .propertyIds(propertyIds)
                            .type(type.name())
                            .customerId(customerId)
                            .items(debtDetailForms)
                            .build();
                    create(debtForm);
                }
            }

            return errors;
        } catch (IOException e) {
            throw new SystemException();
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(final DebtForm form) {
        Debt debt = getDebt(form.getId());

        Long currentUserId = SecurityUtils.getCurrentUserId(true);
        String now = TimeUtils.nowStr();

        // Save debt
        debt.setDate(form.getDate());
        debt.setType(EType.valueOf(form.getType()));
        debt.setUpdatedBy(currentUserId);
        debt.setUpdatedAt(now);

        Debt savedDebt = debtRepository.saveAndFlush(debt);

        // Save debt details
        saveDebtDetail(form.getItems(), form.getDeletedItems(), savedDebt);

        // Save debt properties
        saveDebtProperties(form.getPropertyIds(), savedDebt);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(final String id) {
        Debt debt = getDebt(id);

        debtPropertyRepository.deleteAllByDebt(debt);
        debt.getDebtDetails().forEach(debtDetailPropertyDetailRepository::deleteAllByDebtDetail);

        debtDetailRepository.deleteAllByDebt(debt);
        debtRepository.delete(debt);
    }

    @Override
    public DebtInfoData get(final String id) {
        Debt debt = getDebt(id);

        String createdBy = userRepository.getFullNameById(debt.getCreatedBy());
        String updatedBy = userRepository.getFullNameById(debt.getUpdatedBy());
        BigDecimal totalPrice = debtDetailRepository.countTotalPrice(debt);
        BigDecimal totalUnitPrice = debtDetailRepository.countTotalUnitPrice(debt);

        List<CommonData> properties = debt.getDebtProperties().stream()
                .map(item -> CommonData.builder()
                        .id(item.getProperty().getId())
                        .name(item.getProperty().getName())
                        .build())
                .toList();

        List<DebtDetailData> debtDetails = debt.getDebtDetails().stream()
                .map(item -> {
                    List<DebtDetailPropertyData> propertyDetails = item.getDebtDetailProperties()
                            .stream()
                            .map(ddp -> {
                                PropertyDetail propertyDetail = ddp.getPropertyDetail();

                                return DebtDetailPropertyData.builder()
                                        .id(propertyDetail.getId())
                                        .name(propertyDetail.getName())
                                        .property(propertyDetail.getProperty())
                                        .build();
                            }).sorted(Comparator.comparingInt(a -> a.getProperty().getOrderBy()))
                            .toList();

                    DebtDetailData debtDetail = DataBuilder.to(item, DebtDetailData.class);
                    debtDetail.setPropertyDetails(propertyDetails);

                    return debtDetail;
                }).toList();

        DebtInfoData result = DataBuilder.to(debt, DebtInfoData.class);
        result.setCreatedBy(createdBy);
        result.setUpdatedBy(updatedBy);
        result.setTotalPrice(totalPrice);
        result.setTotalUnitPrice(totalUnitPrice);
        result.setStatus(debt.getStatus().name());
        result.setType(debt.getType().name());
        result.setProperties(properties);
        result.setDebtDetails(debtDetails);

        return result;
    }

    @Override
    public DebtListData getAll(final String search, final String fromDate, final String toDate,
                                final List<String> type, final List<Long> customerId, final int page, final int pageSize) {
        String searchParam = "%" + search.toLowerCase() + "%";
        String status = EDebtStatus.ACTIVE.name();

        int offset = (page - 1) * pageSize;
        long totalItems = debtMapper.countDebt(status, searchParam, fromDate, toDate, type, customerId);
        int totalPages = (int) Math.ceil(totalItems / (pageSize * 1.0));

        List<DebtDataset> datasets = debtMapper.selectDebt(status, searchParam, fromDate, toDate, type, customerId, pageSize, offset);
        List<DebtData> debtDatasets = datasets
                .stream()
                .map(item -> {
                    Customer customer = getCustomer(item.getCustomerId());

                    String createdBy = userRepository.getFullNameById(item.getCreatedBy());
                    String updatedBy = userRepository.getFullNameById(item.getUpdatedBy());

                    DebtData debtData = DataBuilder.to(item, DebtData.class);
                    debtData.setCreatedBy(createdBy);
                    debtData.setUpdatedBy(updatedBy);
                    debtData.setCustomer(customer);

                    return debtData;
                }).toList();
        BigDecimal totalPrice = datasets.stream()
                .map(DebtDataset::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PagingWrapper allDebt = PagingWrapper.builder()
                .data(debtDatasets)
                .page(page)
                .pageSize(pageSize)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .build();

        return DebtListData.builder()
                .allDebt(allDebt)
                .totalPrice(totalPrice)
                .build();
    }

    @Override
    public ResponseEntity<Resource> download(final List<String> ids) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            CellStyle boldStyle = ExcelUtils.getBoldStyle(workbook);
            CellStyle normalStyle = ExcelUtils.getNormalStyle(workbook);
            CellStyle boldRedStyle = ExcelUtils.getBoldRedStyle(workbook);

            ids.forEach(debtId -> {
                DebtInfoData debt = get(debtId);
                EType type = EType.valueOf(debt.getType());
                List<CommonData> properties = debt.getProperties();

                XSSFSheet sheet = workbook.createSheet(debtId);

                int rowIndex = 0;
                Row row1 = sheet.createRow(rowIndex++);
                ExcelUtils.setCell(row1, 0, "Ngày tạo", normalStyle);
                ExcelUtils.setCell(row1, 1, TimeUtils.convertDate(debt.getDate()), boldStyle);

                if (!EType.SCREW.equals(EType.valueOf(debt.getType()))) {
                    ExcelUtils.setCell(row1, 4, "Tổng nhập cây/mét:", boldStyle);
                    ExcelUtils.setCell(row1, 5, Utils.formatCurrency(debt.getTotalUnitPrice().doubleValue()), boldRedStyle);
                }

                Row row2 = sheet.createRow(rowIndex++);
                ExcelUtils.setCell(row2, 0, "Phân loại", normalStyle);
                ExcelUtils.setCell(row2, 1, EType.valueOf(debt.getType()).getName(), boldStyle);
                ExcelUtils.setCell(row2, 4, "Tổng nhập:", boldStyle);
                ExcelUtils.setCell(row2, 5, Utils.formatCurrency(debt.getTotalPrice().doubleValue()), boldRedStyle);

                Row row3 = sheet.createRow(rowIndex++);
                ExcelUtils.setCell(row3, 0, "Nhà cung cấp", normalStyle);
                ExcelUtils.setCell(row3, 1, debt.getCustomer().getName(), boldStyle);

                rowIndex+=2;

                List<String> headerLabels = getHeaderLabels(properties, type, false, true);
                Row headerRow = sheet.createRow(rowIndex++);
                for (var i = 0; i < headerLabels.size(); i++)
                    ExcelUtils.setCell(headerRow, i, headerLabels.get(i), boldStyle);

                List<DebtDetailData> debtDetails = debt.getDebtDetails();
                for (var i = 0; i < debtDetails.size(); i++) {
                    DebtDetailData debtDetail = debtDetails.get(i);

                    int bodyColIndex = 0;
                    Row bodyRow = sheet.createRow(rowIndex++);
                    ExcelUtils.setCell(bodyRow, bodyColIndex++, i + 1, normalStyle);
                    ExcelUtils.setCell(bodyRow, bodyColIndex++, debtDetail.getBranch().getName(), normalStyle);
                    ExcelUtils.setCell(bodyRow, bodyColIndex++, debtDetail.getName(), normalStyle);
                    ExcelUtils.setCell(bodyRow, bodyColIndex++, debtDetail.getNote(), normalStyle);

                    for (CommonData prop: properties) {
                        DebtDetailPropertyData debtDetailProperty = debtDetail.getPropertyDetails()
                                .stream()
                                .filter(propDetail -> prop.id().equals(propDetail.getProperty().getId()))
                                .findFirst()
                                .orElse(null);
                        ExcelUtils.setCell(bodyRow, bodyColIndex++, Objects.isNull(debtDetailProperty) ? "" : debtDetailProperty.getName(), normalStyle);
                    }

                    if (!EType.SCREW.equals(type))
                        ExcelUtils.setCell(bodyRow, bodyColIndex++, debtDetail.getWeight(), normalStyle);

                    ExcelUtils.setCell(bodyRow, bodyColIndex++, debtDetail.getQuantity(), normalStyle);

                    if (!EType.SCREW.equals(EType.valueOf(debt.getType())))
                        ExcelUtils.setCell(bodyRow, bodyColIndex++, debtDetail.getAvgProportion(), normalStyle);

                    ExcelUtils.setCell(bodyRow, bodyColIndex++, Utils.formatCurrency(debtDetail.getUnitPrice().doubleValue()), normalStyle);

                    if (!EType.SCREW.equals(EType.valueOf(debt.getType())))
                        ExcelUtils.setCell(bodyRow, bodyColIndex++, Utils.formatCurrency(debtDetail.getTotalUnitPrice().doubleValue()), normalStyle);

                    ExcelUtils.setCell(bodyRow, bodyColIndex, Utils.formatCurrency(debtDetail.getTotalPrice().doubleValue()), normalStyle);
                }

                for (int i = 0; i < 100; i++)
                    sheet.autoSizeColumn(i);
            });

            return ExcelUtils.write(workbook, TimeUtils.nowStr() + "_CONG_NO.xlsx");
        } catch (IOException e) {
            throw new SystemException();
        }
    }

    @Override
    public ResponseEntity<Resource> downloadTemplate(final String type, final List<Long> propertyIds) {
    try (XSSFWorkbook workbook = new XSSFWorkbook();
         ByteArrayOutputStream out = new ByteArrayOutputStream()) {

        // ==== build dữ liệu properties ====
        List<Property> properties = propertyIds.stream().map(this::getProperty).toList();
        List<CommonData> propertyList = properties.stream()
                .map(p -> CommonData.builder().id(p.getId()).name(p.getName()).build())
                .toList();

        // ==== tạo style ====
        CellStyle boldStyle = ExcelUtils.getBoldStyle(workbook);
        CellStyle boldRedStyle = ExcelUtils.getBoldRedStyle(workbook);
        CellStyle normalStyle = ExcelUtils.getNormalStyle(workbook);
        CellStyle normalRedStyle = ExcelUtils.getNormalRedStyle(workbook);
        CellStyle placeholderStyle = ExcelUtils.getPlaceholderStyle(workbook);

        // ==== tạo sheet ====
        XSSFSheet sheet = workbook.createSheet("Mau_cong_no");

        // header info rows
        Row row0 = sheet.createRow(0);
        ExcelUtils.setCell(row0, 0, "Mã công nợ:", boldStyle);
        ExcelUtils.setCell(row0, 1, "", placeholderStyle);
        ExcelUtils.setCell(row0, 4, "(*) là các trường bắt buộc", normalRedStyle);

        Row row1 = sheet.createRow(1);
        ExcelUtils.setCell(row1, 0, "Ngày tạo:", boldStyle);
        ExcelUtils.setCell(row1, 1, "<Định dạng: YYYYMMDD>", placeholderStyle);

        Row row2 = sheet.createRow(2);
        ExcelUtils.setCell(row2, 0, "Nhà cung cấp:", boldStyle);
        ExcelUtils.setCell(row2, 1, "<Chọn nhà cung cấp>", placeholderStyle);

        Row row3 = sheet.createRow(3);
        ExcelUtils.setCell(row3, 0, "Danh mục:", boldStyle);
        ExcelUtils.setCell(row3, 1, type, placeholderStyle);

        Row row4 = sheet.createRow(4);
        ExcelUtils.setCell(row4, 0, "Thuộc tính:", boldStyle);
        ExcelUtils.setCell(row4, 1, properties.stream()
                .map(item -> String.valueOf(item.getId()))
                .collect(Collectors.joining(",")), placeholderStyle);

        // header row
        List<String> headerLabels = getHeaderLabels(propertyList, EType.IRON, true, false);
        Row headerRow = sheet.createRow(6);
        for (int i = 0; i < headerLabels.size(); i++) {
            ExcelUtils.setCell(headerRow, i, headerLabels.get(i),
                    i == 1 || i == 2 ? boldRedStyle : boldStyle);
        }

        // body rows (demo 10 hàng, tránh quá nặng)
        for (int rowIndex = START_ROW; rowIndex < START_ROW + 10; rowIndex++) {
            Row bodyRow = sheet.createRow(rowIndex);
            int colIndex = 0;
            ExcelUtils.setCell(bodyRow, colIndex++, String.valueOf(rowIndex - START_ROW + 1), normalStyle);
            ExcelUtils.setCell(bodyRow, colIndex++, "<Chọn chi nhánh>", placeholderStyle);
            ExcelUtils.setCell(bodyRow, colIndex++, "", normalStyle);
            ExcelUtils.setCell(bodyRow, colIndex++, "", normalStyle);
            for (int i = 0; i < properties.size(); i++) {
                ExcelUtils.setCell(bodyRow, colIndex++, "<Chọn thuộc tính>", placeholderStyle);
            }
        }

        // autosize
        for (int i = 0; i < headerLabels.size() + properties.size() + 5; i++) {
            sheet.autoSizeColumn(i);
        }

        // ==== write workbook ====
        workbook.write(out);
        ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=MAU_CONG_NO.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);

    } catch (IOException e) {
        throw new RuntimeException("Error generating Excel template", e);
    }
}

    /*public ResponseEntity<Resource> downloadTemplate(final String type, final List<Long> propertyIds) {
        try {
            List<Property> properties = propertyIds.stream().map(this::getProperty).toList();
            List<CommonData> propertyList = properties
                    .stream()
                    .map(property -> CommonData.builder()
                            .id(property.getId())
                            .name(property.getName())
                            .build())
                    .toList();

            XSSFWorkbook workbook = new XSSFWorkbook();
            CellStyle boldStyle = ExcelUtils.getBoldStyle(workbook);
            CellStyle boldRedStyle = ExcelUtils.getBoldRedStyle(workbook);
            CellStyle normalStyle = ExcelUtils.getNormalStyle(workbook);
            CellStyle normalRedStyle = ExcelUtils.getNormalRedStyle(workbook);
            CellStyle placeholderStyle = ExcelUtils.getPlaceholderStyle(workbook);

            XSSFSheet sheet = workbook.createSheet("Mẫu công nợ");

            Row row0 = sheet.createRow(0);
            ExcelUtils.setCell(row0, 0, "Mã công nợ:", boldStyle);
            ExcelUtils.setCell(row0, 1, "", placeholderStyle); // format this cell to string
            ExcelUtils.setCell(row0, 4, "(*) là các trường bắt buộc", normalRedStyle);

            Row row1 = sheet.createRow(1);
            ExcelUtils.setCell(row1, 0, "Ngày tạo:", boldStyle);
            ExcelUtils.setCell(row1, 1, "<Định dạng: YYYYMMDD>", placeholderStyle);

            Row row2 = sheet.createRow(2);
            ExcelUtils.setCell(row2, 0, "Nhà cung cấp:", boldStyle);
            ExcelUtils.setCell(row2, 1, "<Chọn nhà cung cấp>", placeholderStyle);

            Row row3 = sheet.createRow(3);
            ExcelUtils.setCell(row3, 0, "Danh mục:", boldStyle);
            ExcelUtils.setCell(row3, 1, type, placeholderStyle);

            Row row4 = sheet.createRow(4);
            ExcelUtils.setCell(row4, 0, "Thuộc tính:", boldStyle);
            ExcelUtils.setCell(row4, 1, properties.stream()
                    .map(item -> String.valueOf(item.getId()))
                    .collect(Collectors.joining(",")), placeholderStyle);

            List<String> headerLabels = getHeaderLabels(propertyList, EType.IRON, true, false);
            Row headerRow = sheet.createRow(6);
            for (var i = 0; i < headerLabels.size(); i++)
                ExcelUtils.setCell(headerRow, i, headerLabels.get(i), i == 1 || i == 2 ? boldRedStyle : boldStyle);

            for (var rowIndex = START_ROW; rowIndex < MAX_ROW; rowIndex++) {
                Row bodyRow = sheet.createRow(rowIndex);
                int colIndex = 0;

                ExcelUtils.setCell(bodyRow, colIndex++, String.valueOf(rowIndex - START_ROW + 1), normalStyle);       // STT
                ExcelUtils.setCell(bodyRow, colIndex++, "<Chọn chi nhánh>", placeholderStyle);                  // Chi nhánh
                ExcelUtils.setCell(bodyRow, colIndex++, "", normalStyle);                                       // Tên
                ExcelUtils.setCell(bodyRow, colIndex++, "", normalStyle);                                       // Ghi chú

                // Thuộc tính
                for (var i = 0; i < properties.size(); i++)
                    ExcelUtils.setCell(bodyRow, colIndex++, "<Chọn thuộc tính>", placeholderStyle);

                ExcelUtils.setCell(bodyRow, colIndex++, "", normalStyle);
                ExcelUtils.setCell(bodyRow, colIndex++, "", normalStyle);
                ExcelUtils.setCell(bodyRow, colIndex++, "", normalStyle);
                ExcelUtils.setCell(bodyRow, colIndex++, "", normalStyle);
                ExcelUtils.setCell(bodyRow, colIndex++, "", normalStyle);
                ExcelUtils.setCell(bodyRow, colIndex, "", normalStyle);
            }

            // Create dropdown list constraint
            int hiddenSheetIndex = 1;
            List<String> customers = customerRepository.searchCustomer("%%", List.of(ECustomerType.SUPPLIER))
                    .stream()
                    .map(item -> item.getId() + "__" + item.getName())
                    .toList();
            ExcelUtils.setDropdownConstraint(workbook, sheet, "customers_hidden", hiddenSheetIndex++, 2, 2, 1, 1, customers);

            List<String> branches = branchRepository.findAll()
                    .stream()
                    .map(item -> item.getId() + "__" + item.getName())
                    .toList();
            ExcelUtils.setDropdownConstraint(workbook, sheet, "branches_hidden", hiddenSheetIndex++, START_ROW, MAX_ROW, 1, 1, branches);

            for (var i = 0; i < properties.size(); i++) {
                Property property = properties.get(i);
                List<String> propertyDetails = propertyDetailRepository.findAllByProperty(property)
                        .stream()
                        .map(item -> item.getId() + "__" + item.getName())
                        .toList();
                ExcelUtils.setDropdownConstraint(workbook, sheet, "property_details_" + property.getId() + "_hidden", hiddenSheetIndex++, START_ROW, MAX_ROW, 4 + i, 4 + i, propertyDetails);
            }

            for (int i = 0; i < 100; i++)
                sheet.autoSizeColumn(i);

            return ExcelUtils.write(workbook, "MAU_CONG_NO.xlsx");
        } catch (IOException e) {
            throw new SystemException();
        }
    }*/

    // Private
    private Customer getCustomer(final Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(messageHelper.buildDataNotFound("Nhà cung cấp với ID =", customerId));
    }

    private Debt getDebt(final String debtId) {
        return debtRepository.findById(debtId)
                .orElseThrow(messageHelper.buildDataNotFound("Công nợ với ID =", debtId));
    }

    private DebtDetail getDebtDetail(final Long debtDetailId) {
       return debtDetailRepository.findById(debtDetailId)
                .orElseThrow(messageHelper.buildDataNotFound("Sản phẩm trong công nợ với ID =", debtDetailId));
    }

    private Property getProperty(final Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(messageHelper.buildDataNotFound("Thuộc tính với ID =", propertyId));
    }

    private void saveDebtDetail(final List<DebtDetailForm> items, final List<Long> deletedItems, final Debt savedDebt) {
        if (!CollectionUtils.isEmpty(deletedItems)) {
            deletedItems.forEach(id -> {
                DebtDetail debtDetail = getDebtDetail(id);
                debtDetailPropertyDetailRepository.deleteAllByDebtDetail(debtDetail);
                debtDetailRepository.delete(debtDetail);
            });
        }


        List<Branch> branches = branchRepository.findAll();
        items.forEach(item -> {
            Long id = item.getId();

            DebtDetail debtDetail = Objects.nonNull(id) ? getDebtDetail(id) : new DebtDetail();
            Branch branch = branches.stream()
                            .filter(b -> Objects.equals(b.getId(), item.getBranch()))
                            .findFirst()
                            .orElseThrow(messageHelper.buildDataNotFound("Chi nhánh với ID =", item.getBranch()));

            debtDetail.setName(item.getName());
            debtDetail.setNote(item.getNote());
            debtDetail.setUnitPrice(item.getUnitPrice());
            debtDetail.setQuantity(item.getQuantity());
            debtDetail.setWeight(item.getWeight());
            debtDetail.setAvgProportion(item.getAvgProportion());
            debtDetail.setTotalPrice(item.getTotalPrice());
            debtDetail.setTotalUnitPrice(item.getTotalUnitPrice());
            debtDetail.setBranch(branch);
            debtDetail.setDebt(savedDebt);

            DebtDetail savedDebtDetail = debtDetailRepository.saveAndFlush(debtDetail);

            debtDetailPropertyDetailRepository.deleteAllByDebtDetail(savedDebtDetail);
            for (var property : item.getProperties().entrySet()) {
                Long propertyDetailId = property.getValue();
                PropertyDetail propertyDetail = propertyDetailRepository.findById(propertyDetailId)
                        .orElseThrow(messageHelper.buildDataNotFound("Giá trị thuộc tính với ID =", propertyDetailId));

                DebtDetailPropertyDetail debtDetailPropertyDetail = DebtDetailPropertyDetail.builder()
                        .debtDetail(savedDebtDetail)
                        .propertyDetail(propertyDetail)
                        .build();
                debtDetailPropertyDetailRepository.saveAndFlush(debtDetailPropertyDetail);
            }
        });
    }

    private void saveDebtProperties(final List<Long> propertyIds, final Debt savedDebt) {
        debtPropertyRepository.deleteAllByDebt(savedDebt);
        propertyIds.forEach(propertyId -> {
            Property property = getProperty(propertyId);
            DebtProperty debtProperty = DebtProperty.builder()
                    .property(property)
                    .debt(savedDebt)
                    .build();
            debtPropertyRepository.saveAndFlush(debtProperty);
        });
    }

    private List<String> getHeaderLabels(final List<CommonData> properties, final EType type, final boolean hasPrefixId, final boolean isView) {
        StringBuilder headerLabelBuilder = new StringBuilder()
                .append("STT,")
                .append(isView ? "" : "* ").append("Chi nhánh,")
                .append(isView ? "" : "* ").append("Tên,")
                .append("Ghi chú,");
        properties.forEach(item -> headerLabelBuilder.append(hasPrefixId ? item.id() + "__" + item.name() : item.name()).append(","));
        if (!EType.SCREW.equals(type))
            headerLabelBuilder
                    .append("Số KG,")
                    .append("Số lượng cây/mét,");
        else headerLabelBuilder.append("Số lượng,");

        if (!EType.SCREW.equals(type)) headerLabelBuilder.append("TT TB,");
        headerLabelBuilder.append("Đơn giá,");
        if (!EType.SCREW.equals(type)) headerLabelBuilder.append("Đơn giá cây/mét,");
        headerLabelBuilder.append("Thành tiền");

        return List.of(headerLabelBuilder.toString().split(","));
    }
}
