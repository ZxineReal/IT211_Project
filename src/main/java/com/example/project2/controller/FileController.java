package com.example.project2.controller;

import com.example.project2.dto.ApiResponse;
import com.example.project2.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Dung lượng tệp vượt quá giới hạn cho phép (tối đa 5MB)");
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/png") && !contentType.equals("image/jpeg") && !contentType.equals("image/jpg"))) {
            throw new IllegalArgumentException("Định dạng tệp không hỗ trợ (chỉ chấp nhận PNG, JPG, JPEG)");
        }

        Map<?, ?> result = cloudinaryService.uploadFile(file);
        String secureUrl = (String) result.get("secure_url");

        return ResponseEntity.ok(ApiResponse.<String>builder()
                .success(true)
                .message("Tải lên tệp thành công")
                .data(secureUrl)
                .build());
    }
}
