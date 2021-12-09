package com.example.telegraph.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.telegraph.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgetpasswordnav extends Fragment {

EditText resetemail;
Button resetpassord;
   FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgetpasswordnav, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        resetemail=view.findViewById(R.id.email_reset);
        resetpassord=view.findViewById(R.id.reset_password);
        resetpassword( view );
    }

    private void resetpassword(final View v) {
        final String email=resetemail.getText().toString().trim();
        if(email.isEmpty()){
            resetemail.setError("email is requried");
            resetemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            resetemail.setError("please inter valid email");
            resetemail.requestFocus();
            return;
        }
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(v.getContext(), " check yor email", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(v.getContext(), " some thing wrong happend!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }}