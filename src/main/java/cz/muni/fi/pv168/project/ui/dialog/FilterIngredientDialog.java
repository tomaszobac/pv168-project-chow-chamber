package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.filters.IngredientTableFilter;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class FilterIngredientDialog extends EntityDialog<IngredientTableFilter> {
    private final IngredientTableFilter ingredientTableFilter;
    private final JTextField nameField = new JTextField();
    private final JTextField fromCaloriesField = new JTextField();
    private final JTextField toCaloriesField = new JTextField();

    public FilterIngredientDialog(IngredientTableFilter ingredientTableFilter) {
        this.ingredientTableFilter = ingredientTableFilter;
        setNumericWithDecimalFilter(fromCaloriesField);
        setNumericWithDecimalFilter(toCaloriesField);
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

    // Set a DocumentFilter to allow only numeric input and a single decimal point
    public static void setNumericWithDecimalFilter(JTextField textField) {
        // Set a DocumentFilter to allow only numeric input with a configurable decimal point
        Document doc = new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                if (str == null) {
                    return;
                }

                String currentText = getText(0, getLength());
                StringBuilder newText = new StringBuilder(currentText);
                for (int i = 0; i < str.length(); i++) {
                    char c = str.charAt(i);
                    if (!Character.isDigit(c) && c != '.') {
                        return; // Non-numeric character
                    }
                    if (c == '.' && currentText.contains(".")) {
                        return; // Already contains a decimal point
                    }
                    newText.insert(offs + i, c);
                }

                super.remove(0, getLength());
                super.insertString(0, newText.toString(), a);
            }
        };

        textField.setDocument(doc);
    }

    @Override
    IngredientTableFilter getEntity() {
        try {
            // Portions
            String fromCaloriesString = fromCaloriesField.getText();
            String toCaloriesString = toCaloriesField.getText();
            Double fromCalories = fromCaloriesString.isEmpty() ? 0.0 : Double.parseDouble(fromCaloriesString);
            Double toCalories = toCaloriesString.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(toCaloriesString);
            ingredientTableFilter.filterCalories(fromCalories, toCalories);

            // Name
            ingredientTableFilter.filterName(nameField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(FilterIngredientDialog.this,
                    "Incorrect filter parameters",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return this.ingredientTableFilter;
    }
}
