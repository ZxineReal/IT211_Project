package com.example.project2.controller;

import com.example.project2.dto.ApiResponse;
import com.example.project2.service.CourtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/manager/courts")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
public class ManagerCourtController {

    private final CourtService courtService;

    @PostMapping("/{courtId}/images")
    public ResponseEntity<ApiResponse<List<String>>> uploadCourtImages(
            @PathVariable Long courtId,
            @RequestParam("files") List<MultipartFile> files) throws IOException {

        List<String> imageUrls = courtService.addCourtImages(courtId, files);
        return ResponseEntity.ok(ApiResponse.<List<String>>builder()
                .success(true)
                .message("Tải lên hình ảnh sân thành công")
                .data(imageUrls)
                .build());
    }
}
