package fr.roulette.dev.latambouille.ui.recipe;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import fr.roulette.dev.latambouille.AppDatabase;
import fr.roulette.dev.latambouille.R;
import fr.roulette.dev.latambouille.entity.Recipe;
import fr.roulette.dev.latambouille.entity.RecipeDAO;

public class ViewRecipeFragment extends Fragment {
  private AppDatabase database;
  private static final String ARG_RECIPE_ID = "recipeId";
  private int recipeId = -1;
  private RecipeDAO recipeDAO;
  private Recipe recipe;

  public ViewRecipeFragment() {
    // Required empty public constructor
  }

  public static ViewRecipeFragment newInstance(int recipeId) {
    ViewRecipeFragment fragment = new ViewRecipeFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_RECIPE_ID, recipeId);
    fragment.setArguments(args);
    return fragment;
  }

  private void accessDatabase() {
    database = AppDatabase.getInstance(requireContext());
    recipeDAO = database.recipeDao();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      recipeId = getArguments().getInt(ARG_RECIPE_ID, -1);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_view_recipe, container, false);

    ImageButton backButton = view.findViewById(R.id.backButton);
    backButton.setOnClickListener(v -> {
      Activity activity = requireActivity();
      activity.finish();
    });

    if (recipeId != -1) {
      new Thread(() -> {
        accessDatabase();
        recipe = recipeDAO.getRecipeById(recipeId);

        requireActivity().runOnUiThread(() -> {
          updateUI(view, recipe);
        });
      }).start();
    }

    return view;
  }

  private void updateUI(View view, Recipe recipe) {

    if (recipe != null) {
      TextView nameTextView = view.findViewById(R.id.name_placeholder);
      if (nameTextView != null) {
        nameTextView.setText(recipe.getName());
      }

      TextView ingredientsTextView = view.findViewById(R.id.ingredient_placeholder);
      if (ingredientsTextView != null) {
        ingredientsTextView.setText(recipe.getIngredients());
      }

      TextView catTextView = view.findViewById(R.id.cat_placeholder);
      if (catTextView != null) {
        catTextView.setText(database.categoryDao().getCategoryById(recipe.getCategory()).getName());
      }

      TextView instructionsTextView = view.findViewById(R.id.prep_placeholder);
      if (instructionsTextView != null) {
        instructionsTextView.setText(recipe.getInstructions());
      }
    }
  }
}