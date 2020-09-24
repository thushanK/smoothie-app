package com.example.smoothie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.smoothie.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button btnSqueeze;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSqueeze = findViewById(R.id.btnSqueeze);
        fAuth = FirebaseAuth.getInstance();
        Paper.init(this);  //for remember me

        btnSqueeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateLogin();

            }
        });
        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if(UserPhoneKey !=  null && UserPasswordKey != null ){
            if(TextUtils.isEmpty(UserPhoneKey) &&  !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess(UserPhoneKey,UserPasswordKey);
            }

        }




    }

    private void AllowAccess(String contact, String password) {
        fAuth.signInWithEmailAndPassword(contact,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Logged in already", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));


                } else {
                    Toast.makeText(MainActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void navigateLogin() {
        Intent intent = new Intent(this, loginActivity.class);
        startActivity(intent);
    }
}