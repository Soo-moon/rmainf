package com.example.rmain;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.ss.formula.functions.T;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class dropword extends AppCompatActivity {
    private RelativeLayout container;
    private int x;
    private Animation.AnimationListener aniad;
    private ProgressBar progressBar;
    private Timer timer = new Timer();
    private String word;
    private String mean;
    private String sumst;
    private boolean stats=true;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropword);

        container = findViewById(R.id.dropview);
        progressBar = findViewById(R.id.progressBar);

        @SuppressLint("HandlerLeak")
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                try {
                    InputStream is = getBaseContext().getResources().getAssets().open("toeic.xls");
                    Workbook wb = Workbook.getWorkbook(is);

                    final Sheet sheet = wb.getSheet(0);
                    if (sheet != null) {
                        int colTotal = sheet.getColumns();
                        final int rowTotal = sheet.getColumn(colTotal - 1).length;
                        Random rand = new Random();
                        int r = rand.nextInt(rowTotal) + 1;
                        word = sheet.getCell(0, r).getContents();
                        mean = sheet.getCell(1, r).getContents();

                        x = (int) ((Math.random() * 500) + 1);
                        Thread make = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textview(x, word, mean);

                                    }
                                });
                            }
                        });
                        make.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (BiffException e) {
                    e.printStackTrace();
                }

            }
        };

        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
        };

        timer.schedule(tt, 0, 2000);
    }


    public void textview(int x, String word, final String mean) {
        final TextView tv = new TextView(this);

        tv.setText(word);
        tv.setTextSize(30);
        container.addView(tv);

        final Animation ani = new TranslateAnimation(x, x, 0, 1300);
        ani.setDuration(5000);

        ani.setFillAfter(false);
        tv.setAnimation(ani);
        final EditText  tev = findViewById(R.id.tev);
        Button smt = findViewById(R.id.smt);
        smt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumst =tev.getText().toString();
            }
        });

        aniad = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                    final Thread checkthread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (stats)
                                if (sumst == tv.getText().toString()){
                                    tv.clearAnimation();
                                    stats = false;
                                }
                        }
                    });
                    checkthread.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int t = progressBar.getProgress();

                progressBar.setProgress(t - 20);
                if (progressBar.getProgress() == 0) {
                    Toast.makeText(getApplicationContext(), "game over", Toast.LENGTH_LONG).show();
                    timer.cancel();
                }
                tv.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        };

        ani.setAnimationListener(aniad);
        ani.start();


    }
}




