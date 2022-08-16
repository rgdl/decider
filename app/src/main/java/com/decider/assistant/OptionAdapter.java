package com.decider.assistant;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class OptionAdapter extends BaseAdapter {
    private final ArrayList<Option> options;
    private final Context context;

    private static final String LOG_TAG = "OptionAdapter";

        OptionAdapter(Context context, ArrayList<Option> options) {
        this.context = context;
        this.options = options;
    }

    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public Option getItem(int position) {
        return options.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.options_list_item, null);

        Option option = getItem(position);
        EditText optionText = view.findViewById(R.id.option_text);
        optionText.setHint(option.getDisplayText());

        optionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (BuildConfig.DEBUG) {
                    Log.i(LOG_TAG, String.format("Update text to '%s'", s));
                }
                option.setText(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        Button deleteButton = view.findViewById(R.id.delete_option_button);
        deleteButton.setOnClickListener(v -> {
            deleteOption(position);
        });

        return view;
    }

    void addOption() {
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "Adding option");
        }
        options.add(new Option(options.size() + 1));
        notifyDataSetChanged();
    }

    private void deleteOption(int position) {
        if (BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "Deleting option");
        }
        options.remove(position);
        notifyDataSetChanged();
    }

    ArrayList<Option> getOptions() {
        return options;
    }
}
