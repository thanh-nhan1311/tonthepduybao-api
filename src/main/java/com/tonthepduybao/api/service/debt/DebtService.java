package com.tonthepduybao.api.service.debt;

import com.tonthepduybao.api.model.debt.DebtForm;
import com.tonthepduybao.api.model.debt.DebtInfoData;
import com.tonthepduybao.api.model.debt.DebtListData;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * DebtService
 *
 * @author khal
 * @since 2023/07/09
 */
public interface DebtService {

    void create(DebtForm form);

    List<String> createFromFile(MultipartFile file);

    void update(DebtForm form);

    void delete(String id);

    DebtInfoData get(String id);

    DebtListData getAll(String search, String fromDate, String toDate, List<String> type, List<Long> customerId, int page, int pageSize);

    ResponseEntity<Resource> download(List<String> ids);

    ResponseEntity<Resource> downloadTemplate(String type, List<Long> propertyIds);

}
