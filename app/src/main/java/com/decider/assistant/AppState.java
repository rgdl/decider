package com.decider.assistant;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;

class AppState {
    private final boolean decisionMade;
    private final String resultsText;
    private final ArrayList<Option> options;

    AppState(boolean decisionMade, String resultsText, ArrayList<Option> options) {
        this.decisionMade = decisionMade;
        this.resultsText = resultsText;
        this.options = options;
    }

    AppState() {
        decisionMade = defaultDecisionMade();
        resultsText = defaultResultsText();
        options = defaultOptions();
    }

    static boolean defaultDecisionMade() {
        return false;
    }

    static String defaultResultsText() {
        return "";
    }

    static ArrayList<Option> defaultOptions() {
        return new ArrayList<>(Arrays.asList(
            new Option(1),
            new Option(2)
        ));
    }

    boolean getDecisionMade() {
        return decisionMade;
    }

    String getResultsText() {
        return resultsText;
    }

    ArrayList<Option> getOptions() {
        return options;
    }

    @NonNull
    public String toString() {
        return String.format("[decisionMade: %s, resultsText: %s, options: %s]", decisionMade, resultsText, options);
    }
}