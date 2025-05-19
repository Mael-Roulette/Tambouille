package fr.roulette.dev.latambouille;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire d'événements de recettes utilisant le pattern Singleton.
 * Permet de notifier les écouteurs lorsqu'une recette est modifiée/supprimée.
 */
public class RecipeEventManager {
    private static RecipeEventManager instance;
    private List<RecipeChangeListener> listeners = new ArrayList<>();

    private RecipeEventManager() {
        // Constructeur privé pour empêcher l'instanciation directe
    }

    public static synchronized RecipeEventManager getInstance() {
        if (instance == null) {
            instance = new RecipeEventManager();
        }
        return instance;
    }

    /**
     * Ajoute un écouteur pour les événements de recette
     */
    public void addListener(RecipeChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Supprime un écouteur
     */
    public void removeListener(RecipeChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie tous les écouteurs qu'une recette a été modifiée ou supprimée
     */
    public void notifyRecipeChanged() {
        for (RecipeChangeListener listener : listeners) {
            listener.onRecipeChanged();
        }
    }
}