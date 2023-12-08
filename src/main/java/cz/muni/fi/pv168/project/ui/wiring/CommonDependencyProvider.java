package cz.muni.fi.pv168.project.ui.wiring;

import cz.muni.fi.pv168.project.business.model.*;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.crud.CrudService;
import cz.muni.fi.pv168.project.business.service.crud.IngredientCrudService;
import cz.muni.fi.pv168.project.business.service.crud.RecipeCrudService;
import cz.muni.fi.pv168.project.business.service.crud.UnitCrudService;
import cz.muni.fi.pv168.project.business.service.import_export.ExportService;
import cz.muni.fi.pv168.project.business.service.import_export.GenericExportService;
import cz.muni.fi.pv168.project.business.service.import_export.GenericImportService;
import cz.muni.fi.pv168.project.business.service.import_export.ImportService;
import cz.muni.fi.pv168.project.business.service.import_export.format.BatchJsonExporter;
import cz.muni.fi.pv168.project.business.service.import_export.format.BatchJsonImporter;
import cz.muni.fi.pv168.project.business.service.validation.IngredientValidator;
import cz.muni.fi.pv168.project.business.service.validation.RecipeValidator;
import cz.muni.fi.pv168.project.business.service.validation.UnitValidator;
import cz.muni.fi.pv168.project.storage.sql.IngredientSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.RecipeSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.TransactionalImportService;
import cz.muni.fi.pv168.project.storage.sql.UnittSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.dao.IngredientDao;
import cz.muni.fi.pv168.project.storage.sql.dao.RecipeDao;
import cz.muni.fi.pv168.project.storage.sql.db.*;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.IngredientMapper;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.RecipeMapper;

import java.util.List;

/**
 * Common dependency provider for both production and test environment.
 */
public class CommonDependencyProvider implements DependencyProvider {

    private final Repository<Recipe> recipes;
    private final Repository<Ingredient> ingredients;
    private final Repository<Unit> units;
    private final Repository<RecipeIngredient> recipeIngredients;
    private final DatabaseManager databaseManager;
    private final TransactionExecutor transactionExecutor;
    private final CrudService<Recipe> recipeCrudService;
    private final CrudService<Ingredient> ingredientCrudService;
    private final CrudService<Unit> unitCrudService;
    private final CrudService<RecipeIngredient> recipeIngredientCrudService;
    private final ImportService importService;
    private final ExportService exportService;
    private final RecipeValidator recipeValidator;
    private final IngredientValidator ingredientValidator;
    private final UnitValidator unitValidator;

    public CommonDependencyProvider(DatabaseManager databaseManager) {
        var guidProvider = new UuidGuidProvider();

        this.databaseManager = databaseManager;
        var transactionManager = new TransactionManagerImpl(databaseManager);
        this.transactionExecutor = new TransactionExecutorImpl(transactionManager::beginTransaction);
        var transactionConnectionSupplier = new TransactionConnectionSupplier(transactionManager, databaseManager);

        var recipeMapper = new RecipeMapper();
        var recipeDao = new RecipeDao(transactionConnectionSupplier);
        recipeValidator = new RecipeValidator();

        var ingredientMapper = new IngredientMapper();
        var ingredientDao = new IngredientDao(transactionConnectionSupplier);
        ingredientValidator = new IngredientValidator();

        var unitMapper = new UnitMapper();
        var unitDao = new UnitDao(transactionConnectionSupplier);
        unitValidator = new UnitValidator();

        this.recipes = new RecipeSqlRepository(
                recipeDao,
                recipeMapper
        );
        this.ingredients = new IngredientSqlRepository(
                ingredientDao,
                ingredientMapper
        );

        this.units = new UnittSqlRepository(
        recipeCrudService = new RecipeCrudService(recipes, recipeValidator, guidProvider);
        ingredientCrudService = new IngredientCrudService(ingredients, ingredientValidator, guidProvider);
        unitCrudService = new UnitCrudService(units, unitValidator, guidProvider);

        exportService = new GenericExportService(recipeCrudService, unitCrudService, ingredientCrudService,
                List.of(new BatchJsonExporter()));
        var genericImportService = new GenericImportService(employeeCrudService, departmentCrudService,
                List.of(new BatchJsonImporter()));
        importService = new TransactionalImportService(genericImportService, transactionExecutor);
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public DepartmentRepository getDepartmentRepository() {
        return departments;
    }

    @Override
    public EmployeeRepository getEmployeeRepository() {
        return employees;
    }

    @Override
    public TransactionExecutor getTransactionExecutor() {
        return transactionExecutor;
    }

    @Override
    public CrudService<Employee> getEmployeeCrudService() {
        return employeeCrudService;
    }

    @Override
    public CrudService<Department> getDepartmentCrudService() {
        return departmentCrudService;
    }

    @Override
    public ImportService getImportService() {
        return importService;
    }

    @Override
    public ExportService getExportService() {
        return exportService;
    }

    @Override
    public EmployeeValidator getEmployeeValidator() {
        return employeeValidator;
    }
}
