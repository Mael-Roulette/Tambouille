package fr.roulette.dev.latambouille.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.roulette.dev.latambouille.AppDatabase;
import fr.roulette.dev.latambouille.R;
import fr.roulette.dev.latambouille.ui.CategoryAdapter;
import fr.roulette.dev.latambouille.ui.RecipesAdapter;

public class HomeFragment extends Fragment {
  private AppDatabase database;

  public HomeFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    database = AppDatabase.getInstance(requireContext());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, container, false);

    RecyclerView categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
    RecyclerView recipesRecyclerView = view.findViewById(R.id.recipesRecyclerView);

    CategoryAdapter adapter = new CategoryAdapter(database.categoryDao().getAllCategories());
    categoriesRecyclerView.setAdapter(adapter);

    RecipesAdapter adapter2 = new RecipesAdapter(database.recipeDao().getAllRecipes());
    recipesRecyclerView.setAdapter(adapter2);

    return view;
  }
}