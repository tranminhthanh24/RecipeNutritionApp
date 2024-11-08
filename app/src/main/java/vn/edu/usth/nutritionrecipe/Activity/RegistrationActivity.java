package vn.edu.usth.nutritionrecipe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.usth.nutritionrecipe.R;

public class RegistrationActivity extends AppCompatActivity {

    EditText email, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    password.requestFocus();
                    return true;
                }
                return false;
            }
        });

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    confirmPassword.requestFocus();
                    return true;
                }
                return false;
            }});

        confirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    register(v);
                    return true;
                }
                return false;
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void login(View view) {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }

    public void register(View view) {

        String userEmail=this.email.getText().toString();
        String userPassword=this.password.getText().toString();
        String confirmUserPassword=this.confirmPassword.getText().toString();

        if (TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(confirmUserPassword)){
            Toast.makeText(this, "Please enter confirm password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!userPassword.equals(confirmUserPassword)){
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPassword.length() < 6){
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        ParseObject user = new ParseObject("userData");

        user.put("userEmail", userEmail);
        user.put("userPassword", userPassword);

        user.saveInBackground(e -> {
            if (e == null) {
                // Registration was successful
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                // Redirect to login screen or main activity
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            } else {
                // Registration failed
                Toast.makeText(this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}