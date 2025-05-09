package fr.roulette.dev.latambouille;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
  private NavController navController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
            .findFragmentById(R.id.fragmentContainerView);

    if (navHostFragment != null) {
      navController = navHostFragment.getNavController();
    }

    setupCustomNavigation();
  }

  private void setupCustomNavigation() {
    LinearLayout listeButton = findViewById(R.id.listeButton);
    FloatingActionButton centerButton = findViewById(R.id.centerButton);
    LinearLayout categoriesButton = findViewById(R.id.categoriesButton);

    NavOptions navOptions = new NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(navController.getGraph().getStartDestinationId(), false)
            .build();

    listeButton.setOnClickListener(v -> navController.navigate(R.id.recipesListFragment, null, navOptions));

    centerButton.setOnClickListener(v -> {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent);
    });

    categoriesButton.setOnClickListener(v -> navController.navigate(R.id.recipesCatFragment, null, navOptions));
  }
}