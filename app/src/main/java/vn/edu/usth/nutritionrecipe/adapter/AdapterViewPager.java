package vn.edu.usth.nutritionrecipe.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class AdapterViewPager extends FragmentStateAdapter {
    ArrayList<Fragment> arr;
    public AdapterViewPager(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> fragmentArrayList) {
        super(fragmentActivity);
        this.arr = fragmentArrayList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return arr.get(position);
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }
}
