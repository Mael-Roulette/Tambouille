package fr.roulette.dev.latambouille.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import fr.roulette.dev.latambouille.R;
import fr.roulette.dev.latambouille.entity.Recipe;

public class RecipesListAdapter extends ArrayAdapter<Recipe> {
    private List<Recipe> recipes;

    public RecipesListAdapter(Activity context, List<Recipe> recipes) {
        super(context, 0, recipes);
        this.recipes = recipes;
    }

    public void updateData(List<Recipe> newRecipes) {
        this.recipes = newRecipes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return recipes != null ? recipes.size() : 0;
    }

    @Override
    public Recipe getItem(int position) {
        return recipes != null && position >= 0 && position < recipes.size() ? recipes.get(position) : null;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent ) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_recipe_item, parent, false);
        }

        Recipe currentRecipe = getItem(position);

        TextView nameRecipeTextView = listItemView.findViewById(R.id.nameRecipe);
        assert currentRecipe != null;
        nameRecipeTextView.setText(currentRecipe.getName());

        TextView timeRecipeTextView = listItemView.findViewById(R.id.timeRecipe);
        timeRecipeTextView.setText(currentRecipe.getPreparationTime());

        return listItemView;
    }
}
