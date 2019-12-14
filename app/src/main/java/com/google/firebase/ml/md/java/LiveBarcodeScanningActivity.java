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

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.common.base.Objects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.md.R;
import com.google.firebase.ml.md.java.barcodedetection.BarcodeField;
import com.google.firebase.ml.md.java.barcodedetection.BarcodeProcessor;
import com.google.firebase.ml.md.java.barcodedetection.BarcodeResultFragment;
import com.google.firebase.ml.md.java.camera.CameraSource;
import com.google.firebase.ml.md.java.camera.CameraSourcePreview;
import com.google.firebase.ml.md.java.camera.GraphicOverlay;
import com.google.firebase.ml.md.java.camera.WorkflowModel;
import com.google.firebase.ml.md.java.camera.WorkflowModel.WorkflowState;
import com.google.firebase.ml.md.java.settings.SettingsActivity;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** Demonstrates the barcode scanning workflow using camera preview. */
public class LiveBarcodeScanningActivity extends AppCompatActivity implements OnClickListener {

  private static final String TAG = "LiveBarcodeActivity";

  private static LiveBarcodeScanningActivity instance;
  public static String rawValue;
  public static String nameText;
  public static String locationText;
  private FirebaseFirestore db = FirebaseFirestore.getInstance();
  public static String userID;
  FirebaseAuth fAuth;

  public void addToFirestore(String value, String name, String location) {
    Map<String, Object> user = new HashMap<>();
    // user.put("result", value);
    user.put("kurir", name);
    user.put("lokasi", location);
    user.put("barang_id", value);


    // Add a new document with a generated ID
    db.collection("goodsStatus").document(value)
            .set(user)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot added with ID: ");
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
              }
            });
  }

  private CameraSource cameraSource;
  private CameraSourcePreview preview;
  private GraphicOverlay graphicOverlay;
  private View settingsButton;
  private View flashButton;
  private Chip promptChip;
  private AnimatorSet promptChipAnimator;
  private WorkflowModel workflowModel;
  private WorkflowState currentWorkflowState;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_live_barcode);

    fAuth = FirebaseAuth.getInstance();

    preview = findViewById(R.id.camera_preview);
    graphicOverlay = findViewById(R.id.camera_preview_graphic_overlay);
    graphicOverlay.setOnClickListener(this);
    cameraSource = new CameraSource(graphicOverlay);

    promptChip = findViewById(R.id.bottom_prompt_chip);
    promptChipAnimator =
        (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.bottom_prompt_chip_enter);
    promptChipAnimator.setTarget(promptChip);

    findViewById(R.id.close_button).setOnClickListener(this);
    flashButton = findViewById(R.id.flash_button);
    flashButton.setOnClickListener(this);
    settingsButton = findViewById(R.id.settings_button);
    settingsButton.setOnClickListener(this);

    setUpWorkflowModel();

    instance = this;
  }

  public static LiveBarcodeScanningActivity getInstance() {
    return instance;
  }

  public void getDocument(String value) {
    DocumentReference docRef = db.collection("goodsStatus").document(value);
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          if (document.exists()) {
            String kurirtext = document.getString("kurir");
            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
            Toast.makeText(LiveBarcodeScanningActivity.this, kurirtext, Toast.LENGTH_SHORT).show();
          } else {
            Log.d(TAG, "No such  document");
          }
        } else {
          Log.d(TAG, "get failed with ", task.getException());
        }
      }
    });
  }

  public void getProfile(String value) {
    DocumentReference docRef = db.collection("Users").document(value);
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          if (document.exists()) {
            Log.d(TAG, "DocumentSnapshot data profile: " + document.getData());
            nameText = document.getString("Name");
            locationText = document.getString("Location");
            //Toast.makeText(LiveBarcodeScanningActivity.this, nameText+" "+locationText, Toast.LENGTH_SHORT).show();
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

    workflowModel.markCameraFrozen();
    settingsButton.setEnabled(true);
    currentWorkflowState = WorkflowState.NOT_STARTED;
    cameraSource.setFrameProcessor(new BarcodeProcessor(graphicOverlay, workflowModel));
    workflowModel.setWorkflowState(WorkflowState.DETECTING);
  }

  @Override
  protected void onPostResume() {
    super.onPostResume();
    BarcodeResultFragment.dismiss(getSupportFragmentManager());
  }

  @Override
  protected void onPause() {
    super.onPause();
    currentWorkflowState = WorkflowState.NOT_STARTED;
    stopCameraPreview();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (cameraSource != null) {
      cameraSource.release();
      cameraSource = null;
    }
  }

  @Override
  public void onClick(View view) {
    int id = view.getId();
    if (id == R.id.close_button) {
      onBackPressed();

    } else if (id == R.id.flash_button) {
      if (flashButton.isSelected()) {
        flashButton.setSelected(false);
        cameraSource.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF);
      } else {
        flashButton.setSelected(true);
        cameraSource.updateFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
      }

    } else if (id == R.id.settings_button) {
      // Sets as disabled to prevent the user from clicking on it too fast.
      settingsButton.setEnabled(false);
      startActivity(new Intent(this, SettingsActivity.class));
    }
  }

  private void startCameraPreview() {
    if (!workflowModel.isCameraLive() && cameraSource != null) {
      try {
        workflowModel.markCameraLive();
        preview.start(cameraSource);
      } catch (IOException e) {
        Log.e(TAG, "Failed to start camera preview!", e);
        cameraSource.release();
        cameraSource = null;
      }
    }
  }

  private void stopCameraPreview() {
    if (workflowModel.isCameraLive()) {
      workflowModel.markCameraFrozen();
      flashButton.setSelected(false);
      preview.stop();
    }
  }

  private void setUpWorkflowModel() {
    workflowModel = ViewModelProviders.of(this).get(WorkflowModel.class);

    // Observes the workflow state changes, if happens, update the overlay view indicators and
    // camera preview state.
    workflowModel.workflowState.observe(
        this,
        workflowState -> {
          if (workflowState == null || Objects.equal(currentWorkflowState, workflowState)) {
            return;
          }

          currentWorkflowState = workflowState;
          Log.d(TAG, "Current workflow state: " + currentWorkflowState.name());

          boolean wasPromptChipGone = (promptChip.getVisibility() == View.GONE);

          switch (workflowState) {
            case DETECTING:
              promptChip.setVisibility(View.VISIBLE);
              promptChip.setText(R.string.prompt_point_at_a_barcode);
              startCameraPreview();
              break;
            case CONFIRMING:
              promptChip.setVisibility(View.VISIBLE);
              promptChip.setText(R.string.prompt_move_camera_closer);
              startCameraPreview();
              break;
            case SEARCHING:
              promptChip.setVisibility(View.VISIBLE);
              promptChip.setText(R.string.prompt_searching);
              stopCameraPreview();
              break;
            case DETECTED:
            case SEARCHED:
              promptChip.setVisibility(View.GONE);
              stopCameraPreview();
              break;
            default:
              promptChip.setVisibility(View.GONE);
              break;
          }

          boolean shouldPlayPromptChipEnteringAnimation =
              wasPromptChipGone && (promptChip.getVisibility() == View.VISIBLE);
          if (shouldPlayPromptChipEnteringAnimation && !promptChipAnimator.isRunning()) {
            promptChipAnimator.start();
          }
        });

    workflowModel.detectedBarcode.observe(
        this,
        barcode -> {
          if (barcode != null) {
            ArrayList<BarcodeField> barcodeFieldList = new ArrayList<>();
            rawValue = barcode.getRawValue();
            userID = fAuth.getCurrentUser().getUid();
            //getProfile(userID);
            addToFirestore(barcode.getRawValue(), nameText, locationText);
            getDocument(rawValue);
            barcodeFieldList.add(new BarcodeField("Goods ID", rawValue));
            int valueType = barcode.getValueType();
            switch (valueType) {
              case FirebaseVisionBarcode.TYPE_WIFI:
                String ssid = barcode.getWifi().getSsid();
                String password = barcode.getWifi().getPassword();
                int type = barcode.getWifi().getEncryptionType();
                barcodeFieldList.add(new BarcodeField("Wifi Value", ssid));
                break;
              case FirebaseVisionBarcode.TYPE_URL:
                String title = barcode.getUrl().getTitle();
                String url = barcode.getUrl().getUrl();
                break;
            }
            BarcodeResultFragment.show(getSupportFragmentManager(), barcodeFieldList);
          }
        });
  }
}
