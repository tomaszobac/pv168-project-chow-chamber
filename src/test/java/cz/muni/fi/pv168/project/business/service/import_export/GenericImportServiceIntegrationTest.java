package cz.muni.fi.pv168.project.business.service.import_export;

import cz.muni.fi.pv168.project.business.model.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.model.UuidGuidProvider;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.crud.IngredientCrudService;
import cz.muni.fi.pv168.project.business.service.crud.RecipeCrudService;
import cz.muni.fi.pv168.project.business.service.crud.RecipeIngredientCrudService;
import cz.muni.fi.pv168.project.business.service.crud.UnitCrudService;
import cz.muni.fi.pv168.project.business.service.import_export.format.BatchJsonImporter;
import cz.muni.fi.pv168.project.business.service.validation.IngredientValidator;
import cz.muni.fi.pv168.project.business.service.validation.RecipeIngredientValidator;
import cz.muni.fi.pv168.project.business.service.validation.RecipeValidator;
import cz.muni.fi.pv168.project.business.service.validation.UnitValidator;
import cz.muni.fi.pv168.project.business.service.validation.ValidationException;
import cz.muni.fi.pv168.project.storage.sql.IngredientSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.RecipeIngredientSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.RecipeSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.UnitSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.dao.IngredientDao;
import cz.muni.fi.pv168.project.storage.sql.dao.RecipeDao;
import cz.muni.fi.pv168.project.storage.sql.dao.RecipeIngredientDao;
import cz.muni.fi.pv168.project.storage.sql.dao.UnitDao;
import cz.muni.fi.pv168.project.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionConnectionSupplier;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionExecutorImpl;
import cz.muni.fi.pv168.project.storage.sql.db.TransactionManagerImpl;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.IngredientMapper;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.RecipeIngredientMapper;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.RecipeMapper;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.UnitMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;


class GenericImportServiceIntegrationTest {
    @Mock
    private static final Path PROJECT_ROOT = Paths.get(System.getProperty("project.basedir", "")).toAbsolutePath();
    private static final Path TEST_RESOURCES = PROJECT_ROOT.resolve(Path.of("src", "test", "resources"));
    private GenericImportService genericImportService;
    private RecipeCrudService recipeCrudService;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        var recipeRepository = mock(Repository.class);
        var recipeValidator = mock(RecipeValidator.class);
        var guidProvider = mock(GuidProvider.class);
        recipeCrudService = new RecipeCrudService(recipeRepository, recipeValidator, guidProvider);

        var unitRepository = mock(Repository.class);
        var unitValidator = mock(UnitValidator.class);
        var unitCrudService = new UnitCrudService(unitRepository, unitValidator, guidProvider);

        var ingredientRepository = mock(Repository.class);
        var ingredientValidator = mock(IngredientValidator.class);
        var ingredientCrudService = new IngredientCrudService(ingredientRepository, ingredientValidator, guidProvider);

        var recipeIngredientRepository = mock(Repository.class);
        var recipeIngredientValidator = mock(RecipeIngredientValidator.class);
        var recipeIngredientsCrudService = new RecipeIngredientCrudService(recipeIngredientRepository,
                recipeIngredientValidator, guidProvider);

        genericImportService = new GenericImportService(
                recipeCrudService,
                unitCrudService,
                ingredientCrudService,
                recipeIngredientsCrudService,
                List.of(new BatchJsonImporter())
        );
    }

    @Test
    void importNoRecipes() {
        Path importFilePath = TEST_RESOURCES.resolve("empty.json");
        genericImportService.importData(importFilePath.toString());

        assertThat(recipeCrudService.findAll())
                .isEmpty();
    }

    @Test
    void importOneRecipe() {
        Path importFilePath = TEST_RESOURCES.resolve("single-recipe.json");
        genericImportService.importData(importFilePath.toString());

        assertThat(recipeCrudService.findAll())
                .size().isEqualTo(1);
    }

    @Test
    void invalidNameFails() {
        Path importFilePath = TEST_RESOURCES.resolve("invalid-name-recipe.json");

        var stringPath = importFilePath.toString();
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> genericImportService.importData(stringPath))
                .withMessageContaining("'Recipe name' length is not between 2 (inclusive) and 150 (inclusive)");
    }
}
