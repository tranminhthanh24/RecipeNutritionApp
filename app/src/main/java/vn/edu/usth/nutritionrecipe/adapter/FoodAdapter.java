package vn.edu.usth.nutritionrecipe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import vn.edu.usth.nutritionrecipe.R;
import vn.edu.usth.nutritionrecipe.object.FoodList;

public class FoodAdapter extends ArrayAdapter<FoodList> {
    public FoodAdapter(@NonNull Context context, ArrayList<FoodList> dataArrayList) {
        super(context, R.layout.fragment_explore_list_items, dataArrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        FoodList listData = getItem(position);
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_explore_list_items, parent, false);
        }
        ImageView listImage = view.findViewById(R.id.listImage);
        TextView listName = view.findViewById(R.id.listName);
        TextView listTime = view.findViewById(R.id.listTime);

        listImage.setImageResource(listData.image);
        listName.setText(listData.name);
        listTime.setText(listData.time);
        return view;
    }
}
