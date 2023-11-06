package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.filters.IngredientTableFilter;
import cz.muni.fi.pv168.project.ui.filters.RecipeTableFilter;
import cz.muni.fi.pv168.project.ui.filters.components.FilterComboboxBuilder;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterCategoryValues;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategories;
import cz.muni.fi.pv168.project.ui.renderers.CategoryRenderer;
import cz.muni.fi.pv168.project.ui.renderers.SpecialFilterCategoryValuesRenderer;
import cz.muni.fi.pv168.project.util.Either;

import javax.swing.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FilterIngredientDialog extends EntityDialog<IngredientTableFilter> {
    private final IngredientTableFilter ingredientTableFilter;
    private final JTextField nameField = new JTextField();
    private final JTextField fromCaloriesField = new JTextField();
    private final JTextField toCaloriesField = new JTextField();

    public FilterIngredientDialog(IngredientTableFilter ingredientTableFilter) {
        this.ingredientTableFilter = ingredientTableFilter;
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(ingredientTableFilter.getName());
        fromCaloriesField.setText(Double.toString(ingredientTableFilter.getCaloriesFrom()));
        if (ingredientTableFilter.getCaloriesTo() == Double.MAX_VALUE) {
            toCaloriesField.setText("");
        } else {
            toCaloriesField.setText(Double.toString(ingredientTableFilter.getCaloriesTo()));
        }
    }

    private void addFields() {
        add("Name:", nameField);
        add("Calories from:", fromCaloriesField);
        add("Calories to:", toCaloriesField);
    }

    @Override
    IngredientTableFilter getEntity() {
        // Portions
        String fromCaloriesString = fromCaloriesField.getText();
        String toCaloriesString = toCaloriesField.getText();
        Double fromCalories = fromCaloriesString.equals("") ? 0.0 : Double.parseDouble(fromCaloriesString);
        Double toCalories = toCaloriesString.equals("") ? Double.MAX_VALUE : Double.parseDouble(fromCaloriesString);
        ingredientTableFilter.filterCalories(fromCalories, toCalories);

        // Name
        ingredientTableFilter.filterName(nameField.getText());

        return this.ingredientTableFilter;
    }
}
