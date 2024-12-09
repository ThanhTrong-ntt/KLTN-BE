package hcmute.nhom.kltn.service.firebase;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import hcmute.nhom.kltn.exception.SystemErrorException;

/**
 * Class FirebaseStorageService.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Service
public class FirebaseStorageService {
    public static final Logger logger = LoggerFactory.getLogger(FirebaseStorageService.class);

    @Value("${firebase.bucket-name}")
    private String bucketName;

    @Value("${firebase.private-key-path}")
    private String privateKeyPath;

    public String uploadFile(File file, String fileType) {
        BlobId blobId = BlobId.of(bucketName, file.getName());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(fileType).build();
        Resource resource = new ClassPathResource(privateKeyPath);
        try (InputStream inputStream = resource.getInputStream()) {
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            byte[] bytes = Files.readAllBytes(file.toPath());
            storage.create(blobInfo, bytes);
            return getDownloadUrl(file.getName());
        } catch (IOException e) {
            logger.error("Error uploading file to Firebase Storage: {}", e.getMessage());
            throw new SystemErrorException("Error uploading file to Firebase Storage");
        }
    }

    private String getDownloadUrl(String fileName) {
        String downloadUrl = "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media";
        return String.format(downloadUrl, URLEncoder.encode(bucketName, StandardCharsets.UTF_8),
                URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }
}
