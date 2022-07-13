package com.example.canteenapp.UserComponent.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canteenapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ViewHolderHome extends RecyclerView.ViewHolder {
  Map<String, Object> hashmap = new HashMap<>();
  View view;
  TextView foodName, foodPrize, zerovalue;
  ImageView plus, minus;
  ImageView foodPicture;
  int zero;
  FirebaseAuth firebaseAuth;
  FirebaseDatabase firebaseDatabase;
  FirebaseUser user;
  String userID;
  DatabaseReference databaseReference;

  public ViewHolderHome(@NonNull View itemView) {
    super(itemView);
    view = itemView;
    foodName = view.findViewById(R.id.foodName);
    foodPrize = view.findViewById(R.id.foodPrize);
    foodPicture = view.findViewById(R.id.foodPicture);
    zerovalue = view.findViewById(R.id.zeroValue);
    zero = Integer.parseInt(zerovalue.getText().toString());
    plus = view.findViewById(R.id.plus);
    minus = view.findViewById(R.id.minus);
    firebaseAuth = FirebaseAuth.getInstance();
    firebaseDatabase = FirebaseDatabase.getInstance();
    user = firebaseAuth.getCurrentUser();
    userID = user.getUid();
    databaseReference = firebaseDatabase.getReference().child("Users").child(userID).child("OrderedItem");
    //ON CLICLING MINUS IMAGEVIEW
    minus.setOnClickListener(view -> {
      if (zero <= 0) {
        zerovalue.setText("0");
        databaseReference.child(foodName.getText().toString()).removeValue();

      } else {
        zerovalue.setText(Integer.toString(zero - 1));
        zero--;
        hashmap.put(foodName.getText().toString(), zero + "," + foodPrize.getText().toString());
        databaseReference.updateChildren(hashmap);
        if (zero <= 0) {
          zerovalue.setText("0");
          databaseReference.child(foodName.getText().toString()).removeValue();
        }
      }
    });
    //ON CLICKING PLUS IMAGEVIEW
    plus.setOnClickListener(view -> {

      zerovalue.setText(Integer.toString(zero + 1));
      zero++;
      hashmap.put(foodName.getText().toString(), zero + "," + foodPrize.getText().toString());
      databaseReference.updateChildren(hashmap);
    });


  }


}

