package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.model.entities.Unit;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;
import cz.muni.fi.pv168.project.ui.model.tables.UnitTable;
import cz.muni.fi.pv168.project.ui.renderers.UnitComboBoxRenderer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

public class ConverterDialog extends EntityDialog<UnitTable> {
    private final UnitTable unitTable;
    private final JComboBox<UnitType> unitTypeComboBox;
    private final JComboBox<Unit> fromUnitComboBox;
    private final JTextField fromAmountField = new JTextField();
    private final JComboBox<Unit> toUnitComboBox;
    private final JTextField resultField = new JTextField();
    private Unit fromUnit;
    private Unit toUnit;
    private Double fromAmount = 1.0;
    private Double result = 1.0;

    public ConverterDialog(UnitTable unitTable) {
        this.unitTable = Objects.requireNonNull(unitTable, "UnitTable must not be null");
        this.unitTypeComboBox = new JComboBox<>(new DefaultComboBoxModel<>(UnitType.values()));

        this.fromUnitComboBox = new JComboBox<>();
        this.toUnitComboBox = new JComboBox<>();
        this.fromUnitComboBox.setRenderer(new UnitComboBoxRenderer());
        this.toUnitComboBox.setRenderer(new UnitComboBoxRenderer());
        updateUnitComboBoxes();
        this.fromUnit = (Unit) fromUnitComboBox.getSelectedItem();
        this.toUnit = (Unit) toUnitComboBox.getSelectedItem();
        updateResult();

        setLayout(new GridLayout(0,2));
        setFields();
        addFields();
        setupListeners();
    }

    private void setupListeners() {
        fromAmountField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateResult();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateResult();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateResult();
            }
        });

        fromUnitComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateResult();
            }
        });

        toUnitComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateResult();
            }
        });

        unitTypeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateUnitComboBoxes();
            }
        });
    }

    private void updateUnitComboBoxes() {
        UnitType selectedType = (UnitType) unitTypeComboBox.getSelectedItem();
        Vector<Unit> vector = getListFromTable(unitTable).stream()
                .filter(unit -> unit.getType() == selectedType)
                .collect(Collectors.toCollection(Vector::new));

        fromUnitComboBox.setModel(new DefaultComboBoxModel<>(vector));
        toUnitComboBox.setModel(new DefaultComboBoxModel<>(vector));
        updateResult();
    }

    private void updateResult() {
        try {
            fromAmount = Double.parseDouble(fromAmountField.getText());

        } catch (NumberFormatException e) {
            resultField.setText("Invalid input for From Amount");
        }
        try {
            fromUnit = (Unit) Objects.requireNonNull(fromUnitComboBox.getSelectedItem());
            toUnit = (Unit) Objects.requireNonNull(toUnitComboBox.getSelectedItem());
            result = toUnit.convertFromBase(fromUnit.convertToBase(fromAmount));
            resultField.setText(Double.toString(result));
        } catch (NullPointerException e) {
            resultField.setText("From or To Unit is empty");
        }
    }

    private List<Unit> getListFromTable(UnitTable unitTable) {
        ArrayList<Unit> list = new ArrayList<>();

        for (int i = 0; i < unitTable.getRowCount(); i++) {
            list.add((Unit) unitTable.getValueAt(i, 0));
        }

        return list;
    }

    private void setFields() {
        fromAmountField.setText(Double.toString(fromAmount));
        this.resultField.setText(Double.toString(result));
    }

    private void addFields() {
        JLabel typeLabel = new JLabel("Type: ");
        add("", typeLabel, "Cell 0 0");
        add("", unitTypeComboBox, "cell 1 0, span 2, wmin 150lp, wrap");

        JLabel fromLabel = new JLabel("From: ");
        add("", fromLabel, "Cell 0 1");
        add("", fromAmountField, "cell 1 1, wmin 100lp, gapy 10");
        add("", fromUnitComboBox, "cell 1 1, wmin 100lp");

        JLabel toLabel = new JLabel("To: ");
        add("", toLabel, "Cell 0 2");
        add("", toUnitComboBox, "cell 1 2, span 2, wmin 150lp");

        JLabel resultLabel = new JLabel("Result: ");
        add("", resultLabel, "Cell 0 3");
        add("", resultField, "cell 1 3, grow, gapy 10, span 2");
    }

    @Override
    UnitTable getEntity() {
        return unitTable;
    }
}
