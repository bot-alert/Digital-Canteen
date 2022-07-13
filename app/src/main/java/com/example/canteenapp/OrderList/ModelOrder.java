package com.example.canteenapp.OrderList;

public class ModelOrder {
  private String foodName;
  private String foodCount;
  private String foodPrize;
  private String total;
  private String name;
  private String userId;
  private String uniqueId;
  private String email;
  private long time;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  void Modelorder() {
  }

  public String getFoodName() {
    return foodName;
  }

  public void setFoodName(String foodName) {
    this.foodName = foodName;
  }

  public String getFoodCount() {
    return foodCount;
  }

  public void setFoodCount(String foodCount) {
    this.foodCount = foodCount;
  }

  public String getFoodPrize() {
    return foodPrize;
  }

  public void setFoodPrize(String foodPrize) {
    this.foodPrize = foodPrize;
  }

  public String getTotal() {
    return total;
  }

  public void setTotal(String total) {
    this.total = total;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }
}
