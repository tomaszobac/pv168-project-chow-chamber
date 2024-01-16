package cz.muni.fi.pv168.project.business.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a Recipe entity.
 * Extends the Entity class.
 */
public class Recipe extends Entity implements Serializable {
    private String name;
    private String instructions;
    private RecipeCategory category;
    private LocalTime time;
    private int portions;

    /**
     * Creates a new Recipe object with the specified parameters.
     *
     * @param guid         The unique identifier of the recipe.
     * @param name         The name of the recipe.
     * @param category     The category of the recipe.
     * @param time         The cooking time of the recipe.
     * @param portions     The number of portions the recipe yields.
     * @param instructions The cooking instructions for the recipe.
     *
     * @throws NumberFormatException Portions were less or equal to 0.
     */
    @JsonCreator
    public Recipe(@JsonProperty("guid") String guid,
                  @JsonProperty("name") String name,
                  @JsonProperty("category") RecipeCategory category,
                  @JsonProperty("time") LocalTime time,
                  @JsonProperty("portions") int portions,
                  @JsonProperty("instructions") String instructions) throws NumberFormatException {
        super(guid);
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.category = Objects.requireNonNull(category, "category must not be null");
        this.time = Objects.requireNonNull(time, "time must not be null");
        if (portions <= 0) {
            throw new NumberFormatException("Portions must be bigger than 0.");
        }
        this.portions = portions;
        this.instructions = Objects.requireNonNull(instructions, "instructions must not be null");
    }

    /**
     * Constructs a new Recipe object with the given parameters.
     *
     * @param name         the name of the recipe, must not be null
     * @param category     the category of the recipe, must not be null
     * @param time         the time required to prepare the recipe, must not be null
     * @param portions     the number of portions the recipe serves
     * @param instructions the instructions to prepare the recipe, must not be null
     */
    public Recipe(String name, String instructions, RecipeCategory category, LocalTime time, int portions) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.category = Objects.requireNonNull(category, "category must not be null");
        this.time = Objects.requireNonNull(time, "time must not be null");
        if (portions <= 0) {
            throw new NumberFormatException("Portions must be bigger than 0.");
        }
        this.portions = portions;
        this.instructions = Objects.requireNonNull(instructions, "instructions must not be null");
    }

    /**
     * Returns the name of the recipe.
     *
     * @return the name of the recipe
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the object.
     *
     * @param name the name to set
     * @throws NullPointerException if name is null
     */
    public void setName(String name) {
        this.name = Objects.requireNonNull(name,"name must not be null");
    }

    /**
     * Retrieves the category of a recipe.
     *
     * @return The category of the recipe.
     */
    public RecipeCategory getCategory() {
        return category;
    }

    /**
     * Returns the name of the category that the recipe belongs to.
     *
     * @return the name of the category as a String
     */
    public String getCategoryName() {
        return category.getCategory();
    }

    /**
     * Sets the category of the recipe.
     *
     * @param category The category to set for the recipe.
     * @throws NullPointerException if the category is null.
     */
    public void setCategory(RecipeCategory category) {
        this.category = Objects.requireNonNull(category,"Category must not be null");
    }

    /**
     * Returns the time of the recipe.
     *
     * @return the time of the recipe
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Sets the time for this object.
     *
     * @param time the time to set
     * @throws NullPointerException if the time is null
     */
    public void setTime(LocalTime time) {
        this.time = Objects.requireNonNull(time,"Time must not be null");
    }

    /**
     * Retrieves the number of portions for a given object.
     *
     * @return The number of portions.
     */
    public int getPortions() {
        return portions;
    }

    /**
     * Sets the number of portions.
     *
     * @param portions the number of portions
     */
    public void setPortions(int portions) throws NumberFormatException {
        if (portions <= 0) {
            throw new NumberFormatException("Portions must be bigger than 0.");
        }
        this.portions = portions;
    }

    /**
     * Returns the instructions of the given object.
     *
     * @return The instructions of the object.
     */
    public String getInstructions() {
        return this.instructions;
    }

    /**
     * Sets the instructions for a task.
     *
     * @param instructions the instructions to be set
     * @throws NullPointerException if instructions parameter is null
     */
    public void setInstructions(String instructions) {
        this.instructions = Objects.requireNonNull(instructions, "Instructions must not be null");
    }
/**
     * Returns a string representation of the Recipe.
     *
     * @return A string representation of the Recipe in the format "Recipe{category: %s; name: %s; time: %s; portions: %d, numberOfIngredients: %d}",
     *     where %s represents the category, name, and time of the Recipe, and %d represents the portions and number of ingredients.
     *     The string representation may also include instructions and ingredients, depending on implementation.
     */
    @Override
    public String toString() {
        return String.format("Recipe{guid: %s; category: %s; name: %s; time: %s; portions: %d}",
                getGuid(), category.getCategory(), name, time, portions);
    }
}
