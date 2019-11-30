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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.ml.md.R;

import java.util.HashMap;
import java.util.Map;

/** Entry activity to select the detection mode. */
public class MainActivity extends AppCompatActivity {

//  private static final String TAG = "MainActivity";
//
//  private FirebaseFirestore db = FirebaseFirestore.getInstance();
//  public void addAdaLovelace() {
//    Map<String, Object> user = new HashMap<>();
//    user.put("first", "Ada");
//    user.put("last", "Lovelace");
//    user.put("born", 1815);
//
//    // Add a new document with a generated ID
//    db.collection("users")
//            .add(user)
//            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//              @Override
//              public void onSuccess(DocumentReference documentReference) {
//                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//              }
//            })
//            .addOnFailureListener(new OnFailureListener() {
//              @Override
//              public void onFailure(@NonNull Exception e) {
//                Log.w(TAG, "Error adding document", e);
//              }
//            });
//  }

  private enum DetectionMode {
    //ODT_LIVE(R.string.mode_odt_live_title, R.string.mode_odt_live_subtitle),
    //ODT_STATIC(R.string.mode_odt_static_title, R.string.mode_odt_static_subtitle),
    BARCODE_LIVE(R.string.mode_barcode_live_title, R.string.mode_barcode_live_subtitle);

    private final int titleResId;
    private final int subtitleResId;

    DetectionMode(int titleResId, int subtitleResId) {
      this.titleResId = titleResId;
      this.subtitleResId = subtitleResId;
    }
  }

  @Override
  protected void onCreate(@Nullable Bundle bundle) {
    super.onCreate(bundle);

    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    setContentView(R.layout.activity_main);

    RecyclerView modeRecyclerView = findViewById(R.id.mode_recycler_view);
    modeRecyclerView.setHasFixedSize(true);
    modeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    modeRecyclerView.setAdapter(new ModeItemAdapter(DetectionMode.values()));
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (!Utils.allPermissionsGranted(this)) {
      Utils.requestRuntimePermissions(this);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode == Utils.REQUEST_CODE_PHOTO_LIBRARY
        && resultCode == Activity.RESULT_OK
        && data != null) {
      Intent intent = new Intent(this, StaticObjectDetectionActivity.class);
      intent.setData(data.getData());
      startActivity(intent);
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  private class ModeItemAdapter extends RecyclerView.Adapter<ModeItemAdapter.ModeItemViewHolder> {

    private final DetectionMode[] detectionModes;

    ModeItemAdapter(DetectionMode[] detectionModes) {
      this.detectionModes = detectionModes;
    }

    @NonNull
    @Override
    public ModeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new ModeItemViewHolder(
          LayoutInflater.from(parent.getContext())
              .inflate(R.layout.detection_mode_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ModeItemViewHolder modeItemViewHolder, int position) {
      modeItemViewHolder.bindDetectionMode(detectionModes[position]);
    }

    @Override
    public int getItemCount() {
      return detectionModes.length;
    }

    private class ModeItemViewHolder extends RecyclerView.ViewHolder {

      private final TextView titleView;
      private final TextView subtitleView;

      ModeItemViewHolder(@NonNull View view) {
        super(view);
        titleView = view.findViewById(R.id.mode_title);
        subtitleView = view.findViewById(R.id.mode_subtitle);
      }

      void bindDetectionMode(DetectionMode detectionMode) {
        titleView.setText(detectionMode.titleResId);
        subtitleView.setText(detectionMode.subtitleResId);
        itemView.setOnClickListener(
            view -> {
              Activity activity = MainActivity.this;
              switch (detectionMode) {
//                case ODT_LIVE:
//                  activity.startActivity(new Intent(activity, LiveObjectDetectionActivity.class));
//                  break;
//                case ODT_STATIC:
//                  Utils.openImagePicker(activity);
//                  break;
                case BARCODE_LIVE:
                  activity.startActivity(new Intent(activity, LiveBarcodeScanningActivity.class));
                  break;
              }
            });
      }
    }
  }
}
