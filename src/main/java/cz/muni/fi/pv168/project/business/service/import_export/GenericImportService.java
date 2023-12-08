package cz.muni.fi.pv168.project.business.service.import_export;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
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

    private final CrudService<Recipe> recipeCrudService;
    private final CrudService<Unit> unitCrudService;
    private final CrudService<Ingredient> ingredientCrudService;
    private final FormatMapping<BatchImporter> importers;

    public GenericImportService(
            CrudService<Recipe> recipeCrudService,
            CrudService<Unit> unitCrudService,
            CrudService<Ingredient> ingredientCrudService,
            List<BatchImporter> importers) {
        this.recipeCrudService = recipeCrudService;
        this.unitCrudService = unitCrudService;
        this.ingredientCrudService = ingredientCrudService;
        this.importers = new FormatMapping<>(importers);
    }

    @Override
    public void importData(String filePath) {
        recipeCrudService.deleteAll();
        unitCrudService.deleteAll();
        ingredientCrudService.deleteAll();

        BatchJsonImporter batchJsonImporter = new BatchJsonImporter();
        Batch batch = batchJsonImporter.importBatch(
                Objects.requireNonNullElse(filePath, "src/main/resources/database.json"));

        batch.units().forEach(this::createUnit);
        batch.ingredients().forEach(this::createIngredients);
        batch.recipes().forEach(this::createRecipe);
    }

    @Override
    public Collection<Format> getFormats() {
        return importers.getFormats();
    }

    private void createRecipe(Recipe recipe) {
        recipeCrudService.create(recipe)
                .intoException();
    }
    private void createIngredients(Ingredient ingredient) {
        ingredientCrudService.create(ingredient)
                .intoException();
    }
    private void createUnit(Unit unit) {
        unitCrudService.create(unit)
                .intoException();
    }

}
