package cz.muni.fi.pv168.project.bussiness.export;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.model.UuidGuidProvider;
import cz.muni.fi.pv168.project.business.service.crud.IngredientCrudService;
import cz.muni.fi.pv168.project.business.service.crud.RecipeCrudService;
import cz.muni.fi.pv168.project.business.service.crud.RecipeIngredientCrudService;
import cz.muni.fi.pv168.project.business.service.crud.UnitCrudService;
import cz.muni.fi.pv168.project.business.service.import_export.GenericImportService;
import cz.muni.fi.pv168.project.business.service.import_export.format.BatchJsonImporter;
import cz.muni.fi.pv168.project.business.service.validation.IngredientValidator;
import cz.muni.fi.pv168.project.business.service.validation.RecipeIngredientValidator;
import cz.muni.fi.pv168.project.business.service.validation.RecipeValidator;
import cz.muni.fi.pv168.project.business.service.validation.UnitValidator;
import cz.muni.fi.pv168.project.business.service.validation.ValidationException;
import cz.muni.fi.pv168.project.storage.memory.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Integration tests for the {@link GenericImportService}
 */
class GenericImportServiceIntegrationTest {
    private static final Path PROJECT_ROOT = Paths.get(System.getProperty("project.basedir", "")).toAbsolutePath();
    private static final Path TEST_RESOURCES = PROJECT_ROOT.resolve(Path.of("src", "test", "resources"));

    private GenericImportService genericImportService;
    private RecipeCrudService recipeCrudService;

    @BeforeEach
    void setUp() {
        var recipeRepository = new InMemoryRepository<Recipe>(List.of());
        var recipeValidator = new RecipeValidator();
        var uuidGuidProvider = new UuidGuidProvider();
        recipeCrudService = new RecipeCrudService(recipeRepository, recipeValidator, uuidGuidProvider);

        var ingredientRepository = new InMemoryRepository<Ingredient>(List.of());
        var ingredientValidator = new IngredientValidator();
        var ingredientCrudService = new IngredientCrudService(ingredientRepository, ingredientValidator,
                uuidGuidProvider);

        var unitRepository = new InMemoryRepository<Unit>(List.of());
        var unitValidator = new UnitValidator();
        var unitCrudService = new UnitCrudService(unitRepository, unitValidator,
                uuidGuidProvider);

        var recipeIngredientRepository = new InMemoryRepository<RecipeIngredient>(List.of());
        var recipeIngredientValidator = new RecipeIngredientValidator();
        var recipeIngredientsCrudService = new RecipeIngredientCrudService(recipeIngredientRepository,
                recipeIngredientValidator, uuidGuidProvider);

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
    void invalidNameFails() {
        Path importFilePath = TEST_RESOURCES.resolve("invalid-name-recipe.json");

        var stringPath = importFilePath.toString();
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> genericImportService.importData(stringPath))
                .withMessageContaining("'Recipe name' length is not between 2 (inclusive) and 150 (inclusive)");
    }
}