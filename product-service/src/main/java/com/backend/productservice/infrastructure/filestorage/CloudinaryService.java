package com.backend.productservice.infrastructure.filestorage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }


    public String uploadImage(MultipartFile file) throws IOException {
        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return result.get("secure_url").toString();
    }

    public Set<String> uploadImages(List<MultipartFile> files) throws IOException {
        Set<String> urls = new HashSet<>();
        for (MultipartFile file : files) {
            urls.add(uploadImage(file));
        }
        return urls;
    }
}
