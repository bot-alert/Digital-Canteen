package com.example.canteenapp.QrCodeImplementation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.example.canteenapp.AdminComponent.AdminPanel;
import com.example.canteenapp.R;
import com.example.canteenapp.Util.MailUtil;
import com.example.canteenapp.constant.CanteenConstant;
import com.example.canteenapp.constant.FireBaseConstant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.codec.binary.Base64;

import java.util.Objects;


public class QrCamera extends AppCompatActivity {
  private CodeScanner mCodeScanner;
  FirebaseDatabase firebaseDatabase;
  DatabaseReference databaseReferenceConfirmed, databaseReferenceHistory, databaseReferencesUser, databaseReferenceMasterRecord, databaseReferenceTotalMoneyOfUser;
  long maxId = 0, maxIdMaster = 0;
  String foodCount, foodName, foodPrize, name, total, userId, uniqueId;
  long time;
  View bar;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.qr_camera_scan);
    firebaseDatabase = FirebaseDatabase.getInstance();
    bar = findViewById(R.id.bar);
    databaseReferenceConfirmed = firebaseDatabase.getReference(FireBaseConstant.CONFIRMED);
    databaseReferenceHistory = firebaseDatabase.getReference(FireBaseConstant.HISTORY);
    databaseReferenceMasterRecord = firebaseDatabase.getReference().child(FireBaseConstant.MASTER_RECORD);
    databaseReferencesUser = firebaseDatabase.getReference().child(FireBaseConstant.USERS);
    databaseReferenceTotalMoneyOfUser = firebaseDatabase.getReference().child(FireBaseConstant.TOTAL_MONEY_OF_USER);
    databaseReferenceMasterRecord.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()) {
          maxIdMaster = snapshot.getChildrenCount();
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

    databaseReferenceHistory.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()) {
          maxId = snapshot.getChildrenCount();
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
    CodeScannerView scannerView = findViewById(R.id.scanner_view);
    mCodeScanner = new CodeScanner(this, scannerView);
    final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.qr_animation);
    animation.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {

      }

      @Override
      public void onAnimationEnd(Animation animation) {
        bar.setVisibility(View.GONE);
      }

      @Override
      public void onAnimationRepeat(Animation animation) {

      }
    });
    bar.setVisibility(View.VISIBLE);
    bar.startAnimation(animation);
    mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
      byte[] valueDecoded = Base64.decodeBase64(result.getText());
      String decoded = new String(valueDecoded);
      decoded = decoded.replaceAll(CanteenConstant.SALT, "");
      decoded = decoded.replace(CanteenConstant.DIGTIAL_CANTEEN, ",");
      String finalDecoded = decoded;
      uniqueId = finalDecoded.split(",")[0];
      databaseReferenceConfirmed.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            if (Objects.requireNonNull(dataSnapshot.child("uniqueId").getValue()).toString().equals(uniqueId) && dataSnapshot.child("userId").getValue().toString().equals(finalDecoded.split(",")[1])) {
              foodCount = Objects.requireNonNull(dataSnapshot.child("foodCount").getValue()).toString();
              foodName = Objects.requireNonNull(dataSnapshot.child("foodName").getValue()).toString();
              foodPrize = Objects.requireNonNull(dataSnapshot.child("foodPrize").getValue()).toString();
              name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
              total = Objects.requireNonNull(dataSnapshot.child("total").getValue()).toString();
              userId = Objects.requireNonNull(dataSnapshot.child("userId").getValue()).toString();
              time = Long.parseLong(Objects.requireNonNull(dataSnapshot.child("time").getValue()).toString());
              String to = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
              String subject = "Digital Canteen - Order Complete";
              String body = name + " your order for  "
                      + foodName.trim().replace("\n", "") + " is completed";
              Thread thread = new Thread(() -> {
                MailUtil.send(to, subject, body);
              });
              thread.start();
              databaseReferencesUser.child(uniqueId).child("id").removeValue();
              databaseReferenceMasterRecord.child(String.valueOf(maxIdMaster + 1)).child("userName").setValue(name);
              databaseReferenceMasterRecord.child(String.valueOf(maxIdMaster + 1)).child("foodName").setValue(foodName);
              databaseReferenceMasterRecord.child(String.valueOf(maxIdMaster + 1)).child("foodCount").setValue(foodCount);
              databaseReferenceMasterRecord.child(String.valueOf(maxIdMaster + 1)).child("foodPrize").setValue(foodPrize);
              databaseReferenceMasterRecord.child(String.valueOf(maxIdMaster + 1)).child("total").setValue(total);
              databaseReferenceMasterRecord.child(String.valueOf(maxIdMaster + 1)).child("time").setValue(time);

              databaseReferenceHistory.child(String.valueOf(maxId + 1)).child("userName").setValue(name);
              databaseReferenceHistory.child(String.valueOf(maxId + 1)).child("foodName").setValue(foodName);
              databaseReferenceHistory.child(String.valueOf(maxId + 1)).child("foodCount").setValue(foodCount);
              databaseReferenceHistory.child(String.valueOf(maxId + 1)).child("foodPrize").setValue(foodPrize);
              databaseReferenceHistory.child(String.valueOf(maxId + 1)).child("total").setValue(total);
              databaseReferenceHistory.child(String.valueOf(maxId + 1)).child("time").setValue(time);
              databaseReferenceHistory.child(String.valueOf(maxId + 1)).child("comment").setValue("REMOVED BY ADMIN");
              databaseReferenceTotalMoneyOfUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                  if (snapshot.hasChild(uniqueId)) {
                    int total2 = Integer.parseInt(snapshot.child(uniqueId).getValue().toString());
                    databaseReferenceTotalMoneyOfUser.child(uniqueId).setValue(String.valueOf(Integer.parseInt(total) + total2));//not made for same username
                  } else {
                    databaseReferenceTotalMoneyOfUser.child(uniqueId).setValue(total);//not made for same username
                  }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
              });
              databaseReferenceConfirmed.child(userId).removeValue();

            }
          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
      });

      mCodeScanner.releaseResources();
      mCodeScanner.startPreview();
      bar.setVisibility(View.VISIBLE);
      bar.startAnimation(animation);
      Toast toast = Toast.makeText(getApplicationContext(), "DONE!", Toast.LENGTH_LONG);
      toast.setGravity(Gravity.CENTER, 0, 0);
      toast.show();
      startActivity(new Intent(getApplicationContext(), AdminPanel.class));
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    }));
    scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
  }

  @Override
  protected void onResume() {
    super.onResume();
    mCodeScanner.startPreview();
  }

  @Override
  protected void onPause() {
    mCodeScanner.releaseResources();
    super.onPause();
  }
}
