package fr.roulette.dev.latambouille.ui.home;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.roulette.dev.latambouille.AppDatabase;
import fr.roulette.dev.latambouille.R;
import fr.roulette.dev.latambouille.entity.Recipe;
import fr.roulette.dev.latambouille.entity.RecipeDAO;
import fr.roulette.dev.latambouille.ui.RecipesListAdapter;

public class RecipesListFragment extends Fragment {
    private RecipeDAO recipeDAO;

      public RecipesListFragment() {
        // Required empty public constructor
      }

      private void accessDatabase() {
        AppDatabase database = AppDatabase.getInstance(requireContext());
        recipeDAO = database.recipeDao();
      }

      @Override
      public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      }

      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes_list, container, false);

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
          NavController navController = Navigation.findNavController(v);
          navController.navigate(R.id.action_recipesListFragment_to_homeFragment);
        });


        ListView recipesListView = view.findViewById(R.id.recipesListView);

        RecipesListAdapter recipesAdapter = new RecipesListAdapter((Activity) requireContext(), new ArrayList<>());
        recipesListView.setAdapter(recipesAdapter);

        new Thread(() -> {
          accessDatabase();

          List<Recipe> fetchedRecipes = Arrays.asList(recipeDAO.getAllRecipes());

          requireActivity().runOnUiThread(() -> recipesAdapter.updateData(fetchedRecipes));
        }).start();

        return view;
      }
    }
