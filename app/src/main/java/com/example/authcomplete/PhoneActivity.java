package com.example.authcomplete;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {
    EditText phoneShow, verCode;
    private String VerificationId;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference user = database.getReference("User");
    Button verify;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

verCode = findViewById(R.id.VerCodeET);
        mAuth = FirebaseAuth.getInstance();
        phoneShow = findViewById(R.id.phoneShow);

        String numberForVer = getIntent().getStringExtra("PhoneNumber");
phoneShow.setText(numberForVer);
        sendVerCode(numberForVer);




        verify = findViewById(R.id.VerBut);
        verify.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {


                String code = verCode.getText().toString().trim();

                if (code.isEmpty()) {

                    verCode.setError("Enter Code Please");
                    verCode.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationId, code);
        LinkWithCredential(credential);




    }

    private void LinkWithCredential(PhoneAuthCredential credential) {
      final  String password = getIntent().getStringExtra("Password");
        final   String firstName = getIntent().getStringExtra("FirstName");
        final  String lastName = getIntent().getStringExtra("LastName");
        final   String email = getIntent().getStringExtra("Email");
       final String numberForVer = getIntent().getStringExtra("PhoneNumber");


        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            User newUser = new User(email, firstName, lastName, numberForVer, password);
                            user.child(mAuth.getCurrentUser().getUid()).setValue(newUser);

                            Intent intent = new Intent(PhoneActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(PhoneActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });


    }

    private void sendVerCode(String number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallback);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            VerificationId = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code!= null){
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(PhoneActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
        }
    };
}
