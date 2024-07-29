package com.example.kj_project;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class GameActivity extends Activity {
    TextView time;
    TextView[] word;

    String[] textEng, textKor, fileNames;

    List<String> Random;
    int state = 0;
    int temp = -1;
    int selectedEngIndex = -1;
    int selectedKorIndex = -1;
    CountDownTimer timer;
    boolean gameSolved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        time = findViewById(R.id.time);
        word = new TextView[]{
                findViewById(R.id.word1),
                findViewById(R.id.word2),
                findViewById(R.id.word3),
                findViewById(R.id.word4),
                findViewById(R.id.word5),
                findViewById(R.id.word6),
                findViewById(R.id.word7),
                findViewById(R.id.word8),
                findViewById(R.id.word9),
                findViewById(R.id.word10),
        };

        Random = new ArrayList<>();
        textEng = new String[5];
        textKor = new String[5];
        fileNames = new String[10];

        int count = 0;


        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        for (int j = 0; j < 5; j++) {
            fileNames[j * 2] = cYear + "_" + (cMonth + 1) + "_" + cDay + "_" + (j + 1) + ".txt";
            fileNames[j * 2 + 1] = cYear + "_" + (cMonth + 1) + "_" + cDay + "_" + (j + 6) + ".txt";
            String str1 = readWord(fileNames[j * 2]);
            String str2 = readWord(fileNames[j * 2 + 1]);

            if (str1 == null || str2 == null) continue;
            if (str1.equals("") || str2.equals("")) continue;

            Random.add(str1);
            Random.add(str2);
            textEng[count] = str1;
            textKor[count] = str2;
            count++;
        }

        Collections.shuffle(Random);

        for (int i = 0; i < 10 ; i++) {
            word[i].setText(Random.get(i));
        }

        startTimer();

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                finish();
            }
        });

        for (int i = 0; i < word.length; i++) {
            final int index = i;
            word[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (state == 0) {
                        word[index].setBackgroundColor(Color.parseColor("#D3D3D3"));
                        temp = index;
                        state = 1;

                        if (Arrays.asList(textEng).contains(word[index].getText().toString()))
                            selectedEngIndex = Arrays.asList(textEng).indexOf(word[index].getText().toString());
                        else selectedKorIndex = Arrays.asList(textKor).indexOf(word[index].getText().toString());

                    }
                    else if (state == 1) {
                        word[temp].setBackgroundColor(Color.parseColor("#FFFFFF"));

                        if (Arrays.asList(textEng).contains(word[index].getText().toString()))
                            selectedEngIndex = Arrays.asList(textEng).indexOf(word[index].getText().toString());
                        else selectedKorIndex = Arrays.asList(textKor).indexOf(word[index].getText().toString());

                        if (selectedEngIndex != -1 && selectedKorIndex != -1 && selectedEngIndex == selectedKorIndex) {
                            word[temp].setVisibility(View.INVISIBLE);
                            word[index].setVisibility(View.INVISIBLE);
                            checkGameSolved();
                        }

                        state = 0;
                        temp = -1;
                        selectedEngIndex = -1;
                        selectedKorIndex = -1;
                    }
                }
            });
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(16000, 1000) {
            public void onTick(long millisUntilFinished) {
                time.setText(millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                if (!gameSolved) {
                    Toast.makeText(GameActivity.this, "시간 초과되었습니다!", Toast.LENGTH_SHORT).show();
                    time.setText("시간 종료!");
                }
            }
        }.start();
    }

    private void checkGameSolved() {
        for (TextView textView : word) {
            if (textView.getVisibility() == View.VISIBLE) {
                return;
            }
        }
        gameSolved = true;
        Toast.makeText(GameActivity.this, "성공!", Toast.LENGTH_SHORT).show();
        timer.cancel();
    }

    String readWord(String fName) {
        String wordStr = null;
        FileInputStream inFs;
        try {
            inFs = openFileInput(fName);
            byte[] txt = new byte[500];
            inFs.read(txt);
            inFs.close();
            wordStr = (new String(txt)).trim();
        } catch (IOException e) {
        }

        return wordStr;
    }
}
