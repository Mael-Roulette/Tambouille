package fr.roulette.dev.latambouille.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
    primaryKeys = {"recipeId", "categoryId"},
    foreignKeys = {
        @ForeignKey(
            entity = Recipe.class,
            parentColumns = "recipeId",
            childColumns = "recipeId",
            onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(
            entity = Category.class,
            parentColumns = "categoryId",
            childColumns = "categoryId",
            onDelete = ForeignKey.CASCADE
        )
    },
    indices = {
        @Index("recipeId"),
        @Index("categoryId")
    }
)
public class RecipeCategoryRelation {
  public int recipeId;
  public int categoryId;

  public RecipeCategoryRelation(int recipeId, int categoryId) {
    this.recipeId = recipeId;
    this.categoryId = categoryId;
  }
}