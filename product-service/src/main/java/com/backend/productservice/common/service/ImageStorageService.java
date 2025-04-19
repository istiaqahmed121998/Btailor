package com.backend.productservice.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface ImageStorageService {
    public String uploadImage(MultipartFile file) throws IOException;
    public Set<String> uploadImages(List<MultipartFile> files) throws IOException;
}
