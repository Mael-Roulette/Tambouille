package fr.roulette.dev.latambouille.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Recipe {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "recipeId")
  private int recipeId;

  @NonNull
  @ColumnInfo(name = "name")
  private String name;

  @ColumnInfo(name = "ingredients")
  private String ingredients;

  @ColumnInfo(name = "preparation_time")
  private String preparation_time;

  @ColumnInfo(name = "instructions")
  private String instructions;

  @ColumnInfo(name = "image")
  private String image;

  public Recipe() {}

  public Recipe(@NonNull String name, String ingredients, String preparation_time, String instructions, String image) {
    this.name = name;
    this.ingredients = ingredients;
    this.preparation_time = preparation_time;
    this.instructions = instructions;
    this.image = image;
  }

  public int getRecipeId() {
    return recipeId;
  }

  public void setRecipeId(int recipeId) {
    this.recipeId = recipeId;
  }

  @NonNull
  public String getName() {
    return name;
  }

  public void setName(@NonNull String name) {
    this.name = name;
  }

  public String getIngredients() {
    return ingredients;
  }

  public void setIngredients(String ingredients) {
    this.ingredients = ingredients;
  }

  public String getPreparation_time() {
    return preparation_time;
  }

  public void setPreparation_time(String preparation_time) {
    this.preparation_time = preparation_time;
  }

  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}