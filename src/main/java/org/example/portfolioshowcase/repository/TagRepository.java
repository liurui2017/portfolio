package org.example.portfolioshowcase.repository;

import java.util.List;

import org.example.portfolioshowcase.entity.Tag;
import org.example.portfolioshowcase.entity.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("""
            select t, count(w)
            from Work w
            join w.tags t
            where w.status = :status
            group by t
            """)
    List<Object[]> countPublishedWorks(@Param("status") WorkStatus status);

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);
}
