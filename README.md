# 🍽️ La Tambouille

**La Tambouille** est une application Android permettant de :
- 📖 Consulter une liste de recettes
- 🧑‍🍳 Ajouter, visualiser et modifier ses propres recettes
- 🗂️ Classer les recettes dans des catégories (pré-définies)
- 🖼️ Ajouter une image depuis la galerie pour chaque recette
- 📚 Naviguer facilement grâce à une BottomNavigationBar

---

## 📱 Structure de l'application

### `MainActivity`
Contient 3 fragments :
- `HomeFragment` : page d'accueil
- `RecipeListFragment` : liste des recettes
- `CategoryListFragment` : liste des catégories

### `SecondActivity`
Contient 3 fragments :
- `AddRecipeFragment` : ajout d’une recette
- `RecipeDetailFragment` : visualisation d’une recette
- `EditRecipeFragment` : modification d’une recette

---

## 🗂️ Modèle de données (Room)

- `Recipe` : `recipeId`, `name`, `ingredients`, `instructions`, `preparation_time`, `image`
- `Category` : `categoryId`, `name`
- `RecipeCategoryCrossRef` : table de jointure many-to-many entre `Recipe` et `Category`

---

## 🛠️ Technologies utilisées

- Java
- Room (base de données locale)

---

## 📸 Fonctionnalités prévues

- Recherche de recettes par mots-clés
- Filtrage par catégorie

---

## 🔒 Remarques

- Les **catégories sont définies par le développeur** et ne peuvent pas être modifiées par l’utilisateur.
- Les **images de recettes sont sélectionnées** depuis la galerie de l’utilisateur.

---
