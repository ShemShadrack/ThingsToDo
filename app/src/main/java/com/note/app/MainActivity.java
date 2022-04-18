package com.note.app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class MainActivity extends AppCompatActivity {

    public EditText editTextEmailLogin,editTextPasswordLogin;
    public Button loginButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

        editTextEmailLogin = (EditText) findViewById(R.id.editTextEmailLogin);
        editTextPasswordLogin = (EditText) findViewById(R.id.editTextPasswordLogin);
        loginButton = (Button) findViewById(R.id.loginButton);

        mAuth=FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.loginButton:
                        userLogin();
                        break;
                }
            }
        });
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }

    private void userLogin() {
        String email=editTextEmailLogin.getText().toString().trim();
        String password=editTextPasswordLogin.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmailLogin.setError("Email is required");
            editTextEmailLogin.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmailLogin.setError("Please provide a valid email address");
            editTextEmailLogin.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPasswordLogin.setError("Password is required");
            editTextPasswordLogin.requestFocus();
            return;
        }
        if(password.length() <6){
            editTextPasswordLogin.setError("Min password length should be less than 6 characters");
            editTextPasswordLogin.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        //redirect user profile
                        startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                    }else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email to verify account ", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Failed to login please check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });

    }



}
