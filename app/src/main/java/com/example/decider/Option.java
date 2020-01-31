package com.example.decider;

class Option {
    String text;
    String placeHolder;

    Option(int optionNumber) {
        text = "";
        placeHolder = String.format("Option %d", optionNumber);
    }

    Option(String _text, String _placeHolder) {
        text = _text;
        placeHolder = _placeHolder;
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
