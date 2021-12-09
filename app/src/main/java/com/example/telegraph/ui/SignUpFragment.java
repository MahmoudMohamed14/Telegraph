package com.example.telegraph.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.telegraph.R;
import com.example.telegraph.model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {
    private FirebaseAuth mAuth ;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button signUp;
    TextView logIn;
    UserInfo info ;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        confirmPassword =view.findViewById(R.id.confirmPassword);
        logIn = view.findViewById(R.id.login_text);
        signUp = view.findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sign_up(v);
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment5());
            }
        });
    }


    private void sign_up(View v){
        String user_name = firstName.getText().toString().trim()+" "+lastName.getText().toString().trim();
        String user_email = email.getText().toString();
        String user_password = password.getText().toString();
        String user_confirmPassword = confirmPassword.getText().toString();

        if(     user_name != null &&
                user_email != null &&
                user_password != null &&
                user_confirmPassword != null &&
                emailValidation(user_email) &&
                passwordValidation(user_password)&&
                confirmPassword(user_password,user_confirmPassword))
        {

            mAuth.createUserWithEmailAndPassword(user_email, user_password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firstName.setText("");
                                lastName.setText("");
                                email.setText("");
                                password.setText("");
                                confirmPassword.setText("");
                                String token = FirebaseInstanceId.getInstance().getToken();
                                info = new UserInfo(mAuth.getCurrentUser().getUid(),user_name,user_password,token,"","","");
                                reference.child(mAuth.getCurrentUser().getUid()).setValue(info);
                                Navigation.findNavController(v)
                                        .navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment5());
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getContext(), "Sign Up Failed Failed", Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }
        }
    private boolean emailValidation(String email){
            String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);


            return matcher.matches();

    }
    private boolean passwordValidation(String password){
        String regex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    private boolean confirmPassword(String password,String confirm){
        return password.equals(confirm);
    }
}