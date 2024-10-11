package vn.edu.usth.nutritionrecipe.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.usth.nutritionrecipe.Models.Step;
import vn.edu.usth.nutritionrecipe.R;

public class InstructionStepAdapter extends RecyclerView.Adapter<InstructionStepViewHolder> {

    Context context;
    List<Step> list;

    public InstructionStepAdapter(Context context, List<Step> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionStepViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions_steps, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionStepViewHolder holder, int position) {
        holder.detailInstructions_step_number.setText(String.valueOf(list.get(position).number));
        holder.detailInstructions_step_title.setText(list.get(position).step);

        holder.recycler_detailInstructions_ingredients.setHasFixedSize(true);
        holder.recycler_detailInstructions_ingredients.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        InstructionsIngredientsAdapter instructionsIngredientsAdapter = new InstructionsIngredientsAdapter(context, list.get(position).ingredients);
        holder.recycler_detailInstructions_ingredients.setAdapter(instructionsIngredientsAdapter);

        holder.recycler_detailInstructions_equipments.setHasFixedSize(true);
        holder.recycler_detailInstructions_equipments.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        InstructionsEquipmentsAdapter instructionsEquipmentsAdapter = new InstructionsEquipmentsAdapter(context, list.get(position).equipment);
        holder.recycler_detailInstructions_equipments.setAdapter(instructionsEquipmentsAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class InstructionStepViewHolder extends RecyclerView.ViewHolder {
    TextView detailInstructions_step_number, detailInstructions_step_title;
    RecyclerView recycler_detailInstructions_equipments, recycler_detailInstructions_ingredients;

    public InstructionStepViewHolder(@NonNull View itemView) {
        super(itemView);

        detailInstructions_step_number = itemView.findViewById(R.id.detailInstructions_step_number);
        detailInstructions_step_title = itemView.findViewById(R.id.detailInstructions_step_title);
        recycler_detailInstructions_equipments = itemView.findViewById(R.id.recycler_detailInstructions_equipments);
        recycler_detailInstructions_ingredients = itemView.findViewById(R.id.recycler_detailInstructions_ingredients);

    }
}