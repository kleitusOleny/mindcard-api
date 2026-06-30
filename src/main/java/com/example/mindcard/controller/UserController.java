package com.example.mindcard.controller;

import com.example.mindcard.model.UserProfile;
import com.example.mindcard.model.DailyStudyRecord;
import com.example.mindcard.repository.UserProfileRepository;
import com.example.mindcard.repository.DailyStudyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private DailyStudyRecordRepository dailyStudyRecordRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable String userId) {
        return userProfileRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    // Create default profile if not exists
                    UserProfile defaultProfile = new UserProfile(
                            userId,
                            "Learner",
                            "Language Explorer",
                            1,
                            0,
                            0,
                            0,
                            0
                    );
                    defaultProfile.setStudyHistory(new HashMap<>());
                    UserProfile saved = userProfileRepository.save(defaultProfile);
                    return ResponseEntity.ok(saved);
                });
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserProfile> updateUserProfile(@PathVariable String userId, @RequestBody UserProfile updatedProfile) {
        updatedProfile.setUserId(userId);
        UserProfile saved = userProfileRepository.save(updatedProfile);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{userId}/daily-records")
    public ResponseEntity<List<DailyStudyRecord>> getDailyRecords(@PathVariable String userId) {
        return ResponseEntity.ok(dailyStudyRecordRepository.findByUserId(userId));
    }

    @PutMapping("/{userId}/daily-records/{date}")
    public ResponseEntity<DailyStudyRecord> updateDailyRecord(
            @PathVariable String userId,
            @PathVariable String date,
            @RequestBody DailyStudyRecord record
    ) {
        record.setUserId(userId);
        record.setDate(date);
        DailyStudyRecord saved = dailyStudyRecordRepository.save(record);
        return ResponseEntity.ok(saved);
    }
}
