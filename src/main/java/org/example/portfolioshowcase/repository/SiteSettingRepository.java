package org.example.portfolioshowcase.repository;

import java.util.Optional;

import org.example.portfolioshowcase.entity.SiteSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteSettingRepository extends JpaRepository<SiteSetting, Long> {

    Optional<SiteSetting> findBySettingKey(String key);
}
