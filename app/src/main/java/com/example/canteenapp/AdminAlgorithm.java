package com.example.canteenapp;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.canteenapp.Util.CanteenUtil;
import com.example.canteenapp.constant.FireBaseConstant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminAlgorithm extends AppCompatActivity {
  FirebaseDatabase firebaseDatabase;
  DatabaseReference databaseReferenceMasterRecord;


  private static List<AlgoData> listOfData = new ArrayList<>();
  private static List<AlgoData> listOfDailyData = new ArrayList<>();
  private static List<AlgoData> listOfWeeklyData = new ArrayList<>();
  private static List<AlgoData> listOfMonthlyData = new ArrayList<>();
  private static List<AlgoData> listOfSpringData = new ArrayList<>();
  private static List<AlgoData> listOfSummerData = new ArrayList<>();
  private static List<AlgoData> listOfFallData = new ArrayList<>();
  private static List<AlgoData> listOfWinterData = new ArrayList<>();
  private static List<AlgoData> listOfYearData = new ArrayList<>();
  private static Map<String, Long> mapOfDailyData = new HashMap<>();
  private static Map<String, Long> mapOfWeeklyData = new HashMap<>();
  private static Map<String, Long> mapOfMonthlyData = new HashMap<>();
  private static Map<String, Long> mapOfYearlyData = new HashMap<>();
  private static Map<String, Long> mapOfSummerData = new HashMap<>();
  private static Map<String, Long> mapOfSpringData = new HashMap<>();
  private static Map<String, Long> mapOfFallData = new HashMap<>();
  private static Map<String, Long> mapOfWinterData = new HashMap<>();
  private TextView day, week, month, year, spring, summer, winter, fall;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.admin_algorithm);
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReferenceMasterRecord = firebaseDatabase.getReference().child(FireBaseConstant.MASTER_RECORD);
    day = findViewById(R.id.day);
    week = findViewById(R.id.week);
    month = findViewById(R.id.month);
    year = findViewById(R.id.year);
    spring = findViewById(R.id.spring);
    fall = findViewById(R.id.fall);
    winter = findViewById(R.id.winter);
    summer = findViewById(R.id.summer);



  }

  @Override
  protected void onStart() {
    super.onStart();
    databaseReferenceMasterRecord.addListenerForSingleValueEvent(new ValueEventListener() {
      @RequiresApi(api = Build.VERSION_CODES.N)
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (listOfData.isEmpty()) {
          for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            String[] foodCountArray = dataSnapshot.child("foodCount").getValue().toString().replaceFirst("\n", "").split("\n");
            String[] foodNameArray = dataSnapshot.child("foodName").getValue().toString().split("\n");
            String[] foodPrizeArray = dataSnapshot.child("foodPrize").getValue().toString().replaceFirst("\n", "").split("\n");

            if (foodCountArray.length == 1) {
              foodNameArray[0] = foodNameArray[0].trim();
              AlgoData algoData = new AlgoData();
              algoData.setFoodName(foodNameArray[0]);
              algoData.setFoodCount(Integer.parseInt(foodCountArray[0]));
              algoData.setFoodPrize((long) Integer.parseInt(foodPrizeArray[0]) * algoData.getFoodCount());
              algoData.setTime(Long.parseLong(dataSnapshot.child("time").getValue().toString()));
              listOfData.add(algoData);
            }
            for (int i = 1; i < foodCountArray.length; i++) {
              foodNameArray[i] = foodNameArray[i].trim();
              AlgoData algoData = new AlgoData();
              algoData.setFoodName(foodNameArray[i]);
              algoData.setFoodCount(Integer.parseInt(foodCountArray[i]));
              algoData.setFoodPrize((long) Integer.parseInt(foodPrizeArray[i]) * algoData.getFoodCount());
              algoData.setTime(Long.parseLong(dataSnapshot.child("time").getValue().toString()));
              listOfData.add(algoData);
            }
          }
        }
        if (!listOfData.isEmpty()) {
          for (AlgoData algoData : listOfData) {
            if (CanteenUtil.isThisDay(algoData.time)) {
              listOfDailyData.add(algoData);
            }
            if (CanteenUtil.isThisWeek(algoData.time)) {
              listOfWeeklyData.add(algoData);
            }
            if (CanteenUtil.isThisMonth(algoData.time)) {
              listOfMonthlyData.add(algoData);
            }
            if (CanteenUtil.isThisYear(algoData.time)) {
              listOfYearData.add(algoData);
            }
            if (CanteenUtil.isThisSpring(algoData.time)) {
              listOfSpringData.add(algoData);
            }
            if (CanteenUtil.isThisSummer(algoData.time)) {
              listOfSummerData.add(algoData);
            }
            if (CanteenUtil.isThisFall(algoData.time)) {
              listOfFallData.add(algoData);
            }
            if (CanteenUtil.isThisWinter(algoData.time)) {
              listOfWinterData.add(algoData);
            }
          }
          //TODO:to implement what to do with data

          if (!listOfDailyData.isEmpty()) {
            for (AlgoData algoData : listOfDailyData) {
              if (mapOfDailyData.containsKey(algoData.getFoodName())) {
                mapOfDailyData.put(algoData.foodName, mapOfDailyData.get(algoData.getFoodName()) + algoData.getFoodPrize());
              }
              mapOfDailyData.putIfAbsent(algoData.getFoodName(), algoData.getFoodPrize());
            }
            System.out.println("daily:");
          day.setText(getHighestSellingFoodItem(mapOfDailyData));
          }
          if (!listOfWeeklyData.isEmpty()) {
            for (AlgoData algoData : listOfWeeklyData) {
              if (mapOfWeeklyData.containsKey(algoData.getFoodName())) {
                mapOfWeeklyData.put(algoData.foodName, mapOfWeeklyData.get(algoData.getFoodName()) + algoData.getFoodPrize());
              }
              mapOfWeeklyData.putIfAbsent(algoData.getFoodName(), algoData.getFoodPrize());
            }
            System.out.println("weekly:");
            week.setText(getHighestSellingFoodItem(mapOfWeeklyData));
          }
          if (!listOfMonthlyData.isEmpty()) {
            for (AlgoData algoData : listOfMonthlyData) {
              if (mapOfMonthlyData.containsKey(algoData.getFoodName())) {
                mapOfMonthlyData.put(algoData.foodName, mapOfMonthlyData.get(algoData.getFoodName()) + algoData.getFoodPrize());
              }
              mapOfMonthlyData.putIfAbsent(algoData.getFoodName(), algoData.getFoodPrize());
            }
            System.out.println("monthly:");
            month.setText(getHighestSellingFoodItem(mapOfMonthlyData));
          }
          if (!listOfYearData.isEmpty()) {
            for (AlgoData algoData : listOfYearData) {
              if (mapOfYearlyData.containsKey(algoData.getFoodName())) {
                mapOfYearlyData.put(algoData.foodName, mapOfYearlyData.get(algoData.getFoodName()) + algoData.getFoodPrize());
              }
              mapOfYearlyData.putIfAbsent(algoData.getFoodName(), algoData.getFoodPrize());
            }
            System.out.println("yearly");
            year.setText(getHighestSellingFoodItem(mapOfYearlyData));
          }
          if (!listOfSummerData.isEmpty()) {
            for (AlgoData algoData : listOfSummerData) {
              if (mapOfSummerData.containsKey(algoData.getFoodName())) {
                mapOfSummerData.put(algoData.foodName, mapOfSummerData.get(algoData.getFoodName()) + algoData.getFoodPrize());
              }
              mapOfSummerData.putIfAbsent(algoData.getFoodName(), algoData.getFoodPrize());
            }
            System.out.println("summer");
            summer.setText(getHighestSellingFoodItem(mapOfSummerData));
          }
          if (!listOfSpringData.isEmpty()) {
            for (AlgoData algoData : listOfSpringData) {
              if (mapOfSpringData.containsKey(algoData.getFoodName())) {
                mapOfSpringData.put(algoData.foodName, mapOfSpringData.get(algoData.getFoodName()) + algoData.getFoodPrize());
              }
              mapOfSpringData.putIfAbsent(algoData.getFoodName(), algoData.getFoodPrize());
            }
            System.out.println("spring");
            spring.setText(getHighestSellingFoodItem(mapOfSpringData));
          }
          if (!listOfFallData.isEmpty()) {
            for (AlgoData algoData : listOfFallData) {
              if (mapOfFallData.containsKey(algoData.getFoodName())) {
                mapOfFallData.put(algoData.foodName, mapOfFallData.get(algoData.getFoodName()) + algoData.getFoodPrize());
              }
              mapOfFallData.putIfAbsent(algoData.getFoodName(), algoData.getFoodPrize());
            }
            System.out.println("fall");

            fall.setText(getHighestSellingFoodItem(mapOfFallData));
          }
          if (!listOfWinterData.isEmpty()) {
            for (AlgoData algoData : listOfWinterData) {
              mapOfWinterData.putIfAbsent(algoData.getFoodName(), algoData.getFoodPrize());
              if (mapOfWinterData.containsKey(algoData.getFoodName())) {
                mapOfWinterData.put(algoData.foodName, mapOfWinterData.get(algoData.getFoodName()) + algoData.getFoodPrize());
              }
            }
            System.out.println("winter");

            day.setText(getHighestSellingFoodItem(mapOfWinterData));
          }
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

  }

  private String getHighestSellingFoodItem(Map<String, Long> map) {
    String currentMax = null;
    long currentMaxInt = 0;
    for (String name : map.keySet()) {
      if (currentMaxInt < map.get(name)) {
        currentMaxInt = map.get(name);
        currentMax = name;
      }
    }
    return "Food: "+currentMax;
  }
}
