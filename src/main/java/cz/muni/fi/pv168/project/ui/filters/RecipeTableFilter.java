package cz.muni.fi.pv168.project.ui.filters;

import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.project.ui.filters.matchers.EntityMatchers;
import cz.muni.fi.pv168.project.ui.filters.matchers.recipe.RecipeCategoryMatcher;
import cz.muni.fi.pv168.project.ui.filters.matchers.recipe.RecipeLocalTimeMatcher;
import cz.muni.fi.pv168.project.ui.filters.matchers.recipe.RecipeNameMatcher;
import cz.muni.fi.pv168.project.ui.filters.matchers.recipe.RecipePortionsMatcher;
import cz.muni.fi.pv168.project.ui.filters.values.SpecialFilterCategoryValues;
import cz.muni.fi.pv168.project.ui.model.EntityTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.model.entities.Recipe;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategories;
import cz.muni.fi.pv168.project.util.Either;

import javax.swing.table.TableRowSorter;
import java.time.LocalTime;
import java.util.stream.Stream;

public class RecipeTableFilter {
    private final RecipeCompoundMatcher recipeCompoundMatcher;
    private Either<SpecialFilterCategoryValues, RecipeCategories> selectedCategoryValue = Either.left(SpecialFilterCategoryValues.ALL);
    private Integer portionsFrom = 0;
    private Integer portionsTo = Integer.MAX_VALUE;
    private LocalTime timeFrom = LocalTime.of(0,0);
    private LocalTime timeTo = LocalTime.of(23,59);
    private String name = "";

    public RecipeTableFilter(TableRowSorter<RecipeTableModel> rowSorter) {
        recipeCompoundMatcher = new RecipeCompoundMatcher(rowSorter);
        rowSorter.setRowFilter(recipeCompoundMatcher);
    }

    public void filterCategory(Either<SpecialFilterCategoryValues, RecipeCategories> selectedItem) {
        selectedCategoryValue = selectedItem;
        selectedItem.apply(
                l -> recipeCompoundMatcher.setCategoryMatcher(l.getMatcher()),
                r -> recipeCompoundMatcher.setCategoryMatcher(new RecipeCategoryMatcher(r))
        );
    }
    public void filterPortions(Integer from, Integer to) {
        this.portionsFrom = from;
        this.portionsTo = to;
        recipeCompoundMatcher.setPortionsMatcher(new RecipePortionsMatcher(from, to));
    }
    public void filterTime(LocalTime from, LocalTime to) {
        this.timeFrom = from;
        this.timeTo = to;
        recipeCompoundMatcher.setTimeMatcher(new RecipeLocalTimeMatcher(from, to));
    }
    public void filterName(String name) {
        this.name = name;
        recipeCompoundMatcher.setNameMatcher(new RecipeNameMatcher(name));
    }

    public Either<SpecialFilterCategoryValues, RecipeCategories> getSelectedCategoryValue() {
        return selectedCategoryValue;
    }

    public Integer getPortionsFrom() {
        return portionsFrom;
    }

    public Integer getPortionsTo() {
        return portionsTo;
    }

    public LocalTime getTimeFrom() {
        return timeFrom;
    }

    public LocalTime getTimeTo() {
        return timeTo;
    }

    public String getName() {
        return name;
    }

    /**
     * Container class for all matchers for the RecipeTable.
     *
     * This Matcher evaluates to true, if all contained {@link EntityMatcher} instances
     * evaluate to true.
     */
    private static class RecipeCompoundMatcher extends EntityMatcher<Recipe> {

        private final TableRowSorter<RecipeTableModel> rowSorter;
        private EntityMatcher<Recipe> categoryMatcher = EntityMatchers.all();
        private EntityMatcher<Recipe> portionsMatcher = EntityMatchers.all();
        private EntityMatcher<Recipe> timeMatcher = EntityMatchers.all();
        private EntityMatcher<Recipe> nameMatcher = EntityMatchers.all();

        private RecipeCompoundMatcher(TableRowSorter<RecipeTableModel> rowSorter) {
            this.rowSorter = rowSorter;
        }

        private void setCategoryMatcher(EntityMatcher<Recipe> categoryMatcher) {
            this.categoryMatcher = categoryMatcher;
            rowSorter.sort();
        }
        private void setPortionsMatcher(EntityMatcher<Recipe> portionsMatcher) {
            this.portionsMatcher = portionsMatcher;
            rowSorter.sort();
        }
        private void setTimeMatcher(EntityMatcher<Recipe> timeMatcher) {
            this.timeMatcher = timeMatcher;
            rowSorter.sort();
        }
        private  void setNameMatcher(EntityMatcher<Recipe> nameMatcher) {
            this.nameMatcher = nameMatcher;
            rowSorter.sort();
        }

        @Override
        public boolean evaluate(Recipe recipe) {
            return Stream.of(categoryMatcher, portionsMatcher, timeMatcher, nameMatcher)
                    .allMatch(m -> m.evaluate(recipe));
        }
    }
}
