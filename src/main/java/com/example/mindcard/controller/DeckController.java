package com.example.mindcard.controller;

import com.example.mindcard.dto.StudySessionRequest;
import com.example.mindcard.dto.PromptRequest;
import com.example.mindcard.model.Card;
import com.example.mindcard.model.Deck;
import com.example.mindcard.model.UserProfile;
import com.example.mindcard.repository.CardRepository;
import com.example.mindcard.repository.DeckRepository;
import com.example.mindcard.repository.UserProfileRepository;
import com.example.mindcard.service.DeckGeneratorService;
import com.example.mindcard.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users/{userId}/decks")
public class DeckController {

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private DeckGeneratorService deckGeneratorService;

    @GetMapping
    public ResponseEntity<List<Deck>> getDecks(@PathVariable String userId) {
        return ResponseEntity.ok(deckRepository.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Deck> createDeck(@PathVariable String userId, @RequestBody Deck deck) {
        deck.setUserId(userId);
        // Force establish many-to-one links so they persist properly
        if (deck.getCards() != null) {
            for (Card card : deck.getCards()) {
                card.setDeck(deck);
            }
        }
        Deck saved = deckRepository.save(deck);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{deckId}")
    public ResponseEntity<Deck> updateDeck(
            @PathVariable String userId,
            @PathVariable String deckId,
            @RequestBody Deck deckDetails
    ) {
        return deckRepository.findById(deckId)
                .map(deck -> {
                    deck.setName(deckDetails.getName());
                    deck.setCategory(deckDetails.getCategory());
                    if (deckDetails.getCoverUrl() != null) {
                        deck.setCoverUrl(deckDetails.getCoverUrl());
                    }
                    Deck saved = deckRepository.save(deck);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{deckId}")
    public ResponseEntity<Void> deleteDeck(
            @PathVariable String userId,
            @PathVariable String deckId
    ) {
        if (deckRepository.existsById(deckId)) {
            deckRepository.deleteById(deckId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{deckId}/cards")
    public ResponseEntity<Deck> addCard(
            @PathVariable String userId,
            @PathVariable String deckId,
            @RequestBody Card card
    ) {
        return deckRepository.findById(deckId)
                .map(deck -> {
                    deck.addCard(card);
                    // Mastered percentage defaults or updates
                    deck.setMasteredPercentage(calculateMastered(deck.getCards()));
                    Deck saved = deckRepository.save(deck);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{deckId}/cards/{cardId}")
    public ResponseEntity<Deck> updateCard(
            @PathVariable String userId,
            @PathVariable String deckId,
            @PathVariable String cardId,
            @RequestBody Card cardDetails
    ) {
        return deckRepository.findById(deckId)
                .map(deck -> {
                    Optional<Card> existingCard = deck.getCards().stream()
                            .filter(c -> c.getId().equals(cardId))
                            .findFirst();
                    if (existingCard.isPresent()) {
                        Card card = existingCard.get();
                        card.setEnglishWord(cardDetails.getEnglishWord());
                        card.setPronunciation(cardDetails.getPronunciation());
                        card.setPos(cardDetails.getPos());
                        card.setDefinition(cardDetails.getDefinition());
                        card.setExampleSentence(cardDetails.getExampleSentence());
                        card.setSynonyms(cardDetails.getSynonyms());
                        deck.setMasteredPercentage(calculateMastered(deck.getCards()));
                        Deck saved = deckRepository.save(deck);
                        return ResponseEntity.ok(saved);
                    }
                    return ResponseEntity.notFound().<Deck>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{deckId}/cards/{cardId}")
    public ResponseEntity<Deck> deleteCard(
            @PathVariable String userId,
            @PathVariable String deckId,
            @PathVariable String cardId
    ) {
        return deckRepository.findById(deckId)
                .map(deck -> {
                    Optional<Card> existingCard = deck.getCards().stream()
                            .filter(c -> c.getId().equals(cardId))
                            .findFirst();
                    if (existingCard.isPresent()) {
                        deck.removeCard(existingCard.get());
                        deck.setMasteredPercentage(calculateMastered(deck.getCards()));
                        Deck saved = deckRepository.save(deck);
                        return ResponseEntity.ok(saved);
                    }
                    return ResponseEntity.notFound().<Deck>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/study-session")
    public ResponseEntity<UserProfile> recordStudySession(
            @PathVariable String userId,
            @RequestBody StudySessionRequest request
    ) {
        // 1. Fetch UserProfile
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseGet(() -> {
                    UserProfile p = new UserProfile(userId, "Learner", "Language Explorer", 1, 0, 0, 0, 0);
                    return userProfileRepository.save(p);
                });

        // 2. Set study history for today
        String todayStr = LocalDate.now().toString(); // YYYY-MM-DD
        boolean alreadyStudiedToday = profile.getStudyHistory().getOrDefault(todayStr, false);
        profile.getStudyHistory().put(todayStr, true);

        // 3. Calculate streak
        int newStreak = profile.getCurrentStreak();
        if (!alreadyStudiedToday) {
            newStreak += 1;
        }
        if (newStreak == 0) {
            newStreak = 1;
        }
        profile.setCurrentStreak(newStreak);
        profile.setBestStreak(Math.max(profile.getBestStreak(), newStreak));

        // 4. Update XP
        profile.setTotalXp(profile.getTotalXp() + request.getXp());

        // 5. Update Deck mastery
        Optional<Deck> deckOpt = deckRepository.findById(request.getDeckId());
        if (deckOpt.isPresent()) {
            Deck deck = deckOpt.get();
            int newMastery = Math.min(100, deck.getMasteredPercentage() + (request.getAccuracy() / 10));
            deck.setMasteredPercentage(newMastery);
            deckRepository.save(deck);
        }

        // 6. Recalculate unique words learned across all decks
        List<Deck> allDecks = deckRepository.findByUserId(userId);
        long uniqueWords = allDecks.stream()
                .flatMap(d -> d.getCards().stream())
                .map(c -> c.getEnglishWord().trim().toLowerCase())
                .filter(w -> !w.isEmpty())
                .distinct()
                .count();
        profile.setTotalWordsLearned((int) uniqueWords);

        // 7. Save and return updated profile
        UserProfile savedProfile = userProfileRepository.save(profile);
        return ResponseEntity.ok(savedProfile);
    }

    @PostMapping("/generate-ai")
    public ResponseEntity<Deck> generateDeckAi(
            @PathVariable String userId,
            @RequestBody PromptRequest request
    ) {
        try {
            Deck generatedDeck = deckGeneratorService.generateDeckWithFallback(request.getPrompt());
            generatedDeck.setUserId(userId);
            if (generatedDeck.getCards() != null) {
                for (Card card : generatedDeck.getCards()) {
                    card.setDeck(generatedDeck);
                }
            }
            Deck saved = deckRepository.save(generatedDeck);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private int calculateMastered(List<Card> cards) {
        return cards.isEmpty() ? 0 : 10;
    }
}
