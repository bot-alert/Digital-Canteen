package com.example.canteenapp;

public class AlgoData {
  String foodName;
  Integer foodCount;
  long foodPrize;
  long time;

  public AlgoData() {

  }

  public AlgoData(String foodName, Integer foodCount, long time) {
    this.foodName = foodName;
    this.foodCount = foodCount;
    this.time = time;
  }

  public String getFoodName() {
    return foodName;
  }

  public void setFoodName(String foodName) {
    this.foodName = foodName;
  }

  public Integer getFoodCount() {
    return foodCount;
  }

  public void setFoodCount(Integer foodCount) {
    this.foodCount = foodCount;
  }

  public long getFoodPrize() {
    return foodPrize;
  }

  public void setFoodPrize(long foodPrize) {
    this.foodPrize = foodPrize;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  @Override
  public String toString() {
    return "AlgoData{" +
            "foodName='" + foodName + '\'' +
            ", foodCount=" + foodCount +
            ", time=" + time +
            '}';
  }
}
