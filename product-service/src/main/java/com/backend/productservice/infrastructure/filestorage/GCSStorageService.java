package com.backend.productservice.infrastructure.filestorage;

import com.backend.productservice.common.service.ImageStorageService;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
@ConditionalOnProperty(name = "filestorage.provider", havingValue = "gcs")
public class GCSStorageService implements ImageStorageService {
    @Value("${gcp.bucket.name}")
    private String bucketName;

    private final Storage storage;

    public GCSStorageService(Storage storage) {
        this.storage = storage;
    }

    @Override
    public String uploadImage(MultipartFile file, String folder) throws IOException {
        String fileName = file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.create(blobInfo, file.getBytes());
        return "https://storage.googleapis.com/" + bucketName + "/" + fileName;
    }

    @Override
    public Set<String> uploadImages(List<MultipartFile> files) throws IOException {
        Set<String> images=new HashSet<>();
        for (MultipartFile file : files) {
            images.add(uploadImage(file, bucketName));
        }
        return images;
    }

}
