package vn.edu.usth.nutritionrecipe.ui.explore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import vn.edu.usth.nutritionrecipe.FoodAdapter;
import vn.edu.usth.nutritionrecipe.MainActivity;
import vn.edu.usth.nutritionrecipe.R;
import vn.edu.usth.nutritionrecipe.databinding.FragmentExploreBinding;

public class ExploreFragment extends Fragment {

    FoodAdapter foodAdapter;
    ArrayList<FoodList> dataArrayList = new ArrayList<>();
    FoodList foodList;

    private FragmentExploreBinding binding;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}