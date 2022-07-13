package com.example.canteenapp.History;

public class HistoryModel {
  private String foodName;
  private String foodPrize;
  private String foodCount;
  private String total;
  private String userName;
  private String comment;
  private String color;
  private long time;

  void HistoryModel() {
  }

  public String getFoodName() {
    return foodName;
  }

  public void setFoodName(String foodName) {
    this.foodName = foodName;
  }

  public String getFoodPrize() {
    return foodPrize;
  }

  public void setFoodPrize(String foodPrize) {
    this.foodPrize = foodPrize;
  }

  public String getFoodCount() {
    return foodCount;
  }

  public void setFoodCount(String foodCount) {
    this.foodCount = foodCount;
  }

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }
}
