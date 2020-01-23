package com.example.rmain;

import android.content.Context;
import android.widget.Toast;

public class Dan {
    String word;
    String mean;
    Context context;

    public Dan(String word, String mean, Context context) {
        this.word = word;
        this.mean = mean;
        this.context = context;
    }

    @Override
    public boolean equals(Object a) {
        Dan obj = (Dan) a;
        if (obj.word.equals(this.word) && obj.mean.equals(this.mean)) {
            Toast.makeText(context, "중복된 단어 입니다.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (word + mean).hashCode();
    }


}
