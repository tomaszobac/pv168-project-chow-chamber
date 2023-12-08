package cz.muni.fi.pv168.project.wiring;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.business.service.import_export.ExportService;
import cz.muni.fi.pv168.project.business.service.import_export.ImportService;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import cz.muni.fi.pv168.project.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionExecutor;

/**
 * Interface for instance wiring
 */
public interface DependencyProvider {

    DatabaseManager getDatabaseManager();

    Repository<Recipe> getRecipeRepository();

    Repository<Ingredient> getIngredientRepository();

    Repository<Unit> getUnitRepository();

    Repository<RecipeIngredient> getRecipeIngredientRepository();

    TransactionExecutor getTransactionExecutor();

    CrudService<Recipe> getRecipeCrudService();

    CrudService<Ingredient> getIngredientCrudService();

    CrudService<Unit> getUnitCrudService();

    CrudService<RecipeIngredient> getRecipeIngredientCrudService();

    ImportService getImportService();

    ExportService getExportService();

    Validator<Recipe> getRecipeValidator();

    Validator<Ingredient> getIngredientValidator();

    Validator<Unit> getUnitValidator();

    Validator<RecipeIngredient> getRecipeIngredientValidator();
}

