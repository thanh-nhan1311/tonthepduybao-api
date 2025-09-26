package com.tonthepduybao.api.app.helper;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * S3Helper
 *
 * @author khale
 * @since 2022/11/01
 */
public interface S3Helper {

    List<String> upload(String directory, MultipartFile[] mFiles);

    String upload(String directory, MultipartFile mFile, String fName);

    String upload(String directory, MultipartFile mFile);

    String upload(String directory, File file, String fName);

    void delete(String directory, String fileName);

    void delete(String path);

}
