package com.vedangj044.statusview;

import org.jetbrains.annotations.NotNull;

public class text_status_object {

    private String Content, ColorString;
    private int FontNumber;

    public text_status_object(String content, String colorString, int fontNumber) {
        Content = content;
        ColorString = colorString;
        FontNumber = fontNumber;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getColorString() {
        return ColorString;
    }

    public void setColorString(String colorString) {
        ColorString = colorString;
    }

    public int getFontNumber() {
        return FontNumber;
    }

    public void setFontNumber(int fontNumber) {
        FontNumber = fontNumber;
    }

    @NotNull
    @Override
    public String toString() {
        return "text_status_object{" +
                "Content='" + Content + '\'' +
                ", ColorString='" + ColorString + '\'' +
                ", FontNumber=" + FontNumber +
                '}';
    }
}
