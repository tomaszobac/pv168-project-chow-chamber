package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.model.entities.Ingredient;
import cz.muni.fi.pv168.project.ui.model.entities.Unit;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategories;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;
import cz.muni.fi.pv168.project.ui.model.tables.UnitTable;

import javax.swing.*;
import java.time.DateTimeException;
import java.util.ArrayList;

public class IngredientDialog extends EntityDialog<Ingredient> {
    private final JTextField nameField = new JTextField();
    private final JTextField caloryField = new JTextField();
    private final JTextField unitField = new JTextField();
    private final JComboBox<Unit> unitComboBox;
    private final Ingredient ingredient;

    public IngredientDialog(Ingredient ingredient, JTable unitTable) {
        this.ingredient = ingredient;
        unitComboBox = new JComboBox<>();
        for (int i = 0; i < unitTable.getRowCount(); i++) {
            unitComboBox.addItem((Unit) unitTable.getValueAt(i, 0));
        }
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(ingredient.getName());
        caloryField.setText(Double.toString(ingredient.getCalories()));
        unitField.setText(ingredient.getUnitName());
    }

    private void addFields() {
        add("Name:", nameField);
        add("Calories:", caloryField);
        add("Unit:", unitComboBox);
    }

    @Override
    Ingredient getEntity() {
        try{
            ingredient.setName(nameField.getText());
            ingredient.setCalories(Double.parseDouble(caloryField.getText()));
            ingredient.setUnit((Unit) unitComboBox.getSelectedItem());
        } catch (DateTimeException | NumberFormatException e){
            return null;
        }
        return ingredient;
    }
}