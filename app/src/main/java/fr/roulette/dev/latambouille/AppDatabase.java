package fr.roulette.dev.latambouille;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import fr.roulette.dev.latambouille.entity.Category;
import fr.roulette.dev.latambouille.entity.CategoryDAO;
import fr.roulette.dev.latambouille.entity.Recipe;
import fr.roulette.dev.latambouille.entity.RecipeDAO;
import fr.roulette.dev.latambouille.entity.RecipeCategoryRelation;
import fr.roulette.dev.latambouille.entity.RecipeCategoryDAO;

@Database(entities = {Recipe.class, Category.class, RecipeCategoryRelation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
  private static final String DATABASE_NAME = "latambouille_db";
  private static AppDatabase instance;

  public abstract RecipeDAO recipeDao();
  public abstract CategoryDAO categoryDao();
  public abstract RecipeCategoryDAO recipeCategoryDao();

  public static synchronized AppDatabase getInstance(Context context) {
    if (instance == null) {
      instance = Room.databaseBuilder(
              context.getApplicationContext(),
              AppDatabase.class,
              DATABASE_NAME)
          .allowMainThreadQueries()
          .build()
      ;
    }
    return instance;
  }
}