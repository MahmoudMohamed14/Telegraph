package com.example.telegraph.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telegraph.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    EditText username,passwordd;
    TextView forgetpassword,sinup;
    Button sinin;
    TextView signUp;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null) {

            Navigation.findNavController(getView()).navigate(LoginFragmentDirections.actionLoginFragment5ToUsersFragment());

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username=view.findViewById(R.id.user_name);
        passwordd=view.findViewById(R.id.pass_word);
        forgetpassword=view.findViewById(R.id.forget_password);
        sinin=view.findViewById(R.id.sin_in);
        signUp = view.findViewById(R.id.sin_up_text);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(LoginFragmentDirections.actionLoginFragment5ToSignUpFragment());
            }
        });
        sinin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_in(v);
            }
        });
    }


    private void sign_in(View v) {
        String email = username.getText().toString().trim();
        String password = passwordd.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Navigation.findNavController(getView()).navigate(LoginFragmentDirections.actionLoginFragment5ToUsersFragment());
                    } else {
                        Toast.makeText(v.getContext(), "failed password", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}