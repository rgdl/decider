package com.example.decider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button deciderButton = (Button) findViewById(R.id.deciderButton);
        EditText nOptions = (EditText) findViewById(R.id.nOptions);

        deciderButton.setOnClickListener(
                v -> handleClickDeciderButton(Integer.parseInt(nOptions.getText().toString()))
        );

    }

    void handleClickDeciderButton(int options) {
        Log.d("HERE!", "decided...");
    }
}
