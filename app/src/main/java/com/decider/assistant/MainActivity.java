package com.decider.assistant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;


// TODO: refer to this: https://github.com/AppLovin/AppLovin-MAX-SDK-Android/tree/master/AppLovin%20MAX%20Demo%20App%20-%20Kotlin

public class MainActivity extends AppCompatActivity implements MaxAdViewAdListener {
    private TextView resultsText;
    private boolean decisionMade;

    private ListView optionsListView;
    private ViewGroup sceneRoot;

    private Scene optionsScene;
    private Scene resultsScene;

    private static final String LOG_TAG = "DeciderMain";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "Creating activity, retrieving preferences");
        }
        AppState appState = PreferenceStorer.retrievePreferences(this);
        applyAppState(appState);

        //TODO: convert to test ads!
//        setupAds();
        setupHandlers();

        if (decisionMade) {
            showDecisionView();
        } else {
            showOptionsListView();
        }
        Utils.setupUI(this, sceneRoot);
    }

    void applyAppState(AppState appState) {
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "Applying app state");
        }
        decisionMade = appState.getDecisionMade();
        resultsText.setText(appState.getResultsText());
        optionsListView.setAdapter(new OptionAdapter(this, appState.getOptions()));
    }

    AppState extractAppState() {
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "Extracting app state");
        }
        return new AppState(
            decisionMade,
            resultsText.getText().toString(),
            ((OptionAdapter) optionsListView.getAdapter()).getOptions()
        );
    }

    private void setupAds() {
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

    private void setupHandlers() {
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "Setting up handlers");
        }
        findViewById(R.id.decider_button).setOnClickListener(
            v -> handleClickDeciderButton()
        );
        findViewById(R.id.decide_again_button).setOnClickListener(
            v -> handleClickDecideAgainButton()
        );
        findViewById(R.id.add_option_button).setOnClickListener(
            v -> handleClickAddOptionButton(optionsListView)
        );
    }

    private void setupViews() {
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "Setting up views");
        }
        setContentView(R.layout.activity_main);
        optionsListView = findViewById(R.id.options_list);
        resultsText = findViewById(R.id.resultsText);
        sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        optionsScene = new Scene(sceneRoot, (ViewGroup) sceneRoot.findViewById(R.id.input_content));
        resultsScene = new Scene(sceneRoot, (ViewGroup) sceneRoot.findViewById(R.id.results_content));
    }

    @Override
    public void onPause() {
        AppState appState = extractAppState();
        if (BuildConfig.DEBUG) {
            Log.i(
                LOG_TAG,
                String.format("Pausing activity, storing preferences: %s", appState.toString())
            );
        }
        PreferenceStorer.storePreferences(this, appState);
        super.onPause();
    }

    private void handleClickDeciderButton() {
        ArrayList<Option> options = ((OptionAdapter) optionsListView.getAdapter()).getOptions();
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "Clicked 'Decide' button");
            Log.i(LOG_TAG, String.format("Options = %s", options));
        }
        if (options.isEmpty()) {
            return;
        }

        Random random = new SecureRandom();
        String choice = options.get(random.nextInt(options.size())).getDisplayText();
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, String.format("Chose %s", choice));
        }
        resultsText.setText(String.format("Choose: %s", choice));
        decisionMade = true;
        showDecisionView();
    }

    private void handleClickDecideAgainButton() {
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "Clicked 'DecideAgain' button");
        }
        decisionMade = false;
        showOptionsListView();
    }

    void showOptionsListView() {
        TransitionManager.go(optionsScene);
    }

    void showDecisionView() {
        TransitionManager.go(resultsScene);
    }

    private void handleClickAddOptionButton(ListView optionsListView) {
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "Clicked 'Add Option' button");
        }
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
