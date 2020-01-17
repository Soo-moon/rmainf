package com.example.rmain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class mean extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mean);


        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mean.this, word.class);
                startActivity(intent);
            }
        });//돌아가기버튼누르면 다시 단어 화면으로


        TextView meantv =findViewById(R.id.meantv);
        //db연결후 settext
        meantv.setText("");


    }
}
