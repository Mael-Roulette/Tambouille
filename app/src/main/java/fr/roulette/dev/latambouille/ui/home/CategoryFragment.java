package fr.roulette.dev.latambouille.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.roulette.dev.latambouille.AppDatabase;
import fr.roulette.dev.latambouille.R;
import fr.roulette.dev.latambouille.SecondActivity;
import fr.roulette.dev.latambouille.entity.CategoryDAO;
import fr.roulette.dev.latambouille.entity.Recipe;
import fr.roulette.dev.latambouille.entity.RecipeDAO;
import fr.roulette.dev.latambouille.ui.RecipesListAdapter;

public class CategoryFragment extends Fragment {

    private static final String TAG = "CategoryFragment";
    private int categoryId;
    private RecipesListAdapter recipesAdapter;
    private RecipeDAO recipeDAO;
    private CategoryDAO categoryDAO;

    public CategoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            categoryId = getArguments().getInt("catId", -1);
        }

        accessDatabase();
    }

    private void accessDatabase() {
        AppDatabase database = AppDatabase.getInstance(requireContext());
        recipeDAO = database.recipeDao();
        categoryDAO = database.categoryDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_categoryFragment_to_homeFragment);
        });

        TextView catTitle = view.findViewById(R.id.cat_title);
        catTitle.setText(categoryDAO.getCategoryNameById(categoryId));

        ListView recipesListView = view.findViewById(R.id.recipesListView);

        recipesAdapter = new RecipesListAdapter((Activity) requireContext(), new ArrayList<>());
        recipesListView.setAdapter(recipesAdapter);

        recipesListView.setOnItemClickListener((parent, v, position, id) -> {
            Recipe selectedRecipe = recipesAdapter.getItem(position);

            Intent intent = new Intent(requireContext(), SecondActivity.class);
            assert selectedRecipe != null;
            intent.putExtra("recipeId", selectedRecipe.getRecipeId());
            intent.putExtra("fragmentToLoad", "viewRecipe");

            startActivity(intent);
        });

        if (categoryId != -1) {
            loadRecipesByCategory();
        } else {
            Log.e(TAG, "ID de catégorie invalide");
        }

        return view;
    }

    private void loadRecipesByCategory() {
        new Thread(() -> {
            accessDatabase();

            List<Recipe> fetchedRecipes = Arrays.asList(recipeDAO.getRecipesByCategory(categoryId));

            if (isAdded() && getActivity() != null) {
                requireActivity().runOnUiThread(() -> recipesAdapter.updateData(fetchedRecipes));
            }
        }).start();
    }
}
