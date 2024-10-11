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

import vn.edu.usth.nutritionrecipe.Models.Equipment;
import vn.edu.usth.nutritionrecipe.R;

public class InstructionsEquipmentsAdapter extends RecyclerView.Adapter<InstructionEquipmentsViewHolder> {

    Context context;
    List<Equipment> list;

    public InstructionsEquipmentsAdapter(Context context, List<Equipment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionEquipmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionEquipmentsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions_steps_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionEquipmentsViewHolder holder, int position) {
        holder.detailInstructions_step_items_name.setText(list.get(position).name);
        holder.detailInstructions_step_items_name.setSelected(true);

        Picasso.get().load("https://img.spoonacular.com/equipment_100x100/" + list.get(position).image).into(holder.detailInstructions_step_items_image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstructionEquipmentsViewHolder extends RecyclerView.ViewHolder {

    ImageView detailInstructions_step_items_image;
    TextView detailInstructions_step_items_name;

    public InstructionEquipmentsViewHolder(@NonNull View itemView) {
        super(itemView);
        detailInstructions_step_items_image = itemView.findViewById(R.id.detailInstructions_step_items_image);
        detailInstructions_step_items_name = itemView.findViewById(R.id.detailInstructions_step_items_name);

    }
}
