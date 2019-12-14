package com.google.firebase.ml.md.java;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.md.R;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    private static final String TAG = "register";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText mName, mEmail,mPassword;
    Button mRegister;
    TextView mLoginBtn;
    Spinner mLocation;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName       = findViewById(R.id.name);
        mEmail      = findViewById(R.id.email_register);
        mPassword   = findViewById(R.id.pwd_register);
        mRegister   = findViewById(R.id.button_register);
        mLoginBtn   = findViewById(R.id.to_login);
        mLocation   = findViewById(R.id.location);

        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String location = mLocation.getSelectedItem().toString().trim();

                if(TextUtils.isEmpty(name)){
                    mName.setError("Name is Required.");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length()<8){
                    mPassword.setError("Password must be >= 8 characters");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(register.this, "User Created. Check your Email for verification.", Toast.LENGTH_SHORT).show();
                                        userID=fAuth.getCurrentUser().getUid();
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("Name", name);
                                        user.put("Location", location);
                                        db.collection("Users").document(userID)
                                                .set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "Profile Saved!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error!", e);
                                                    }
                                                });
                                        startActivity(new Intent(getApplicationContext(),login.class));
                                        finish();
                                    }else{
                                        Toast.makeText(register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });

            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
            }
        });
    }

}
