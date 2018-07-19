package me.chrislewis.mentorship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText emailInput;
    private EditText nameInput;
    private Button registerButton;
    private ParseUser newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameInput = findViewById(R.id.newUsername);
        passwordInput = findViewById(R.id.newPassword);
        emailInput = findViewById(R.id.newEmail);
        nameInput = findViewById(R.id.newName);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            Log.d("SignUpActivity", "Successfully registered new user!");
                            Intent intent = new Intent(SignUpActivity.this, SignUpDetailsActivity.class);
                            startActivity(intent);
                            finish();
                            //login(usernameInput.getText().toString(), passwordInput.getText().toString());
                        }
                        else {
                            Log.d("SignUpActivity", "Failed to register new user.");
                        }
                    }
                });
            }
        });
    }

    private void registerUser() {
        newUser = new ParseUser();
        newUser.setUsername(usernameInput.getText().toString());
        newUser.setPassword(passwordInput.getText().toString());
        newUser.setEmail(emailInput.getText().toString());
        newUser.put("name", nameInput.getText().toString());
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e == null) {
                    Log.d("SignUpActivity", "Log in successful!");
                    final Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Log.d("SignUpActivity", "Login failure.");
                    e.printStackTrace();
                }
            }
        });
    }
}
