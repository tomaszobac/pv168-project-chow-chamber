package cz.muni.fi.pv168.project.ui.dialog;

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

public class FilterRecipeDialog extends EntityDialog<RecipeTableFilter> {
    private final RecipeTableFilter recipeTableFilter;
    private final JTextField nameField = new JTextField();
    private final JComboBox<Either<SpecialFilterCategoryValues, RecipeCategories>> categoryComboBox;
    private final JTextField fromTimeField = new JTextField();
    private final JTextField toTimeField = new JTextField();
    private final JTextField fromPortionsField = new JTextField();
    private final JTextField toPortionsField = new JTextField();

    public FilterRecipeDialog(RecipeTableFilter recipeTableFilter) {
        this.recipeTableFilter = recipeTableFilter;
        this.categoryComboBox = createCategoryFilter(recipeTableFilter);
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(recipeTableFilter.getName());
        categoryComboBox.setSelectedItem(recipeTableFilter.getSelectedCategoryValue());
        fromTimeField.setText(recipeTableFilter.getTimeFrom().toString());
        toTimeField.setText(recipeTableFilter.getTimeTo().toString());
        fromPortionsField.setText(Integer.toString(recipeTableFilter.getPortionsFrom()));
        toPortionsField.setText(Integer.toString(recipeTableFilter.getPortionsTo()));
    }

    private void addFields() {
        add("Name:", nameField);
        add("Category:", categoryComboBox);
        add("Time from:", fromTimeField);
        add("Time to:", toTimeField);
        add("Portions from:", fromPortionsField);
        add("Portions to:", toPortionsField);
    }

    private static JComboBox<Either<SpecialFilterCategoryValues, RecipeCategories>> createCategoryFilter(
            RecipeTableFilter recipeTableFilter) {
        return FilterComboboxBuilder.create(SpecialFilterCategoryValues.class, RecipeCategories.values())
                .setSpecialValuesRenderer(new SpecialFilterCategoryValuesRenderer())
                .setValuesRenderer(new CategoryRenderer())
                .setFilter(recipeTableFilter::filterCategory)
                .build();
    }

    @Override
    RecipeTableFilter getEntity() {
        // Portions
        String fromPortions = fromPortionsField.getText();
        String toPortions = toPortionsField.getText();
        recipeTableFilter.filterPortions(Integer.parseInt(fromPortions), Integer.parseInt(toPortions));

        // Times
        String fromTimeString = fromTimeField.getText();
        String toTimeString = toTimeField.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime fromTime = LocalTime.parse(fromTimeString, formatter);
        LocalTime toTime = LocalTime.parse(toTimeString, formatter);
        recipeTableFilter.filterTime(fromTime, toTime);

        // Name
        recipeTableFilter.filterName(nameField.getText());

        return this.recipeTableFilter;
    }
}
