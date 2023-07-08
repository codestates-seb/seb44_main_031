package competnion.infra.s3.util;

import org.springframework.stereotype.Component;

@Component
public class S3Util {

//    private final AmazonS3 amazonS3;
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucket;
//
//    public S3Util(AmazonS3 amazonS3) {
//        this.amazonS3 = amazonS3;
//    }
//
//    public String uploadImage(MultipartFile multipartFile) {
//        String imageName = String.format("%s-%s", UUID.randomUUID(), multipartFile.getOriginalFilename());
//
//        // 업로드되는 객체의 속성을 설정
//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentLength(multipartFile.getSize());
//
//        try {
//            amazonS3.putObject(bucket, imageName, multipartFile.getInputStream(), objectMetadata);
//        } catch (IOException e) {
//            throw new BusinessException(INVALID_INPUT_VALUE);
//        }
//
//        URL imageUrl = amazonS3.getUrl(bucket, imageName);
//        return imageUrl.toString();
//    }
//
//    public void deleteImage(String imgUrl) {
//        String key = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
//        String decodeKey = URLDecoder.decode(key, UTF_8);
//
//        delete(decodeKey);
//    }
//
//    public void delete(String key) {
//        amazonS3.deleteObject(bucket, key);
//    }
}
