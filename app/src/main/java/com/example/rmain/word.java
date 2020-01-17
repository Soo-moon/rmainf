package com.example.rmain;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


import static android.speech.tts.TextToSpeech.ERROR;

public class word extends AppCompatActivity {

    private int row = 1;
    private String fword = null;
    private String fmean = null;
    private TextToSpeech tts;
    DBHelper dbHelper = new DBHelper(getApplicationContext(),"Myword.db",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word);


        final TextView dataview =findViewById(R.id.dataview);

        //
        dbHelper.creatset();
        String data = dbHelper.getResult();
        dataview.setText(data);
        //추가

        try {
            InputStream is = getBaseContext().getResources().getAssets().open("toeic.xls");
            Workbook wb = Workbook.getWorkbook(is);

            if (wb != null) {
                final Sheet sheet = wb.getSheet(0);
                if (sheet != null) {
                    int colTotal = sheet.getColumns();
                    final int rowTotal = sheet.getColumn(colTotal-1).length;

                    final TextView wordtext = findViewById(R.id.wordTextview);
                    final TextView meantext = findViewById(R.id.meansTextview);
                    Button addword = findViewById(R.id.addwordButton);
                    Button nextbutton = findViewById(R.id.nextButton);
                    Button backbutton = findViewById(R.id.backButton);
                    Button voicebutton =findViewById(R.id.voiceButton);

                    fword = sheet.getCell(0,row).getContents();
                    wordtext.setText(fword);

                    fmean = sheet.getCell(1,row).getContents();
                    meantext.setText(fmean);

                    nextbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           if (row == rowTotal){
                               Toast.makeText(getApplicationContext(),"마지막단어입니다.",Toast.LENGTH_SHORT).show();
                           }
                           else {
                               row++;
                               fmean = sheet.getCell(1,row).getContents();
                               fword = sheet.getCell(0,row).getContents();

                               wordtext.setText(fword);
                               meantext.setText(fmean);
                           }
                        }
                    });

                    backbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (row == 1){
                                Toast.makeText(getApplicationContext(),"첫번째단어입니다.",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                row--;
                                fmean = sheet.getCell(1, row).getContents();
                                fword = sheet.getCell(0, row).getContents();

                                wordtext.setText(fword);
                                meantext.setText(fmean);
                            }
                        }
                    });

                    tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != ERROR){
                                tts.setLanguage(Locale.US);
                            }
                        }
                    });

                    voicebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                tts.speak(wordtext.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
                        }
                    });



                    addword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dbHelper.add(fword,fmean);
                            dataview.setText(dbHelper.getResult());
                        }
                    });


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

    }
protected  void onDestroy(){
        super.onDestroy();
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts=null;
        }
        dbHelper.insert();
}



}
