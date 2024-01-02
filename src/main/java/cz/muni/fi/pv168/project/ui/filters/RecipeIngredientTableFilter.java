package cz.muni.fi.pv168.project.ui.filters;

import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatchers;
import cz.muni.fi.pv168.project.ui.filters.matchers.recipeIngredient.RecipeGuidMatcher;
import cz.muni.fi.pv168.project.ui.model.RecipeIngredientsTableModel;

import javax.swing.table.TableRowSorter;
import java.util.stream.Stream;

public class RecipeIngredientTableFilter {
    private final RecipeIngredientCompoundMatcher recipeIngredientCompoundMatcher;
    String guid;

    public RecipeIngredientTableFilter(TableRowSorter<RecipeIngredientsTableModel> rowSorter) {
        recipeIngredientCompoundMatcher = new RecipeIngredientCompoundMatcher(rowSorter);
        rowSorter.setRowFilter(recipeIngredientCompoundMatcher);
    }

    public void filterGuid(String guid) {
        this.guid = guid;
        recipeIngredientCompoundMatcher.setGuidMatcher(new RecipeGuidMatcher(guid));
    }

    public String getGuid() {
        return guid;
    }

    /**
     * Container class for all matchers for the UnitTable.
     *
     * This Matcher evaluates to true, if all contained {@link EntityMatcher} instances
     * evaluate to true.
     */
    private static class RecipeIngredientCompoundMatcher extends EntityMatcher<RecipeIngredient> {

        private final TableRowSorter<RecipeIngredientsTableModel> rowSorter;
        private EntityMatcher<RecipeIngredient> guidMatcher = EntityMatchers.all();

        private RecipeIngredientCompoundMatcher(TableRowSorter<RecipeIngredientsTableModel> rowSorter) {
            this.rowSorter = rowSorter;
        }

        private  void setGuidMatcher(EntityMatcher<RecipeIngredient> guidMatcher) {
            this.guidMatcher = guidMatcher;
            rowSorter.sort();
        }

        @Override
        public boolean evaluate(RecipeIngredient recipeIngredient) {
            return Stream.of(guidMatcher)
                    .allMatch(m -> m.evaluate(recipeIngredient));
        }
    }
}
