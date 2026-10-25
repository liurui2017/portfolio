package org.example.portfolioshowcase.service;

import java.util.HashMap;
import java.util.Map;

import org.example.portfolioshowcase.dto.request.SiteInfoRequest;
import org.example.portfolioshowcase.dto.response.SiteInfoResponse;
import org.example.portfolioshowcase.entity.SiteSetting;
import org.example.portfolioshowcase.repository.SiteSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SiteSettingService {

    private final SiteSettingRepository siteSettingRepository;

    @Transactional(readOnly = true)
    public SiteInfoResponse getSiteInfo() {
        return buildSiteInfo();
    }

    @Transactional
    public SiteInfoResponse update(SiteInfoRequest req) {
        upsert("title", req.title());
        upsert("about", req.about());
        upsert("avatarUrl", req.avatarUrl());
        upsert("github", req.github());
        upsert("email", req.email());
        return buildSiteInfo();
    }

    private void upsert(String key, String value) {
        SiteSetting setting = siteSettingRepository.findBySettingKey(key).orElseGet(() -> {
            SiteSetting s = new SiteSetting();
            s.setSettingKey(key);
            return s;
        });
        setting.setValue(value);
        siteSettingRepository.save(setting);
    }

    private SiteInfoResponse buildSiteInfo() {
        Map<String, String> settings = new HashMap<>();
        for (SiteSetting s : siteSettingRepository.findAll()) {
            settings.put(s.getSettingKey(), s.getValue());
        }
        return new SiteInfoResponse(
                settings.get("title"),
                settings.get("about"),
                settings.get("avatarUrl"),
                settings.get("github"),
                settings.get("email"));
    }
}
