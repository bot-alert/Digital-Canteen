package com.example.canteenapp.History;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canteenapp.AdminComponent.AdminPanel;
import com.example.canteenapp.R;
import com.example.canteenapp.Util.CanteenUtil;
import com.example.canteenapp.constant.CanteenConstant;
import com.example.canteenapp.constant.FireBaseConstant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class itemHistory extends AppCompatActivity {
  FirebaseDatabase firebaseDatabase;
  DatabaseReference databaseReference, databaseReference2;
  RecyclerView recyclerViewHistory;
  FirebaseDatabase firebaseDatabase1;
  DatabaseReference databaseReference1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_iteam_history);
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference(FireBaseConstant.HISTORY);
    firebaseDatabase1 = FirebaseDatabase.getInstance();
    databaseReference1 = firebaseDatabase1.getReference(FireBaseConstant.HISTORY);
    recyclerViewHistory = (RecyclerView) findViewById(R.id.recycleviewhistory);
    databaseReference2 = firebaseDatabase.getReference(FireBaseConstant.TODAYS_HITS);
    recyclerViewHistory.setHasFixedSize(true);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
    layoutManager.setReverseLayout(true);
    layoutManager.setStackFromEnd(true);
    recyclerViewHistory.setLayoutManager(layoutManager);
  }

  @Override
  protected void onStart() {
    super.onStart();

    FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<HistoryModel>().setQuery(databaseReference, HistoryModel.class).build();
    final FirebaseRecyclerAdapter<HistoryModel, HistoryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<HistoryModel, HistoryViewHolder>(options) {
      @Override
      protected void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i, @NonNull HistoryModel historyModel) {
        if (CanteenUtil.getYearAndDayFromMilliSecond(System.currentTimeMillis()).equals(CanteenUtil.getYearAndDayFromMilliSecond(historyModel.getTime()))) {
          historyViewHolder.username.setText(historyModel.getUserName());
          historyViewHolder.foodNameHistory.setText(historyModel.getFoodName());
          historyViewHolder.foodCountHistory.setText(historyModel.getFoodCount());
          historyViewHolder.foodPrizeHistory.setText(historyModel.getFoodPrize());
          historyViewHolder.totalHistory.setText(historyModel.getTotal());
          historyViewHolder.comment.setText(historyModel.getComment());
          historyViewHolder.time.setText(CanteenUtil.ConvertMilliSecondsToFormattedDate(historyModel.getTime()));
          if (historyModel.getComment() != null) {
            int colorValue;
            if (historyModel.getComment().contains(CanteenConstant.USER)) {
              colorValue = Color.parseColor("#20222b");
            } else {
              colorValue = Color.parseColor("#00e73a");
            }
            historyViewHolder.comment.setTextColor(colorValue);
          }
        } else {
          historyViewHolder.cardViewForOrderList.setVisibility(View.GONE);
        }

      }

      @NonNull
      @Override
      public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteamhistorytemp, parent, false);
        return new HistoryViewHolder(view);

      }
    };

    firebaseRecyclerAdapter.startListening();
    firebaseRecyclerAdapter.notifyDataSetChanged();
    recyclerViewHistory.setItemViewCacheSize(900);
    recyclerViewHistory.setAdapter(firebaseRecyclerAdapter);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finishAffinity();
    Intent intent = new Intent(getApplicationContext(), AdminPanel.class);
    startActivity(intent);
  }

}