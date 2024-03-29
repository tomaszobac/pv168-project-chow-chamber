package cz.muni.fi.pv168.project.business.service.import_export;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.business.service.import_export.batch.Batch;
import cz.muni.fi.pv168.project.business.service.import_export.batch.BatchExporter;
import cz.muni.fi.pv168.project.business.service.import_export.batch.BatchOperationException;
import cz.muni.fi.pv168.project.business.service.import_export.format.Format;
import cz.muni.fi.pv168.project.business.service.import_export.format.FormatMapping;

import java.util.Collection;
import java.util.List;

/**
 * Generic synchronous implementation of the {@link ExportService}
 */
public class GenericExportService implements ExportService {

    private final CrudService<Recipe> recipeCrudService;
    private final CrudService<Unit> unitCrudService;
    private final CrudService<Ingredient> ingredientCrudService;
    private final CrudService<RecipeIngredient> recipeIngredientCrudService;
    private final FormatMapping<BatchExporter> exporters;

    public GenericExportService(
            CrudService<Recipe> recipeCrudService,
            CrudService<Unit> unitCrudService,
            CrudService<Ingredient> ingredientCrudService,
            CrudService<RecipeIngredient> recipeIngredientCrudService,
            List<BatchExporter> exporters
    ) {
        this.recipeCrudService = recipeCrudService;
        this.unitCrudService = unitCrudService;
        this.ingredientCrudService = ingredientCrudService;
        this.recipeIngredientCrudService = recipeIngredientCrudService;
        this.exporters = new FormatMapping<>(exporters);
    }

    @Override
    public Collection<Format> getFormats() {
        return exporters.getFormats();
    }

    @Override
    public void exportData(String filePath) {
        var exporter = getExporter(filePath);

        var batch = new Batch(unitCrudService.findAll(), ingredientCrudService.findAll(), recipeCrudService.findAll(), recipeIngredientCrudService.findAll());
        exporter.exportBatch(batch, filePath);
    }

    public Integer getExportEntitiesCount() {
        return recipeCrudService.findAll().size() + unitCrudService.findAll().size() +
                ingredientCrudService.findAll().size() + recipeIngredientCrudService.findAll().size();
    }

    private BatchExporter getExporter(String filePath) {
        var extension = filePath.substring(filePath.lastIndexOf('.') + 1);
        var importer = exporters.findByExtension(extension);
        if (importer == null)
            throw new BatchOperationException("Extension %s has no registered formatter".formatted(extension));
        return importer;
    }
}
