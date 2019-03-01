package com.example.todolist;

import android.graphics.drawable.Drawable;

import java.text.SimpleDateFormat;

public class Item {
    boolean checked;
    String text;


    public Item(String text, boolean b){
        this.text = text;
        checked = b;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked){
        this.checked = checked;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

}
