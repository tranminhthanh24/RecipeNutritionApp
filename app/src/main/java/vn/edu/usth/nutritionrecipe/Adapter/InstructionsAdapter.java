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

import vn.edu.usth.nutritionrecipe.Models.InstructionsResponse;
import vn.edu.usth.nutritionrecipe.R;

public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsViewHolder> {

    Context context;
    List<InstructionsResponse> list;

    public InstructionsAdapter(Context context, List<InstructionsResponse> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionsViewHolder holder, int position) {
        holder.detailInstructions_name.setText(list.get(position).name);
        holder.recycler_detailInstructions_steps.setHasFixedSize(true);

        holder.recycler_detailInstructions_steps.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        InstructionStepAdapter stepAdapter = new InstructionStepAdapter(context, list.get(position).steps);
        holder.recycler_detailInstructions_steps.setAdapter(stepAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstructionsViewHolder extends RecyclerView.ViewHolder {
    TextView detailInstructions_name;
    RecyclerView recycler_detailInstructions_steps;

    public InstructionsViewHolder(@NonNull View itemView) {
        super(itemView);
        detailInstructions_name = itemView.findViewById(R.id.detailInstructions_name);
        recycler_detailInstructions_steps = itemView.findViewById(R.id.recycler_detailInstructions_steps);
    }
}
