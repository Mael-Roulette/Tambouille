package fr.roulette.dev.latambouille.ui.recipe;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import fr.roulette.dev.latambouille.AppDatabase;
import fr.roulette.dev.latambouille.R;
import fr.roulette.dev.latambouille.entity.Category;

public class AddRecipeFragment extends Fragment {
  private AppDatabase database;
  private static final int PICK_IMAGE_REQUEST = 1;
  private Uri selectedImageUri;
  private ImageView imageView;
  private Button selectImageButton;

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

    imageView = view.findViewById(R.id.imageView);
    selectImageButton = view.findViewById(R.id.selectImageButton);

    selectImageButton.setOnClickListener(v -> {
      openGallery();
    });

    return view;
  }

  private void openGallery() {
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
    registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
      if (uri != null) {
        Log.d("PhotoPicker", "Selected URI: " + uri);
      } else {
        Log.d("PhotoPicker", "No media selected");
      }
    });

    pickMedia.launch(new PickVisualMediaRequest.Builder()
      .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
      .build());
  }
}