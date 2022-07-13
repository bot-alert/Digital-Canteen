package com.example.canteenapp.FoodList;


public class FoodModel {
  private String foodName;
  private String foodPicture;
  private String foodPrize;
  private String foodId;

  public FoodModel() {
  }

  public String getFoodName() {
    return foodName;
  }

  public void setFoodName(String foodName) {
    this.foodName = foodName;
  }

  public String getFoodPicture() {
    return foodPicture;
  }

  public void setFoodPicture(String foodPicture) {
    this.foodPicture = foodPicture;
  }

  public String getFoodPrize() {
    return foodPrize;
  }

  public void setFoodPrize(String foodPrize) {
    this.foodPrize = foodPrize;
  }

  public String getFoodId() {
    return foodId;
  }

  public void setFoodId(String foodId) {
    this.foodId = foodId;
  }
}
