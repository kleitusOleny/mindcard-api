package com.example.mindcard.repository;

import com.example.mindcard.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck, String> {
    List<Deck> findByUserId(String userId);
}
