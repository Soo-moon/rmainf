package com.example.rmain;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

public class NextBack implements View.OnClickListener {
    int rowmax;
    Context context;
    word word = new word();
    int row = word.row;


    public NextBack(int rowmax, Context context) {
        this.rowmax = rowmax;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.backButton) {
            if (row == 1)
                Toast.makeText(context, "첫번째단어입니다.", Toast.LENGTH_SHORT).show();
            else {
                row--;
                word.getSheet(row);
            }
        }
        if (id == R.id.nextButton) {
            if (row == rowmax)
                Toast.makeText(context, "마지막 단어 입니다.", Toast.LENGTH_SHORT).show();
            else {
                row++;
                word.getSheet(row);

            }
        }
    }
}

