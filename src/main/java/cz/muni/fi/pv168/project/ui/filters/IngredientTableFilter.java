package cz.muni.fi.pv168.project.ui.filters;

import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatchers;
import cz.muni.fi.pv168.project.ui.filters.matchers.ingredient.IngredientCaloriesMatcher;
import cz.muni.fi.pv168.project.ui.filters.matchers.ingredient.IngredientNameMatcher;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.business.model.Ingredient;

import javax.swing.table.TableRowSorter;
import java.util.stream.Stream;

public class IngredientTableFilter {
    private final IngredientCompoundMatcher ingredientCompoundMatcher;
    private String name = "";
    private Double caloriesFrom = 0.0;
    private Double caloriesTo = Double.MAX_VALUE;

    public IngredientTableFilter(TableRowSorter<IngredientTableModel> rowSorter) {
        ingredientCompoundMatcher = new IngredientCompoundMatcher(rowSorter);
        rowSorter.setRowFilter(ingredientCompoundMatcher);
    }

    public void filterName(String name) {
        this.name = name;
        ingredientCompoundMatcher.setNameMatcher(new IngredientNameMatcher(name));
    }
    public void filterCalories(Double from, Double to) {
        caloriesFrom = from;
        caloriesTo = to;
        ingredientCompoundMatcher.setCaloriesMatcher(new IngredientCaloriesMatcher(from, to));
    }

    public String getName() {
        return name;
    }

    public Double getCaloriesFrom() {
        return caloriesFrom;
    }

    public Double getCaloriesTo() {
        return caloriesTo;
    }

    /**
     * Container class for all matchers for the UnitTable.
     *
     * This Matcher evaluates to true, if all contained {@link EntityMatcher} instances
     * evaluate to true.
     */
    private static class IngredientCompoundMatcher extends EntityMatcher<Ingredient> {

        private final TableRowSorter<IngredientTableModel> rowSorter;
        private EntityMatcher<Ingredient> nameMatcher = EntityMatchers.all();
        private EntityMatcher<Ingredient> caloriesMatcher = EntityMatchers.all();

        private IngredientCompoundMatcher(TableRowSorter<IngredientTableModel> rowSorter) {
            this.rowSorter = rowSorter;
        }

        private  void setNameMatcher(EntityMatcher<Ingredient> nameMatcher) {
            this.nameMatcher = nameMatcher;
            rowSorter.sort();
        }

        private void setCaloriesMatcher(EntityMatcher<Ingredient> caloriesMatcher) {
            this.caloriesMatcher = caloriesMatcher;
            rowSorter.sort();
        }

        @Override
        public boolean evaluate(Ingredient ingredient) {
            return Stream.of(nameMatcher, caloriesMatcher)
                    .allMatch(m -> m.evaluate(ingredient));
        }
    }
}
