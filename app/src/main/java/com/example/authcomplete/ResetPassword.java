package com.example.authcomplete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    EditText emailEt;
    Button resetPwd;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(ResetPassword.this);

        emailEt = findViewById(R.id.emailETreset);
        resetPwd = findViewById(R.id.resetPass);

        final String emailForReset = emailEt.getText().toString().trim();

        resetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (emailForReset.isEmpty()) {
                    emailEt.setError("Please input an Email");
                } else {
                    progressDialog.setMessage("Sending Mail...");
                    progressDialog.show();
                    mAuth.sendPasswordResetEmail(emailForReset)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ResetPassword.this, "Email Sent!", Toast.LENGTH_SHORT).show();


                                    } else {
                                        Toast.makeText(ResetPassword.this, "Email not Sent!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
