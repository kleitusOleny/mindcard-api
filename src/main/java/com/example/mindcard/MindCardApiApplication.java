package com.example.mindcard;

import com.example.mindcard.model.UserProfile;
import com.example.mindcard.repository.UserProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.HashMap;

@SpringBootApplication
public class MindCardApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MindCardApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoDataSeeder(UserProfileRepository userProfileRepository) {
        return args -> {
            if (userProfileRepository.count() <= 1) {
                // Seed mock users for the leaderboard
                UserProfile u1 = new UserProfile("alex_chen", "Alex Chen", "Grandmaster", 8, 12, 15, 2450, 45);
                u1.setStudyHistory(new HashMap<>());
                userProfileRepository.save(u1);

                UserProfile u2 = new UserProfile("sarah_kim", "Sarah Kim", "Expert Explorer", 7, 10, 12, 2100, 32);
                u2.setStudyHistory(new HashMap<>());
                userProfileRepository.save(u2);

                UserProfile u3 = new UserProfile("mike_johnson", "Mike Johnson", "Word Wizard", 6, 9, 10, 1850, 28);
                u3.setStudyHistory(new HashMap<>());
                userProfileRepository.save(u3);

                UserProfile u4 = new UserProfile("emma_wilson", "Emma Wilson", "Language Enthusiast", 5, 8, 8, 1600, 15);
                u4.setStudyHistory(new HashMap<>());
                userProfileRepository.save(u4);

                UserProfile u5 = new UserProfile("david_lee", "David Lee", "Daily Challenger", 5, 8, 9, 1400, 12);
                u5.setStudyHistory(new HashMap<>());
                userProfileRepository.save(u5);

                UserProfile u6 = new UserProfile("lisa_brown", "Lisa Brown", "Novice Scholar", 4, 7, 7, 1200, 9);
                u6.setStudyHistory(new HashMap<>());
                userProfileRepository.save(u6);

                UserProfile u7 = new UserProfile("james_taylor", "James Taylor", "Card Collector", 4, 7, 8, 1000, 7);
                u7.setStudyHistory(new HashMap<>());
                userProfileRepository.save(u7);

                UserProfile u8 = new UserProfile("amy_garcia", "Amy Garcia", "Quick Reviewer", 3, 5, 6, 850, 5);
                u8.setStudyHistory(new HashMap<>());
                userProfileRepository.save(u8);

                UserProfile u9 = new UserProfile("chris_anderson", "Chris Anderson", "Streak Builder", 3, 5, 5, 700, 4);
                u9.setStudyHistory(new HashMap<>());
                userProfileRepository.save(u9);

                UserProfile u10 = new UserProfile("nina_martinez", "Nina Martinez", "Active Learner", 2, 4, 4, 550, 2);
                u10.setStudyHistory(new HashMap<>());
                userProfileRepository.save(u10);

                System.out.println("Successfully seeded mock user profiles for leaderboard!");
            }
        };
    }
}
