package fr.roulette.dev.latambouille.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
  @PrimaryKey(autoGenerate = true)
  @NonNull
  public int categoryId;

  @NonNull
  @ColumnInfo(name = "name")
  public String name;

  public Category(@NonNull String name) {
    this.name = name;
  }

  public String getName() { return name; }
}

