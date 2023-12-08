package cz.muni.fi.pv168.project.wiring;

import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.model.UuidGuidProvider;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.business.service.crud.RecipeCrudService;
import cz.muni.fi.pv168.project.business.service.crud.IngredientCrudService;
import cz.muni.fi.pv168.project.business.service.crud.UnitCrudService;
import cz.muni.fi.pv168.project.business.service.import_export.ExportService;
import cz.muni.fi.pv168.project.business.service.import_export.GenericExportService;
import cz.muni.fi.pv168.project.business.service.import_export.GenericImportService;
import cz.muni.fi.pv168.project.business.service.import_export.ImportService;
import cz.muni.fi.pv168.project.business.service.validation.RecipeValidator;
import cz.muni.fi.pv168.project.business.service.validation.IngredientValidator;
import cz.muni.fi.pv168.project.business.service.validation.UnitValidator;
import cz.muni.fi.pv168.project.business.service.import_export.format.BatchJsonExporter;
import cz.muni.fi.pv168.project.business.service.import_export.format.BatchJsonImporter;
import cz.muni.fi.pv168.project.business.service.validation.Validator;
import cz.muni.fi.pv168.project.storage.sql.RecipeSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.IngredientSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.UnitSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.dao.RecipeDao;
import cz.muni.fi.pv168.project.storage.sql.dao.IngredientDao;
import cz.muni.fi.pv168.project.storage.sql.dao.UnitDao;
import cz.muni.fi.pv168.project.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionConnectionSupplier;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionExecutor;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionExecutorImpl;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionManagerImpl;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.RecipeMapper;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.IngredientMapper;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.UnitMapper;

import java.util.List;

/**
 * Common dependency provider for both production and test environment.
 */
public class CommonDependencyProvider implements DependencyProvider {

    private final Repository<Recipe> recipes;
    private final Repository<Ingredient> ingredients;
    private final Repository<Unit> units;
    private final DatabaseManager databaseManager;
    private final TransactionExecutor transactionExecutor;
    private final CrudService<Recipe> recipeCrudService;
    private final CrudService<Ingredient> ingredientCrudService;
    private final CrudService<Unit> unitCrudService;
    private final ImportService importService;
    private final ExportService import_exportService;
    private final RecipeValidator recipeValidator;
    private final IngredientValidator ingredientValidator;
    private final UnitValidator unitValidator;

    public CommonDependencyProvider(DatabaseManager databaseManager) {
        this.recipeValidator = new RecipeValidator();
        this.ingredientValidator = new IngredientValidator();
        this.unitValidator = new UnitValidator();

        var guidProvider = new UuidGuidProvider();

        this.databaseManager = databaseManager;
        var transactionManager = new TransactionManagerImpl(databaseManager);
        this.transactionExecutor = new TransactionExecutorImpl(transactionManager::beginTransaction);
        var transactionConnectionSupplier = new TransactionConnectionSupplier(transactionManager, databaseManager);

        this.recipes = new RecipeSqlRepository(
                new RecipeDao(transactionConnectionSupplier),
                new RecipeMapper()
        );
        this.ingredients = new IngredientSqlRepository(
                new IngredientDao(transactionConnectionSupplier),
                new IngredientMapper()
        );
        this.units = new UnitSqlRepository(
                new UnitDao(transactionConnectionSupplier),
                new UnitMapper()
        );

        this.recipeCrudService = new RecipeCrudService(this.recipes, this.recipeValidator, guidProvider);
        this.ingredientCrudService = new IngredientCrudService(this.ingredients, this.ingredientValidator, guidProvider);
        this.unitCrudService = new UnitCrudService(this.units, this.unitValidator, guidProvider);

        this.import_exportService = new GenericExportService(this.recipeCrudService, this.unitCrudService, this.ingredientCrudService,
                List.of(new BatchJsonExporter()));
        this.importService = new GenericImportService(this.recipeCrudService, this.unitCrudService, this.ingredientCrudService,
                List.of(new BatchJsonImporter()));
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public Repository<Ingredient> getIngredientRepository() {
        return ingredients;
    }

    @Override
    public Repository<Unit> getUnitRepository() {
        return units;
    }

    @Override
    public Repository<Recipe> getRecipeRepository() {
        return recipes;
    }

    @Override
    public TransactionExecutor getTransactionExecutor() {
        return transactionExecutor;
    }

    @Override
    public CrudService<Recipe> getRecipeCrudService() {
        return recipeCrudService;
    }

    @Override
    public CrudService<Ingredient> getIngredientCrudService() {
        return ingredientCrudService;
    }

    @Override
    public CrudService<Unit> getUnitCrudService() {
        return unitCrudService;
    }

    @Override
    public ImportService getImportService() {
        return importService;
    }

    @Override
    public ExportService getExportService() {
        return import_exportService;
    }

    @Override
    public RecipeValidator getRecipeValidator() {
        return recipeValidator;
    }

    @Override
    public Validator<Ingredient> getIngredientValidator() {
        return ingredientValidator;
    }

    @Override
    public Validator<Unit> getUnitValidator() {
        return unitValidator;
    }
}
