package com.decider.assistant;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubView;

import java.util.ArrayList;
import java.util.Random;

import static com.mopub.common.logging.MoPubLog.LogLevel.NONE;

public class MainActivity extends AppCompatActivity implements MoPubView.BannerAdListener {
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

    private Scene optionsScene;
    private Scene resultsScene;

    // Setup for MoPub Ad
    private MoPubView bannerAd;

    private SdkInitializationListener initSdkListener() {
        return () -> {
            // SDK initialization complete. You may now request ads.
        };
    }

    @Override
    public void onBannerLoaded(MoPubView bannerAd) {}
    @Override
    public void onBannerFailed(MoPubView moPubView, MoPubErrorCode moPubErrorCode) {}
    @Override
    public void onBannerClicked(MoPubView moPubView) {}
    @Override
    public void onBannerExpanded(MoPubView moPubView) {}
    @Override
    public void onBannerCollapsed(MoPubView moPubView) {}

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

        ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        optionsScene = new Scene(sceneRoot, (ViewGroup) sceneRoot.findViewById(R.id.input_content));
        resultsScene = new Scene(sceneRoot, (ViewGroup) sceneRoot.findViewById(R.id.results_content));

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
                options.add(new Option(i + 1));
            }
            showOptionsListView();
        }

        final SdkConfiguration.Builder configBuilder = new SdkConfiguration.Builder(Utils.AD_UNIT_ID);

        configBuilder.withLogLevel(NONE);
        MoPub.initializeSdk(this, configBuilder.build(), initSdkListener());

        bannerAd = (MoPubView) findViewById(R.id.adview);
        bannerAd.setAdUnitId(Utils.AD_UNIT_ID);
        bannerAd.loadAd();
        bannerAd.setBannerAdListener(this);

        Utils.setupUI(this, sceneRoot);
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
        TransitionManager.go(optionsScene);
    }

    void showDecisionView() {
        TransitionManager.go(resultsScene);
    }

    void handleClickAddOptionButton(ListView optionsListView) {
        ((OptionAdapter) optionsListView.getAdapter()).addOption();
    }
}
