package com.example.smoothie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterUserActivity extends AppCompatActivity {

    Button btnCreateUserAccount;
    EditText txtUserName, txtUserContact, txtUserEmail, txtUserPassword;
    ProgressDialog loadingBar;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        btnCreateUserAccount = findViewById(R.id.btnCreateUserAccount);

        txtUserName = findViewById(R.id.txtUserName);
        txtUserContact = findViewById(R.id.txtUserContact);
        txtUserEmail = findViewById(R.id.txtUserEmail);
        txtUserPassword = findViewById(R.id.txtUserPassword);

        fAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);

        //        if(fAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
//            finish();
//        }

        btnCreateUserAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String name = txtUserName.getText().toString();
        String contact = txtUserContact.getText().toString();
        String email = txtUserEmail.getText().toString();
        String password = txtUserPassword.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Enter your name",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(contact)){
            Toast.makeText(this, "Please Enter your phone number",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter your email",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter your password",Toast.LENGTH_LONG).show();
        }else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking the account");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            VaildateContactNumber(name, contact, email, password);
        }

    }

    private void VaildateContactNumber(final String name, final String contact, final String email, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!(dataSnapshot.child("Users").child(contact).exists())){
                            HashMap<String, Object> userdataMap = new HashMap<>();
                            userdataMap.put("contact", contact);
                            userdataMap.put("email", email);
                            userdataMap.put("name", name);
                            userdataMap.put("password", password);

                            RootRef.child("Users").child(contact).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUserActivity.this, "Congratulations, Your account has been created", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent =  new Intent(RegisterUserActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }else{
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterUserActivity.this, "Error, Please Try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(RegisterUserActivity.this, "This "+contact+ " already exists !", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            Toast.makeText(RegisterUserActivity.this, "Please try again using another phone number", Toast.LENGTH_SHORT).show();

                            Intent intent =  new Intent(RegisterUserActivity.this, MainActivity.class);
                            startActivity(intent);

                        }
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}