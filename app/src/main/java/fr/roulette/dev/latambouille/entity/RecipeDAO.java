package fr.roulette.dev.latambouille.entity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface RecipeDAO {
  @Insert
  void insertRecipe(Recipe recipe);

  @Update
  void updateRecipe(Recipe recipe);

  @Query("DELETE FROM Recipe WHERE recipeId = :id")
  void deleteRecipeById(int id);

  @Query("SELECT * FROM Recipe")
  Recipe[] getAllRecipes();

  @Query("SELECT * FROM Recipe WHERE recipeId = :id")
  Recipe getRecipeById(int id);

  @Query("SELECT * FROM Recipe WHERE category = :category")
  Recipe[] getRecipesByCategory(int category);

  @Query("SELECT * FROM Recipe WHERE name = :name")
  Recipe getRecipeByName(String name);

  @Query("SELECT * FROM Recipe WHERE ingredients LIKE :ingredients")
  Recipe getRecipeByIngredients(String ingredients);

  @Query("SELECT * FROM Recipe WHERE instructions LIKE :instructions")
  Recipe getRecipeByInstructions(String instructions);
}
