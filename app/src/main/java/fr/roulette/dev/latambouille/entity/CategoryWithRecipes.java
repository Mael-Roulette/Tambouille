package fr.roulette.dev.latambouille.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class CategoryWithRecipes {
  @Embedded
  public Category category;

  @Relation(
      parentColumn = "categoryId",
      entityColumn = "recipeId",
      associateBy = @Junction(RecipeCategoryRelation.class)
  )
  public List<Recipe> recipes;
}