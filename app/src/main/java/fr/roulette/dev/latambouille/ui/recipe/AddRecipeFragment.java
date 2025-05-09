package fr.roulette.dev.latambouille.ui.recipe;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import fr.roulette.dev.latambouille.R;

public class AddRecipeFragment extends Fragment {

  public AddRecipeFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_recipe, container, false);

    ImageButton backButton = view.findViewById(R.id.backButton);
    backButton.setOnClickListener(v -> {
      Activity activity = requireActivity();
      activity.finish();
    });

    return view;
  }
}