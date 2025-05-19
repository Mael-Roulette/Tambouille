package fr.roulette.dev.latambouille.entity;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CategoryDAO {
  @Insert
  void insertCategory(Category category);

  @Update
  void updateCategory(Category category);

  @Delete
  void deleteCategory(Category category);

  @Query("SELECT * FROM Category")
  Category[] getAllCategories();

  @Query("SELECT * FROM Category WHERE categoryId = :id")
  Category getCategoryById(int id);

  @Query("SELECT * FROM Category WHERE name = :name")
  Category getCategoryByName(String name);

  @Query("SELECT name FROM Category WHERE categoryId = :id")
  String getCategoryNameById(int id);

}
