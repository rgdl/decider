package com.decider.assistant;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

class PreferenceStorer {
    private static final String DECIDER_SHARED_PREFERENCES = "DECIDER_SHARED_PREFERENCES";
    private static final String DECISION_MADE_KEY = "DECISION_MADE";
    private static final String RESULTS_TEXT_KEY = "RESULTS_TEXT";
    private static final String OPTIONS_LIST_LABELS_KEY = "OPTIONS_LIST_LABELS";
    private static final String OPTIONS_LIST_PLACEHOLDERS_KEY = "OPTIONS_LIST_PLACEHOLDERS";
    private static final String LOG_TAG = "PreferenceStorer";

    private static void storeDecisionMade(SharedPreferences.Editor editor, AppState appState) {
        int valueToStore = appState.getDecisionMade() ? 1 : 0;
        if (BuildConfig.DEBUG) {
            Log.i(
                LOG_TAG,
                String.format("Storing decisionMade: %d", valueToStore)
            );
        }
        editor.putInt(DECISION_MADE_KEY, valueToStore);
    }

    private static void storeResultsText(SharedPreferences.Editor editor, AppState appState) {
        String valueToStore = appState.getResultsText();
        if (BuildConfig.DEBUG) {
            Log.i(
                LOG_TAG,
                String.format("Storing resultsText: %s", valueToStore)
            );
        }
        editor.putString(RESULTS_TEXT_KEY, valueToStore);
    }

    private static void storeOptions(SharedPreferences.Editor editor, AppState appState) {
        JSONArray labels = new JSONArray();
        JSONArray placeholders = new JSONArray();
        List<Option> options = appState.getOptions();
        for (int i = 0; i < options.size(); i++) {
            labels.put(options.get(i).getText());
            placeholders.put(options.get(i).getPlaceholder());
        }
        if (BuildConfig.DEBUG) {
            Log.i(
                LOG_TAG,
                String.format("Storing optionLabels: %s", labels)
            );
            Log.i(
                LOG_TAG,
                String.format("Storing optionHints: %s", placeholders)
            );
        }
        editor.putString(OPTIONS_LIST_LABELS_KEY, labels.toString());
        editor.putString(OPTIONS_LIST_PLACEHOLDERS_KEY, placeholders.toString());
    }

    static void storePreferences(Context context, AppState appState) {
        SharedPreferences prefs = context.getSharedPreferences(
            DECIDER_SHARED_PREFERENCES, Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        storeDecisionMade(editor, appState);
        storeResultsText(editor, appState);
        storeOptions(editor, appState);

        editor.apply();
    }

    private static boolean preferencesAreEmpty(SharedPreferences prefs) {
        return !(prefs.contains(DECISION_MADE_KEY)
            || prefs.contains(RESULTS_TEXT_KEY)
            || prefs.contains(OPTIONS_LIST_LABELS_KEY)
            || prefs.contains(OPTIONS_LIST_PLACEHOLDERS_KEY));
    }

    private static boolean retrieveDecisionMade(SharedPreferences prefs) {
        int decisionMadeInt = prefs.getInt(DECISION_MADE_KEY, -1);
        boolean decisionMade = (decisionMadeInt < 0)
            ? AppState.defaultDecisionMade()
            : (decisionMadeInt == 1);
        if (BuildConfig.DEBUG) {
            Log.i(
                LOG_TAG,
                String.format(
                    "Retrieving decisionMade: %s, parsed to (%s)",
                    decisionMadeInt,
                    decisionMade
                )
            );
        }
        return decisionMade;
    }

    private static String retrieveResultsText(SharedPreferences prefs) {
        String resultsText = prefs.getString(RESULTS_TEXT_KEY, AppState.defaultResultsText());
        if (BuildConfig.DEBUG) {
            Log.i(
                LOG_TAG,
                String.format("Retrieving resultsText: %s", resultsText)
            );
        }
        return resultsText;
    }

    private static ArrayList<Option> retrieveOptions(SharedPreferences prefs) {
        ArrayList<Option> options = new ArrayList<>(10);

        try {
            String optionLabelsString = prefs.getString(OPTIONS_LIST_LABELS_KEY, "");
            String optionHintsString = prefs.getString(OPTIONS_LIST_PLACEHOLDERS_KEY, "");

            String err = "No existing options data";
            assert !(optionLabelsString.isEmpty() || optionHintsString.isEmpty()) : err;

            JSONArray optionLabels = new JSONArray(optionLabelsString);
            JSONArray optionHints = new JSONArray(optionHintsString);
            if (BuildConfig.DEBUG) {
                Log.i(
                    LOG_TAG,
                    String.format("Retrieving optionLabels: %s", optionLabels)
                );
                Log.i(
                    LOG_TAG,
                    String.format("Retrieving optionHints: %s", optionHints)
                );
            }
            int nOptions = Math.min(optionLabels.length(), optionHints.length());
            for (int i = 0; i < nOptions; i++) {
                String label = optionLabels.get(i).toString();
                String placeHolder = optionHints.get(i).toString();
                options.add(new Option(label, placeHolder));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error loading options, using defaults");
            // Default starting Options
            options = AppState.defaultOptions();
        }
        return options;
    }

    static AppState retrievePreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
            DECIDER_SHARED_PREFERENCES, Context.MODE_PRIVATE
        );
        if (preferencesAreEmpty(prefs)) {
            return new AppState();
        }
        return new AppState(
            retrieveDecisionMade(prefs),
            retrieveResultsText(prefs),
            retrieveOptions(prefs)
        );
    }
}
