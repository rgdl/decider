package com.example.decider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    boolean decisionMade = false;
    Button deciderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deciderButton = (Button) findViewById(R.id.deciderButton);
        deciderButton.setText(getString(R.string.decider_button_label_before));
        EditText nOptions = (EditText) findViewById(R.id.nOptions);

        deciderButton.setOnClickListener(
                v -> handleClickDeciderButton(Integer.parseInt(nOptions.getText().toString()))
        );
    }

    void handleClickDeciderButton(int options) {
        if (!decisionMade) {
            Random random = new Random();
            int choice = random.nextInt(options) + 1;

            TextView resultsText = (TextView) findViewById(R.id.resultsText);

            resultsText.setText(String.format("Choose option %d", choice));

            decisionMade = true;
            deciderButton.setText(getString(R.string.decider_button_label_after));
        } else {
            decisionMade = false;
            deciderButton.setText(getString(R.string.decider_button_label_before));
        }

    }
}
