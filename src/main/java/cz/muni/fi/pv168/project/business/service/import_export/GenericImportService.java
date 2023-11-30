package cz.muni.fi.pv168.project.business.service.import_export;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.service.import_export.batch.Batch;
import cz.muni.fi.pv168.project.business.service.import_export.batch.BatchImporter;
import cz.muni.fi.pv168.project.business.service.import_export.format.BatchJsonImporter;
import cz.muni.fi.pv168.project.business.service.import_export.format.Format;
import cz.muni.fi.pv168.project.business.service.import_export.format.FormatMapping;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Generic synchronous implementation of the {@link ImportService}.
 */
public class GenericImportService implements ImportService {

    private final RecipeTableModel recipeTableModel;
    private final UnitTableModel unitTableModel;
    private final IngredientTableModel ingredientTableModel;
    private final FormatMapping<BatchImporter> importers;

    public GenericImportService(
            RecipeTableModel recipeTableModel,
            UnitTableModel unitTableModel,
            IngredientTableModel ingredientTableModel,
            List<BatchImporter> importers) {
        this.recipeTableModel = recipeTableModel;
        this.unitTableModel = unitTableModel;
        this.ingredientTableModel = ingredientTableModel;
        this.importers = new FormatMapping<>(importers);
    }

    @Override
    public void importData(String filePath) {
        recipeTableModel.deleteAll();
        ingredientTableModel.deleteAll();
        unitTableModel.deleteAll();

        BatchJsonImporter batchJsonImporter = new BatchJsonImporter();
        Batch batch = batchJsonImporter.importBatch(
                Objects.requireNonNullElse(filePath, "src/main/resources/database.json"));

        batch.units().forEach(this::createUnit);
        batch.ingredients().forEach(this::createIngredients);
        batch.recipes().forEach(this::createRecipe);

        recipeTableModel.refresh();
        ingredientTableModel.refresh();
        unitTableModel.refresh();
    }

    @Override
    public Collection<Format> getFormats() {
        return importers.getFormats();
    }

    private void createUnit(Unit unit) {
        unitTableModel.addRow(unit);
    }

    private void createIngredients(Ingredient ingredient) {
        ingredientTableModel.addRow(ingredient);
    }

    private void createRecipe(Recipe recipe) {
        recipeTableModel.addRow(recipe);
    }
}
