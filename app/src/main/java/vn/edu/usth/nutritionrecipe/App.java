package vn.edu.usth.nutritionrecipe;

import android.app.Application;
import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Fwbvp394UxZN5d7RJoufuJWEeJzDI1ek9HPPy9F7")
                .clientKey("Yql2tOqO5RXlmP2C9Va4vCwbXGjg6QwCpvRv8DjX")
                .server("https://parseapi.back4app.com")
                .build());
    }
}
