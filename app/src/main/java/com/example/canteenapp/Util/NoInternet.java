package com.example.canteenapp.Util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.canteenapp.MainActivity;
import com.example.canteenapp.R;

public class NoInternet extends AppCompatActivity {
  Button checkInternet;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nointernet);
    checkInternet = findViewById(R.id.checkinternt);
    checkInternet.setOnClickListener(v -> {
      if (isInternetAvailable()) {
        Intent intent = new Intent(NoInternet.this, MainActivity.class);
        startActivity(intent);
      } else {
        Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_LONG).show();
      }
    });
  }

  public boolean isInternetAvailable() {
    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finishAffinity();
  }
}