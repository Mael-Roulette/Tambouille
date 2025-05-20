package fr.roulette.dev.latambouille.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

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
  private RecipesAdapter searchResultsAdapter;
  private SearchView searchView;
  private RecyclerView searchResultsRecyclerView;
  private TextView searchResultsTitle;
  private ConstraintLayout normalContent;

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
    searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);
    searchView = view.findViewById(R.id.searchView);
    searchResultsTitle = view.findViewById(R.id.searchResultsTitle);
    normalContent = view.findViewById(R.id.normalContent);

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

    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
    recipesRecyclerView.setLayoutManager(layoutManager);
    recipesRecyclerView.setAdapter(recipesAdapter);

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        performSearch(query);
        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        if (newText.length() >= 2) {
          performSearch(newText);
        } else if (newText.isEmpty()) {
          showNormalContent();
        }
        return true;
      }
    });

    searchView.setOnCloseListener(() -> {
      showNormalContent();
      return false;
    });

    searchResultsAdapter = new RecipesAdapter(new Recipe[0]);
    searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    searchResultsRecyclerView.setAdapter(searchResultsAdapter);

    searchResultsAdapter.setOnItemClickListener(recipe -> {
      Intent intent = new Intent(requireContext(), SecondActivity.class);
      intent.putExtra("recipeId", recipe.getRecipeId());
      intent.putExtra("fragmentToLoad", "viewRecipe");
      startActivity(intent);
    });

    return view;
  }

  private void performSearch(String query) {
    if (query == null || query.trim().isEmpty()) {
      showNormalContent();
      return;
    }

    new Thread(() -> {
      final Recipe[] searchResultsRecipes = searchRecipes(query);

      if (isAdded() && getActivity() != null) {
        requireActivity().runOnUiThread(() -> {
          if (searchResultsRecipes.length > 0) {
            searchResultsAdapter.updateRecipes(searchResultsRecipes);
            showSearchResults();
          } else {
            searchResultsAdapter.updateRecipes(new Recipe[0]);
            showSearchResults();
          }
        });
      }
    }).start();
  }

  private String removeAccents(String s) {
    if (s == null) return null;
    String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
    return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
  }

  private Recipe[] searchRecipes(String query) {
    query = removeAccents(query.toLowerCase());
    List<Recipe> results = new ArrayList<>();

    for (Recipe recipe : recipeDAO.getAllRecipes()) {
      String name = removeAccents(recipe.getName() != null ? recipe.getName().toLowerCase() : "");
      String instructions = removeAccents(recipe.getInstructions() != null ? recipe.getInstructions().toLowerCase() : "");
      String categoryName = null;
      if (categoryDAO != null) {
        int catId = recipe.getCategory();
        categoryName = categoryDAO.getCategoryById(catId) != null ? removeAccents(categoryDAO.getCategoryById(catId).getName().toLowerCase()) : null;
      }

      boolean matchName = name.contains(query);
      boolean matchInstructions = instructions.contains(query);
      boolean matchCategory = categoryName != null && categoryName.contains(query);

      if (matchName || matchInstructions || matchCategory) {
        results.add(recipe);
      }
    }

    return results.toArray(new Recipe[0]);
  }

  private void showSearchResults() {
    normalContent.setVisibility(View.GONE);
    searchResultsTitle.setVisibility(View.VISIBLE);
    searchResultsRecyclerView.setVisibility(View.VISIBLE);
  }

  private void showNormalContent() {
    searchResultsTitle.setVisibility(View.GONE);
    searchResultsRecyclerView.setVisibility(View.GONE);
    normalContent.setVisibility(View.VISIBLE);
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
        requireActivity().runOnUiThread(() -> {
          recipesAdapter.updateRecipes(recipes);

          CharSequence query = searchView.getQuery();
          if (query != null && !query.toString().isEmpty()) {
            performSearch(query.toString());
          }
        });
      }
    }).start();
  }
}