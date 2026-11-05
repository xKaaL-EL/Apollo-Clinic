package com.hospital.hms.service;

import com.hospital.hms.dto.FollowUpDto;
import java.util.List;

public interface FollowUpService {
    FollowUpDto createFollowUp(FollowUpDto followUpDto);
    FollowUpDto updateFollowUp(Long id, FollowUpDto followUpDto);
    void deleteFollowUp(Long id);
    FollowUpDto getFollowUpById(Long id);
    List<FollowUpDto> getAllFollowUps(String sortBy);
    List<FollowUpDto> searchFollowUps(String query);
    FollowUpDto createFollowUpForDoctor(FollowUpDto dto, Long userId);
    FollowUpDto updateFollowUpForDoctor(Long id, FollowUpDto dto, Long userId);
    void deleteFollowUpForDoctor(Long id, Long userId);
    FollowUpDto getFollowUpForDoctor(Long id, Long userId);
    List<FollowUpDto> getFollowUpsForDoctor(Long userId);
}
