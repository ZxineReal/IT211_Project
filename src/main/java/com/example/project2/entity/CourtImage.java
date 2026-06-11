package com.example.project2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "court_images")
@Getter @Setter
public class CourtImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    private String publicId;                // Cloudinary public_id để xoá sau này

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;
}
