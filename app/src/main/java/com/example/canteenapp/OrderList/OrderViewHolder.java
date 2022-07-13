package com.example.canteenapp.OrderList;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canteenapp.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {

  View view;
  TextView topfoodname, topfoodprice, topfoodcount, username, total, tokenadmin, time, title;
  Button done, orderredy, cancel;
  CardView cardView;

  public OrderViewHolder(@NonNull View itemView) {
    super(itemView);
    view = itemView;
    topfoodname = view.findViewById(R.id.foodnameorser);
    topfoodprice = view.findViewById(R.id.foodprizeorder);
    topfoodcount = view.findViewById(R.id.foodcountorder);
    username = view.findViewById(R.id.usersnameorder);
    total = view.findViewById(R.id.totalprizeorder);
    orderredy = view.findViewById(R.id.orderredy);
    done = view.findViewById(R.id.donebutton);
    cardView = view.findViewById(R.id.cardviewfororderlist);
    tokenadmin = view.findViewById(R.id.tokenadmin);
    time = view.findViewById(R.id.time);
    cancel = view.findViewById(R.id.orderCancel);

  }

}
