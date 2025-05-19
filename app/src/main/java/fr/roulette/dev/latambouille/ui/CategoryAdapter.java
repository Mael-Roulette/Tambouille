package fr.roulette.dev.latambouille.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.roulette.dev.latambouille.R;
import fr.roulette.dev.latambouille.entity.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Category[] categories;
    private CategoryAdapter.OnCategoryClickListener clickListener;

    public CategoryAdapter(Category[] categories) {
        this.categories = categories;
    }

    public void setOnItemClickListener(CategoryAdapter.OnCategoryClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories[position];
        holder.categoryName.setText(category.name);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onCategoryClick(category);
            }
        });
    }

    public void updateCategories(Category[] categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.length : 0;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.recipeName);
        }
    }
}