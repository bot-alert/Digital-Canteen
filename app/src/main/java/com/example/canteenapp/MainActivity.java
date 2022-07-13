package com.example.canteenapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.canteenapp.AdminComponent.AdminPanel;
import com.example.canteenapp.Util.NoInternet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
  TextView email, password, forgotPassword;
  Button button;
  FirebaseAuth firebaseAuth;
  ProgressBar progressBar;
  FrameLayout frameLayout;
  boolean isResetPassword = false;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    email = findViewById(R.id.Emailoflogin);
    password = findViewById(R.id.Passwordlogin);
    button = findViewById(R.id.loginbutton);
    progressBar = findViewById(R.id.progressBarLoginPage);
    progressBar.setVisibility(View.GONE);
    forgotPassword = findViewById(R.id.forgotPassword);
    frameLayout = findViewById(R.id.mainActivityFrameLayout);
    firebaseAuth = FirebaseAuth.getInstance();
    final TextView passwordToggler = findViewById(R.id.passwordToogle);
    passwordToggler.setText("SHOW");


    forgotPassword.setOnClickListener(v -> {
      frameLayout.setVisibility(View.GONE);
      button.setText("RESET");
      isResetPassword = true;
    });

    //THIS IS FOR PASSWORD SHOW METHOD
    passwordToggler.setOnClickListener(view -> {
      String state = passwordToggler.getText().toString();
      if (state.equals("SHOW")) {
        passwordToggler.setText("HIDE");
        password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
      } else {
        passwordToggler.setText("SHOW");
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
      }
    });

    button.setOnClickListener(view -> {
      if (isResetPassword) {
        progressBar.setVisibility(View.VISIBLE);
        String Email = email.getText().toString();
        if (TextUtils.isEmpty(Email)) {
          Toast.makeText(MainActivity.this, "Email is missing", Toast.LENGTH_SHORT).show();
          return;
        }
        firebaseAuth.sendPasswordResetEmail(Email)
                .addOnCompleteListener(task -> {
                  if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Reset link send successfully", Toast.LENGTH_SHORT).show();
                    frameLayout.setVisibility(View.VISIBLE);
                    button.setText("LOGIN");
                    progressBar.setVisibility(View.GONE);

                  }
                  if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Reset link sending failed", Toast.LENGTH_SHORT).show();
                    frameLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    button.setText("LOGIN");
                  }
                });

      }
      if (!isResetPassword) {
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        if (Email.matches("admin") && Password.matches("admin")) {
          Toast.makeText(MainActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(getApplicationContext(), AdminPanel.class);
          startActivity(intent);
        }
        if (TextUtils.isEmpty(Email)) {
          Toast.makeText(MainActivity.this, "Email is missing", Toast.LENGTH_SHORT).show();
          return;
        }
        if (TextUtils.isEmpty(Password)) {
          Toast.makeText(MainActivity.this, "Password is missing", Toast.LENGTH_SHORT).show();
          return;
        }
        if (Password.length() < 8) {
          Toast.makeText(MainActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
          return;
        } else {
          progressBar.setVisibility(View.VISIBLE);
          firebaseAuth.signInWithEmailAndPassword(Email, Password)
                  .addOnCompleteListener(MainActivity.this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                      // Sign in success, update UI with the signed-in user's information

                      Intent intent = new Intent(getApplicationContext(), BottomNavigation.class);
                      startActivity(intent);

                    } else {
                      // If sign in fails, display a message to the user.
                      Toast.makeText(MainActivity.this, "Authentication failed.",
                              Toast.LENGTH_SHORT).show();
                    }
                  });
        }
      }
      isResetPassword = false;
    });

  }


  @Override
  protected void onStart() {
    super.onStart();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    if (user != null) {
      startActivity(new Intent(MainActivity.this, BottomNavigation.class));
    }
    if (!isInternetAvailable()) {
      startActivity(new Intent(MainActivity.this, NoInternet.class));
    }
  }

  public boolean isInternetAvailable() {
    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    frameLayout.setVisibility(View.VISIBLE);
    button.setText("LOGIN");
    progressBar.setVisibility(View.GONE);
    finishAffinity();
  }
}