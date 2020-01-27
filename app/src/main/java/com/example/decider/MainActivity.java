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

    TextView resultsText;

    private AdView mAdView;

    private Option defaultOptionText(int optionNumber) {
        return new Option(optionNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < INTIAL_OPTION_COUNT; i++) {
            options.add(defaultOptionText(i + 1));
        }

        ListView optionsListView = findViewById(R.id.options_list);
        optionsListView.setAdapter(new OptionAdapter(this, options));

        LinearLayout inputLayout = findViewById(R.id.input_content);
        LinearLayout resultsLayout = findViewById(R.id.results_content);

        findViewById(R.id.decider_button).setOnClickListener(
                v -> handleClickDeciderButton(inputLayout, resultsLayout)
        );
        findViewById(R.id.decide_again_button).setOnClickListener(
                v -> handleClickDecideAgainButton(inputLayout, resultsLayout)
        );
        findViewById(R.id.add_option_button).setOnClickListener(
                v -> handleClickAddOptionButton(optionsListView)
        );

        resultsText = findViewById(R.id.resultsText);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    void handleClickDeciderButton(LinearLayout inputLayout, LinearLayout resultsLayout) {
        if (options.size() == 0) {
            return;
        }

        Random random = new Random();
        String choice = options.get(random.nextInt(options.size())).getText();
        resultsText.setText("Choose: " + choice);

        inputLayout.setVisibility(LinearLayout.GONE);
        resultsLayout.setVisibility(LinearLayout.VISIBLE);
    }

    void handleClickDecideAgainButton(LinearLayout inputLayout, LinearLayout resultsLayout) {
        inputLayout.setVisibility(LinearLayout.VISIBLE);
        resultsLayout.setVisibility(LinearLayout.GONE);
    }

    void handleClickAddOptionButton(ListView optionsListView) {
        options.add(defaultOptionText(options.size() + 1));
        optionsListView.invalidateViews();
    }
}
