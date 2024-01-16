package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.ui.action.recipeIngredient.DeleteRecipeIngredientAction;
import cz.muni.fi.pv168.project.ui.filters.RecipeIngredientTableFilter;
import cz.muni.fi.pv168.project.ui.model.RecipeIngredientsTableModel;
import cz.muni.fi.pv168.project.ui.model.tables.RecipeIngredientsTable;
import cz.muni.fi.pv168.project.ui.renderers.IngredientComboBoxRenderer;
import cz.muni.fi.pv168.project.ui.renderers.UnitComboBoxRenderer;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomIngredientDialog extends JDialog {
    private final JComboBox<Ingredient> ingredientComboBox = new JComboBox<>();
    private final JTextField ingredientTextField = new JTextField();
    private final JComboBox<Unit> unitComboBox = new JComboBox<>();
    private final JTextField amountTextField = new JTextField();
    private final List<RecipeIngredient> newRecipeIngredients = new ArrayList<>();
    private boolean exitThroughDone = false;

    public CustomIngredientDialog(JFrame parentFrame, Recipe recipe, JTable ingredientTable, JTable unitTable, JTable recipeIngredientsTable, RecipeIngredientTableFilter filter) {
        super(parentFrame, "Recipe ingredients", true);
        setLayout(new BorderLayout());

        setupComboBoxes(ingredientTable, unitTable);

        JPanel topPanel = createTopPanel(recipe, recipeIngredientsTable, ingredientTable);

        filter.filterGuid(recipe.getGuid());
        add(new JScrollPane(recipeIngredientsTable), BorderLayout.CENTER);

        JPanel bottomPanel = createBottomPanel();

        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parentFrame);
        updateUnitComboBox(unitTable, false);
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> {
            exitThroughDone = true;
            dispose();
        });

        bottomPanel.add(doneButton);
        bottomPanel.add(closeButton);
        return bottomPanel;
    }

    private JPanel createTopPanel(Recipe recipe, JTable recipeIngredientsTable, JTable ingredientTable) {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("Select Ingredient: "));
        topPanel.add(ingredientTextField);
        ingredientTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterComboBox(ingredientTable);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                filterComboBox(ingredientTable);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                filterComboBox(ingredientTable);
            }
        });
        topPanel.add(ingredientComboBox);
        topPanel.add(new JLabel("amount:"));
        topPanel.add(amountTextField);
        topPanel.add(new JLabel("in unit:"));
        topPanel.add(unitComboBox);

        JButton addButton = createButton("Add Ingredient", new Color(42, 162, 26));
        addButton.addActionListener(e -> {
            Ingredient selectedIngredient = (Ingredient) ingredientComboBox.getSelectedItem();
            Unit selectedUnit = (Unit) unitComboBox.getSelectedItem();
            String amount = amountTextField.getText();
            try {
                if (selectedIngredient != null && !amount.isEmpty() && (selectedIngredient.getUnit().getType().equals(selectedUnit.getType()))) {
                    int row = doesRecipeIngredientExist(recipe.getGuid(), selectedIngredient.getGuid(), (RecipeIngredientsTable) recipeIngredientsTable);
                    if (row == -1) {
                        RecipeIngredient newIngredient = new RecipeIngredient(recipe, selectedIngredient, selectedUnit, Double.parseDouble(amountTextField.getText()));
                        newRecipeIngredients.add(newIngredient);
                        ((RecipeIngredientsTableModel) recipeIngredientsTable.getModel()).addRow(newIngredient);
                    } else {
                        RecipeIngredientsTableModel model = (RecipeIngredientsTableModel) recipeIngredientsTable.getModel();
                        RecipeIngredient recIng = (RecipeIngredient) model.getValueAt(row, 0);
                        model.deleteRow(row);
                        recIng.setUnit(selectedUnit);
                        recIng.setAmount(Double.parseDouble(amountTextField.getText()));
                        model.addRow(recIng);
                    }
                } else {
                    JOptionPane.showMessageDialog(CustomIngredientDialog.this, amount.isEmpty() ? "Please fill in amount" : "Selected unit type must match ingredient unit type", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(CustomIngredientDialog.this, "Amount must be a number bigger than 0");
            }
        });

        JButton deleteButton = createButton("Delete Ingredient", new Color(182, 28, 28));
        Action deleteAction = new DeleteRecipeIngredientAction(recipeIngredientsTable);
        deleteButton.addActionListener(deleteAction);
        deleteButton.addActionListener(e -> {
            int selectedRow = recipeIngredientsTable.getSelectedRow();
            if (selectedRow >= 0) {
                RecipeIngredient ingredientToDelete = ((RecipeIngredientsTableModel) recipeIngredientsTable.getModel()).getEntity(selectedRow);
                if (ingredientToDelete != null) {
                    ((RecipeIngredientsTableModel) recipeIngredientsTable.getModel()).deleteRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(CustomIngredientDialog.this, "Please select an ingredient to delete", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        topPanel.add(addButton, BorderLayout.SOUTH);
        topPanel.add(deleteButton, BorderLayout.SOUTH);
        return topPanel;
    }

    private JButton createButton(String name, Color color) {
        JButton button = new JButton(name);
        button.setForeground(Color.WHITE);
        Font buttonFont = button.getFont();
        button.setFont(new Font(buttonFont.getFontName(), Font.BOLD, buttonFont.getSize()));
        button.setBackground(color);
        return button;
    }

    private void setupComboBoxes(JTable ingredientTable, JTable unitTable) {
        for (int i = 0; i < ingredientTable.getRowCount(); i++) {
            ingredientComboBox.addItem((Ingredient) ingredientTable.getValueAt(i, 0));
        }
        ingredientComboBox.setRenderer(new IngredientComboBoxRenderer());
        if (ingredientComboBox.getItemCount() > 0) {
            ingredientComboBox.setSelectedIndex(0);
        }
        ingredientComboBox.addActionListener( e -> updateUnitComboBox(unitTable, false));

        updateUnitComboBox(unitTable, true);
        unitComboBox.setRenderer(new UnitComboBoxRenderer());
    }

    private void updateUnitComboBox(JTable unitTable, boolean all) {
        Ingredient selectedItem = (Ingredient) ingredientComboBox.getSelectedItem();
        if (Objects.isNull(selectedItem)) {
            return;
        }
        unitComboBox.removeAllItems();
        for (int i = 0; i < unitTable.getRowCount(); i++) {
            Unit unit = (Unit) unitTable.getValueAt(i, 0);
            if(all || selectedItem.getUnit().getType().equals(unit.getType())) {
                unitComboBox.addItem(unit);
            }
        }
    }

    private void filterComboBox(JTable ingredientTable) {
        ingredientComboBox.removeAllItems();
        String search = ingredientTextField.getText();
        for (int i = 0; i < ingredientTable.getRowCount(); i++) {
            Ingredient ingredient = (Ingredient) ingredientTable.getModel().getValueAt(i, 0);
            if (ingredient.getName().contains(search)) {
                ingredientComboBox.addItem(ingredient);
            }
        }
    }

    private int doesRecipeIngredientExist(String recipeGuid, String ingredientGuid, RecipeIngredientsTable recIngTable) {
        RecipeIngredientsTableModel model = (RecipeIngredientsTableModel) recIngTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            RecipeIngredient recIng = (RecipeIngredient) model.getValueAt(i, 0);
            if (recIng.getRecipe().getGuid().equals(recipeGuid) && recIng.getIngredient().getGuid().equals(ingredientGuid)) {
                return i;
            }
        }
        return -1;
    }

    public boolean getExitThroughDone() {
        return exitThroughDone;
    }

    public List<RecipeIngredient> getNewRecipeIngredients() {
        return newRecipeIngredients;
    }
}
