package com.example.canteenapp.History;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canteenapp.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
  View view;
  TextView username, foodNameHistory, foodPrizeHistory, foodCountHistory, totalHistory, comment, time;
  CardView cardViewForOrderList;

  public HistoryViewHolder(@NonNull View itemView) {
    super(itemView);
    view = itemView;
    username = view.findViewById(R.id.usersnamehistory);
    foodNameHistory = view.findViewById(R.id.foodnamehistory);
    foodPrizeHistory = view.findViewById(R.id.foodprizehistory);
    foodCountHistory = view.findViewById(R.id.foodcounthistory);
    totalHistory = view.findViewById(R.id.totalprizehsiory);
    comment = view.findViewById(R.id.comment);
    cardViewForOrderList = view.findViewById(R.id.cardviewfororderlisthistory);
    time = view.findViewById(R.id.timeHistory);
  }
}
