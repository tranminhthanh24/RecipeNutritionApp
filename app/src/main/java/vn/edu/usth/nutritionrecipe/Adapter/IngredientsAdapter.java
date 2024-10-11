package vn.edu.usth.nutritionrecipe.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vn.edu.usth.nutritionrecipe.Models.ExtendedIngredient;
import vn.edu.usth.nutritionrecipe.R;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewHolder> {

    Context context;
    List<ExtendedIngredient> list;

    public IngredientsAdapter(Context context, List<ExtendedIngredient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_ingredients, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        holder.detailIngredients_name.setText(list.get(position).name);
        holder.detailIngredients_name.setSelected(true);
        holder.detailIngredients_quantity.setText(list.get(position).original);
        holder.detailIngredients_quantity.setSelected(true);
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/" + list.get(position).image).into(holder.detailIngredients_image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class IngredientsViewHolder extends RecyclerView.ViewHolder {
    TextView detailIngredients_name, detailIngredients_quantity;
    ImageView detailIngredients_image;

    public IngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        detailIngredients_name = itemView.findViewById(R.id.detailIngredients_name);
        detailIngredients_quantity = itemView.findViewById(R.id.detailIngredients_quantity);
        detailIngredients_image = itemView.findViewById(R.id.detailIngredients_image);

    }
}