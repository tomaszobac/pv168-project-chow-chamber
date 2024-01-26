package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.ui.renderers.UnitComboBoxRenderer;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class IngredientDialog extends EntityDialog<Ingredient> {
    private final JTextField nameField = new JTextField();
    private final JTextField caloryField = new JTextField();
    private final JComboBox<Unit> unitComboBox;
    private final Ingredient ingredient;
    private boolean returnedOK = false;

    public IngredientDialog(Ingredient ingredient, JTable unitTable) {
        this.ingredient = ingredient;
        unitComboBox = new JComboBox<>();
        for (int i = 0; i < unitTable.getRowCount(); i++) {
            unitComboBox.addItem((Unit) unitTable.getValueAt(i, 0));
        }
        unitComboBox.setRenderer(new UnitComboBoxRenderer());
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(ingredient.getName());
        caloryField.setText(Double.toString(ingredient.getCalories()));
        unitComboBox.setSelectedItem(ingredient.getUnit());
    }

    private void addFields() {
        add("Name:", nameField);
        add("Calories:", caloryField);
        add("Unit:", unitComboBox);
    }

    public boolean getReturnedOK() {
        return returnedOK;
    }

    @Override
    Ingredient getEntity() {
        returnedOK = true;
        try{
            String name = nameField.getText();
            if (name.length() > 256 || name.isBlank()) {
                throw new IllegalArgumentException("Invalid name");
            }
            ingredient.setName(name);
            ingredient.setCalories(Double.parseDouble(caloryField.getText()));
            ingredient.setUnit((Unit) unitComboBox.getSelectedItem());
        } catch (IllegalArgumentException | NullPointerException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An error has occured");
        }
        return ingredient;
    }
}