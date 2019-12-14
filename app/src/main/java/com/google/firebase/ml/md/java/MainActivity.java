/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.ml.md.java;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.md.R;

/** Entry activity to select the detection mode. */
public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private Button button;
  private TextView simpleTextView;
  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  String userID;
  FirebaseAuth fAuth;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    LiveBarcodeScanningActivity lbsa = new LiveBarcodeScanningActivity();
    userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
    lbsa.getProfile(userID);

    BottomNavigationView navView = findViewById(R.id.nav_view);
    // Passing each menu ID as a set of Ids because each
    // menu should be considered as top level destinations.
    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
            R.id.navigation_home, R.id.navigation_scan, R.id.navigation_profile)
            .build();
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    NavigationUI.setupWithNavController(navView, navController);
  }

  public void getDocument() {
    DocumentReference docRef = db.collection("goodsStatus").document(LiveBarcodeScanningActivity.rawValue);
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          if (document.exists()) {
            Log.d(TAG, "DocumentSnapshot data WOY: " + document.getData());
            String kurirtext = document.getString("kurir");
            simpleTextView.setText(LiveBarcodeScanningActivity.rawValue + kurirtext);
          } else {
            Log.d(TAG, "No such  document");
          }
        } else {
          Log.d(TAG, "get failed with ", task.getException());
        }
      }
    });
  }

  public void getProfile() {
    userID=fAuth.getCurrentUser().getUid();
    Log.d(TAG, "UserID : " + userID);
    DocumentReference docRef = db.collection("Users").document(userID);
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          if (document.exists()) {
            Log.d(TAG, "DocumentSnapshot data profile: " + document.getData());
            String nameText = document.getString("Name");
            String locationText = document.getString("Location");
            Toast.makeText(MainActivity.this, nameText, Toast.LENGTH_SHORT).show();
          } else {
            Log.d(TAG, "No such  document");
          }
        } else {
          Log.d(TAG, "get failed with ", task.getException());
        }
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (!Utils.allPermissionsGranted(this)) {
      Utils.requestRuntimePermissions(this);
    }
  }

  public void openLiveBarcodeScanningActivity(){
    Intent intent = new Intent(this, LiveBarcodeScanningActivity.class);
    startActivity(intent);
  }

  public void logout(View view){
    FirebaseAuth.getInstance().signOut();
    startActivity(new Intent(getApplicationContext(), login.class));
    finish();
  }

}
