package com.tonthepduybao.api.app.helper;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.tonthepduybao.api.app.constant.Constant;
import com.tonthepduybao.api.app.exception.model.S3Exception;
import com.tonthepduybao.api.app.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * S3HelperImpl
 *
 * @author khale
 * @since 2022/11/01
 */
@Component
@RequiredArgsConstructor
public class S3HelperImpl implements S3Helper {

    private final Constant constant;
    private final AmazonS3 amazonS3;

    @Override
    public List<String> upload(final String directory, final MultipartFile[] mFiles) {
        return Arrays.stream(mFiles)
                .map(mFile -> upload(directory, mFile, ""))
                .collect(Collectors.toList());
    }

    @Override
    public String upload(final String directory, final MultipartFile mFile, final String fName) {
        if (Objects.isNull(mFile)) return null;

        String originalFilename = mFile.getOriginalFilename();
        String extensionWithDot = "." + Utils.getExt(originalFilename);
        String fileName = StringUtils.hasLength(fName) ? fName + extensionWithDot : originalFilename;

        try {
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(mFile.getBytes());

            String endpoint = upload(directory, file, fName);

            file.delete();
            fos.close();

            return endpoint;
        } catch (IOException e) {
            throw new S3Exception(List.of(fileName, directory));
        }
    }

    @Override
    public String upload(final String directory, final MultipartFile mFile) {
        if (Objects.isNull(mFile)) return null;

        String originalFilename = mFile.getOriginalFilename();
        String fileNameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf("."));
        return upload(directory, mFile, fileNameWithoutExtension);
    }

    @Override
    public String upload(final String directory, final File file, final String fName) {
        if (Objects.isNull(file)) return null;

        String fileName = file.getName();
        String fileExtension = "." + Utils.getExt(fileName);
        String key = directory + File.separator + (StringUtils.hasLength(fName) ? fName + fileExtension : fileName);

        if (amazonS3.doesObjectExist(constant.getR2BucketName(), key))
            amazonS3.deleteObject(constant.getR2BucketName(), key);

        PutObjectRequest por = new PutObjectRequest(constant.getR2BucketName(), key, file)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3.putObject(por);

        return key;
    }

    @Override
    public void delete(final String directory, final String fileName) {
        String key = directory + File.separator + fileName;
        delete(key);
    }

    @Override
    public void delete(final String path) {
        if (StringUtils.hasLength(path) && amazonS3.doesObjectExist(constant.getR2BucketName(), path))
            amazonS3.deleteObject(constant.getR2BucketName(), path);
    }

}
