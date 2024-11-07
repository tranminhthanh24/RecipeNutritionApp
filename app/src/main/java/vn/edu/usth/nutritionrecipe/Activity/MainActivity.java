package vn.edu.usth.nutritionrecipe.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebStorage;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import vn.edu.usth.nutritionrecipe.Adapter.AdapterViewPager;
import vn.edu.usth.nutritionrecipe.Fragment.ExploreFragment;
import vn.edu.usth.nutritionrecipe.Fragment.FavoriteFragment;
import vn.edu.usth.nutritionrecipe.Fragment.HomeFragment;
import vn.edu.usth.nutritionrecipe.R;

public class MainActivity extends AppCompatActivity {

    ViewPager2 pagerMain;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    BottomNavigationView navView;
    FloatingActionButton fabRefresh; // FAB for Refresh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        pagerMain = findViewById(R.id.pagerMain);
        navView = findViewById(R.id.nav_view);
        fabRefresh = findViewById(R.id.fab_refresh); // Initialize the FAB

        // Add fragments to list
        fragmentArrayList.add(new HomeFragment());
        fragmentArrayList.add(new ExploreFragment());
        fragmentArrayList.add(new FavoriteFragment());

        // Setup ViewPager2 Adapter
        AdapterViewPager adapterViewPager = new AdapterViewPager(this, fragmentArrayList);
        pagerMain.setAdapter(adapterViewPager);

        // Sync ViewPager2 with BottomNavigationView
        navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                pagerMain.setCurrentItem(0);
                return true;
            } else if (itemId == R.id.navigation_explore) {
                pagerMain.setCurrentItem(1);
                return true;
            } else if (itemId == R.id.navigation_favorite) {
                pagerMain.setCurrentItem(2);
                return true;
            }
            return false;
        });

        // Sync ViewPager2 swipe with BottomNavigationView selection
        pagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 0) {
                    navView.setSelectedItemId(R.id.navigation_home);
                } else if (position == 1) {
                    navView.setSelectedItemId(R.id.navigation_explore);
                } else if (position == 2) {
                    navView.setSelectedItemId(R.id.navigation_favorite);
                }
            }
        });

        // Set up refresh button functionality
        fabRefresh.setOnClickListener(v -> clearCacheAndRestart());
    }

    // Method to clear cache and restart the activity
    private void clearCacheAndRestart() {
        try {
            // Clear WebView cache
            WebView webView = new WebView(this);
            webView.clearCache(true);
            webView.clearHistory();
            webView.destroy();

            // Clear app cache
            deleteDir(getCacheDir());

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Restart main activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Close current activity
    }

    // Helper method to delete cache directory recursively
    private boolean deleteDir(java.io.File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new java.io.File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}
