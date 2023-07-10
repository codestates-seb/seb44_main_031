package competnion.infra.s3.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import competnion.global.exception.BusinessLogicException;
import competnion.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.UUID;


import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class S3Util {

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile multipartFile) {
        String imageName = String.format("%s-%s", UUID.randomUUID(), multipartFile.getOriginalFilename());

        // 업로드되는 객체의 속성을 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, imageName, multipartFile.getInputStream(), objectMetadata)
//                            .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
            );
        } catch (IOException e) {
            throw new BusinessLogicException(ExceptionCode.INVALID_INPUT_VALUE);
        }

        URL imageUrl = amazonS3Client.getUrl(bucket, imageName);
        return imageUrl.toString();
    }

    public void deleteImage(String imgUrl) {
        String key = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
        String decodeKey = URLDecoder.decode(key, UTF_8);

        delete(decodeKey);
    }

    public void delete(String key) {
        amazonS3Client.deleteObject(bucket, key);
    }

    public void isFileAnImageOrThrow(final MultipartFile image) {
        String fileExtension = FilenameUtils.getExtension(requireNonNull(image.getOriginalFilename()).toLowerCase());
        if (!fileExtension.equals("jpg") && !fileExtension.equals("jpeg") && !fileExtension.equals("png"))
            throw new BusinessLogicException(ExceptionCode.INVALID_INPUT_VALUE);
    }
}
