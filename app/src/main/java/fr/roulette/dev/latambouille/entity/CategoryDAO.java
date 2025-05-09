package fr.roulette.dev.latambouille.entity;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CategoryDAO {
  @Insert
  public void insertCategory(Category category);

  @Update
  public void updateCategory(Category category);

  @Delete
  public void deleteCategory(Category category);

  @Query("SELECT * FROM Category")
  public Category[] getAllCategories();

  @Query("SELECT * FROM Category WHERE categoryId = :id")
  public Category getCategoryById(int id);

  @Query("SELECT * FROM Category WHERE name = :name")
  public Category getCategoryByName(String name);
}
