package com.google.firebase.ml.md.java.scan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.ml.md.R;
import com.google.firebase.ml.md.java.LiveBarcodeScanningActivity;
import com.google.firebase.ml.md.java.MainActivity;

public class ScanFragment extends Fragment {
    private ScanViewModel scanViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        startActivity(new Intent(getActivity().getApplicationContext(), LiveBarcodeScanningActivity.class));
        OnBackPressedCallback callback = new OnBackPressedCallback(true){
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(getActivity().getApplicationContext(), MainActivity.class));
            }
        };
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }
}
