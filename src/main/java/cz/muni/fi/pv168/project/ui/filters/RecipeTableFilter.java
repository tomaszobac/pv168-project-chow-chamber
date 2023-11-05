package cz.muni.fi.pv168.project.ui.filters;

import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatchers;
import cz.muni.fi.pv168.project.ui.filters.matchers.recipe.RecipeCategoryMatcher;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterCategoryValues;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.model.entities.Recipe;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategories;
import cz.muni.fi.pv168.project.util.Either;

import javax.swing.table.TableRowSorter;
import java.util.stream.Stream;

public class RecipeTableFilter {
    private final RecipeCompoundMatcher recipeCompoundMatcher;

    public RecipeTableFilter(TableRowSorter<RecipeTableModel> rowSorter) {
        recipeCompoundMatcher = new RecipeCompoundMatcher(rowSorter);
        rowSorter.setRowFilter(recipeCompoundMatcher);
    }

    public void filterCategory(Either<SpecialFilterCategoryValues, RecipeCategories> selectedItem) {
        selectedItem.apply(
                l -> recipeCompoundMatcher.setCategoryMatcher(l.getMatcher()),
                r -> recipeCompoundMatcher.setCategoryMatcher(new RecipeCategoryMatcher(r))
        );
    }

    /**
     * Container class for all matchers for the EmployeeTable.
     *
     * This Matcher evaluates to true, if all contained {@link EntityMatcher} instances
     * evaluate to true.
     */
    private static class RecipeCompoundMatcher extends EntityMatcher<Recipe> {

        private final TableRowSorter<RecipeTableModel> rowSorter;
        private EntityMatcher<Recipe> categoryMatcher = EntityMatchers.all();

        private RecipeCompoundMatcher(TableRowSorter<RecipeTableModel> rowSorter) {
            this.rowSorter = rowSorter;
        }

        private void setCategoryMatcher(EntityMatcher<Recipe> categoryMatcher) {
            this.categoryMatcher = categoryMatcher;
            rowSorter.sort();
        }

        @Override
        public boolean evaluate(Recipe recipe) {
            return Stream.of(categoryMatcher)
                    .allMatch(m -> m.evaluate(recipe));
        }
    }
}
