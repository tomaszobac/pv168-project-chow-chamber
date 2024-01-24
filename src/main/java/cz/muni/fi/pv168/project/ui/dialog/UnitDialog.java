package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class UnitDialog extends EntityDialog<Unit> {
    private final JTextField nameField = new JTextField();
    private final ComboBoxModel<UnitType> typeField = new DefaultComboBoxModel<>(UnitType.values());
    private final JTextField baseField = new JTextField();
    private final Unit unit;
    private boolean returnedOK = false;

    public UnitDialog(Unit unit) {
        this.unit = unit;
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(unit.getName());
        typeField.setSelectedItem(unit.getType());
        baseField.setText(Double.toString(unit.getConversionToBase()));
    }

    private void addFields() {
        var unitComboBox = new JComboBox<>(typeField);
        add("Name:", nameField);
        add("Type:", unitComboBox);
        add("Base:", baseField);
    }

    public boolean getReturnedOK() {
        return returnedOK;
    }

    @Override
    Unit getEntity() {
        returnedOK = true;
        unit.setName(nameField.getText());
        unit.setType((UnitType) typeField.getSelectedItem());
        try{
            unit.setConversionToBase(Double.parseDouble(baseField.getText()));
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
            return null;
        }
        return unit;
    }
}
