package cz.muni.fi.pv168.project.ui.dialog;

import cz.muni.fi.pv168.project.ui.filters.RecipeTableFilter;
import cz.muni.fi.pv168.project.ui.filters.components.FilterComboboxBuilder;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterCategoryValues;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;
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
import java.time.format.DateTimeParseException;

public class FilterRecipeDialog extends EntityDialog<RecipeTableFilter> {
    private final RecipeTableFilter recipeTableFilter;
    private final JTextField nameField = new JTextField();
    private final JComboBox<Either<SpecialFilterCategoryValues, RecipeCategory>> categoryComboBox;
    private final JTextField fromTimeField = new JTextField();
    private final JTextField toTimeField = new JTextField();
    private final JTextField fromPortionsField = new JTextField();
    private final JTextField toPortionsField = new JTextField();

    public FilterRecipeDialog(RecipeTableFilter recipeTableFilter) {
        this.recipeTableFilter = recipeTableFilter;
        this.categoryComboBox = createCategoryFilter();
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

    private static JComboBox<Either<SpecialFilterCategoryValues, RecipeCategory>> createCategoryFilter() {
        return FilterComboboxBuilder.create(SpecialFilterCategoryValues.class, RecipeCategory.values())
                .setSpecialValuesRenderer(new SpecialFilterCategoryValuesRenderer())
                .setValuesRenderer(new CategoryRenderer())
                .build();
    }

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
                        return;
                    }
                    if (!Character.isDigit(c) && c != ':') {
                        return;
                    }
                    if (c == ':' && currentText.contains(":")) {
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

    private LocalTime getTime(String timeString) {
        int hours = 0;
        int minutes = 0;
        if (timeString.isBlank()) {
            return LocalTime.of(hours, minutes);
        }
        try {

            if (timeString.matches("\\d+")) {
                minutes = Integer.parseInt(timeString);
            } else {
                String[] parts = timeString.split(":");

                if (parts.length == 2) {
                    hours = Integer.parseInt(parts[0]);
                    minutes = Integer.parseInt(parts[1]);
                } else {
                    throw new IllegalArgumentException("Invalid time format: " + timeString);
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid time format: " + timeString, e);
        }

        if (minutes > 59) {
            hours += minutes / 60;
            minutes = minutes % 60;
        } else if (minutes < 0)
            minutes = 0;

        if (hours > 23)
            hours = 23;
        else if (hours < 0)
            hours = 0;

        return LocalTime.of(hours, minutes);
    }

    @Override
    RecipeTableFilter getEntity() {
        try {
            Either<SpecialFilterCategoryValues, RecipeCategory> selectedCategory =
                    (Either<SpecialFilterCategoryValues, RecipeCategory>) categoryComboBox.getSelectedItem();

            if (selectedCategory != null) {
                recipeTableFilter.filterCategory(selectedCategory);
            }

            String fromPortionsString = fromPortionsField.getText();
            String toPortionsString = toPortionsField.getText();
            int fromPortions = fromPortionsString.isEmpty() ? 0 : Integer.parseInt(fromPortionsString);
            int toPortions = toPortionsString.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(toPortionsString);
            recipeTableFilter.filterPortions(fromPortions, toPortions);

            String fromTimeString = fromTimeField.getText();
            String toTimeString = toTimeField.getText();

            LocalTime fromTime = fromTimeString.isEmpty() ? LocalTime.of(0, 0) : getTime(fromTimeString);
            LocalTime toTime = fromTimeString.isEmpty() ? LocalTime.of(23, 59) : getTime(toTimeString);
            recipeTableFilter.filterTime(fromTime, toTime);
            recipeTableFilter.filterName(nameField.getText());

        } catch (NumberFormatException | DateTimeParseException e) {
            JOptionPane.showMessageDialog(FilterRecipeDialog.this,
                    "Incorrect filter parameters",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return this.recipeTableFilter;
    }
}
