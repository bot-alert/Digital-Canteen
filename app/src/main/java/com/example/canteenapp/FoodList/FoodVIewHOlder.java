package com.example.canteenapp.FoodList;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canteenapp.R;

public class FoodVIewHOlder extends RecyclerView.ViewHolder {
  View view;
  TextView foodNametemp, foodPrizetemp;
  ImageView foodPicturetemp;
  Button remove;

  public FoodVIewHOlder(@NonNull View itemView) {
    super(itemView);
    view = itemView;
    foodNametemp = view.findViewById(R.id.foodNamefoodtemp);
    foodPrizetemp = view.findViewById(R.id.foodPrizeFoodtemp);
    foodPicturetemp = view.findViewById(R.id.foodPicturefoodtemp);
    remove = view.findViewById(R.id.removefoodtemp);

  }
}
