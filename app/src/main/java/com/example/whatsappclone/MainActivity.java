package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText mPhoneNumber, mcode ;
    private Button mSend;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this); // 1. untuk menggunakan firebase di dalam apikasi

        userIsLoggedIn();
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mcode = findViewById(R.id.code);
        mSend = findViewById(R.id.send);

        mSend.setOnClickListener(new View.OnClickListener() { // NOTED step 5 started
            @Override
            public void onClick(View v) {
                startPhoneNumberVerification();
            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);  // 7 Sign in fucntion // NOTED 
            }

            @Override
            public void onVerificationFailed(FirebaseException e) { } // and the message not same and the user input something wrong , this method will be call
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                    userIsLoggedIn();

            }
        });
    }

    private void userIsLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
            finish();
            return;
        }
    }


    private void startPhoneNumberVerification() { //  firebase documentation
        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                mPhoneNumber.getText().toString(),
                60,                                 // the time user input the code
                TimeUnit.SECONDS,
                 this,
                mCallbacks                              // what happen next
        );
    }
}
