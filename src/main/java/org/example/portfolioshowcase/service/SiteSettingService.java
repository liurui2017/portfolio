package org.example.portfolioshowcase.service;

import java.util.Map;
import java.util.stream.Collectors;

import org.example.portfolioshowcase.dto.response.SiteInfoResponse;
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
        Map<String, String> settings = siteSettingRepository.findAll().stream()
                .collect(Collectors.toMap(s -> s.getSettingKey(), s -> s.getValue()));
        return new SiteInfoResponse(
                settings.get("title"),
                settings.get("about"),
                settings.get("avatarUrl"),
                settings.get("github"),
                settings.get("email"));
    }
}
