package vn.edu.usth.nutritionrecipe.ui.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import vn.edu.usth.nutritionrecipe.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.usth.nutritionrecipe.databinding.FragmentExploreBinding;

public class ExploreFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ExploreViewModel exploreViewModel =
                new ViewModelProvider(this).get(ExploreViewModel.class);

        FragmentExploreBinding binding = FragmentExploreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.rcvFood;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<String> fruitList = new ArrayList<>();
        fruitList.add(getString(R.string.item1));
        fruitList.add(getString(R.string.item2));
        fruitList.add(getString(R.string.item3));
        fruitList.add(getString(R.string.item4));

        FruitAdapter adapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);

        recyclerView.setVisibility(View.VISIBLE);

        final TextView textView = binding.btnLoadMore;
        exploreViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    private class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

        private List<String> fruits;

        public FruitAdapter(List<String> fruits) {
            this.fruits = fruits;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(fruits.get(position));
        }

        @Override
        public int getItemCount() {
            return fruits.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}