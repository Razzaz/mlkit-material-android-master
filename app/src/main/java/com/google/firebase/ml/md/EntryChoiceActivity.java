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

package com.google.firebase.ml.md;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.md.java.register;

import java.util.Timer;
import java.util.TimerTask;

public class EntryChoiceActivity extends AppCompatActivity {

    private static final String TAG = "EntryChoiceActivity";
    public static String nameText;
    public static String locationText;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String userID;
    FirebaseAuth fAuth;
    Timer timer;

        @Override
        protected void onCreate(@Nullable Bundle bundle) {
            super.onCreate(bundle);
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
            setContentView(R.layout.activity_entry_choice);

            //fAuth = FirebaseAuth.getInstance();
            //userID=fAuth.getCurrentUser().getUid();
            //getProfile(userID);

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    openActivityMain();
                }
            }, 2000);

        }
        public void openActivityMain(){
            Intent intent = new Intent(this, register.class);
            startActivity(intent);
            finish();
        }

//        public void getProfile(String value) {
//            DocumentReference docRef = db.collection("Users").document(value);
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            Log.d(TAG, "DocumentSnapshot data profile: " + document.getData());
//                            nameText = document.getString("Name");
//                            locationText = document.getString("Location");
//                            //Toast.makeText(LiveBarcodeScanningActivity.this, nameText+" "+locationText, Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.d(TAG, "No such  document");
//                        }
//                    } else {
//                        Log.d(TAG, "get failed with ", task.getException());
//                    }
//                }
//            });
//        }
}
