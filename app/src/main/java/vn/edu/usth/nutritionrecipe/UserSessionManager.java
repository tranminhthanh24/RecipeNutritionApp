package vn.edu.usth.nutritionrecipe;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;

public class UserSessionManager {

    private static final String PREF_NAME = "UserPrefs";
    private static final String USER_ID_KEY = "userId";

    private Context context;

    public UserSessionManager(Context context) {
        this.context = context;
    }

    // Save the user ID to SharedPreferences
    public void saveUserIdToStorage(String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID_KEY, userId);
        editor.apply();
    }

    // Get the user ID from SharedPreferences
    public String getUserIdFromStorage() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_ID_KEY, null); // Returns null if no user is logged in
    }

    // Login user by verifying email and password
    public void loginUser(String email, String password) {
        // Assume login logic with Parse API
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("email", email);
        query.whereEqualTo("password", password); // Replace this with proper password check
        query.findInBackground((objects, e) -> {
            if (e == null && !objects.isEmpty()) {
                ParseObject user = objects.get(0);
                String userId = user.getObjectId(); // Or any other unique identifier
                saveUserIdToStorage(userId); // Save user ID upon successful login
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                // Proceed to the next screen or home activity
            } else {
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Logout the user by removing user ID from SharedPreferences
    public void logoutUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_ID_KEY); // Remove the user ID
        editor.apply();
    }

    // Check if the user is logged in
    public boolean isUserLoggedIn() {
        return getUserIdFromStorage() != null;
    }
}

