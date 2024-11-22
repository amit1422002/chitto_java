package com.example.emptyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView userDataTextView;
    private Button logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userDataTextView = view.findViewById(R.id.user_data_text_view);
        logoutButton = view.findViewById(R.id.logout_button);


        fetchUserData();


        logoutButton.setOnClickListener(v -> logout());

        return view;
    }


    private void fetchUserData() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder userData = new StringBuilder();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String name = userSnapshot.child("name").getValue(String.class);
                    String email = userSnapshot.child("emailid").getValue(String.class);
                    Long age = userSnapshot.child("age").getValue(Long.class);

                    userData.append("Name: ").append(name).append("\n");
                    userData.append("Email: ").append(email).append("\n");
                    userData.append("Age: ").append(age).append("\n\n");
                }

                userDataTextView.setText(userData.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userDataTextView.setText("Failed to load data: " + error.getMessage());
            }
        });
    }


    private void logout() {

        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        Toast.makeText(getActivity(), "Logged out successfully!", Toast.LENGTH_SHORT).show();


        getActivity().finish();
    }
}
