package fr.roulette.dev.latambouille.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class RecipeWithCategories {
  @Embedded
  public Recipe recipe;

  @Relation(
      parentColumn = "recipeId",
      entityColumn = "categoryId",
      associateBy = @Junction(RecipeCategoryRelation.class)
  )
  public List<Category> categories;
}