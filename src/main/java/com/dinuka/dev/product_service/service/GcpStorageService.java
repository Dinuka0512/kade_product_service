package com.dinuka.dev.product_service.service;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class GcpStorageService {

    private final Storage storage;
    private final String bucketName;

    public GcpStorageService(Storage storage, @Value("${gcp.bucket.name:kade_marketplace}") String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String objectName = "products/" + UUID.randomUUID() + extension;

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        Blob blob = storage.create(blobInfo, file.getBytes());

        blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        return String.format("https://storage.googleapis.com/%s/%s", bucketName, objectName);
    }

    public void deleteFile(String fileUrl) {
        try {
            String objectName = fileUrl.replace("https://storage.googleapis.com/" + bucketName + "/", "");
            BlobId blobId = BlobId.of(bucketName, objectName);
            storage.delete(blobId);
        } catch (Exception e) {
            // Log error but don't throw - file might not exist
        }
    }
}
