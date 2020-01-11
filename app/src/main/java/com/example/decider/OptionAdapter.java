package com.example.decider;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

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
    public Object getItem(int pos) {
        return options.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.options_list_item, null);
        }

        Button deleteButton = view.findViewById(R.id.delete_option_button);
        deleteButton.setOnClickListener(v -> {
            options.remove(position);
            notifyDataSetChanged();
        });

        EditText optionText = view.findViewById(R.id.option_text);
        optionText.setHint(options.get(position).getText());

        optionText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void afterTextChanged(Editable s) {
                options.get(position).setText(s.toString());
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        return view;
    }
}
