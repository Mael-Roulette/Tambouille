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

  @ColumnInfo(name = "preparationTime")
  private String preparationTime;

  @ColumnInfo(name = "category")
  private int category;

  @ColumnInfo(name = "instructions")
  private String instructions;

  @ColumnInfo(name = "image")
  private String image;

  public Recipe(@NonNull String name, String ingredients, String preparationTime, int category, String instructions, String image) {
    this.name = name;
    this.ingredients = ingredients;
    this.preparationTime = preparationTime;
    this.category = category;
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

  public String getPreparationTime() {
    return preparationTime;
  }

  public void setPreparation_time(String preparation_time) {
    this.preparationTime = preparation_time;
  }

  public int getCategory() {
    return category;
  }

  public void setCategory(int category) {
    this.category = category;
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