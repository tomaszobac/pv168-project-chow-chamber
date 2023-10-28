package cz.muni.fi.pv168.project.ui.model;

import java.time.LocalTime;
import java.util.Objects;

public class Recipe {
    private String name;
    private RecipeCategories category;
    private LocalTime time;
    private int portions;

    public Recipe(String name, RecipeCategories category, LocalTime time, int portions) {
        this.name = name;
        this.category = category;
        this.time = time;
        this.portions = portions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name,"name must not be null");
    }

    public RecipeCategories getCategory() {
        return category;
    }

    public String getCategoryName() {
        return category.getCategory();
    }

    public void setCategory(RecipeCategories category) {
        this.category = Objects.requireNonNull(category,"name must not be null");
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = Objects.requireNonNull(time,"name must not be null");
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
