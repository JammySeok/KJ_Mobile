package com.example.kj_project;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;

public class ShowActicity extends Activity {
    LinearLayout[] lnrLayout;
    TextView[] textEng, textKor;
    String[] fileNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show);

        lnrLayout = new LinearLayout[5];
        textEng = new TextView[5];
        textKor = new TextView[5];
        fileNames = new String[10];

        for (int i = 30; i >= 0; i--) {
            Calendar cal = Calendar.getInstance();
            int cYear = cal.get(Calendar.YEAR);
            int cMonth = cal.get(Calendar.MONTH);
            int cDay = cal.get(Calendar.DAY_OF_MONTH) - i;

            for (int j = 0; j < 5; j++) {
                fileNames[j * 2] = cYear + "_" + (cMonth + 1) + "_" + cDay + "_" + (j + 1) + ".txt";
                fileNames[j * 2 + 1] = cYear + "_" + (cMonth + 1) + "_" + cDay + "_" + (j + 6) + ".txt";
                String str1 = readWord(fileNames[j * 2]);
                String str2 = readWord(fileNames[j * 2 + 1]);

                if(str1 == null || str2 == null) continue;
                if(str1.equals("") || str2.equals("")) continue;

                LinearLayout parentLayout = findViewById(R.id.addWord);

                lnrLayout[j] = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                lnrLayout[j].setLayoutParams(layoutParams);
                lnrLayout[j].setOrientation(LinearLayout.HORIZONTAL);
                parentLayout.addView(lnrLayout[j]);

                textEng[j] = new TextView(this);
                LinearLayout.LayoutParams textViewParams1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                textKor[j] = new TextView(this);
                LinearLayout.LayoutParams textViewParams2 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                textViewParams1.weight = 1;
                textEng[j].setLayoutParams(textViewParams1);
                textEng[j].setGravity(Gravity.CENTER);
                textEng[j].setTextSize(20);
                textEng[j].setText(str1);
                lnrLayout[j].addView(textEng[j]);

                textViewParams2.weight = 1;
                textKor[j].setLayoutParams(textViewParams2);
                textKor[j].setGravity(Gravity.CENTER);
                textKor[j].setTextSize(20);
                textKor[j].setText(str2);
                lnrLayout[j].addView(textKor[j]);
            }
        }

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
