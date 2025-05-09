package fr.roulette.dev.latambouille.entity;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface RecipeDAO {
  @Insert
  public void insertRecipe(Recipe recipe);

  @Update
  public void updateRecipe(Recipe recipe);

  @Delete
  public void deleteRecipe(Recipe recipe);

  @Query("SELECT * FROM Recipe")
  public Recipe[] getAllRecipes();

  @Query("SELECT * FROM Recipe WHERE recipeId = :id")
  public Recipe getRecipeById(int id);

  @Query("SELECT * FROM Recipe WHERE name = :name")
  public Recipe getRecipeByName(String name);

  @Query("SELECT * FROM Recipe WHERE ingredients LIKE :ingredients")
  public Recipe getRecipeByIngredients(String ingredients);

  @Query("SELECT * FROM Recipe WHERE instructions LIKE :instructions")
  public Recipe getRecipeByInstructions(String instructions);
}
