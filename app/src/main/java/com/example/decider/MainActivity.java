package com.example.decider;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int INTIAL_OPTION_COUNT = 2;

    ArrayList<Option> options = new ArrayList<>();

    String DECISION_MADE_KEY = "DECISION_MADE";
    String RESULTS_TEXT_KEY = "RESULTS_TEXT";
    String OPTIONS_LIST_LABELS_KEY = "OPTIONS_LIST_LABELS";
    String OPTIONS_LIST_PLACEHOLDERS_KEY = "OPTIONS_LIST_PLACEHOLDERS";

    TextView resultsText;

    LinearLayout inputLayout;
    LinearLayout resultsLayout;

    boolean decisionMade = false;

    private AdView mAdView;

    private Option defaultOptionText(int optionNumber) {
        return new Option(optionNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView optionsListView = findViewById(R.id.options_list);
        optionsListView.setAdapter(new OptionAdapter(this, options));

        inputLayout = findViewById(R.id.input_content);
        resultsLayout = findViewById(R.id.results_content);

        findViewById(R.id.decider_button).setOnClickListener(v -> handleClickDeciderButton());
        findViewById(R.id.decide_again_button).setOnClickListener(v -> handleClickDecideAgainButton());
        findViewById(R.id.add_option_button).setOnClickListener(v -> handleClickAddOptionButton(optionsListView));

        resultsText = findViewById(R.id.resultsText);

        if (savedInstanceState != null) {
            // Retrieve the necessary state data for resuming where we left off:
            // - options list
            // - results text
            // - decisionMade

            decisionMade = (savedInstanceState.getInt(DECISION_MADE_KEY) == 1);
            resultsText.setText(savedInstanceState.getString(RESULTS_TEXT_KEY));

            ArrayList<String> optionListLabels = savedInstanceState.getStringArrayList(OPTIONS_LIST_LABELS_KEY);
            ArrayList<String> optionListPlaceholders = savedInstanceState.getStringArrayList(OPTIONS_LIST_PLACEHOLDERS_KEY);

            for (int i = 0; i < optionListLabels.size(); i++) {
                options.add(new Option(optionListLabels.get(i), optionListPlaceholders.get(i)));
            }

            if (decisionMade) {
                showDecisionView();
            } else {
                showOptionsListView();
            }
        } else {
            // default values
            for (int i = 0; i < INTIAL_OPTION_COUNT; i++) {
                options.add(defaultOptionText(i + 1));
            }
            showOptionsListView();
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onSaveInstanceState(Bundle stateToSave) {
        stateToSave.putInt(DECISION_MADE_KEY, decisionMade ? 1 : 0);
        stateToSave.putString(RESULTS_TEXT_KEY, String.valueOf(resultsText.getText()));

        // No need to store whole Option, just store the text and placeholders, then recreate
        ArrayList<String> optionListLabels = new ArrayList<>();
        ArrayList<String> optionListPlaceholders = new ArrayList<>();
        for (Option option : options) {
            optionListLabels.add(option.text);
            optionListPlaceholders.add(option.placeHolder);
        }
        stateToSave.putStringArrayList(OPTIONS_LIST_LABELS_KEY, optionListLabels);
        stateToSave.putStringArrayList(OPTIONS_LIST_PLACEHOLDERS_KEY, optionListPlaceholders);
        super.onSaveInstanceState(stateToSave);
    }

    void handleClickDeciderButton() {
        if (options.size() == 0) {
            return;
        }

        Random random = new Random();
        String choice = options.get(random.nextInt(options.size())).getText();
        resultsText.setText("Choose: " + choice);
        decisionMade = true;
        showDecisionView();
    }

    void handleClickDecideAgainButton() {
        decisionMade = false;
        showOptionsListView();
    }

    void showOptionsListView() {
        inputLayout.setVisibility(LinearLayout.VISIBLE);
        resultsLayout.setVisibility(LinearLayout.GONE);
    }

    void showDecisionView() {
        inputLayout.setVisibility(LinearLayout.GONE);
        resultsLayout.setVisibility(LinearLayout.VISIBLE);
    }

    void handleClickAddOptionButton(ListView optionsListView) {
        options.add(defaultOptionText(options.size() + 1));
        optionsListView.invalidateViews();
    }
}
