package fr.roulette.dev.latambouille.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import fr.roulette.dev.latambouille.R;

public class RecipesCatFragment extends Fragment {
  public RecipesCatFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_recipes_cat, container, false);

    ImageButton backButton = view.findViewById(R.id.backButton);
    backButton.setOnClickListener(v -> {
      NavController navController = Navigation.findNavController(v);
      navController.navigate(R.id.action_recipesCatFragment_to_homeFragment);
    });

    return view;
  }
}