package org.example.portfolioshowcase.repository;

import java.util.Optional;

import org.example.portfolioshowcase.entity.Work;
import org.example.portfolioshowcase.entity.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WorkRepository extends JpaRepository<Work, Long>, JpaSpecificationExecutor<Work> {

    Optional<Work> findBySlugAndStatus(String slug, WorkStatus status);
}
