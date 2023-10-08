package cz.muni.fi.pv168.project.ui.model;

import java.util.Objects;

public class Recipe {
    private String name;
    private String category;
    private String time;
    private String portions;

    public Recipe(String name, String category, String time, String portions) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = Objects.requireNonNull(category,"name must not be null");;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = Objects.requireNonNull(time,"name must not be null");;
    }

    public String getPortions() {
        return portions;
    }

    public void setPortions(String portions) {
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
