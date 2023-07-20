package competnion.infra.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import competnion.global.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static competnion.global.exception.ExceptionCode.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class S3Util {

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;
    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public String uploadImage(MultipartFile multipartFile) throws BusinessLogicException {
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
            throw new BusinessLogicException(S3_IMAGE_UPLOAD_FAILED);
        }

        URL imageUrl = amazonS3Client.getUrl(bucket, imageName);
        return imageUrl.toString();
    }

    /**
     * TODO : 리팩토링 필요.........
     */
    public List<String> uploadImageList(List<MultipartFile> imageList) throws BusinessLogicException{
        List<String> imgUrlList = new ArrayList<>();

        for (MultipartFile image : imageList) {
            String imageName = String.format("%s-%s", UUID.randomUUID(), image.getOriginalFilename());
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(image.getSize());
            objectMetadata.setContentType(image.getContentType());

            try {
                amazonS3Client.putObject(
                        new PutObjectRequest(bucket, imageName, image.getInputStream(), objectMetadata)
                );
                imgUrlList.add(amazonS3Client.getUrl(bucket, imageName).toString());
            } catch (IOException e) {
                throw new BusinessLogicException(S3_IMAGE_UPLOAD_FAILED);
            }
        }
        return imgUrlList;
    }

    public void deleteImage(String imgUrl) {
        String key = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
        String decodeKey = URLDecoder.decode(key, UTF_8);

        delete(decodeKey);
    }

    public void delete(String key) {
        amazonS3Client.deleteObject(bucket, key);
    }

    public void isFileAnImageOrThrow(final List<MultipartFile> images) {
        images.stream()
                .map(image -> FilenameUtils.getExtension(
                        requireNonNull(image.getOriginalFilename()).toLowerCase()))
                .filter(fileExtension ->
                        !fileExtension.equals("jpg") && !fileExtension.equals("jpeg") && !fileExtension.equals("png"))
                .forEach(fileExtension -> {
            throw new BusinessLogicException(INVALID_IMAGE_EXTENSION);
        });
    }

    public void checkImageCount(final List<MultipartFile> images) {
        long count = images.size();
        if (count > 3) throw new BusinessLogicException(OVER_THE_IMAGE_MAX_LIST);
    }
}
