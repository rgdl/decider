package com.example.decider;

class Option {
    private String text;

    Option(int optionNumber) {
        text = String.format("option %d", optionNumber);
    }

    String getText() {
        return text;
    }

    void setText(String newText) {
        text = newText;
    }
}
