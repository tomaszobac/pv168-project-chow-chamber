package cz.muni.fi.pv168.project.business.service.import_export;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.business.service.import_export.batch.Batch;
import cz.muni.fi.pv168.project.business.service.import_export.batch.BatchImporter;
import cz.muni.fi.pv168.project.business.service.import_export.format.BatchJsonImporter;
import cz.muni.fi.pv168.project.business.service.import_export.format.Format;
import cz.muni.fi.pv168.project.business.service.import_export.format.FormatMapping;

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
    private final CrudService<RecipeIngredient> recipeIngredientCrudService;
    private final FormatMapping<BatchImporter> importers;

    public GenericImportService(
            CrudService<Recipe> recipeCrudService,
            CrudService<Unit> unitCrudService,
            CrudService<Ingredient> ingredientCrudService,
            CrudService<RecipeIngredient> recipeIngredientCrudService,
            List<BatchImporter> importers) {
        this.recipeCrudService = recipeCrudService;
        this.unitCrudService = unitCrudService;
        this.ingredientCrudService = ingredientCrudService;
        this.recipeIngredientCrudService = recipeIngredientCrudService;
        this.importers = new FormatMapping<>(importers);
    }

    @Override
    public void importData(String filePath, boolean rewrite) {
        if (rewrite){
            recipeIngredientCrudService.deleteAll();
            recipeCrudService.deleteAll();
            ingredientCrudService.deleteAll();
            unitCrudService.deleteAll();
        }
        BatchJsonImporter batchJsonImporter = new BatchJsonImporter();
        Batch batch = batchJsonImporter.importBatch(
                Objects.requireNonNullElse(filePath, "src/main/resources/database.json"));

        Collection<Unit> units = batch.units();
        if (units != null) {
            units.forEach(this::createUnit);
        }

        Collection<Ingredient> ingredients = batch.ingredients();
        if (ingredients != null) {
            ingredients.forEach(this::createIngredients);
        }

        Collection<Recipe> recipes = batch.recipes();
        if (recipes != null) {
            recipes.forEach(this::createRecipe);
        }

        Collection<RecipeIngredient> recipeIngredients = batch.recipeIngredients();
        if (recipeIngredients != null) {
            recipeIngredients.forEach(this::createRecipeIngredient);
        }
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
    private void createRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipeIngredientCrudService.create(recipeIngredient)
                .intoException();
    }

}
