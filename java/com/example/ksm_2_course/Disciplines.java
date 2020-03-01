package com.example.ksm_2_course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Disciplines extends AppCompatActivity
{
    final String FILE_NAME = "data_disc.json";
    final int FALSE = 0xFFF56D6D, TRUE = 0xFFDFFFBF;

    Button res; // Кнопка результата
    SharedPreferences Pref;
    Button buts[][] = new Button[7][2]; // Кнопки "Сдано" и "Защита"
    int complete = 0, Labs; // complete - подсчет сданих лаб, Labs - хранит количество лабораторних
    ArrayList<Discipline> discs = new ArrayList<Discipline>(); //Дисциплины
    Discipline current; // текущая дисциплина

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplines);

        Intent intent = getIntent();

        // Достать объект Дисциплина с json, возвращает массив дисциплин
        int num = GetCode(intent.getStringExtra("Name")); // индекс для выбора дисциплины из массива дисциплин
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Discipline>>() {}.getType();
        discs = gson.fromJson(JSONHelper.read(this, FILE_NAME), listType);
        current = discs.get(num);


        Labs = current.getLabs(); // количество лабораторных
        // Получение полей лабораторных работ
        String table = "Line_", stringId;
        int id;
        LinearLayout Main_view = (LinearLayout)findViewById(R.id.Main_view);
        LinearLayout[] lines = new LinearLayout[Labs];
        for (int i = 0; i < 7; ++i)
        {
            stringId = table + Integer.toString(i);
            id = getResources().getIdentifier(stringId, "id", getApplicationContext().getPackageName());
            if (i < Labs)
                lines[i] = (LinearLayout)findViewById(id);
            else
                Main_view.removeView((LinearLayout)findViewById(id));
        }



        // Получение кнопок
        table = "B_";
        for(int i=0;i<Labs; ++i)
        {
            stringId = table + Integer.toString(i) + "_" + Integer.toString(0);
            id = getResources().getIdentifier(stringId, "id", getApplicationContext().getPackageName());
            buts[i][0] = (Button)(findViewById(id));

            stringId = table + Integer.toString(i) + "_" + Integer.toString(1);
            id = getResources().getIdentifier(stringId, "id", getApplicationContext().getPackageName());
            buts[i][1] = (Button)(findViewById(id));
        }
        res = (Button) findViewById(R.id.res); // результат в процентах

        RestoreAll(discs.get(num));
    }

    // Загрузка информации о дисциплине
    protected void RestoreAll(Discipline current)
    {
        boolean[][] compl_but = current.getComplete();
        int color;

        complete = current.getProgress();
        for (int i = 0, size = Labs; i < size; ++i)
        {
            color = compl_but[i][0] ? TRUE : FALSE;
            buts[i][0].setBackgroundColor(color);

            color = compl_but[i][1] ? TRUE : FALSE;
            buts[i][1].setBackgroundColor(color);
        }

        ((Button)(findViewById(R.id.Disc))).setText(current.getName());
        ((Button)(findViewById(R.id.val))).setText(current.getValue());
        ((Button)(findViewById(R.id.teach))).setText(current.getTeacher());

        res.setText(Integer.toString(complete * 50 / Labs) + "%");
    }

    protected void SaveAll()
    {
        boolean[][] compl_but = new boolean[Labs][2];

        for (int i = 0; i < Labs; ++i)
        {
            if (((ColorDrawable)buts[i][0].getBackground()).getColor() == 0xFFDFFFBF)
                compl_but[i][0] = true;
            else
                compl_but[i][0] = false;

            if (((ColorDrawable)buts[i][1].getBackground()).getColor() == 0xFFDFFFBF)
                compl_but[i][1] = true;
            else
                compl_but[i][1] = false;
        }

        current.setComplete(compl_but);
        current.setProgress(complete);

        Gson gson = new Gson();
        String jsonString = gson.toJson(discs);
        JSONHelper.create(this, FILE_NAME, jsonString);
    }

    public void OnClick(View v)
    {
        Button but = (Button) v;

        if (((ColorDrawable) but.getBackground()).getColor() == 0xFFF56D6D)
        {
            ++complete;
            but.setBackgroundColor(0xFFDFFFBF);
        } else
        {
            --complete;
            but.setBackgroundColor(0xFFF56D6D);
        }

        res.setText(Integer.toString(complete * 50 / Labs) + "%");
         SaveAll();
    }

    private int GetCode(String name)
    {
        switch(name)
        {
            case "Алгоритми та методи обчислень":
                return 0;
            case "Архітектура комп'ютерів":
                return 1;
            case "Комп'ютерна схемотехніка":
                return 2;
            case "Організація баз данних":
                return 3;
            case "Основи безпеки життєдіяльності":
                return 4;
            case "Сучасні методи програмування":
                return 5;
            default:
                return -1;
        }
    }

}


