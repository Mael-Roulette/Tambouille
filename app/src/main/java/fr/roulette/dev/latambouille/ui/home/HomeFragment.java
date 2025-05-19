package fr.roulette.dev.latambouille.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import fr.roulette.dev.latambouille.AppDatabase;
import fr.roulette.dev.latambouille.R;
import fr.roulette.dev.latambouille.RecipeChangeListener;
import fr.roulette.dev.latambouille.RecipeEventManager;
import fr.roulette.dev.latambouille.SecondActivity;
import fr.roulette.dev.latambouille.entity.CategoryDAO;
import fr.roulette.dev.latambouille.entity.Recipe;
import fr.roulette.dev.latambouille.entity.RecipeDAO;
import fr.roulette.dev.latambouille.ui.CategoryAdapter;
import fr.roulette.dev.latambouille.ui.RecipesAdapter;

public class HomeFragment extends Fragment implements RecipeChangeListener {
  private RecipesAdapter recipesAdapter;
  private RecipeDAO recipeDAO;
  private CategoryDAO categoryDAO;

  public HomeFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AppDatabase database = AppDatabase.getInstance(requireContext());
    recipeDAO = database.recipeDao();
    categoryDAO = database.categoryDao();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, container, false);

    RecyclerView categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
    RecyclerView recipesRecyclerView = view.findViewById(R.id.recipesRecyclerView);

    CategoryAdapter catAdapter = new CategoryAdapter(categoryDAO.getAllCategories());
    categoriesRecyclerView.setAdapter(catAdapter);

    catAdapter.setOnItemClickListener(category -> {
      Bundle args = new Bundle();
      args.putInt("catId", category.getCategoryId());

      NavController navController = Navigation.findNavController(view);
      navController.navigate(R.id.action_homeFragment_to_categoryFragment, args);
    });

    recipesAdapter = new RecipesAdapter(recipeDAO.getAllRecipes());
    recipesRecyclerView.setAdapter(recipesAdapter);

    recipesAdapter.setOnItemClickListener(recipe -> {
      Intent intent = new Intent(requireContext(), SecondActivity.class);
      intent.putExtra("recipeId", recipe.getRecipeId());
      intent.putExtra("fragmentToLoad", "viewRecipe");
      startActivity(intent);
    });

    return view;
  }

  @Override
  public void onStart() {
    super.onStart();
    RecipeEventManager.getInstance().addListener(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    RecipeEventManager.getInstance().removeListener(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    refreshRecipesList();
  }

  @Override
  public void onRecipeChanged() {
    refreshRecipesList();
  }

  private void refreshRecipesList() {
    new Thread(() -> {
      final Recipe[] recipes = recipeDAO.getAllRecipes();
      if (isAdded() && getActivity() != null) {
        requireActivity().runOnUiThread(() -> recipesAdapter.updateRecipes(recipes));
      }
    }).start();
  }
}