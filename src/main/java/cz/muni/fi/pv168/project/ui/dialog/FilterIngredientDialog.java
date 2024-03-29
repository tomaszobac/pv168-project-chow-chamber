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

    /**
     * Sets a DocumentFilter on the provided JTextField to allow only numeric input with a single decimal point.
     * The DocumentFilter ensures that only valid numeric characters (0-9) and a single decimal point are allowed.
     * If the input contains non-numeric characters or more than one decimal point, the insertion is rejected.
     *
     * @param textField The JTextField to which the DocumentFilter will be applied.
     */
    public static void setNumericWithDecimalFilter(JTextField textField) {
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
                        return;
                    }
                    if (c == '.' && currentText.contains(".")) {
                        return;
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
            String fromCaloriesString = fromCaloriesField.getText();
            String toCaloriesString = toCaloriesField.getText();
            Double fromCalories = fromCaloriesString.isEmpty() ? 0.0 : Double.parseDouble(fromCaloriesString);
            Double toCalories = toCaloriesString.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(toCaloriesString);
            ingredientTableFilter.filterCalories(fromCalories, toCalories);
            ingredientTableFilter.filterName(nameField.getText());
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(FilterIngredientDialog.this,
                    "Incorrect filter parameters",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return this.ingredientTableFilter;
    }
}
