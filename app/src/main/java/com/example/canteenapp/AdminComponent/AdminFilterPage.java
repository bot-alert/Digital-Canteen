package com.example.canteenapp.AdminComponent;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.canteenapp.DatePickerFragment;
import com.example.canteenapp.R;
import com.example.canteenapp.Util.CanteenUtil;
import com.example.canteenapp.constant.FireBaseConstant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminFilterPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
  DatabaseReference databaseReferenceMasterRecord;
  FirebaseDatabase firebaseDatabase;
  List<DataEntry> foodNameAndCount = new ArrayList<>();
  Map<String, Integer> map = new HashMap<>();
  AnyChartView anyChartView;
  Button buttonStart, buttonEnd, buttonDateInBetween;
  private int BUTTON_ID = 00;
  private int LIST_ID = 01;
  TextView startText;
  TextView endText;
  long startTime, endTime;
  Pie pie;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.admin_filter_page);
    anyChartView = findViewById(R.id.any_chart_view);
    buttonStart = findViewById(R.id.buttonDateStart);
    endText = findViewById(R.id.textViewEnd);
    startText = findViewById(R.id.textViewStart);
    buttonDateInBetween = findViewById(R.id.buttonDateInBetween);
    buttonEnd = findViewById(R.id.buttonDateEnd);
    anyChartView.setProgressBar(findViewById(R.id.progressBarAdminFilter));
    firebaseDatabase = FirebaseDatabase.getInstance();

    databaseReferenceMasterRecord = firebaseDatabase.getReference().child(FireBaseConstant.MASTER_RECORD);
    databaseInstance(false);
    buttonStart.setOnClickListener(v -> {
      DialogFragment datePicker = new DatePickerFragment();
      datePicker.show(getFragmentManager(), "start");
      BUTTON_ID = 00;
    });
    buttonEnd.setOnClickListener(v -> {
      DialogFragment datePicker = new DatePickerFragment();
      datePicker.show(getFragmentManager(), "end");
      BUTTON_ID = 01;
    });
    buttonDateInBetween.setOnClickListener(v -> {
      if (!startText.getText().equals("") && !endText.getText().equals("")) {
        LIST_ID = 01;
        foodNameAndCount = new ArrayList<>();
        map = new HashMap<>();
        databaseInstance(true);

      } else {
        Toast.makeText(getApplicationContext(), "Please both select date", Toast.LENGTH_SHORT).show();
      }
    });

  }

  private void setUpAnyChart(String year) {

    pie = AnyChart.pie();
    pie.data(foodNameAndCount);
    pie.animation(true, 2000);
    pie.title("Food sold in year " + year);
    pie.background("#fafafa");
    pie.legend()
            .background("#fafafa")
            .position("center-bottom")
            .itemsLayout(LegendLayout.HORIZONTAL)
            .align(Align.CENTER);
    anyChartView.setChart(pie);

  }


  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finishAffinity();
    LIST_ID = 01;
    foodNameAndCount = new ArrayList<>();
    Intent intent = new Intent(getApplicationContext(), AdminPanel.class);
    startActivity(intent);
  }

  @Override
  public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month);
    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    Date date = c.getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    if (BUTTON_ID == 00) {
      startText.setText(dateFormat.format(date));
      startTime = c.getTimeInMillis();
    }
    if (BUTTON_ID == 01) {
      endText.setText(dateFormat.format(date));
      endTime = c.getTimeInMillis();
    }

  }

  public void databaseInstance(boolean calledFromFilter) {
    databaseReferenceMasterRecord.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (map.isEmpty()) {
          for (DataSnapshot innerSnapShot : snapshot.getChildren()) {
            if (!calledFromFilter) {
              if (CanteenUtil.getYearFromMilliSecond(Long.parseLong(innerSnapShot.child("time").getValue().toString())) == CanteenUtil.getYearFromMilliSecond(System.currentTimeMillis())) {
                String[] foodCount = innerSnapShot.child("foodCount").getValue().toString().replaceFirst("\n", "").split("\n");
                String[] foodName = innerSnapShot.child("foodName").getValue().toString().split("\n");
                if (foodCount.length == 1) {
                  foodName[0] = foodName[0].trim();
                  if (map.containsKey(foodName[0])) {
                    map.put(foodName[0], map.get(foodName[0]) + Integer.parseInt(foodCount[0]));
                  }
                  if (!map.containsKey(foodName[0])) {
                    map.put(foodName[0], Integer.parseInt(foodCount[0]));
                  }
                }
                for (int i = 1; i < foodCount.length; i++) {
                  foodName[i] = foodName[i].trim();
                  if (map.containsKey(foodName[i])) {
                    map.put(foodName[i], map.get(foodName[i]) + Integer.parseInt(foodCount[i]));
                  }
                  if (!map.containsKey(foodName[i])) {
                    map.put(foodName[i], Integer.parseInt(foodCount[i]));
                  }
                }
              }

            }
            if (calledFromFilter) {
              if (CanteenUtil.isInBetweenTwoDate(startTime, endTime, Long.parseLong(innerSnapShot.child("time").getValue().toString()))) {
                String[] foodCount = innerSnapShot.child("foodCount").getValue().toString().replaceFirst("\n", "").split("\n");
                String[] foodName = innerSnapShot.child("foodName").getValue().toString().split("\n");
                if (foodCount.length == 1) {
                  foodName[0] = foodName[0].trim();
                  if (map.containsKey(foodName[0])) {
                    map.put(foodName[0], map.get(foodName[0]) + Integer.parseInt(foodCount[0]));
                  }
                  if (!map.containsKey(foodName[0])) {
                    map.put(foodName[0], Integer.parseInt(foodCount[0]));
                  }
                }
                for (int i = 1; i < foodCount.length; i++) {
                  foodName[i] = foodName[i].trim();
                  if (map.containsKey(foodName[i])) {
                    map.put(foodName[i], map.get(foodName[i]) + Integer.parseInt(foodCount[i]));
                  }
                  if (!map.containsKey(foodName[i])) {
                    map.put(foodName[i], Integer.parseInt(foodCount[i]));
                  }
                }
              }
            }
          }
        }


        if (!map.isEmpty()) {
          if (LIST_ID == 01) {
            for (Map.Entry<String, Integer> set : map.entrySet()) {
              foodNameAndCount.add(new ValueDataEntry(set.getKey(), set.getValue()));
            }
            if (calledFromFilter) {
              APIlib.getInstance().setActiveAnyChartView(anyChartView);
              anyChartView.clear();
              pie.dispose();
              int firstYear = CanteenUtil.getYearFromMilliSecond(startTime);
              int secondYear = CanteenUtil.getYearFromMilliSecond(endTime);
              if (firstYear != secondYear) {
                setUpAnyChart(firstYear + "-" + secondYear);
              } else {
                setUpAnyChart(String.valueOf(firstYear));
              }
            }
            if (!calledFromFilter) {
              setUpAnyChart(String.valueOf(CanteenUtil.getYearFromMilliSecond(System.currentTimeMillis())));
            }
            LIST_ID = 02;
          }
        }

      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

  }

  @Override
  protected void onStart() {
    super.onStart();
  }
}