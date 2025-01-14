package com.example.emptyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CertificateFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_certificate, container, false);


        Button winCertificateButton = view.findViewById(R.id.win_certificate_button);


        winCertificateButton.setOnClickListener(v -> {
            // Open WinCertificateActivity
            Intent intent = new Intent(getActivity(), WinCertificateActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
