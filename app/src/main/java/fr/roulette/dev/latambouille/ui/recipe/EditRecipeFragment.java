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

public class EditRecipeFragment extends Fragment {
  private AppDatabase database;
  private Uri selectedImageUri;
  private ImageView imageView;
    private TextInputLayout nameInputLayout;
  private TextInputLayout ingredientInputLayout;
  private Spinner timeSpinner;
  private Spinner categorySpinner;
  private EditText prepInput;
  private int recipeId = -1;
  private Recipe existingRecipe;


  private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

  public EditRecipeFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    database = AppDatabase.getInstance(requireContext());

    if (getArguments() != null) {
      recipeId = getArguments().getInt("recipeId", -1);
    }

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

    ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(
            requireContext(),
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

    ArrayAdapter<String> catAdapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categoryNames
    );
    catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    categorySpinner.setAdapter(catAdapter);

    if (recipeId != -1) {
      loadExistingRecipe();
    } else {
      Toast.makeText(getContext(), "Erreur: Aucune recette à modifier", Toast.LENGTH_SHORT).show();
      requireActivity().finish();
    }

    selectImageButton.setOnClickListener(v -> openGallery());

    saveButton.setOnClickListener(v -> updateRecipe());

    return view;
  }

  private void openGallery() {
    pickMedia.launch(new PickVisualMediaRequest.Builder()
            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
            .build());
  }

  private void loadExistingRecipe() {
    try {
      existingRecipe = database.recipeDao().getRecipeById(recipeId);

      if (existingRecipe != null) {
        Objects.requireNonNull(nameInputLayout.getEditText()).setText(existingRecipe.getName());
        Objects.requireNonNull(ingredientInputLayout.getEditText()).setText(existingRecipe.getIngredients());
        prepInput.setText(existingRecipe.getInstructions());

        selectSpinnerItemByValue(timeSpinner, existingRecipe.getPreparationTime());

        Category category = database.categoryDao().getCategoryById(existingRecipe.getCategory());
        if (category != null) {
          selectSpinnerItemByValue(categorySpinner, category.getName());
        }

        if (existingRecipe.getImage() != null && !existingRecipe.getImage().isEmpty()) {
          selectedImageUri = Uri.parse(existingRecipe.getImage());
          imageView.setImageURI(selectedImageUri);
        }
      } else {
        Toast.makeText(getContext(), "Recette introuvable", Toast.LENGTH_SHORT).show();
        requireActivity().finish();
      }
    } catch (Exception e) {
      Log.e("EditRecipeFragment", "Erreur lors du chargement de la recette", e);
      Toast.makeText(getContext(), "Erreur lors du chargement de la recette", Toast.LENGTH_SHORT).show();
      requireActivity().finish();
    }
  }

  private void selectSpinnerItemByValue(Spinner spinner, String value) {
    for (int i = 0; i < spinner.getCount(); i++) {
      if (spinner.getItemAtPosition(i).toString().equals(value)) {
        spinner.setSelection(i);
        break;
      }
    }
  }

  private void updateRecipe() {
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

        existingRecipe.setName(name);
        existingRecipe.setIngredients(ingredients);
        existingRecipe.setPreparation_time(time);
        existingRecipe.setCategory(categoryId);
        existingRecipe.setInstructions(preparation);

        if (selectedImageUri != null) {
          existingRecipe.setImage(selectedImageUri.toString());
        }

        database.recipeDao().updateRecipe(existingRecipe);

        Toast.makeText(getContext(), "Recette mise à jour avec succès", Toast.LENGTH_SHORT).show();

        requireActivity().finish();
      } catch (Exception e) {
        Log.e("EditRecipeFragment", "Erreur lors de la mise à jour de la recette", e);
        Toast.makeText(getContext(), "Erreur lors de la mise à jour de la recette", Toast.LENGTH_SHORT).show();
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