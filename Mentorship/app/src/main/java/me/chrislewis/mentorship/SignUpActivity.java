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
        newUser.setUsername(usernameInput.getText().toString());
        newUser.setPassword(passwordInput.getText().toString());
        newUser.setEmail(emailInput.getText().toString());
        newUser.put("name", nameInput.getText().toString());
        newUser.put("education", "N/A");
        newUser.put("organization", "N/A");
        newUser.put("relativeDistance", 10);
        newUser.put("location", new ParseGeoPoint());
        newUser.put("job", "N/A");
        newUser.put("description", "N/A");
        newUser.saveInBackground();
    }

}
