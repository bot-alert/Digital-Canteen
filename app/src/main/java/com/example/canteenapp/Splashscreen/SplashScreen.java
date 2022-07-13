package com.example.canteenapp.Splashscreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.canteenapp.MainActivity;
import com.example.canteenapp.R;
import com.example.canteenapp.Util.MailUtil;

public class SplashScreen extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);
    Thread thread = new Thread() {
      @Override
      public void run() {
        super.run();
        try {
          sleep(2000);
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          Intent intent = new Intent(SplashScreen.this, MainActivity.class);
          startActivity(intent);
        }
      }
    };
    thread.start();
  }
}