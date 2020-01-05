package com.example.decider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int INTIAL_OPTION_COUNT = 2;

    boolean decisionMade = false;
    ArrayList<String> options = new ArrayList<>();

    TextView resultsText;

    private String defaultOptionText(int optionNumber) {
        return String.format("option %d", optionNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < INTIAL_OPTION_COUNT; i++) {
            options.add(defaultOptionText(i + 1));
        }

        ListView optionsListView = findViewById(R.id.options_list);
        optionsListView.setAdapter(new ArrayAdapter<>(this, R.layout.options_list_item, options));

        LinearLayout inputLayout = findViewById(R.id.input_content);
        LinearLayout resultsLayout = findViewById(R.id.results_content);
        Button deciderButton = findViewById(R.id.decider_button);
        deciderButton.setText(getString(R.string.decider_button_label_before));
        deciderButton.setOnClickListener(v -> handleClickDeciderButton(deciderButton, inputLayout, resultsLayout));

        Button addOptionButton = findViewById(R.id.add_option_button);
        addOptionButton.setOnClickListener(v -> handleClickAddOptionButton(optionsListView));

        resultsText = findViewById(R.id.resultsText);
    }

    void handleClickDeciderButton(Button deciderButton, LinearLayout inputLayout, LinearLayout resultsLayout) {
        if (!decisionMade) {
            Random random = new Random();
            String choice = options.get(random.nextInt(options.size()));
            resultsText.setText("Choose: " + choice);

            decisionMade = true;
            deciderButton.setText(getString(R.string.decider_button_label_after));
            inputLayout.setVisibility(LinearLayout.GONE);
            resultsLayout.setVisibility(LinearLayout.VISIBLE);
        } else {
            decisionMade = false;
            deciderButton.setText(getString(R.string.decider_button_label_before));
            inputLayout.setVisibility(LinearLayout.VISIBLE);
            resultsLayout.setVisibility(LinearLayout.GONE);
        }

    }

    void handleClickAddOptionButton(ListView optionsListView) {
        options.add(defaultOptionText(options.size() + 1));
        optionsListView.invalidateViews();
    }
}
