package com.vedangj044.statusview.Calenders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.vedangj044.statusview.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalenderActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private MutableLiveData<List<CalendarDay>> eventList = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        setTitle("Scheduled Meeting");

        calendarView = findViewById(R.id.calender_data);
        calendarView.setDateSelected(CalendarDay.today(), true);

        CountDownTimer cp = new CountDownTimer(5000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                List<CalendarDay> responseDays = new ArrayList<>();

                responseDays.add(CalendarDay.from(2020, 10, 20));
                responseDays.add(CalendarDay.from(2020, 10, 22));
                responseDays.add(CalendarDay.from(2020, 10, 24));
                responseDays.add(CalendarDay.from(2020, 10, 26));

                eventList.setValue(responseDays);
                Log.v("Hello", "added");
            }
        };
        cp.start();


        eventList.observe(this, new Observer<List<CalendarDay>>() {
            @Override
            public void onChanged(List<CalendarDay> calendarDays) {

                for(CalendarDay days : calendarDays){
                    calendarView.addDecorator(new DayViewDecorator() {
                        @Override
                        public boolean shouldDecorate(CalendarDay day) {
                            return day.equals(days);
                        }

                        @Override
                        public void decorate(DayViewFacade view) {
                            view.setBackgroundDrawable(ContextCompat.getDrawable(CalenderActivity.this, R.drawable.oval_black_solid));
                        }
                    });
                }

            }
        });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                if(eventList.getValue() != null){
                    if(eventList.getValue().contains(date)){
                        Toast.makeText(CalenderActivity.this, "Displays events", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}