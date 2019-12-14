package com.google.firebase.ml.md.java.Profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.ml.md.R;
import com.google.firebase.ml.md.java.LiveBarcodeScanningActivity;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView textView = root.findViewById(R.id.textViewName);
        final TextView textView1 = root.findViewById(R.id.textViewLocation);
        profileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(LiveBarcodeScanningActivity.nameText);
                textView1.setText(LiveBarcodeScanningActivity.locationText);
            }
        });
        return root;
    }
}
