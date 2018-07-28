package me.chrislewis.mentorship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private TextView signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            username = findViewById(R.id.tvUsername);
            password = findViewById(R.id.tvPassword);
            loginButton = findViewById(R.id.loginButton);
            signUpButton = findViewById(R.id.tvSignUp);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String usernameString = username.getText().toString();
                    String passwordString = password.getText().toString();
                    login(usernameString, passwordString);
                }
            });

            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if(e == null) {
                    Log.d("LoginActivity", "Log in successful!");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Log.d("LoginActivity", "Login failure.");
                    e.printStackTrace();
                }
            }
        });
    }
}
