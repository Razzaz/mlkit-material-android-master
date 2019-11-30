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

public class EntryChoiceActivity extends AppCompatActivity {

    private static final String TAG = "EntryChoiceActivity";
    private Button button;

//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    public void addToFirestore() {
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Ada");
//        user.put("last", "Lovelace");
//        user.put("born", 1815);
//
//        // Add a new document with a generated ID
//        db.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });
//    }
    // add this line for git test

//    private enum EntryMode {
//        ENTRY_JAVA(R.string.entry_java_title, R.string.entry_java_subtitle);
//        //ENTRY_KOTLIN(R.string.entry_kotlin_title, R.string.entry_kotlin_subtitle);
//
//        private final int titleResId;
//        private final int subtitleResId;
//
//        EntryMode(int titleResId, int subtitleResId) {
//            this.titleResId = titleResId;
//            this.subtitleResId = subtitleResId;
//        }
//    }
//
        @Override
        protected void onCreate(@Nullable Bundle bundle) {
            super.onCreate(bundle);
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
            setContentView(R.layout.activity_entry_choice);

            button = findViewById(R.id.buttonGo);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openActivityMain();
                }
            });

        }

        public void openActivityMain(){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
//
//    private class EntryItemAdapter extends RecyclerView.Adapter<EntryItemAdapter.EntryItemViewHolder> {
//
//        private final EntryMode[] entryModes;
//
//        EntryItemAdapter(EntryMode[] entryModes) {
//            this.entryModes = entryModes;
//        }
//
//        @NonNull
//        @Override
//        public EntryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            return new EntryItemViewHolder(
//                    LayoutInflater.from(parent.getContext())
//                            .inflate(R.layout.entry_item, parent, false));
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull EntryItemViewHolder entryItemViewHolder, int position) {
//            entryItemViewHolder.bindEntryMode(entryModes[position]);
//        }
//
//        @Override
//        public int getItemCount() {
//            return entryModes.length;
//        }
//
//        private class EntryItemViewHolder extends RecyclerView.ViewHolder {
//
//            private final TextView titleView;
//            private final TextView subtitleView;
//
//            EntryItemViewHolder(@NonNull View view) {
//                super(view);
//                titleView = view.findViewById(R.id.entry_title);
//                subtitleView = view.findViewById(R.id.entry_subtitle);
//            }
//
//            void bindEntryMode(EntryMode entryMode) {
//                titleView.setText(entryMode.titleResId);
//                subtitleView.setText(entryMode.subtitleResId);
//                itemView.setOnClickListener(view -> {
//                    Activity activity = EntryChoiceActivity.this;
//                    //switch (entryMode) {
//                    //  case ENTRY_JAVA:
//                        activity.startActivity(new Intent(activity, MainActivity.class));
//                    //    break;
//                    //}
//                });
//            }
//        }
//    }
}
