package com.vedangj044.statusview.ModelObject;

import org.jetbrains.annotations.NotNull;

public class TextStatusObject extends StatusObject {

    private String Content, ColorString;
    private int FontNumber;

    /*
    * Content = Main text that has to be displayed
    * ColorString = String of color code that has to set has background
    * FontNumber = Int value of the font resource
    * */

    public TextStatusObject(String content, String colorString, int fontNumber) {
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
