package com.example.ksm_2_course;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import java.util.Calendar;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;


public class Lessons_schedule extends AppCompatActivity {

    TableLayout tableLayout_r[] = new TableLayout[4];
    TextView textViewMonday[][] = new TextView[4][4];
    TextView textViewTuesday[][] = new TextView[4][4];
    TextView textViewWednesday[][] = new TextView[4][4];
    TextView textViewThursday[][] = new TextView[4][4];
    static  String FILE_NAME;
    int first_day=2 ;
    static int week;
    static int endTimeM,endTimeH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_schedule);

        //////////////////////////////////////////////
                      //Обводка//
        //////////////////////////////////////////////

        //получаем день недели (пн,вт и пр)
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //создаем обьект для обводки
        tableLayout_r[0] = (TableLayout) findViewById(R.id.tableLayout_mon);
        tableLayout_r[1] = (TableLayout) findViewById(R.id.tableLayout_tue);
        tableLayout_r[2] = (TableLayout) findViewById(R.id.tableLayout_wed);
        tableLayout_r[3] = (TableLayout) findViewById(R.id.tableLayout_thur);

        //при совпадении дней делаем обводку красной (т.е меняем файл с noborders на borders)
        if(dayOfWeek==2){
            tableLayout_r[0].setBackground(getResources().getDrawable(R.drawable.borders));
        }else if(dayOfWeek==3){
            tableLayout_r[1].setBackground(getResources().getDrawable(R.drawable.borders));
        }else if(dayOfWeek==4){
            tableLayout_r[2].setBackground(getResources().getDrawable(R.drawable.borders));
        }else if(dayOfWeek==5){
            tableLayout_r[3].setBackground(getResources().getDrawable(R.drawable.borders));
        }

        //////////////////////////////////////////////
                        //Данные в таблице//
        //////////////////////////////////////////////

       //выбираю базу данных
        if(MainActivity.StatusButton==0){
            FILE_NAME = "knt518.json";
        }else if(MainActivity.StatusButton==1){
            FILE_NAME = "knt528.json";
        }

        //создаю обьекты таблицы
        int id;

        String table1 = "table",stringId;
        for(int i=0;i<4;i++){
            for(int j=1;j<4;j++){
                stringId = table1 + Integer.toString(i) + "_" + Integer.toString(j);
                id = getResources().getIdentifier(stringId, "id", getApplicationContext().getPackageName());
                textViewMonday[i][j] = (TextView) findViewById(id);
            }
        }

        String table2 = "table2",stringId2;
        for(int i=0;i<4;i++){
            for(int j=1;j<4;j++){
                stringId2 = table2 + Integer.toString(i) + "_" + Integer.toString(j);
                id = getResources().getIdentifier(stringId2, "id", getApplicationContext().getPackageName());
                textViewTuesday[i][j] = (TextView) findViewById(id);
            }
        }

        String table3 = "table3",stringId3;
        for(int i=0;i<4;i++){
            for(int j=1;j<4;j++){
                stringId3 = table3 + Integer.toString(i) + "_" + Integer.toString(j);
                id = getResources().getIdentifier(stringId3, "id", getApplicationContext().getPackageName());
                textViewWednesday[i][j] = (TextView) findViewById(id);
            }
        }

        String table4 = "table4",stringId4;
        for(int i=0;i<4;i++){
            for(int j=1;j<4;j++){
                stringId4 = table4 + Integer.toString(i) + "_" + Integer.toString(j);
                id = getResources().getIdentifier(stringId4, "id", getApplicationContext().getPackageName());
                textViewThursday[i][j] = (TextView) findViewById(id);
            }
        }


        //узнаю тип недели (числитель или знаменатель)
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        TextView nameOfWeek = (TextView) findViewById(R.id.nameOfWeek);
        Button changeScheduleButton = (Button) findViewById(R.id.changeScheduleButton);
        if(dayOfMonth==1){
            week=1;
            nameOfWeek.setText("ЧИСЕЛЬНИК");
            changeScheduleButton.setText("ЗНАМЕННИК");
        }else if(((dayOfMonth-first_day)/7)%2==0){
            week=0;
            nameOfWeek.setText("ЗНАМЕННИК");
            changeScheduleButton.setText("ЧИСЕЛЬНИК");
        }else{
            week=1;
            nameOfWeek.setText("ЧИСЕЛЬНИК");
            changeScheduleButton.setText("ЗНАМЕННИК");
        }

        setDataBase();

        //////////////////////////////////////////////
                        //Таймер//
        //////////////////////////////////////////////
        int currentTimeH = calendar.get(Calendar.HOUR_OF_DAY),currentTimeM = calendar.get(Calendar.MINUTE),currentTimeS = calendar.get(Calendar.SECOND);
        int currentTime = currentTimeH*60+currentTimeM;
        TextView timeUntil = (TextView) findViewById(R.id.timeUntil);

        //начало и конец пары (в минутах)
        int[][] lessons = { { 510, 590 }, { 605, 685 }, { 715, 795}, { 805, 885}, { 895, 975 } };

        //нахожу какая сейчас пара
        //endTimeM времени осталось до конца пары (в минутах)
        if(currentTime>=lessons[0][0] && currentTime<lessons[0][1]){
            endTimeM = lessons[0][1]-currentTime;
            time(currentTimeS);

        }else if(currentTime>=lessons[1][0] && currentTime<lessons[1][1]){
            endTimeM = lessons[1][1]-currentTime;
            time(currentTimeS);

        }else if(currentTime>=lessons[2][0] && currentTime<lessons[2][1]){
            endTimeM = lessons[2][1]-currentTime;
            time(currentTimeS);

        }else if(currentTime>=lessons[3][0] && currentTime<lessons[3][1]){
            endTimeM = lessons[3][1]-currentTime;
            time(currentTimeS);

        }else if(currentTime>=lessons[4][0] && currentTime<lessons[4][1]){
            endTimeM = lessons[4][1]-currentTime;
            time(currentTimeS);

        }else if(currentTime>=lessons[0][1] && currentTime<lessons[1][0]){
            timeUntil.setText("Початок II пари:");
            endTimeM = lessons[1][0]-currentTime;
            time(currentTimeS);

        }else if(currentTime>=lessons[1][1] && currentTime<lessons[2][0]){
            timeUntil.setText("Початок III пари:");
            endTimeM = lessons[2][0]-currentTime;
            time(currentTimeS);

        }else if(currentTime>=lessons[2][1] && currentTime<lessons[3][0]){
            timeUntil.setText("Початок IV пари:");
            endTimeM = lessons[3][0]-currentTime;
            time(currentTimeS);

        }else if(currentTime>=lessons[3][1] && currentTime<lessons[4][0]){
            timeUntil.setText("Початок V пари:");
            endTimeM = lessons[4][0]-currentTime;
            time(currentTimeS);

        }else{
            timeUntil.setText("Початок I пари:");
            if(currentTime>lessons[0][0]) endTimeM = 24*60-currentTime+lessons[0][0];
            else endTimeM = lessons[0][0]-currentTime;
            time(currentTimeS);
        }
    }

    public void time(int currentTime){
        TextView textView12 = (TextView) findViewById(R.id.textView12);
        endTimeH=endTimeM/60;
        endTimeM%=60;
        --endTimeM;
        if(endTimeH==0) textView12.setText(" ");
        timer((60-currentTime)*1000);
    }
    public void timer(int seconds){
        final TextView timerS = (TextView) findViewById(R.id.timerS);
        TextView timerM = (TextView) findViewById(R.id.timerM);
        TextView timerH = (TextView) findViewById(R.id.timerH);
        timerM.setText(Integer.toString(endTimeM));
        if(endTimeH!=0) timerH.setText(Integer.toString(endTimeH));
        new CountDownTimer(seconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished<10){
                    timerS.setText(""  + millisUntilFinished/1000);
                }else{
                    timerS.setText("" + millisUntilFinished/1000);
                }

            }

            @Override
            public void onFinish() {
                --endTimeM;
                if(endTimeM>0) timer(60000);
                else if(endTimeH>0){
                    --endTimeH;
                    endTimeM=60;
                }
            }
        }.start();
    }

    public void OnClick(View v)
    {
        TextView nameOfWeek = (TextView) findViewById(R.id.nameOfWeek);
        Button changeScheduleButton = (Button) findViewById(R.id.changeScheduleButton);
       if(week == 1) {
           nameOfWeek.setText("ЗНАМЕННИК");
           changeScheduleButton.setText("ЧИСЕЛЬНИК");
           week = 0;
       } else if(week == 0) {
           nameOfWeek.setText("ЧИСЕЛЬНИК");
           changeScheduleButton.setText("ЗНАМЕННИК");
           week = 1;
       }
       setDataBase();
    }

    public void setDataBase(){
        //открываю файл и загружаю данные с базы
        try{

            //gson
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Lessons_schedule_Items>>() {}.getType();
            List<Lessons_schedule_Items> list = gson.fromJson(JSONHelper.loadJSONFromAsset(this,FILE_NAME), listType);

            //выбираем неделю
            Lessons_schedule_Items day = list.get(week);

            //выбираем номер пары в неделе
            int count = 0,j=1;
            for(int i=0;i<4;i++){
                ItemType lesson = day.types.get(count);
                textViewMonday[i][j].setText(lesson.getRoom()); j++;
                textViewMonday[i][j].setText(lesson.getSubject()); j++;
                textViewMonday[i][j].setText(lesson.getType());
                if(lesson.getType().equals("ЛК")){
                    textViewMonday[i][j].setBackgroundColor(getResources().getColor(R.color.green));
                }else if(lesson.getType().equals("ЛР")){
                    textViewMonday[i][j].setBackgroundColor(getResources().getColor(R.color.orange));
                }else if(lesson.getType().equals("-")){
                    textViewMonday[i][j].setBackgroundColor(getResources().getColor(R.color.blue));
                }
                j=1;
                count++;
            }

            for(int i=0;i<4;i++){
                ItemType lesson = day.types.get(count);
                textViewTuesday[i][j].setText(lesson.getRoom()); j++;
                textViewTuesday[i][j].setText(lesson.getSubject()); j++;
                textViewTuesday[i][j].setText(lesson.getType());
                if(lesson.getType().equals("ЛК")){
                    textViewTuesday[i][j].setBackgroundColor(getResources().getColor(R.color.green));
                }else if(lesson.getType().equals("ЛР")){
                    textViewTuesday[i][j].setBackgroundColor(getResources().getColor(R.color.orange));
                }else if(lesson.getType().equals("-")){
                    textViewTuesday[i][j].setBackgroundColor(getResources().getColor(R.color.blue));
                }
                j=1;
                count++;
            }

            for(int i=0;i<4;i++){
                ItemType lesson = day.types.get(count);
                textViewWednesday[i][j].setText(lesson.getRoom()); j++;
                textViewWednesday[i][j].setText(lesson.getSubject()); j++;
                textViewWednesday[i][j].setText(lesson.getType());
                if(lesson.getType().equals("ЛК")){
                    textViewWednesday[i][j].setBackgroundColor(getResources().getColor(R.color.green));
                }else if(lesson.getType().equals("ЛР")){
                    textViewWednesday[i][j].setBackgroundColor(getResources().getColor(R.color.orange));
                }else if(lesson.getType().equals("-")){
                    textViewWednesday[i][j].setBackgroundColor(getResources().getColor(R.color.blue));
                }
                j=1;
                count++;
            }

            for(int i=0;i<4;i++){
                ItemType lesson = day.types.get(count);
                textViewThursday[i][j].setText(lesson.getRoom()); j++;
                textViewThursday[i][j].setText(lesson.getSubject()); j++;
                textViewThursday[i][j].setText(lesson.getType());
                if(lesson.getType().equals("ЛК")){
                    textViewThursday[i][j].setBackgroundColor(getResources().getColor(R.color.green));
                }else if(lesson.getType().equals("ЛР")){
                    textViewThursday[i][j].setBackgroundColor(getResources().getColor(R.color.orange));
                }else if(lesson.getType().equals("-")){
                    textViewThursday[i][j].setBackgroundColor(getResources().getColor(R.color.blue));
                }
                j=1;
                count++;
            }

        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
}


