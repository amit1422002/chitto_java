package com.example.emptyactivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WinCertificateActivity extends AppCompatActivity {

    private EditText answerField;
    private Button submitButton;
    private TextView timerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_certificate);


        answerField = findViewById(R.id.answer_field);
        submitButton = findViewById(R.id.submit_button);
        timerTextView = findViewById(R.id.timer_text_view);


        submitButton.setEnabled(false);


        new CountDownTimer(30000, 1000) { // 30 seconds timer
            @Override
            public void onTick(long millisUntilFinished) {
                // Update timer text
                long secondsLeft = millisUntilFinished / 1000;
                timerTextView.setText("Time left: " + secondsLeft + " seconds");
            }

            @Override
            public void onFinish() {

                answerField.setEnabled(false);
                submitButton.setEnabled(true);
                timerTextView.setText("Time's up! Submit your answer.");
            }
        }.start();


        submitButton.setOnClickListener(v -> {
            String answer = answerField.getText().toString().trim();
            if (!answer.isEmpty()) {
                Toast.makeText(WinCertificateActivity.this, "Answer submitted successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(WinCertificateActivity.this, "Please write your answer!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
