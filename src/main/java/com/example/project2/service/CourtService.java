package com.example.project2.service;

import com.example.project2.entity.Court;
import com.example.project2.entity.CourtImage;
import com.example.project2.exception.NotFoundException;
import com.example.project2.repository.CourtImageRepository;
import com.example.project2.repository.CourtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CourtService {

    private final CourtRepository courtRepository;
    private final CourtImageRepository courtImageRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public List<String> addCourtImages(Long courtId, List<MultipartFile> files) throws IOException {
        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sân với ID: " + courtId));

        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("Danh sách tệp tải lên rỗng");
        }

        List<String> uploadedUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            if (file.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException("Dung lượng tệp vượt quá giới hạn 10MB: " + file.getOriginalFilename());
            }

            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("image/png") && !contentType.equals("image/jpeg") && !contentType.equals("image/jpg"))) {
                throw new IllegalArgumentException("Định dạng tệp không hỗ trợ cho tệp: " + file.getOriginalFilename());
            }

            Map<?, ?> uploadResult = cloudinaryService.uploadFile(file);
            String secureUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            CourtImage courtImage = new CourtImage();
            courtImage.setCourt(court);
            courtImage.setImageUrl(secureUrl);
            courtImage.setPublicId(publicId);

            courtImageRepository.save(courtImage);
            uploadedUrls.add(secureUrl);

            // Nếu sân chưa có ảnh chính (imageUrl), gán ảnh đầu tiên làm ảnh chính
            if (court.getImageUrl() == null || court.getImageUrl().isEmpty()) {
                court.setImageUrl(secureUrl);
                courtRepository.save(court);
            }
        }

        return uploadedUrls;
    }
}
