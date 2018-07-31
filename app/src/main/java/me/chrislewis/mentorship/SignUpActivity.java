package me.chrislewis.mentorship;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import me.chrislewis.mentorship.models.User;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText emailInput;
    private EditText nameInput;
    private Button registerButton;
    private ParseUser newUser;
    private User user;

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
                            Bundle loginInfo = new Bundle();
                            loginInfo.putString("username", usernameInput.getText().toString());
                            loginInfo.putString("password", passwordInput.getText().toString());
                            intent.putExtras(loginInfo);
                            startActivity(intent);
                            finish();
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
        user = new User(newUser);

        user.setUsername(usernameInput.getText().toString());
        user.setPassword(passwordInput.getText().toString());
        user.setEmail(emailInput.getText().toString());
        user.setName(nameInput.getText().toString());
        user.setEducation("N/A");
        user.setOrganization("N/A");
        user.setLocation(new ParseGeoPoint());
        user.setJob("N/A");
        user.setSync(false);
        user.setDescription("N/A");
        user.saveInBackground();

    }

}
