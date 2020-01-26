package com.example.decider;

class Option {
    private String text;
    private String placeHolder;

    Option(int optionNumber) {
        text = "";
        placeHolder = String.format("Option %d", optionNumber);
    }

    String getText() {
        return text.equals("") ? placeHolder : text;
    }

    void setText(String newText) {
        text = newText;
    }

    @Override
    public String toString() {
        return getText();
    }
}
