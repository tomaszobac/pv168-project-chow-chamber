package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.filters.RecipeTableFilter;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterCategoryValues;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategories;

import javax.swing.*;

public class FilterRecipeDialog extends EntityDialog<RecipeTableFilter> {
    private final RecipeTableFilter filter;
    private final JTextField nameField = new JTextField();
    private final ComboBoxModel<RecipeCategories> categoryField = new DefaultComboBoxModel<>(RecipeCategories.values());
    private final JTextField fromTimeField = new JTextField();
    private final JTextField toTimeField = new JTextField();
    private final JTextField fromPortionsField = new JTextField();
    private final JTextField toPortionsField = new JTextField();

    public FilterRecipeDialog(RecipeTableFilter filter) {
        this.filter = filter;
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText("");
        categoryField.setSelectedItem(SpecialFilterCategoryValues.ALL);
        fromTimeField.setText("00:00");
        toTimeField.setText("23:59");
        fromPortionsField.setText("0");
        toPortionsField.setText("100");
    }

    private void addFields() {
        var categoryComboBox = new JComboBox<>(categoryField);
        add("Name:", nameField);
        add("Category:", categoryComboBox);
        add("Time from:", fromTimeField);
        add("Time to:", toTimeField);
        add("Portions from:", fromPortionsField);
        add("Portions from:", toPortionsField);
    }

    @Override
    RecipeTableFilter getEntity() {
        return this.filter;
    }
}
