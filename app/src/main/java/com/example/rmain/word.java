package com.example.rmain;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


import static android.speech.tts.TextToSpeech.ERROR;

 public  class  word extends AppCompatActivity {

    public static int row = 1;
    static String fword = null;
    static String fmean = null;
    private TextToSpeech tts;
    static Sheet sheet;
    static TextView wordtext;
    static TextView meantext;


    DBHelper dbHelper = new DBHelper(this, "Myword.db", null, 1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word);


        final TextView dataview = findViewById(R.id.dataview);

        //
        dbHelper.creatset();
        String data = dbHelper.getResult();
        dataview.setText(data);
        //추가

        try {
            InputStream is = getBaseContext().getResources().getAssets().open("toeic.xls");
            Workbook wb = Workbook.getWorkbook(is);

            if (wb != null) {
                sheet = wb.getSheet(0);
                if (sheet != null) {
                    int colTotal = sheet.getColumns();
                    final int rowTotal = sheet.getColumn(colTotal - 1).length;

                    Button.OnClickListener listener = new NextBack(rowTotal,getApplicationContext());

                    wordtext = findViewById(R.id.wordTextview);
                    meantext = findViewById(R.id.meansTextview);
                    Button addword = findViewById(R.id.addwordButton);
                    Button nextbutton = findViewById(R.id.nextButton);
                    Button backbutton = findViewById(R.id.backButton);
                    Button voicebutton = findViewById(R.id.voiceButton);

                    nextbutton.setOnClickListener(listener);
                    backbutton.setOnClickListener(listener);
                    getSheet(row);

                    tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status != ERROR) {
                                tts.setLanguage(Locale.US);
                            }
                        }
                    });
                    voicebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tts.speak(wordtext.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                        }
                    });
                    addword.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dbHelper.add(fword, fmean);
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

    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }

    }

    protected void onStop() {
        super.onStop();
        dbHelper.insert();
    }

    public static void getSheet(int row) {
        fmean = sheet.getCell(1, row).getContents();
        fword = sheet.getCell(0, row).getContents();
        wordtext.setText(fword);
        meantext.setText(fmean);
    }




}
