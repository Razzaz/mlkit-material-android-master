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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.md.java.LiveBarcodeScanningActivity;
import com.google.firebase.ml.md.java.MainActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class EntryChoiceActivity extends AppCompatActivity {

    private static final String TAG = "EntryChoiceActivity";
    //private Button button;
    Timer timer;

        @Override
        protected void onCreate(@Nullable Bundle bundle) {
            super.onCreate(bundle);
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
            setContentView(R.layout.activity_entry_choice);

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    openActivityMain();
                }
            }, 2000);
//            button = findViewById(R.id.buttonGo);
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openActivityMain();
//                }
//            });
        }
        public void openActivityMain(){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
}
