package com.example.decider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    boolean decisionMade = false;
    Button deciderButton;
    LinearLayout inputLayout;
    LinearLayout resultsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deciderButton = findViewById(R.id.decider_button);
        deciderButton.setText(getString(R.string.decider_button_label_before));

        inputLayout = findViewById(R.id.input_content);
        resultsLayout = findViewById(R.id.results_content);

        EditText nOptions = findViewById(R.id.nOptions);

        deciderButton.setOnClickListener(
                v -> handleClickDeciderButton(Integer.parseInt(nOptions.getText().toString()))
        );
    }

    void handleClickDeciderButton(int options) {
        if (!decisionMade) {
            Random random = new Random();
            int choice = random.nextInt(options) + 1;

            TextView resultsText = findViewById(R.id.resultsText);

            resultsText.setText(String.format("Choose option %d", choice));

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
}
