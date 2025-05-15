package fr.roulette.dev.latambouille.ui.recipe;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import fr.roulette.dev.latambouille.AppDatabase;
import fr.roulette.dev.latambouille.R;
import fr.roulette.dev.latambouille.entity.Category;

public class AddRecipeFragment extends Fragment {
  private AppDatabase database;

  public AddRecipeFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    database = AppDatabase.getInstance(requireContext());
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

    Spinner timeSpinner = (Spinner) view.findViewById(R.id.time_input);
    ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(
            getContext(),
            R.array.time_array,
            android.R.layout.simple_spinner_item
    );
    timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    timeSpinner.setAdapter(timeAdapter);

    Category[] categories = database.categoryDao().getAllCategories();

    List<String> categoryNames = new ArrayList<>();
    for (Category cat : categories) {
      categoryNames.add(cat.getName());
    }

    Spinner catSpinner = view.findViewById(R.id.cat_input);
    ArrayAdapter<String> catAdapter = new ArrayAdapter<>(
            getContext(),
            android.R.layout.simple_spinner_item,
            categoryNames
    );
    catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    catSpinner.setAdapter(catAdapter);


    return view;
  }
}