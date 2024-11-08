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
import com.parse.ParseQuery;
import com.parse.ParseUser;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.usth.nutritionrecipe.R;
import vn.edu.usth.nutritionrecipe.UserSessionManager;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;

    private UserSessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        sessionManager = new UserSessionManager(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

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
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (validateInput()) {
                        login(v);
                        return true;
                    } else {
                        showError();
                        return false;
                    }
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

    public void register(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }

    public void login(View view) {
        String userEmail = this.email.getText().toString();
        String userPassword = this.password.getText().toString();

        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("userData");
        query.whereEqualTo("userEmail", userEmail);
        query.whereEqualTo("userPassword", userPassword);

        // Attempt login by querying the database
        query.getFirstInBackground((user, e) -> {
            if (e == null) {
                // Login successful
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                String userId = user.getObjectId();
                sessionManager.saveUserIdToStorage(userId);

                // Redirect to main activity
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                // Login failed
                Toast.makeText(this, "Login Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput() {
        return !email.getText().toString().isEmpty() && !password.getText().toString().isEmpty();
    }

    private void showError() {
        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
    }
}