package fr.roulette.dev.latambouille.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import fr.roulette.dev.latambouille.AppDatabase;
import fr.roulette.dev.latambouille.R;
import fr.roulette.dev.latambouille.ui.CategoryAdapter;

public class RecipesCatFragment extends Fragment {
  private AppDatabase database;

  public RecipesCatFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    database = AppDatabase.getInstance(requireContext());
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recipes_cat, container, false);

    ImageButton backButton = view.findViewById(R.id.backButton);
    backButton.setOnClickListener(v -> {
      NavController navController = Navigation.findNavController(v);
      navController.navigate(R.id.action_recipesCatFragment_to_homeFragment);
    });

    RecyclerView categoriesRecyclerView = view.findViewById(R.id.recyclerViewCategories);
    CategoryAdapter categoryAdapter = new CategoryAdapter(database.categoryDao().getAllCategories());

    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
    categoriesRecyclerView.setLayoutManager(layoutManager);
    categoriesRecyclerView.setAdapter(categoryAdapter);

    return view;
  }
}