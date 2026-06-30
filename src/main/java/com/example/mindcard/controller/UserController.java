package com.example.mindcard.controller;

import com.example.mindcard.model.UserProfile;
import com.example.mindcard.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserProfileRepository userProfileRepository;

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
}
