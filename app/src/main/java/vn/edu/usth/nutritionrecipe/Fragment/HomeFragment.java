package vn.edu.usth.nutritionrecipe.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.usth.nutritionrecipe.Adapter.RandomRecipeAdapter;
import vn.edu.usth.nutritionrecipe.Listeners.RandomRecipeResponseListener;
import vn.edu.usth.nutritionrecipe.Listeners.RecipeClickListener;
import vn.edu.usth.nutritionrecipe.Models.RandomRecipeApiResponse;
import vn.edu.usth.nutritionrecipe.R;
import vn.edu.usth.nutritionrecipe.RequestManager;

public class HomeFragment extends Fragment {

    private ProgressDialog dialog;
    private RequestManager manager;
    private RandomRecipeAdapter adapter;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private SearchView searchView;
    private List<String> tags = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading...");

        searchView = view.findViewById(R.id.searchView_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tags.clear();
                tags.add(query);
                manager.getRandomRecipes(randomRecipeResponseListener, tags);
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        spinner = view.findViewById(R.id.spinner_tags);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.tags, R.layout.spinner_text);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(spinnerSelectedListener);

        recyclerView = view.findViewById(R.id.recycler_random);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        manager = new RequestManager(getContext());
        //manager.getRandomRecipes(randomRecipeResponseListener);
        //dialog.show();

        return view;
    }

    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            adapter = new RandomRecipeAdapter(getContext(), response.recipes, recipeClickListener);
            recyclerView.setAdapter(adapter);
            dialog.dismiss();
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    };

    private final AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            tags.clear();
            tags.add(adapterView.getSelectedItem().toString());
            manager.getRandomRecipes(randomRecipeResponseListener, tags);
            dialog.show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            // Handle recipe click event (e.g., open Detail Activity)
        }
    };
}
