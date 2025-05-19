package fr.roulette.dev.latambouille.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.roulette.dev.latambouille.R;
import fr.roulette.dev.latambouille.entity.Recipe;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private Recipe[] recipes;
    private OnRecipeClickListener clickListener;

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    public RecipesAdapter(Recipe[] recipes) {
        this.recipes = recipes;
    }

    public void setOnItemClickListener(OnRecipeClickListener listener) {
        this.clickListener = listener;
    }

    public void updateRecipes(Recipe[] recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes[position];
        holder.recipeName.setText(recipe.getName());

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onRecipeClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes != null ? recipes.length : 0;
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
        }
    }
}