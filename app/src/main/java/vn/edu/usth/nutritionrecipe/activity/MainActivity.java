package vn.edu.usth.nutritionrecipe.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

import vn.edu.usth.nutritionrecipe.R;
import vn.edu.usth.nutritionrecipe.adapter.AdapterViewPager;
import vn.edu.usth.nutritionrecipe.fragment.ExploreFragment;
import vn.edu.usth.nutritionrecipe.fragment.FavoriteFragment;
import vn.edu.usth.nutritionrecipe.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

    ViewPager2 pagerMain;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        pagerMain = findViewById(R.id.pagerMain);
        navView = findViewById(R.id.nav_view);

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
    }
}
