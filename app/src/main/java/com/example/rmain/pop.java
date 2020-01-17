package com.example.rmain;

import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class pop extends AppCompatActivity {
    static boolean stopbutton = false;
    private String mean;
    private String word;
    private boolean stbutton = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop);

        try {
            InputStream is = getBaseContext().getResources().getAssets().open("toeic.xls");
            Workbook wb = Workbook.getWorkbook(is);

            if (wb != null) {
                final Sheet sheet = wb.getSheet(0);
                if (sheet != null) {
                    int colTotal = sheet.getColumns();
                    final int rowTotal = sheet.getColumn(colTotal-1).length;


                    final TextView wordview = findViewById(R.id.word);
                    final TextView meanview = findViewById(R.id.mean);

                    final Button stop = findViewById(R.id.stop);
                    stop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           if(stbutton == true){
                               stbutton=false;
                               stop.setText("》");
                           }
                           else {
                               stbutton=true;
                               stop.setText("∥");
                           }
                        }
                    });

                    final Timer timer = new Timer();
                    final TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run()
                        {
                            if(stbutton == true){
                                Random rand = new Random();
                                int r = rand.nextInt(rowTotal)+1;

                                word = sheet.getCell(0,r).getContents();
                                mean = sheet.getCell(1,r).getContents();

                                meanview.setText(mean);
                                wordview.setText(word);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                meanview.setText(mean);
                                                wordview.setText(word);
                                            }
                                        });
                                    }
                                });
                            }

                        }
                    };
                    timer.schedule(timerTask,0,2700);


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }

    }
}
