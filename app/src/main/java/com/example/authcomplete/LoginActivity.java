package com.example.authcomplete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailorPhone, passwordLogin ;
    private Button login;
    private TextView forgotPwd;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailorPhone = findViewById(R.id.emailOrPhoneET);
        passwordLogin = findViewById(R.id.passwordLoginET);
        login = findViewById(R.id.loginBut);
        forgotPwd = findViewById(R.id.forgotPasswordTv);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser = mAuth.getCurrentUser();
                if(mUser != null){
                    Toast.makeText(LoginActivity.this, "You are logged in already", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();

                }
            }
        };

forgotPwd.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(LoginActivity.this, ResetPassword.class);
        startActivity(intent);
    }
});

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String LoginEmail = emailorPhone.getText().toString();
                String LoginPassword = passwordLogin.getText().toString();
                if (LoginEmail.isEmpty()) {
                    emailorPhone.setError("Please enter Email");
                    emailorPhone.requestFocus();
                } else if (LoginPassword.isEmpty()) {
                    passwordLogin.setError("Enter your Password Please");
                    passwordLogin.requestFocus();

                } else if (LoginEmail.isEmpty() && LoginPassword.isEmpty()) {

                    Toast.makeText(LoginActivity.this, "Please fill the Boxes", Toast.LENGTH_SHORT).show();
                } else if (!(LoginEmail.isEmpty() && LoginPassword.isEmpty())) {
                    mAuth.signInWithEmailAndPassword(LoginEmail, LoginPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Error Occured!! ", Toast.LENGTH_SHORT).show();
                            }
else {
                                Intent intHome = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intHome);
                            }

                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Error Occured!! ", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
