package com.backend.productservice.common.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Set;
@Service
public interface ImageStorageService {
    String uploadImage(MultipartFile file, String folder) throws IOException;
    Set<String> uploadImages(List<MultipartFile> files) throws IOException;
}
