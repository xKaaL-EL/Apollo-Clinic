package com.hospital.hms.controller.api;

import com.hospital.hms.dto.FollowUpDto;
import com.hospital.hms.service.FollowUpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/followups")
public class FollowUpApiController {

    private final FollowUpService followUpService;

    @Autowired
    public FollowUpApiController(FollowUpService followUpService) {
        this.followUpService = followUpService;
    }

    @GetMapping
    public ResponseEntity<List<FollowUpDto>> getAllFollowUps(@RequestParam(value = "sortBy", required = false) String sortBy, HttpSession session) {
        if ("DOCTOR".equals(session.getAttribute("hms_role"))) return ResponseEntity.ok(followUpService.getFollowUpsForDoctor((Long) session.getAttribute("hms_user_id")));
        return ResponseEntity.ok(followUpService.getAllFollowUps(sortBy));
    }

    @GetMapping("/search")
    public ResponseEntity<List<FollowUpDto>> searchFollowUps(@RequestParam("query") String query) {
        return ResponseEntity.ok(followUpService.searchFollowUps(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FollowUpDto> getFollowUpById(@PathVariable("id") Long id, HttpSession session) {
        if ("DOCTOR".equals(session.getAttribute("hms_role"))) return ResponseEntity.ok(followUpService.getFollowUpForDoctor(id, (Long) session.getAttribute("hms_user_id")));
        return ResponseEntity.ok(followUpService.getFollowUpById(id));
    }

    @PostMapping
    public ResponseEntity<FollowUpDto> createFollowUp(@Valid @RequestBody FollowUpDto followUpDto, HttpSession session) {
        if ("DOCTOR".equals(session.getAttribute("hms_role"))) return new ResponseEntity<>(followUpService.createFollowUpForDoctor(followUpDto, (Long) session.getAttribute("hms_user_id")), HttpStatus.CREATED);
        return new ResponseEntity<>(followUpService.createFollowUp(followUpDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FollowUpDto> updateFollowUp(@PathVariable("id") Long id, @Valid @RequestBody FollowUpDto followUpDto, HttpSession session) {
        if ("DOCTOR".equals(session.getAttribute("hms_role"))) return ResponseEntity.ok(followUpService.updateFollowUpForDoctor(id, followUpDto, (Long) session.getAttribute("hms_user_id")));
        return ResponseEntity.ok(followUpService.updateFollowUp(id, followUpDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFollowUp(@PathVariable("id") Long id, HttpSession session) {
        if ("DOCTOR".equals(session.getAttribute("hms_role"))) { followUpService.deleteFollowUpForDoctor(id, (Long) session.getAttribute("hms_user_id")); return ResponseEntity.noContent().build(); }
        followUpService.deleteFollowUp(id);
        return ResponseEntity.noContent().build();
    }
}
