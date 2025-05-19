package fr.roulette.dev.latambouille.ui.recipe;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import fr.roulette.dev.latambouille.AppDatabase;
import fr.roulette.dev.latambouille.R;
import fr.roulette.dev.latambouille.RecipeEventManager;
import fr.roulette.dev.latambouille.entity.Recipe;
import fr.roulette.dev.latambouille.entity.RecipeDAO;
import fr.roulette.dev.latambouille.entity.Category;

public class ViewRecipeFragment extends Fragment {
  private static final String TAG = "ViewRecipeFragment";
  private AppDatabase database;
  private static final String ARG_RECIPE_ID = "recipeId";
  private int recipeId = -1;
  private RecipeDAO recipeDAO;
  private Recipe recipe;

  public ViewRecipeFragment() {
  }

  public static ViewRecipeFragment newInstance(int recipeId) {
    ViewRecipeFragment fragment = new ViewRecipeFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_RECIPE_ID, recipeId);
    fragment.setArguments(args);
    return fragment;
  }

  private void accessDatabase() {
    try {
      database = AppDatabase.getInstance(requireContext());
      recipeDAO = database.recipeDao();
    } catch (Exception e) {
      Log.e(TAG, "Erreur lors de l'accès à la base de données", e);
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      recipeId = getArguments().getInt(ARG_RECIPE_ID, -1);
    }

    accessDatabase();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_view_recipe, container, false);

    ImageButton backButton = view.findViewById(R.id.backButton);
    backButton.setOnClickListener(v -> {
      Activity activity = requireActivity();
      activity.finish();
    });

    ImageButton deleteButton = view.findViewById(R.id.deleteButton);
    deleteButton.setOnClickListener(v -> {
      confirmAndDeleteRecipe();
    });

    if (recipeId != -1) {
      new Thread(() -> {
        recipe = recipeDAO.getRecipeById(recipeId);

        if (recipe != null) {
          requireActivity().runOnUiThread(() -> {
            try {
              updateUI(view, recipe);
            } catch (Exception e) {
              Log.e(TAG, "Erreur lors de la mise à jour de l'UI", e);
            }
          });
        } else {
          Log.e(TAG, "Recette non trouvée avec l'ID: " + recipeId);
        }
      }).start();
    } else {
      Log.e(TAG, "ID de recette invalide: " + recipeId);
    }

    Button editButton = view.findViewById(R.id.button_edit);
    editButton.setOnClickListener(v -> {
      Bundle args = new Bundle();
      args.putInt("recipeId", recipeId);

      NavController navController = Navigation.findNavController(v);
      navController.navigate(R.id.action_viewRecipeFragment_to_editRecipeFragment, args);
    });

    return view;
  }

  private void updateUI(View view, Recipe recipe) {
    try {
      TextView nameTextView = view.findViewById(R.id.name_placeholder);
      if (nameTextView != null) {
        nameTextView.setText(recipe.getName());
      }

      TextView ingredientsTextView = view.findViewById(R.id.ingredient_placeholder);
      if (ingredientsTextView != null) {
        ingredientsTextView.setText(recipe.getIngredients());
      }

      TextView timeTextView = view.findViewById(R.id.time_placeholder);
      if (timeTextView != null) {
        timeTextView.setText(recipe.getPreparationTime());
      }

      TextView catTextView = view.findViewById(R.id.cat_placeholder);
      if (catTextView != null) {
        try {
          Category category = database.categoryDao().getCategoryById(recipe.getCategory());
          if (category != null) {
            catTextView.setText(category.getName());
          } else {
            catTextView.setText("Catégorie inconnue");
          }
        } catch (Exception e) {
          Log.e(TAG, "Erreur lors de la récupération de la catégorie", e);
          catTextView.setText("Catégorie inconnue");
        }
      }

      TextView instructionsTextView = view.findViewById(R.id.prep_placeholder);
      if (instructionsTextView != null) {
        instructionsTextView.setText(recipe.getInstructions());
      }

      ImageView recipeImageView = view.findViewById(R.id.imageView);
      if ( recipeImageView != null && recipe.getImage() != null && !recipe.getImage().isEmpty()) {
        try {
          Uri imageUri = Uri.parse(recipe.getImage());
          recipeImageView.setImageURI(imageUri);
        } catch (Exception e) {
          Log.e(TAG, "Erreur lors du chargement de l'image", e);
          recipeImageView.setImageResource(R.drawable.ic_photo);
        }
      } else if (recipeImageView != null) {
        recipeImageView.setImageResource(R.drawable.ic_photo);
      }
    } catch (Exception e) {
      Log.e(TAG, "Erreur lors de la mise à jour de l'interface utilisateur", e);
    }
  }

  private void confirmAndDeleteRecipe() {
    new AlertDialog.Builder(requireContext())
            .setTitle("Supprimer la recette")
            .setMessage("Voulez-vous vraiment supprimer cette recette ?")
            .setPositiveButton("Supprimer", (dialog, which) -> {
              deleteRecipe();
            })
            .setNegativeButton("Annuler", null)
            .show();
  }

  private void deleteRecipe() {
    if (recipeId != -1) {
      new Thread(() -> {
        accessDatabase();
        recipeDAO.deleteRecipeById(recipeId);

        requireActivity().runOnUiThread(() -> {
          RecipeEventManager.getInstance().notifyRecipeChanged();
          Toast.makeText(requireContext(), "Recette supprimée", Toast.LENGTH_SHORT).show();
          requireActivity().finish();
        });
      }).start();
    }
  }
}