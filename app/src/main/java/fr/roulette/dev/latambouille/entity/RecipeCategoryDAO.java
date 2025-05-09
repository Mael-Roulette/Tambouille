package fr.roulette.dev.latambouille.entity;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface RecipeCategoryDAO {
  @Insert
  public void insertRecipeCategoryRelation(RecipeCategoryRelation relation);

  @Delete
  public void deleteRecipeCategoryRelation(RecipeCategoryRelation relation);

  @Query("DELETE FROM RecipeCategoryRelation WHERE recipeId = :recipeId AND categoryId = :categoryId")
  public void deleteRelationByIds(int recipeId, int categoryId);

  @Query("DELETE FROM RecipeCategoryRelation WHERE recipeId = :recipeId")
  public void deleteAllCategoriesForRecipe(int recipeId);

  @Transaction
  @Query("SELECT * FROM Recipe WHERE recipeId = :recipeId")
  public RecipeWithCategories[] getRecipeWithCategories(int recipeId);

  @Transaction
  @Query("SELECT * FROM Recipe")
  public RecipeWithCategories[] getAllRecipesWithCategories();

  @Transaction
  @Query("SELECT * FROM Category WHERE categoryId = :categoryId")
  public CategoryWithRecipes[] getCategoryWithRecipes(int categoryId);

  @Transaction
  @Query("SELECT * FROM Category")
  public CategoryWithRecipes[] getAllCategoriesWithRecipes();

  @Query("SELECT * FROM Category " +
      "INNER JOIN RecipeCategoryRelation ON Category.categoryId = RecipeCategoryRelation.categoryId " +
      "WHERE RecipeCategoryRelation.recipeId = :recipeId")
  public Category[] getCategoriesForRecipe(int recipeId);

  @Query("SELECT * FROM Recipe " +
      "INNER JOIN RecipeCategoryRelation ON Recipe.recipeId = RecipeCategoryRelation.recipeId " +
      "WHERE RecipeCategoryRelation.categoryId = :categoryId")
  public Recipe[] getRecipesForCategory(int categoryId);
}