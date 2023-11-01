package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.testGen.TestTable;
import cz.muni.fi.pv168.project.ui.model.entities.Ingredient;
import cz.muni.fi.pv168.project.ui.model.entities.Recipe;
import cz.muni.fi.pv168.project.ui.model.entities.Unit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomIngredientDialog extends JDialog {
    private final JComboBox<Ingredient> ingredientComboBox = new JComboBox<>();
    private final JComboBox<Unit> unitComboBox = new JComboBox<>();
    private final JTextField amountTextField = new JTextField();
    private final DefaultTableModel ingredientTableModel = new DefaultTableModel(new String[]{"Ingredient", "Amount", "Unit"}, 0);
    private final JTable ingredientTable = new JTable(ingredientTableModel);
    private final Recipe recipe;

    public CustomIngredientDialog(JFrame parentFrame, Recipe recipe) {
        super(parentFrame, "Recipe ingredients", true);
        this.recipe = recipe;
        setLayout(new BorderLayout());
        for (Ingredient ingredient : TestTable.getTableThree()) {
            ingredientComboBox.addItem(ingredient);
        }
        for (Unit unit : TestTable.getTableTwo()) {
            unitComboBox.addItem(unit);
        }

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("Select Ingredient: "));
        topPanel.add(ingredientComboBox);
        topPanel.add(new JLabel("amount:"));
        topPanel.add(amountTextField);
        topPanel.add(new JLabel("in unit:"));
        topPanel.add(unitComboBox);

        JButton addButton = new JButton("Add Ingredient");
        addButton.setBackground(new Color(26, 72, 93));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ingredient selectedIngredient = (Ingredient) ingredientComboBox.getSelectedItem();
                Unit selectedUnit = (Unit) unitComboBox.getSelectedItem();
                String amount = amountTextField.getText();
                if (selectedIngredient != null && !amount.isEmpty() && (selectedIngredient.getUnit().getName().equals(selectedUnit.getName()))) {
                    ingredientTableModel.addRow(new String[]{selectedIngredient.getName(), amount, selectedUnit.getName()});
                    recipe.addIngredient(new Ingredient(selectedIngredient.getName(), selectedIngredient.getCalories(), selectedIngredient.getUnit(), Double.parseDouble(amountTextField.getText())));
                } else {
                    // Display an error dialog
                    JOptionPane.showMessageDialog(CustomIngredientDialog.this, amount.isEmpty() ? "Please fill in amount" : "Selected unit type must match ingredient unit type", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        topPanel.add(addButton, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        JScrollPane ingredientScrollPane = new JScrollPane(ingredientTable);
        add(ingredientScrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(parentFrame);
    }

    public DefaultTableModel getIngredientListModel() {
        return ingredientTableModel;
    }
}
