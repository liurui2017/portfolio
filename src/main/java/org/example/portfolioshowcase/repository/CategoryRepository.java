package org.example.portfolioshowcase.repository;

import java.util.List;

import org.example.portfolioshowcase.entity.Category;
import org.example.portfolioshowcase.entity.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
            select c, count(w)
            from Work w
            join w.category c
            where w.status = :status
            group by c
            """)
    List<Object[]> countPublishedWorks(@Param("status") WorkStatus status);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);
}
