package com.tonthepduybao.api.service.product;

import com.tonthepduybao.api.app.helper.MessageHelper;
import com.tonthepduybao.api.app.utils.DataBuilder;
import com.tonthepduybao.api.app.utils.TimeUtils;
import com.tonthepduybao.api.entity.Branch;
import com.tonthepduybao.api.entity.Product;
import com.tonthepduybao.api.entity.ProductPropertyDetail;
import com.tonthepduybao.api.entity.PropertyDetail;
import com.tonthepduybao.api.entity.enumeration.EProductStatus;
import com.tonthepduybao.api.entity.enumeration.EType;
import com.tonthepduybao.api.model.product.*;
import com.tonthepduybao.api.repository.*;
import com.tonthepduybao.api.security.utils.SecurityUtils;
import com.tonthepduybao.api.entity.ProductCategory;
import com.tonthepduybao.api.model.productCategory.ProductCategoryData;
import com.tonthepduybao.api.model.productCategory.ProductCategoryForm;  
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * ProductServiceImpl
 *
 * @author Nhan
 * @since 2025/07/23
 */
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private static final org.apache.poi.ss.usermodel.DataFormatter DATA_FORMATTER = new org.apache.poi.ss.usermodel.DataFormatter();

    private final MessageHelper messageHelper;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final PropertyDetailRepository propertyDetailRepository;
    private final ProductPropertyDetailRepository productPropertyDetailRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;

    @Override
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void create(final ProductForm form) {
        Long currentUserId = SecurityUtils.getCurrentUserId(true);
        String now = TimeUtils.nowStr();

        Branch branch = getBranch(form.branch());

        Product product = Product.builder()
                .name(form.name())
                .date(form.date())
                .quantity(form.quantity())
                .size(form.size())
                .sizeCalculator(form.sizeCalculator())
                .branch(branch)
                .type(EType.valueOf(form.type()))
                .status(EProductStatus.ACTIVE)
                .createdAt(now)
                .createdBy(currentUserId)
                .updatedAt(now)
                .updatedBy(currentUserId)
                .parent(form.parent())
                .build();
        Product savedProduct = productRepository.save(product);

        // Save product properties
        saveProductPropertyDetail(form.properties(), savedProduct);
    }

    @Override
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createAll(final ProductListForm listForm) {
        listForm.data().forEach(this::create);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(final Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(messageHelper.buildDataNotFound("Sản phẩm với ID =", id));

        productPropertyDetailRepository.deleteAllByProduct(product);
        productRepository.delete(product);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteAll(final String type) {
        if ("ALL".equals(type)) {
            productPropertyDetailRepository.deleteAll();
            productRepository.deleteAll();
        } else {
            List<Product> products = productRepository.findAllByType(EType.valueOf(type));
            products.forEach(productPropertyDetailRepository::deleteAllByProduct);
            productRepository.deleteAll(products);
        }
    }

    @Override
    public PagingWrapper getAll(final String search, final List<String> type, final List<Long> branchId, final int page, final int pageSize) {
        String searchParam = "%" + search.toLowerCase() + "%";
        String status = EProductStatus.ACTIVE.name();

        int offset = (page - 1) * pageSize;
        long totalItems = productMapper.countProduct(status, searchParam, type, branchId);
        int totalPages = (int) Math.ceil(totalItems / (pageSize * 1.0));
        List<ProductData> productDatasets = productMapper.selectProduct(status, searchParam, type, branchId, pageSize, offset)
                .stream()
                .map(item -> {
                    Branch branch = getBranch(item.getBranchId());
                    String parent = Objects.isNull(item.getParent()) ? "" : productRepository.getNameById(item.getParent());

                    String createdBy = userRepository.getFullNameById(item.getCreatedBy());
                    String updatedBy = userRepository.getFullNameById(item.getUpdatedBy());

                    ProductData productData = DataBuilder.to(item, ProductData.class);
                    productData.setCreatedBy(createdBy);
                    productData.setUpdatedBy(updatedBy);
                    productData.setParent(parent);
                    productData.setBranch(DataBuilder.to(branch, BranchModel.class));

                    return productData;
                }).toList();

        return PagingWrapper.builder()
                .data(productDatasets)
                .page(page)
                .pageSize(pageSize)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .build();
    }

    @Override
    public ProductDetailData get(final Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(messageHelper.buildDataNotFound("Sản phẩm với ID =", id));

        String createdBy = userRepository.getFullNameById(product.getCreatedBy());
        String updatedBy = userRepository.getFullNameById(product.getUpdatedBy());

        List<PropertyDetailData> properties = product.getProductPropertyDetails().stream()
                .map(item -> PropertyDetailData.builder()
                        .id(item.getPropertyDetail().getId())
                        .name(item.getPropertyDetail().getName())
                        .build())
                .toList();

        ProductDetailData data = DataBuilder.to(product, ProductDetailData.class);
        data.setProperties(properties);
        data.setCreatedBy(createdBy);
        data.setUpdatedBy(updatedBy);
        data.setType(product.getType().name());
        data.setStatus(product.getStatus().name());
        data.setBranch(DataBuilder.to(product.getBranch(), BranchModel.class));

        return data;
    }

    @Override
    public List<ProductOptionData> getAllOption(Long branchId) {
        Branch branch = getBranch(branchId);
        return productRepository.findAllByBranch(branch)
                .stream().map(item -> ProductOptionData.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .quantity(item.getQuantity())
                .build())
                .toList();
    }

    // Private functions
    private Branch getBranch(final Long branchId) {
        return branchRepository.findById(branchId)
                .orElseThrow(messageHelper.buildDataNotFound("Chi nhánh với ID =", branchId));
    }

    private void saveProductPropertyDetail(final Map<Long, Long> properties, final Product savedProduct) {
        productPropertyDetailRepository.deleteAllByProduct(savedProduct);

        for (var property : properties.entrySet()) {
            Long propertyDetailId = property.getValue();
            if (Objects.nonNull(propertyDetailId)) {
                PropertyDetail propertyDetail = propertyDetailRepository.findById(propertyDetailId)
                        .orElseThrow(messageHelper.buildDataNotFound("Giá trị thuộc tính với ID =", propertyDetailId));

                ProductPropertyDetail productPropertyDetail = ProductPropertyDetail.builder()
                        .product(savedProduct)
                        .propertyDetail(propertyDetail)
                        .build();
                productPropertyDetailRepository.saveAndFlush(productPropertyDetail);
            }
        }
    }

    public ResponseEntity<ByteArrayResource> downloadTemplate() {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Mau_san_pham");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Tên sản phẩm");
            headerRow.createCell(1).setCellValue("Số lượng");
            headerRow.createCell(2).setCellValue("Chi nhánh");
            headerRow.createCell(3).setCellValue("Thuộc tính");

            // Example data row
            Row row = sheet.createRow(1);
            row.createCell(0).setCellValue("<Nhập tên sản phẩm>");
            row.createCell(1).setCellValue("<Nhập số lượng>");
            row.createCell(2).setCellValue("<Chọn chi nhánh>");
            row.createCell(3).setCellValue("<Chọn thuộc tính>");

            // Auto size columns
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            // Convert workbook to ByteArrayResource for download
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

                // Return the file as a response for download
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Mau_san_pham.xlsx")
                        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                        .body(resource);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel template", e);
        }
    }

    /**
     * Method to upload bulk products from an Excel file.
     */
    public List<String> uploadFromFile(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                errors.add("File Excel không có sheet nào.");
                return errors;
            }

            int firstRow = sheet.getFirstRowNum();
            int lastRow = sheet.getLastRowNum();
            for (int rowIndex = firstRow + 1; rowIndex <= lastRow; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                String name = getCellValue(row, 0);
                String quantityStr = getCellValue(row, 1);
                String branchName = getCellValue(row, 2);
                String propertyName = getCellValue(row, 3);

                if (StringUtils.isEmpty(name) || StringUtils.isEmpty(quantityStr) || StringUtils.isEmpty(branchName) || StringUtils.isEmpty(propertyName)) {
                    errors.add("Dòng " + (rowIndex + 1) + " có lỗi: Tên sản phẩm, Số lượng, Chi nhánh, hoặc Thuộc tính không hợp lệ.");
                    continue;
                }

                // Validate quantity is a number (normalize commas/spaces)
                int quantity;
                try {
                    String normalizedQty = quantityStr == null ? "" : quantityStr.trim().replaceAll("[\\s,]", "");
                    quantity = Integer.parseInt(normalizedQty);
                } catch (NumberFormatException e) {
                    errors.add("Dòng " + (rowIndex + 1) + " có lỗi: Số lượng không hợp lệ.");
                    continue;
                }

                Branch branch = getBranchByName(branchName);
                if (branch == null) {
                    errors.add("Dòng " + (rowIndex + 1) + " có lỗi: Chi nhánh không tồn tại.");
                    continue;
                }

                // Add the product to the database
                Product product = new Product();
                product.setName(name);
                product.setQuantity(quantity);
                product.setBranch(branch);
                product.setCreatedAt(TimeUtils.nowStr());
                product.setCreatedBy(SecurityUtils.getCurrentUserId(true));
                product.setUpdatedAt(TimeUtils.nowStr());
                product.setUpdatedBy(SecurityUtils.getCurrentUserId(true));
                product.setStatus(EProductStatus.ACTIVE);
                product.setType(EType.PRODUCT); // Set type according to your logic

                Product savedProduct = productRepository.save(product);

                // Handle product properties (if any) - support comma-separated property names
                if (propertyName != null && propertyName.trim().length() > 0) {
                    String[] propertyNames = propertyName.split(",");
                    for (String pn : propertyNames) {
                        String trimmed = pn.trim();
                        try {
                            saveProductPropertyDetailByName(trimmed, savedProduct);
                        } catch (RuntimeException ex) {
                            errors.add("Dòng " + (rowIndex + 1) + " thuộc tính lỗi: " + ex.getMessage());
                        }
                    }
                }
            }
        } catch (IOException e) {
            errors.add("Lỗi đọc file Excel: " + e.getMessage());
        }
        return errors;
    }

    // Private function to get cell value
    // Use DataFormatter to preserve displayed cell values
    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        return cell != null ? DATA_FORMATTER.formatCellValue(cell) : "";
    }

    // Private function to get Branch by name (assuming you want to look up by name)
    private Branch getBranchByName(String branchName) {
        return branchRepository.findByName(branchName)
                .orElse(null); // Handle as needed, maybe return null or throw an exception
    }

    // Private function to save product properties
    private void saveProductPropertyDetailByName(String propertyName, Product savedProduct) {
    PropertyDetail propertyDetail = propertyDetailRepository.findByName(propertyName)
        .orElseThrow(() -> new RuntimeException("Thuộc tính không tồn tại: " + propertyName));

    ProductPropertyDetail productPropertyDetail = ProductPropertyDetail.builder()
        .product(savedProduct)
        .propertyDetail(propertyDetail)
        .build();

    productPropertyDetailRepository.save(productPropertyDetail);
    }
}
                   