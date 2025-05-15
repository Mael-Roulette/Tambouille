package fr.roulette.dev.latambouille;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

public class SecondActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_second);

    String fragmentToLoad = getIntent().getStringExtra("fragmentToLoad");

    if (fragmentToLoad != null && fragmentToLoad.equals("viewRecipe")) {
      int recipeId = getIntent().getIntExtra("recipeId", -1);
      Bundle bundle = new Bundle();
      bundle.putInt("recipeId", recipeId);

      NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
              .findFragmentById(R.id.fragmentContainerView);
      NavController navController = navHostFragment.getNavController();
      navController.navigate(R.id.viewRecipeFragment, bundle);
    }
  }
}