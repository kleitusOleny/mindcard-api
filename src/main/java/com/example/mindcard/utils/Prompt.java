package com.example.mindcard.utils;

import org.springframework.lang.NonNull;

public class Prompt {
    @NonNull
    public static String getFullPrompt(String userPrompt) {
        String systemInstruction = "You are an expert language teacher. Create a deck of flashcards based on the user's request.\n" +
                "You MUST return a JSON object with the following schema:\n" +
                "{\n" +
                "  \"deckName\": \"Name of the deck\",\n" +
                "  \"category\": \"Math, Science, Languages, History, etc.\",\n" +
                "  \"cards\": [\n" +
                "    {\n" +
                "      \"englishWord\": \"Word/Phrase to learn\",\n" +
                "      \"pronunciation\": \"Phonetic pronunciation e.g. /\\u02ccser.\\u0259n\\u02c8d\\u026ap.\\u0259.ti/\",\n" +
                "      \"pos\": \"Noun, Verb, or Adj\",\n" +
                "      \"definition\": \"Clear concise translation/definition\",\n" +
                "      \"exampleSentence\": \"An illustrative example sentence using the word\",\n" +
                "      \"synonyms\": \"comma separated synonyms if any\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n" +
                "Create exactly 5 to 10 high-quality cards.";

        return systemInstruction + "\n\nUser request: " + userPrompt;
    }
}
