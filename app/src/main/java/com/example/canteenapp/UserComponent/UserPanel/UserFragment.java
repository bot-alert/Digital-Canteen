package com.example.canteenapp.UserComponent.UserPanel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.canteenapp.MainActivity;
import com.example.canteenapp.Util.NoInternet;
import com.example.canteenapp.R;
import com.example.canteenapp.constant.FireBaseConstant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {

  private static final int RESULT_OK = -1;
  private UserViewModel notificationsViewModel;
  TextView name, email, phone, uploadImage, weeklyExpenses, monthlyExpenses, yearlyExpenses;
  FirebaseAuth firebaseAuth;
  FirebaseDatabase firebaseDatabase;
  DatabaseReference databaseReference, databaseReference4;
  FirebaseUser user;
  String userID;
  EditText editTextUserSectionNsme, editUserSectionEmail, editUserSectionPhone;
  Button logout, resetdata;
  String NAME;
  StorageReference storageReference;
  CircleImageView picture;
  ImageView cameraIcon;
  CardView cardView;
  ProgressBar progressBar;
  NumberFormat nf = NumberFormat.getInstance();
  private static final DecimalFormat df = new DecimalFormat("0.00");


  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    notificationsViewModel =
            ViewModelProviders.of(this).get(UserViewModel.class);
    View root = inflater.inflate(R.layout.fragment_user, container, false);
    name = root.findViewById(R.id.userSectionNsme);
    email = root.findViewById(R.id.userSectionEmail);
    phone = root.findViewById(R.id.userSectionPhone);
    cardView = root.findViewById(R.id.dashcard);
    progressBar = root.findViewById(R.id.progressBarpic);
    progressBar.setVisibility(View.GONE);
    cameraIcon = root.findViewById(R.id.cameraIcon);
    picture = root.findViewById(R.id.picture);
    uploadImage = root.findViewById(R.id.uploadPicture);
    firebaseAuth = FirebaseAuth.getInstance();
    weeklyExpenses = root.findViewById(R.id.weeklyExpenses);
    monthlyExpenses = root.findViewById(R.id.monthlyExpenses);
    editTextUserSectionNsme = root.findViewById(R.id.editTextUserSectionNsme);
    editUserSectionEmail = root.findViewById(R.id.editUserSectionEmail);
    editUserSectionPhone = root.findViewById(R.id.editUserSectionPhone);
    yearlyExpenses = root.findViewById(R.id.yearlyExpenses);
    firebaseDatabase = FirebaseDatabase.getInstance();
    user = firebaseAuth.getCurrentUser();
    userID = user.getUid();
    databaseReference4 = firebaseDatabase.getReference().child(FireBaseConstant.TOTAL_MONEY_OF_USER);
    databaseReference = firebaseDatabase.getReference().child(FireBaseConstant.USERS).child(userID);
    storageReference = FirebaseStorage.getInstance().getReference().child(userID);
    logout = root.findViewById(R.id.logout);
    resetdata = root.findViewById(R.id.resetdata);

    //FOR PHONE NUMBER EDIT
    phone.setOnClickListener(v -> {
      phone.setVisibility(View.GONE);
      editUserSectionPhone.setText(phone.getText());
      editUserSectionPhone.setVisibility(View.VISIBLE);
    });
    editUserSectionPhone.setOnEditorActionListener((v1, actionId, event) -> {
      if (v1.getText().toString() != phone.getText()) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          databaseReference.child("phone").setValue(v1.getText().toString());
          phone.setVisibility(View.VISIBLE);
          editUserSectionPhone.setVisibility(View.GONE);
          InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
          imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
          return true;
        }
      }
      return false;
    });

    //FOR EMAIL EDIT
    email.setOnClickListener(v -> {
      email.setVisibility(View.GONE);
      editUserSectionEmail.setText(email.getText());
      editUserSectionEmail.setVisibility(View.VISIBLE);
    });
    editUserSectionEmail.setOnEditorActionListener((v2, actionId, event) -> {
      if (v2.getText().toString() != email.getText()) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          databaseReference.child("email").setValue(v2.getText().toString());
          user.updateEmail(v2.getText().toString())
                  .addOnCompleteListener(task -> {
                  });
          email.setVisibility(View.VISIBLE);
          editUserSectionEmail.setVisibility(View.GONE);
          InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
          imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
          return true;
        }
      }
      return false;
    });
//FOR USERNAME EDIT
    name.setOnClickListener(v -> {
      name.setVisibility(View.GONE);
      editTextUserSectionNsme.setText(name.getText());
      editTextUserSectionNsme.setVisibility(View.VISIBLE);
    });
    editTextUserSectionNsme.setOnEditorActionListener((v3, actionId, event) -> {
      if (v3.getText().toString() != name.getText()) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          databaseReference.child("name").setValue(v3.getText().toString());
          name.setVisibility(View.VISIBLE);
          editTextUserSectionNsme.setVisibility(View.GONE);
          InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
          imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
          return true;
        }
      }
      return false;
    });
    resetdata.setOnClickListener(v -> {
      databaseReference4.child(userID).removeValue();
      weeklyExpenses.setText("Rs.0");
      monthlyExpenses.setText("Rs.0");
      yearlyExpenses.setText("Rs.0");
    });
    logout.setOnClickListener(view -> {
      firebaseAuth.signOut();
      Intent intent = new Intent(getActivity(), MainActivity.class);
      startActivity(intent);
    });
    return root;
  }

  public void Uploaded() {
    CropImage.activity()
            .start(getContext(), this);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    storageReference.listAll()
            .addOnSuccessListener(listResult -> {
              for (StorageReference item : listResult.getItems()) {
                item.delete();
              }
            }).addOnFailureListener(e -> {
            });
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
      CropImage.ActivityResult result = CropImage.getActivityResult(data);
      if (resultCode == RESULT_OK) {
        progressBar.setVisibility(View.VISIBLE);
        Uri ImageData = result.getUri();
        final StorageReference imageName = storageReference.child("image" + ImageData.getLastPathSegment());
        imageName.putFile(ImageData).addOnSuccessListener(taskSnapshot -> imageName.getDownloadUrl().addOnSuccessListener(uri -> {

          String hashMap = String.valueOf(uri);
          databaseReference.child("image").setValue(hashMap).addOnSuccessListener(aVoid -> {
            Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
          });
        }));
      } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
        Toast.makeText(getContext(), "Image Upload Failed", Toast.LENGTH_LONG).show();
      }
    }
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == RESULT_OK) {

    }
  }


  @Override
  public void onStart() {
    super.onStart();
    if (!isInternetAvailable()) {
      startActivity(new Intent(getContext(), NoInternet.class));
    }
    databaseReference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.child("image").getValue() != null) {
          uploadImage.setVisibility(View.GONE);
          Picasso.get().load(snapshot.child("image").getValue().toString()).into(picture);
        }
        String EMAIL = snapshot.child("email").getValue().toString();
        NAME = snapshot.child("name").getValue().toString();
        String PHONE = snapshot.child("phone").getValue().toString();
        name.setText(NAME);
        email.setText(EMAIL);
        phone.setText(PHONE);
        databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.getValue() != null) {
              if (snapshot.hasChild(userID)) {
                String userT = snapshot.child(userID).getValue().toString();
                weeklyExpenses.setText("Rs." + nf.format(Float.parseFloat(df.format(Float.parseFloat(userT) / 52))));
                monthlyExpenses.setText("Rs." + nf.format(Float.parseFloat(df.format(Float.parseFloat(userT) / 12))));
                yearlyExpenses.setText("Rs." + nf.format(Float.parseFloat(df.format(Float.parseFloat(userT)))));

              }
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
        });
        picture.setOnClickListener(view -> Uploaded());

      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
        name.setText("Error Loading");
        email.setText("Error Loading");
        phone.setText("Error Loading");
      }
    });
  }


  public boolean isInternetAvailable() {
    ConnectivityManager connectivityManager = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

  }
}