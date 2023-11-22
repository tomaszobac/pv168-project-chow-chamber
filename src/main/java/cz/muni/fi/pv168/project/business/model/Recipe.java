package cz.muni.fi.pv168.project.business.model;

import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

public class Recipe extends Entity {
    private String name;
    private String instructions;
    private RecipeCategory category;
    private LocalTime time;
    private int portions;
    private ArrayList<Ingredient> ingredients;
    private int numberOfIngredients = 0;

    public Recipe() {
    }

    public Recipe(String guid, String name, RecipeCategory category, LocalTime time, int portions,
                  ArrayList<Ingredient> ingredients, String instructions) {
        super(guid);
        this.name = name;
        this.category = category;
        this.time = time;
        this.portions = portions;
        this.ingredients = ingredients;
        this.numberOfIngredients = ingredients.size();
        this.instructions = instructions;
    }

    public Recipe(String name, RecipeCategory category, LocalTime time, int portions,
                  ArrayList<Ingredient> ingredients, String instructions) {
        this.name = name;
        this.category = category;
        this.time = time;
        this.portions = portions;
        this.ingredients = ingredients;
        this.numberOfIngredients = ingredients.size();
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name,"name must not be null");
    }

    public RecipeCategory getCategory() {
        return category;
    }

    public String getCategoryName() {
        return category.getCategory();
    }

    public void setCategory(RecipeCategory category) {
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

    public String getInstructions() {
        return this.instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
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
        this.numberOfIngredients = ingredients.size();
    }
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        numberOfIngredients++;
    }

    public int getNumberOfIngredients() {
        return numberOfIngredients;
    }
}
