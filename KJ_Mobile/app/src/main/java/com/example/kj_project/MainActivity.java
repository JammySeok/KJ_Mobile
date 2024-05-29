package com.example.kj_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText[] edtEng, edtKor;
    Button btnSave, hideEng, hideKor, btnShow;
    DatePicker dPicker;
    String[] fileNames, tempEn, tempKr;
    int enON, krON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("하루 단어 5개");

        edtEng = new EditText[]{
                findViewById(R.id.edtEng1),
                findViewById(R.id.edtEng2),
                findViewById(R.id.edtEng3),
                findViewById(R.id.edtEng4),
                findViewById(R.id.edtEng5)
        };

        edtKor = new EditText[]{
                findViewById(R.id.edtKor1),
                findViewById(R.id.edtKor2),
                findViewById(R.id.edtKor3),
                findViewById(R.id.edtKor4),
                findViewById(R.id.edtKor5)
        };

        btnSave = findViewById(R.id.btnSave);
        hideEng = findViewById(R.id.hideEng);
        hideKor = findViewById(R.id.hideKor);
        btnShow = findViewById(R.id.btnShow);
        dPicker = findViewById(R.id.dPicker);

        fileNames = new String[10];
        tempEn = new String[5];
        tempKr = new String[5];

        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < 5; i++) {
            fileNames[i * 2] = cYear + "_" + (cMonth + 1) + "_" + cDay + "_" + (i + 1) + ".txt";
            fileNames[i * 2 + 1] = cYear + "_" + (cMonth + 1) + "_" + cDay + "_" + (i + 6) + ".txt";
            String str1 = readWord(fileNames[i * 2]);
            String str2 = readWord(fileNames[i * 2 + 1]);
            edtEng[i].setText(str1);
            edtKor[i].setText(str2);
        }
        btnSave.setEnabled(true);


        dPicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                for (int i = 0; i < 5; i++) {
                    fileNames[i * 2] = year + "_" + (month + 1) + "_" + day + "_" + (i + 1) + ".txt";
                    fileNames[i * 2 + 1] = year + "_" + (month + 1) + "_" + day + "_" + (i + 6) + ".txt";
                    String str1 = readWord(fileNames[i * 2]);
                    String str2 = readWord(fileNames[i * 2 + 1]);
                    edtEng[i].setText(str1);
                    edtKor[i].setText(str2);
                }
                btnSave.setEnabled(true);
                hideEng.setText("단어 숨기기");
                hideKor.setText("뜻 숨기기");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 5; i++) {
                    try {
                        FileOutputStream outFs1 = openFileOutput(fileNames[i * 2], Context.MODE_PRIVATE);
                        FileOutputStream outFs2 = openFileOutput(fileNames[i * 2 + 1], Context.MODE_PRIVATE);
                        String str1 = edtEng[i].getText().toString();
                        String str2 = edtKor[i].getText().toString();
                        outFs1.write(str1.getBytes());
                        outFs2.write(str2.getBytes());
                        outFs1.close();
                        outFs2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        hideEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buttonText = hideEng.getText().toString();
                if (buttonText.equals("단어 숨기기")) {
                    for (int i = 0; i < 5; i++) {
                        tempEn[i] = edtEng[i].getText().toString();
                        edtEng[i].setText("");
                    }

                    hideEng.setText("단어 표시");
                    enON = 0;
                    btnSave.setEnabled(false);
                }
                else if (buttonText.equals("단어 표시")) {
                    for (int i = 0; i < 5; i++)
                        edtEng[i].setText(tempEn[i]);

                    hideEng.setText("단어 숨기기");
                    enON = 1;
                    if(krON == 1) btnSave.setEnabled(true);
                }
            }
        });

        hideKor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buttonText = hideKor.getText().toString();
                if (buttonText.equals("뜻 숨기기")) {
                    for (int i = 0; i < 5; i++) {
                        tempKr[i] = edtKor[i].getText().toString();
                        edtKor[i].setText("");
                    }

                    hideKor.setText("뜻 표시");
                    krON = 0;
                    btnSave.setEnabled(false);
                }
                else if (buttonText.equals("뜻 표시")) {
                    for (int i = 0; i < 5; i++)
                        edtKor[i].setText(tempKr[i]);

                    hideKor.setText("뜻 숨기기");
                    krON = 1;
                    if(enON == 1) btnSave.setEnabled(true);
                }
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShowActicity.class);
                startActivity(intent);
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
            btnSave.setText("수정하기");
        } catch (IOException e) {
            btnSave.setText("단어 저장");
        }

        return wordStr;
    }
}
