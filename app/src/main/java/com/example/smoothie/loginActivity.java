package com.example.smoothie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smoothie.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

public class loginActivity extends AppCompatActivity {

    //not working kindly check.......
    //**
    //**
    //**
    //**
    //**
    //**
    EditText login_contact, login_password;
    TextView txtAccountNav;
    Button btnLogin;
    ProgressDialog loadingBar;
    FirebaseAuth fAuth;

    private String parentDbName = "Users";
    private CheckBox chkBoxRememberMe ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtAccountNav = findViewById(R.id.txtIDontHaveAccount);
        login_contact = findViewById(R.id.login_contact);
        login_password = findViewById(R.id.login_password);

        btnLogin = findViewById(R.id.btnLogin);

        loadingBar = new ProgressDialog(this);

        fAuth = FirebaseAuth.getInstance();
        chkBoxRememberMe = findViewById(R.id.chkBoxRememberMe);
        Paper.init(this);

        txtAccountNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateRegister();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }

    private void loginUser() {
//        String contact = login_contact.getText().toString();
//        String password = login_password.getText().toString();
//
//        if(TextUtils.isEmpty(contact)){
//            Toast.makeText(this, "Please Enter your phone number",Toast.LENGTH_LONG).show();
//        }else if(TextUtils.isEmpty(password)){
//            Toast.makeText(this, "Please Enter your password",Toast.LENGTH_LONG).show();
//        }else{
//            loadingBar.setTitle("Login Account");
//            loadingBar.setMessage("Please wait, while we are checking the account");
//            loadingBar.setCanceledOnTouchOutside(false);
//            loadingBar.show();
//
//            AllowAccessToAccount(contact, password);
//        }

        String contact = login_contact.getText().toString().trim();
        String password = login_password.getText().toString().trim();

        if(TextUtils.isEmpty(contact)){
            login_contact.setError("Email is Required");
            return;
        }

        else if(TextUtils.isEmpty(password)){
            login_password.setError("Password is required");
            return;
        }

        else{
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the account");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(contact, password);
        }

    }

    private void AllowAccessToAccount(final String contact, String password) {
//        final DatabaseReference RootRef;
//        RootRef = FirebaseDatabase.getInstance().getReference();
//
//        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child(parentDbName).child(contact).exists()){
//                    Users usersData = dataSnapshot.child(parentDbName).child(contact).getValue(Users.class);
//
//                    if(usersData.getContact().equals(login_contact)){
//                        if(usersData.getPassword().equals(login_password)){
//                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
//
//                            Intent intent =  new Intent(LoginActivity.this, HomeActivity.class);
//                            startActivity(intent);
//                            loadingBar.dismiss();
//
//                        }else{
//                            loadingBar.dismiss();
//                            Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }else{
//                    Toast.makeText(LoginActivity.this, "Account with this "+contact+ " number do not exists", Toast.LENGTH_SHORT).show();
//                    loadingBar.dismiss();
//                    Toast.makeText(LoginActivity.this, "You need to create an account", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        //this is not working
        if(chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey,contact);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }

        fAuth.signInWithEmailAndPassword(contact,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(loginActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    loadingBar.dismiss();

                }
                else{
                    Toast.makeText(loginActivity.this,"Error !" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    public void navigateRegister(){
        Intent intent = new Intent(this,RegisterUserActivity.class);
        startActivity(intent);
    }
}