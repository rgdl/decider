package com.example.decider;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;

import java.util.ArrayList;

public class OptionAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Option> options;
    private Context context;

    public OptionAdapter(Context context, ArrayList<Option> options) {
        this.options = options;
        this.context = context;
    }

    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public Option getItem(int pos) {
        return options.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.options_list_item, null);

        Option option = getItem(position);
        EditText optionText = view.findViewById(R.id.option_text);
        optionText.setHint(option.getText());

        optionText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void afterTextChanged(Editable s) {
                option.setText(s.toString());
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        Button deleteButton = view.findViewById(R.id.delete_option_button);
        deleteButton.setOnClickListener(v -> {
            deleteOption(position);
        });

        return view;
    }

    void addOption() {
        options.add(new Option(options.size() + 1));
        notifyDataSetChanged();
    }

    void deleteOption(int position) {
        options.remove(position);
        notifyDataSetChanged();
    }
}
