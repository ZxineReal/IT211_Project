package com.example.project2.repository;

import com.example.project2.entity.Court;
import com.example.project2.entity.enums.CourtStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

    Page<Court> findByStatus(CourtStatus status, Pageable pageable);

    boolean existsByName(String name);
}