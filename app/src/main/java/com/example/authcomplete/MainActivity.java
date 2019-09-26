package com.example.authcomplete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText firstName, lastName, phone, email, password;
    private Button signUpBut;
    private TextView loginTv;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference user = database.getReference("User");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(MainActivity.this);

        firstName = findViewById(R.id.firstNameET);
        lastName = findViewById(R.id.lastNameET);
        phone = findViewById(R.id.phoneET);
        email = findViewById(R.id.emailET);
        password = findViewById(R.id.passwordET);
        signUpBut = findViewById(R.id.signUpButton);
        loginTv = findViewById(R.id.loginTv);

        mAuth = FirebaseAuth.getInstance();

        signUpBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String myEmail = email.getText().toString();
                final String myPassword = password.getText().toString();
                final String myPhone = phone.getText().toString().trim();
                final String myFirstName = firstName.getText().toString().trim();
                final String myLastName = lastName.getText().toString().trim();
                if (myEmail.isEmpty()) {
                    email.setError("Please enter Email");
                    email.requestFocus();
                } else if (myPassword.isEmpty()) {
                    password.setError("Enter your Password Please");
                    password.requestFocus();

                } else if (myFirstName.isEmpty()) {
                    firstName.setError("Enter your First Name Please");
                    firstName.requestFocus();

                }
                else if (myLastName.isEmpty()) {
                    lastName.setError("Enter your Last Name Please");
                    lastName.requestFocus();

                }
                else if (!(myEmail.isEmpty() && myPassword.isEmpty() && myFirstName.isEmpty() && myLastName.isEmpty())) {
                    progressDialog.setMessage("Signing Up...");
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(myEmail, myPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()){
                             progressDialog.dismiss();
                             Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_SHORT).show();



                             Intent intent = new Intent(MainActivity.this, PhoneActivity.class);
                             intent.putExtra("PhoneNumber", myPhone);
                             intent.putExtra("FirstName", myFirstName);
                             intent.putExtra("LastName", myLastName);
                             intent.putExtra("Email", myEmail);
                             intent.putExtra("Password", myPassword);
                             startActivity(intent);
                         } else {

                             Toast.makeText(MainActivity.this, "Login unsuccessful: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                         }
                        }
                    });





                } else {
                    Toast.makeText(MainActivity.this, "Error Occured!! ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }

}
