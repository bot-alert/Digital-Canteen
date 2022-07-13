package com.example.canteenapp.FoodList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canteenapp.AdminComponent.AdminPanel;
import com.example.canteenapp.R;
import com.example.canteenapp.constant.FireBaseConstant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Food extends AppCompatActivity {
  FirebaseDatabase firebaseDatabase;
  DatabaseReference databaseReference;
  RecyclerView recyclerView;
  Button button, fooddone;
  RelativeLayout hiddenrelative;
  TextView fodname, foodpz, Foodpic;
  long maxid = 0;
  String foodname, foodprize, foodpic;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_food);
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference(FireBaseConstant.FOOD_CARD);
    button = findViewById(R.id.addnewfood);
    fooddone = findViewById(R.id.DONefOOD);
    fodname = findViewById(R.id.setFoodname);
    foodpz = findViewById(R.id.setFoodprize);
    Foodpic = findViewById(R.id.setFoodpicture);
    recyclerView = findViewById(R.id.recyclerfood);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    hiddenrelative = findViewById(R.id.hiddenrelative);
    hiddenrelative.setVisibility(View.GONE);
    databaseReference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()) {
          int counteroflineno = 0;
          for (DataSnapshot postSnapshot : snapshot.getChildren()) {
            counteroflineno = 0;
            counteroflineno = Integer.parseInt(postSnapshot.getKey());
          }
          maxid = counteroflineno;
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
    fooddone.setOnClickListener(view -> {
      hiddenrelative.setVisibility(View.GONE);
      foodname = fodname.getText().toString();
      foodprize = foodpz.getText().toString();
      foodpic = Foodpic.getText().toString();
      if (foodname.equals("") | foodprize.equals("") | foodpic.equals("")) {
        Toast.makeText(getApplicationContext(), "All field required!", Toast.LENGTH_LONG).show();
      } else {
        databaseReference.child(String.valueOf(maxid + 1)).child("foodName").setValue(foodname);
        databaseReference.child(String.valueOf(maxid + 1)).child("foodPrize").setValue(foodprize);
        databaseReference.child(String.valueOf(maxid + 1)).child("foodPicture").setValue(foodpic);
        databaseReference.child(String.valueOf(maxid + 1)).child("foodId").setValue(String.valueOf(maxid + 1));
        Toast.makeText(getApplicationContext(), "FOOD ADDED", Toast.LENGTH_LONG).show();
      }
      fodname.setText("");
      foodpz.setText("");
      Foodpic.setText("");
    });
    button.setOnClickListener(view -> hiddenrelative.setVisibility(View.VISIBLE));
  }

  @Override
  protected void onStart() {
    super.onStart();

    FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<FoodModel>().setQuery(databaseReference, FoodModel.class).build();
    FirebaseRecyclerAdapter<FoodModel, FoodVIewHOlder> firebaseRecyclerAdapter = new
            FirebaseRecyclerAdapter<FoodModel, FoodVIewHOlder>(options) {
              @Override
              protected void onBindViewHolder(@NonNull FoodVIewHOlder foodVIewHOlder, final int i, @NonNull final FoodModel foodModel) {
                foodVIewHOlder.foodNametemp.setText(foodModel.getFoodName());
                foodVIewHOlder.foodPrizetemp.setText(foodModel.getFoodPrize());
                Picasso.get().load(foodModel.getFoodPicture()).into(foodVIewHOlder.foodPicturetemp);
                foodVIewHOlder.remove.setOnClickListener(view -> databaseReference.child(foodModel.getFoodId()).removeValue());
              }

              @NonNull
              @Override
              public FoodVIewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.foodtemp, parent, false);
                return new FoodVIewHOlder(view);
              }
            };

    firebaseRecyclerAdapter.startListening();
    firebaseRecyclerAdapter.notifyDataSetChanged();
    recyclerView.setAdapter(firebaseRecyclerAdapter);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finishAffinity();
    Intent intent = new Intent(getApplicationContext(), AdminPanel.class);
    startActivity(intent);
  }
}
