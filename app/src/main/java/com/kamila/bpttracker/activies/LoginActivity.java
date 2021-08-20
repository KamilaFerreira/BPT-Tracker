package com.kamila.bpttracker.activies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.kamila.bpttracker.R;
import com.kamila.bpttracker.config.ConfigFirebase;
import com.kamila.bpttracker.model.User;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText edtEmail;
    EditText edtPassword;
    User user;
    FirebaseAuth authentication;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textEmail = edtEmail.getText().toString();
                String textPassword = edtPassword.getText().toString();
                //here the code will validate if the User fill up all the fields, case the fields are not filled, the app will display a message
                if (!textEmail.isEmpty()) {
                    if (!textPassword.isEmpty()) {

                        user = new User(textEmail, textPassword);

                        validateLogin();
                        //this piece of code will validate the existing user

                    } else {
                        Toast.makeText(LoginActivity.this, "Please, fill up the Password", //Toast displays a message
                                Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Please, fill up the E-mail", //Toast displays a message
                            Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    //this method will validate the existing User
    public void validateLogin() {

        authentication = ConfigFirebase.getFirebaseAuthentication();
        authentication.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    openMainScreen();

                } else {
                    String exception = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        exception = "User does not exist";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        exception = "Email and Password does not exist";
                    } catch (Exception e) {
                        exception = "Error to Register User" + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, exception, // the Toast displyas the error message
                            Toast.LENGTH_LONG).show();
                }

            }
        });

    }



    //this method Opens the Main Screen

    private void openMainScreen() {
        //authentication.signOut();
        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email" , user.getEmail());
        editor.apply();

        startActivity(new Intent(this, MainActivity.class));
        finish(); //this method closes the Login Activity

    }
}


