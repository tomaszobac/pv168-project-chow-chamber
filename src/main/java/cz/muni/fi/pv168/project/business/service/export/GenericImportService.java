package cz.muni.fi.pv168.project.business.service.export;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.business.service.export.batch.BatchImporter;
import cz.muni.fi.pv168.project.business.service.export.batch.BatchOperationException;
import cz.muni.fi.pv168.project.business.service.export.format.Format;
import cz.muni.fi.pv168.project.business.service.export.format.FormatMapping;

import java.util.Collection;

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
            Collection<BatchImporter> importers
    ) {
        this.recipeCrudService = recipeCrudService;
        this.unitCrudService = unitCrudService;
        this.ingredientCrudService = ingredientCrudService;
        this.importers = new FormatMapping<>(importers);
    }

    @Override
    public void importData(String filePath) {
        recipeCrudService.deleteAll();
        ingredientCrudService.deleteAll();
        unitCrudService.deleteAll();

        var batch = getImporter(filePath).importBatch(filePath);

        batch.units().forEach(this::createUnit);
        batch.ingredients().forEach(this::createIngredients);
        batch.recipes().forEach(this::createRecipe);
    }

    private void createUnit(Unit unit) {
        unitCrudService.create(unit)
                .intoException();
    }

    private void createIngredients(Ingredient ingredient) {
        ingredientCrudService.create(ingredient)
                .intoException();
    }

    private void createRecipe(Recipe recipe) {
        recipeCrudService.create(recipe)
                .intoException();
    }

    @Override
    public Collection<Format> getFormats() {
        return importers.getFormats();
    }

    private BatchImporter getImporter(String filePath) {
        var extension = filePath.substring(filePath.lastIndexOf('.') + 1);
        var importer = importers.findByExtension(extension);
        if (importer == null) {
            throw new BatchOperationException("Extension %s has no registered formatter".formatted(extension));
        }

        return importer;
    }
}
