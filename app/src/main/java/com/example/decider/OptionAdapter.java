package com.example.decider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OptionAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> options;
    private Context context;

    public OptionAdapter(Context context, ArrayList<String> options) {
        this.options= options;
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

        TextView optionText = view.findViewById(R.id.option_text);
        optionText.setText(options.get(position));

        return view;
    }
}
