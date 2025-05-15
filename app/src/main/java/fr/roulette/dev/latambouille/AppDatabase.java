package fr.roulette.dev.latambouille;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import fr.roulette.dev.latambouille.entity.Category;
import fr.roulette.dev.latambouille.entity.CategoryDAO;
import fr.roulette.dev.latambouille.entity.Recipe;
import fr.roulette.dev.latambouille.entity.RecipeDAO;
import fr.roulette.dev.latambouille.entity.RecipeCategoryRelation;

@Database(entities = {Recipe.class, Category.class, RecipeCategoryRelation.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
  private static final String DATABASE_NAME = "latambouille_db";
  private static final String PREFS_NAME = "database_prefs";
  private static final String DB_INITIALIZED_KEY = "database_initialized";

  private static AppDatabase instance;

  public abstract RecipeDAO recipeDao();
  public abstract CategoryDAO categoryDao();

  public static synchronized AppDatabase getInstance(Context context) {
    if (instance == null) {
      instance = Room.databaseBuilder(
                      context.getApplicationContext(),
                      AppDatabase.class,
                      DATABASE_NAME)
              .allowMainThreadQueries()
              .build();

      initializeDataIfNeeded(context, instance);
    }
    return instance;
  }

  private static void initializeDataIfNeeded(Context context, AppDatabase db) {
    SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    boolean isInitialized = prefs.getBoolean(DB_INITIALIZED_KEY, false);

    if (!isInitialized) {
      addCategories(db);
      addRecipesSample(db);

      SharedPreferences.Editor editor = prefs.edit();
      editor.putBoolean(DB_INITIALIZED_KEY, true);
      editor.apply();
    }
  }

  private static void addCategories(AppDatabase db) {
    String[] newCat = {"Salé", "Sucré", "Épicé", "acide", "entrée", "plat", "dessert", "pâtisserie", "frit", "rôti", "grillé", "végétarien", "poisson", "poulet"};

    for (String cat : newCat) {
      Category category = new Category(cat);
      db.categoryDao().insertCategory(category);
    }
  }

  private static void addRecipesSample(AppDatabase db) {
    Category saleCat = db.categoryDao().getCategoryByName("Salé");
    Category sucreCat = db.categoryDao().getCategoryByName("Sucré");

    Recipe quiche = new Recipe(
"Quiche Lorraine",
"Pâte brisée, lardons, œufs, crème fraîche, gruyère",
"45 minutes",
            saleCat.getCategoryId(),
"Préchauffer le four. Faire revenir les lardons. Mélanger œufs, crème et fromage. Verser sur la pâte avec les lardons. Enfourner.",
"quiche.jpg"
    );

    Recipe crepes = new Recipe(
"Crêpes",
"Farine, œufs, lait, sucre, beurre",
"30 minutes",
            sucreCat.getCategoryId(),
"Mélanger les ingrédients pour obtenir une pâte lisse. Laisser reposer 1h. Faire cuire chaque crêpe dans une poêle beurrée.",
"crepes.jpg"
    );

    Recipe ratatouille = new Recipe(
"Ratatouille",
"Courgettes, aubergines, poivrons, tomates, oignons, ail",
"1 heure",
  saleCat.getCategoryId(),
"Couper tous les légumes. Faire revenir les oignons et l’ail, puis ajouter les légumes progressivement. Laisser mijoter à feu doux.",
"ratatouille.jpg"
    );


    db.recipeDao().insertRecipe(quiche);
    db.recipeDao().insertRecipe(crepes);
    db.recipeDao().insertRecipe(ratatouille);
  }
}