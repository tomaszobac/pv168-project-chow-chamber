package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.filters.RecipeTableFilter;
import cz.muni.fi.pv168.project.ui.filters.components.FilterComboboxBuilder;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterCategoryValues;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategories;
import cz.muni.fi.pv168.project.ui.renderers.CategoryRenderer;
import cz.muni.fi.pv168.project.ui.renderers.SpecialFilterCategoryValuesRenderer;
import cz.muni.fi.pv168.project.util.Either;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FilterRecipeDialog extends EntityDialog<RecipeTableFilter> {
    private final RecipeTableFilter recipeTableFilter;
    private final JTextField nameField = new JTextField();
    private final JComboBox<Either<SpecialFilterCategoryValues, RecipeCategories>> categoryComboBox;
    private final JTextField fromTimeField = new JTextField();
    private final JTextField toTimeField = new JTextField();
    private final JTextField fromPortionsField = new JTextField();
    private final JTextField toPortionsField = new JTextField();

    public FilterRecipeDialog(RecipeTableFilter recipeTableFilter) {
        this.recipeTableFilter = recipeTableFilter;
        this.categoryComboBox = createCategoryFilter(recipeTableFilter);
        setNumericWithSeparatorFilter(fromPortionsField, false);
        setNumericWithSeparatorFilter(toPortionsField, false);
        setNumericWithSeparatorFilter(fromTimeField, true);
        setNumericWithSeparatorFilter(toTimeField, true);
        setValues();
        addFields();
    }

    private void setValues() {
        nameField.setText(recipeTableFilter.getName());

        categoryComboBox.setSelectedItem(recipeTableFilter.getSelectedCategoryValue());

        fromTimeField.setText(recipeTableFilter.getTimeFrom().toString());
        if (recipeTableFilter.getTimeTo() == LocalTime.MAX) {
            toTimeField.setText("");
        } else {
            toTimeField.setText(recipeTableFilter.getTimeTo().toString());
        }
        fromPortionsField.setText(Integer.toString(recipeTableFilter.getPortionsFrom()));
        if (recipeTableFilter.getPortionsTo() == Integer.MAX_VALUE) {
            toPortionsField.setText("");
        } else {
            toPortionsField.setText(Integer.toString(recipeTableFilter.getPortionsTo()));
        }
    }

    private void addFields() {
        add("Name:", nameField);
        add("Category:", categoryComboBox);
        add("Time from:", fromTimeField);
        add("Time to:", toTimeField);
        add("Portions from:", fromPortionsField);
        add("Portions to:", toPortionsField);
    }

    private static JComboBox<Either<SpecialFilterCategoryValues, RecipeCategories>> createCategoryFilter(
            RecipeTableFilter recipeTableFilter) {
        return FilterComboboxBuilder.create(SpecialFilterCategoryValues.class, RecipeCategories.values())
                .setSpecialValuesRenderer(new SpecialFilterCategoryValuesRenderer())
                .setValuesRenderer(new CategoryRenderer())
                .setFilter(recipeTableFilter::filterCategory)
                .build();
    }

    // Set a DocumentFilter to allow only numeric input with a configurable decimal point
    public static void setNumericWithSeparatorFilter(JTextField textField, boolean allowSeparator) {
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
                    if (c == ':' && !allowSeparator) {
                        return; // Separator not allowed
                    }
                    if (!Character.isDigit(c) && c != ':') {
                        return; // Non-numeric character
                    }
                    if (c == ':' && currentText.contains(":")) {
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
    RecipeTableFilter getEntity() {
        try {
            // Portions
            String fromPortionsString = fromPortionsField.getText();
            String toPortionsString = toPortionsField.getText();
            int fromPortions = fromPortionsString.equals("") ? 0 : Integer.parseInt(fromPortionsString);
            int toPortions = toPortionsString.equals("") ? Integer.MAX_VALUE : Integer.parseInt(toPortionsString);
            recipeTableFilter.filterPortions(fromPortions, toPortions);

            // Times
            String fromTimeString = fromTimeField.getText();
            String toTimeString = toTimeField.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

            LocalTime fromTime = fromTimeString.equals("") ? LocalTime.of(0, 0) : LocalTime.parse(fromTimeString, formatter);
            LocalTime toTime = fromTimeString.equals("") ? LocalTime.of(23, 59) : LocalTime.parse(toTimeString, formatter);
            recipeTableFilter.filterTime(fromTime, toTime);

            // Name
            recipeTableFilter.filterName(nameField.getText());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(FilterRecipeDialog.this,
                    "Incorrect filter parameters",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return this.recipeTableFilter;
    }
}
