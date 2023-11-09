package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;

import javax.swing.*;
import java.time.DateTimeException;

public class IngredientDialog extends EntityDialog<Ingredient> {
    private final JTextField nameField = new JTextField();
    private final JTextField caloryField = new JTextField();
    private final JTextField unitField = new JTextField();
    private final Ingredient ingredient;

    public IngredientDialog(Ingredient ingredient) {
        this.ingredient = ingredient;
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
        add("Unit:", unitField);
    }

    @Override
    Ingredient getEntity() {
        try{
            ingredient.setName(nameField.getText());
            ingredient.setCalories(Double.parseDouble(caloryField.getText()));
            ingredient.setUnit(new Unit(unitField.getName(), UnitType.Volume, 1.0)); // TODO: keep units somewhere so we can show created units here in combobox for example, alternatively we can compare entered value with existing units and call a new unit creation window to add new unit
        } catch (DateTimeException | NumberFormatException e){
            return null;
        }
        return ingredient;
    }
}