package cz.muni.fi.pv168.project.ui.model.entities;

import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategories;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

public class Recipe {
    private String name;
    private RecipeCategories category;
    private LocalTime time;
    private int portions;
    private ArrayList<Ingredient> ingredients;
    private int numberOfIngredients = 0;

    public Recipe(String name, RecipeCategories category, LocalTime time, int portions, ArrayList<Ingredient> ingredients) {
        this.name = name;
        this.category = category;
        this.time = time;
        this.portions = portions;
        this.ingredients = ingredients;
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

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        numberOfIngredients++;
    }
}
