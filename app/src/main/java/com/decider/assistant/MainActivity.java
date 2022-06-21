package com.decider.assistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements MaxAdViewAdListener {
    private static final int INTIAL_OPTION_COUNT = 2;

    ArrayList<Option> options = new ArrayList<>();

    String DECIDER_SHARED_PREFERENCES = "DECIDER_SHARED_PREFERENCES";
    String DECISION_MADE_KEY = "DECISION_MADE";
    String RESULTS_TEXT_KEY = "RESULTS_TEXT";
    String OPTIONS_LIST_LABELS_KEY = "OPTIONS_LIST_LABELS";
    String OPTIONS_LIST_PLACEHOLDERS_KEY = "OPTIONS_LIST_PLACEHOLDERS";

    TextView resultsText;
    LinearLayout inputLayout;
    LinearLayout resultsLayout;
    ListView optionsListView;
    ViewGroup sceneRoot;

    boolean decisionMade = false;

    private Scene optionsScene;
    private Scene resultsScene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAds();
        setupViews();
        setupHandlers();
        retrievePreferences();

        if (decisionMade) {
            showDecisionView();
        } else {
            showOptionsListView();
        }

        Utils.setupUI(this, sceneRoot);
    }

    void setupAds() {
        MaxAdView adView = new MaxAdView(Utils.AD_UNIT_ID, this);
        adView.setListener(this);

        // Stretch to the width of the screen for banners to be fully functional
        int width = ViewGroup.LayoutParams.MATCH_PARENT;

        // Banner height on phones and tablets is 50 and 90, respectively
        int heightPx = getResources().getDimensionPixelSize(R.dimen.banner_height);

        adView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));

        // Set background or background color for banners to be fully functional
        adView.setBackgroundColor(getResources().getColor(R.color.colorDark));

        ViewGroup rootView = findViewById(R.id.ad_wrapper);
        rootView.addView(adView);

        // Load the ad
        adView.loadAd();
    }

    void setupHandlers() {
        findViewById(R.id.decider_button).setOnClickListener(v -> handleClickDeciderButton());
        findViewById(R.id.decide_again_button).setOnClickListener(v -> handleClickDecideAgainButton());
        findViewById(R.id.add_option_button).setOnClickListener(v -> handleClickAddOptionButton(optionsListView));
    }

    void setupViews() {
        setContentView(R.layout.activity_main);

        optionsListView = findViewById(R.id.options_list);
        optionsListView.setAdapter(new OptionAdapter(this, options));

        inputLayout = findViewById(R.id.input_content);
        resultsLayout = findViewById(R.id.results_content);
        resultsText = findViewById(R.id.resultsText);

        sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        optionsScene = new Scene(sceneRoot, (ViewGroup) sceneRoot.findViewById(R.id.input_content));
        resultsScene = new Scene(sceneRoot, (ViewGroup) sceneRoot.findViewById(R.id.results_content));
    }

    @Override
    public void onPause() {
        storePreferences();
        super.onPause();
    }

    void storePreferences() {
        SharedPreferences prefs = getSharedPreferences(DECIDER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();

        editor.putInt(DECISION_MADE_KEY, decisionMade ? 1 : 0);
        editor.putString(RESULTS_TEXT_KEY, String.valueOf(resultsText.getText()));

        JSONArray labels = new JSONArray();
        JSONArray placeholders = new JSONArray();
        for (int i = 0; i < options.size(); i++) {
            labels.put(options.get(i).text);
            placeholders.put(options.get(i).placeHolder);
        }
        editor.putString(OPTIONS_LIST_LABELS_KEY, labels.toString());
        editor.putString(OPTIONS_LIST_PLACEHOLDERS_KEY, placeholders.toString());

        editor.apply();
    }

    void retrievePreferences() {
        SharedPreferences prefs = getSharedPreferences(DECIDER_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        decisionMade = prefs.getInt(DECISION_MADE_KEY, 0) == 1;
        resultsText.setText(prefs.getString(RESULTS_TEXT_KEY, ""));

        try {
            String optionsListLabelsString = prefs.getString(OPTIONS_LIST_LABELS_KEY, "");
            String optionListPlaceholdersString = prefs.getString(OPTIONS_LIST_PLACEHOLDERS_KEY, "");

            if (optionsListLabelsString.equals("")) {
                throw new JSONException("No existing options data - is this the first time?");
            }

            JSONArray optionListLabels = new JSONArray(optionsListLabelsString);
            JSONArray optionListPlaceholders = new JSONArray(optionListPlaceholdersString);
            for (int i = 0; i < optionListLabels.length(); i++) {
                String label = optionListLabels.get(i).toString();
                String placeHolder = optionListPlaceholders.get(i).toString();
                options.add(new Option(label, placeHolder));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            // Default starting Options
            for (int i = 0; i < INTIAL_OPTION_COUNT; i++) {
                options.add(new Option(i + 1));
            }
        }
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
        TransitionManager.go(optionsScene);
    }

    void showDecisionView() {
        TransitionManager.go(resultsScene);
    }

    void handleClickAddOptionButton(ListView optionsListView) {
        ((OptionAdapter) optionsListView.getAdapter()).addOption();
    }

    // MAX Ad Listener
    @Override
    public void onAdLoaded(final MaxAd maxAd) {}

    @Override
    public void onAdLoadFailed(final String adUnitId, final MaxError error) {}

    @Override
    public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error) {}

    @Override
    public void onAdClicked(final MaxAd maxAd) {}

    @Override
    public void onAdExpanded(final MaxAd maxAd) {}

    @Override
    public void onAdCollapsed(final MaxAd maxAd) {}

    @Override
    public void onAdDisplayed(final MaxAd maxAd) { /* DO NOT USE - THIS IS RESERVED FOR FULLSCREEN ADS ONLY AND WILL BE REMOVED IN A FUTURE SDK RELEASE */ }

    @Override
    public void onAdHidden(final MaxAd maxAd) { /* DO NOT USE - THIS IS RESERVED FOR FULLSCREEN ADS ONLY AND WILL BE REMOVED IN A FUTURE SDK RELEASE */ }
}
