package com.example.project2.repository;

import com.example.project2.entity.BadmintonCluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadmintonClusterRepository extends JpaRepository<BadmintonCluster, Long> {
}
