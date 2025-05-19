package fr.roulette.dev.latambouille.ui.recipe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fr.roulette.dev.latambouille.AppDatabase;
import fr.roulette.dev.latambouille.R;
import fr.roulette.dev.latambouille.entity.Category;
import fr.roulette.dev.latambouille.entity.Recipe;

public class AddRecipeFragment extends Fragment {
  private AppDatabase database;
  private Uri selectedImageUri;
  private ImageView imageView;
  private TextInputLayout nameInputLayout;
  private TextInputLayout ingredientInputLayout;
  private Spinner timeSpinner;
  private Spinner categorySpinner;
  private EditText prepInput;

  private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

  public AddRecipeFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    database = AppDatabase.getInstance(requireContext());

    pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
      if (uri != null) {
        Log.d("PhotoPicker", "Selected URI: " + uri);
        selectedImageUri = uri;
        imageView.setImageURI(uri);

        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
        requireContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
      } else {
        Log.d("PhotoPicker", "No media selected");
      }
    });
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

    imageView = view.findViewById(R.id.imageView);
    Button selectImageButton = view.findViewById(R.id.selectImageButton);
    Button saveButton = view.findViewById(R.id.button);

    nameInputLayout = view.findViewById(R.id.name_input);
    ingredientInputLayout = view.findViewById(R.id.ingredient_input);
    timeSpinner = view.findViewById(R.id.time_input);
    categorySpinner = view.findViewById(R.id.cat_input);
    prepInput = view.findViewById(R.id.prep_input);

    ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.time_array, android.R.layout.simple_spinner_item);
    timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    timeSpinner.setAdapter(timeAdapter);

    Category[] categories = database.categoryDao().getAllCategories();

    List<String> categoryNames = new ArrayList<>();
    for (Category cat : categories) {
      categoryNames.add(cat.getName());
    }

    ArrayAdapter<String> catAdapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categoryNames
    );
    catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    categorySpinner.setAdapter(catAdapter);

    selectImageButton.setOnClickListener(v -> openGallery());

    saveButton.setOnClickListener(v -> saveRecipe());

    return view;
  }

  private void openGallery() {
    pickMedia.launch(new PickVisualMediaRequest.Builder()
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
            .build());
  }

  private void saveRecipe() {
    if (validateForm()) {
      try {
        String name = Objects.requireNonNull(nameInputLayout.getEditText()).getText().toString().trim();
        String ingredients = Objects.requireNonNull(ingredientInputLayout.getEditText()).getText().toString().trim();
        String time = timeSpinner.getSelectedItem().toString();
        String preparation = prepInput.getText().toString().trim();
        String categoryName = categorySpinner.getSelectedItem().toString();
        Category[] categories = database.categoryDao().getAllCategories();
        int categoryId = 1;

        for (Category category : categories) {
          if (category.getName().equals(categoryName)) {
            categoryId = category.getCategoryId();
            break;
          }
        }

        String imageUriString = selectedImageUri != null ? selectedImageUri.toString() : "";
        Recipe recipe = new Recipe( name, ingredients, time, categoryId, preparation, imageUriString );

        database.recipeDao().insertRecipe(recipe);

        Toast.makeText(getContext(), "Recette ajoutée avec succès", Toast.LENGTH_SHORT).show();

        requireActivity().finish();
      } catch (Exception e) {
        Log.e("AddRecipeFragment", "Erreur lors de la sauvegarde de la recette", e);
        Toast.makeText(getContext(), "Erreur lors de l'ajout de la recette", Toast.LENGTH_SHORT).show();
      }
    }
  }

  private boolean validateForm() {
    boolean isValid = true;

    if (Objects.requireNonNull(nameInputLayout.getEditText()).getText().toString().trim().isEmpty()) {
      nameInputLayout.setError("Veuillez entrer un nom de recette");
      isValid = false;
    } else {
      nameInputLayout.setError(null);
    }

    if (Objects.requireNonNull(ingredientInputLayout.getEditText()).getText().toString().trim().isEmpty()) {
      ingredientInputLayout.setError("Veuillez entrer des ingrédients");
      isValid = false;
    } else {
      ingredientInputLayout.setError(null);
    }

    if (prepInput.getText().toString().trim().isEmpty()) {
      prepInput.setError("Veuillez entrer les étapes de préparation");
      isValid = false;
    } else {
      prepInput.setError(null);
    }

    return isValid;
  }
}