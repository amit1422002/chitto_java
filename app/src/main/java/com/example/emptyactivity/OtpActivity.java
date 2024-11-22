package com.example.emptyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String verificationId;

    private EditText otpField;
    private Button verifyOtpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Retrieve phone number from intent
        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        // Initialize UI components
        otpField = findViewById(R.id.otp_field);
        verifyOtpButton = findViewById(R.id.verify_otp_button);

        // Initially disable the Verify OTP button
        verifyOtpButton.setEnabled(false);

        // Send OTP
        sendOtp(phoneNumber);

        // Handle Verify OTP button click
        verifyOtpButton.setOnClickListener(v -> {
            String otp = otpField.getText().toString().trim();
            if (!otp.isEmpty()) {
                verifyOtp(otp);
            } else {
                Toast.makeText(this, "Enter a valid OTP", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOtp(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout duration
                .setActivity(this)                // Activity for callbacks
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        // Auto-retrieval of OTP, directly sign in the user
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        // Handle verification failure
                        Toast.makeText(OtpActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        // Save the verification ID for manual OTP entry
                        OtpActivity.this.verificationId = verificationId;
                        Toast.makeText(OtpActivity.this, "OTP sent to your phone.", Toast.LENGTH_SHORT).show();

                        // Enable the Verify OTP button now that we have the verification ID
                        verifyOtpButton.setEnabled(true);
                    }
                })
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOtp(String otp) {
        if (verificationId == null) {
            Toast.makeText(this, "Verification ID is missing. Please wait for OTP to be sent.", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in successful
                        Toast.makeText(OtpActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        navigateToHome();
                    } else {
                        // Sign-in failed
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Sign-in failed";
                        Toast.makeText(OtpActivity.this, "Sign-in failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToHome() {
        Intent intent = new Intent(OtpActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(); // Prevent going back to OTP screen
    }
}
