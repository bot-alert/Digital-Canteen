package com.example.canteenapp.UserComponent.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.canteenapp.Util.NoInternet;
import com.example.canteenapp.R;
import com.example.canteenapp.Util.CanteenUtil;
import com.example.canteenapp.constant.CanteenConstant;
import com.example.canteenapp.constant.FireBaseConstant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import org.apache.commons.codec.binary.Base64;


public class DashboardFragment extends Fragment {


  private FirebaseDatabase firebaseDatabase;

  private DatabaseReference databaseReferencehistory, databaseReferenceuserid, databaseReferenceconfromid, databaseReference4, databaseReference10;
  FirebaseAuth firebaseAuth;
  FirebaseUser user;
  List<String> userIdList = new ArrayList();
  String userID, id;
  String username, foodname, foodcount, foodprize, total;
  Button cancel;
  Button pay;
  TextView dashName, dashQuantity, dashPrize, dashTotal, tokenuser, orderredytext, turn, timeDash, tokenNoOfQrCode;
  CardView dashcard;
  ProgressBar dashprogess;
  private QRGEncoder qrgEncoder;
  private Bitmap bitmap;
  long maxid = 0, time;
  int lineNo;


  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

    firebaseAuth = FirebaseAuth.getInstance();
    firebaseDatabase = FirebaseDatabase.getInstance();
    user = firebaseAuth.getCurrentUser();
    userID = user.getUid();
    dashprogess = root.findViewById(R.id.dashprogess);
    dashprogess.setVisibility(View.VISIBLE);
    turn = root.findViewById(R.id.turn);
    dashName = root.findViewById(R.id.dashfoodname);
    dashQuantity = root.findViewById(R.id.dashquantity);
    orderredytext = root.findViewById(R.id.orderredytext);
    dashPrize = root.findViewById(R.id.dashprize);
    dashTotal = root.findViewById(R.id.dashtotal);
    timeDash = root.findViewById(R.id.timeDashBoard);
    cancel = root.findViewById(R.id.cancle);
    dashcard = root.findViewById(R.id.dashcard);
    tokenuser = root.findViewById(R.id.tokenuser);
    tokenNoOfQrCode = root.findViewById(R.id.tokenNoOfQrCode);
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReferencehistory = firebaseDatabase.getReference(FireBaseConstant.HISTORY);
    databaseReference10 = firebaseDatabase.getReference(FireBaseConstant.CONFIRMED);
    databaseReference4 = firebaseDatabase.getReference(FireBaseConstant.TODAYS_HITS);
    pay = root.findViewById(R.id.pay);


    //DASH ON CLICK
    dashcard.setOnClickListener(v -> {
      String inputValue = CanteenConstant.SALT + userID + CanteenConstant.DIGTIAL_CANTEEN + lineNo + CanteenConstant.SALT;
      byte[] bytesEncoded = Base64.encodeBase64(inputValue.getBytes());
      inputValue = new String(bytesEncoded);
      if (inputValue.length() > 0) {
        WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = Math.min(width, height);
        smallerDimension = smallerDimension * 3 / 4;
        qrgEncoder = new QRGEncoder(
                inputValue, null,
                QRGContents.Type.TEXT,
                smallerDimension);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.WHITE);
        try {
          final Dialog dialog = new Dialog(getContext());
          dialog.setContentView(R.layout.qr_code_view_layout);
          dialog.setCanceledOnTouchOutside(true);
          dialog.setCancelable(true);
          View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.qr_code_view_layout, root.findViewById(R.id.qrcodeLayout));
          ImageView qrCodeImage = bottomSheetView.findViewById(R.id.qrCodeImage);
          TextView qrTokenNo = bottomSheetView.findViewById(R.id.tokenNoOfQrCode);
          qrTokenNo.setText(String.valueOf(lineNo));
          bitmap = qrgEncoder.getBitmap();
          qrCodeImage.setImageBitmap(bitmap);
          dialog.setContentView(bottomSheetView);
          dialog.show();
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        //TODO::what text value for qr is empty
      }
    });


    pay.setOnClickListener(v -> {
      String url = "https://esewa.com.np/#/home";
      Intent i = new Intent(Intent.ACTION_VIEW);
      i.setData(Uri.parse(url));
      startActivity(i);
    });
    databaseReferenceuserid = firebaseDatabase.getReference("Users").child(userID);
    dashcard.setVisibility(View.GONE);
    databaseReferencehistory.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()) {
          maxid = snapshot.getChildrenCount();
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
    databaseReferenceuserid.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.child("id").getValue() != null) {
          id = snapshot.child("id").getValue().toString();
          databaseReferenceconfromid = firebaseDatabase.getReference(FireBaseConstant.CONFIRMED).child(id);
          databaseReferenceconfromid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

              if (snapshot.getValue() != null) {
                dashprogess.setVisibility(View.GONE);
                dashcard.setVisibility(View.VISIBLE);
                String foodName = snapshot.child("foodName").getValue().toString();

                dashName.setText(foodName);
                String foodCount = snapshot.child("foodCount").getValue().toString();
                foodCount = foodCount.replaceFirst("\n", "");
                dashQuantity.setText(foodCount);
                String foodPrize = snapshot.child("foodPrize").getValue().toString();
                foodPrize = foodPrize.replaceFirst("\n", "");
                dashPrize.setText(foodPrize);
                dashTotal.setText(snapshot.child("total").getValue().toString());
                tokenuser.setText(snapshot.child("userId").getValue().toString());
                lineNo = Integer.parseInt(snapshot.child("userId").getValue().toString());
                timeDash.setText(CanteenUtil.ConvertMilliSecondsToPrettyTime(Long.parseLong(snapshot.child("time").getValue().toString())));

                if (snapshot.child(FireBaseConstant.NOTIFICATION_ID).exists()) {
                  orderredytext.setText("Your order is Ready");
                  orderredytext.setVisibility(View.VISIBLE);
                }
                final int token = Integer.parseInt(snapshot.child("userId").getValue().toString());
                databaseReference10.addValueEventListener(new ValueEventListener() {
                  @RequiresApi(api = Build.VERSION_CODES.O)
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                      int counteroflineno = 0;
                      for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        userIdList.add(postSnapshot.getKey());
                        if (token == Integer.parseInt(postSnapshot.getKey())) {
                          counteroflineno++;
                          break;
                        }
                        counteroflineno++;
                      }
                      counteroflineno = counteroflineno - 1;
                      String turnText = "Your turn is after " + counteroflineno + " " + "people";
                      turn.setText(turnText);
                      if (counteroflineno == 0) {
                        orderredytext.setVisibility(View.VISIBLE);
                      }
                    }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
                });
                username = snapshot.child("name").getValue().toString();
                foodname = snapshot.child("foodName").getValue().toString();
                foodcount = snapshot.child("foodCount").getValue().toString();
                foodprize = snapshot.child("foodPrize").getValue().toString();
                total = snapshot.child("total").getValue().toString();
                time = Long.parseLong(snapshot.child("time").getValue().toString());
                databaseReferenceuserid.removeEventListener(this);
              } else {
                dashcard.setVisibility(View.GONE);
                dashprogess.setVisibility(View.GONE);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
          });

        } else {
          dashprogess.setVisibility(View.GONE);
        }

      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });


    cancel.setOnClickListener(view -> {
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("userName").setValue(username);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("foodName").setValue(foodname);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("foodCount").setValue(foodcount);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("foodPrize").setValue(foodprize);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("total").setValue(total);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("time").setValue(time);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("comment").setValue("REMOVED BY USER");
      firebaseDatabase.getReference(FireBaseConstant.CONFIRMED).child(id).removeValue();
      firebaseDatabase.getReference(FireBaseConstant.USERS).child(userID).child("id").removeValue();
      dashcard.setVisibility(View.GONE);
    });
    return root;
  }


  @Override
  public void onStart() {
    super.onStart();
    if (!isInternetAvailable()) {
      startActivity(new Intent(getContext(), NoInternet.class));
    }
  }


  public boolean isInternetAvailable() {
    ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
  }
}