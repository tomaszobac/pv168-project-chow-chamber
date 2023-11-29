package cz.muni.fi.pv168.project.bussiness.export;


import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.model.UuidGuidProvider;
import cz.muni.fi.pv168.project.business.service.crud.EntityAlreadyExistsException;
import cz.muni.fi.pv168.project.business.service.crud.IngredientCrudService;
import cz.muni.fi.pv168.project.business.service.crud.RecipeCrudService;
import cz.muni.fi.pv168.project.business.service.crud.UnitCrudService;
import cz.muni.fi.pv168.project.business.service.import_export.GenericImportService;
import cz.muni.fi.pv168.project.business.service.import_export.format.BatchJsonImporter;
import cz.muni.fi.pv168.project.business.service.validation.IngredientValidator;
import cz.muni.fi.pv168.project.business.service.validation.RecipeValidator;
import cz.muni.fi.pv168.project.business.service.validation.UnitValidator;
import cz.muni.fi.pv168.project.business.service.validation.ValidationException;
import cz.muni.fi.pv168.project.storage.memory.InMemoryRepository;
import cz.muni.fi.pv168.project.ui.model.IngredientTableModel;
import cz.muni.fi.pv168.project.ui.model.RecipeTableModel;
import cz.muni.fi.pv168.project.ui.model.UnitTableModel;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThat;


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

        RecipeTableModel recipeTableModel = new RecipeTableModel(recipeCrudService);
        IngredientTableModel ingredientTableModel = new IngredientTableModel(ingredientCrudService);
        UnitTableModel unitTableModel = new UnitTableModel(unitCrudService);
        genericImportService = new GenericImportService(
                recipeTableModel,
                unitTableModel,
                ingredientTableModel,
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
    void singleRecipe() {
        Path importFilePath = TEST_RESOURCES.resolve("single-recipe.json");
        genericImportService.importData(importFilePath.toString());

        // We need to specify the 'usingRecursiveFieldByFieldElementComparator' to actually compare
        // all fields of the Employee and all fields of the Department. If we don't specify this comparator,
        // comparison is done by using the 'equals' method. The 'equals' method compares only the 'guid' fields.

        assertThat(recipeCrudService.findAll())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        new Recipe( "76c59af3-6e9a-4fcb-bd7e-d0a163ed8b45", "Krtkův dort", RecipeCategory.ZAKUSEK, LocalTime.NOON, 2,
                                new ArrayList<>(),"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
                );
    }

    @Test
    void multipleRecipes() {
        Path importFilePath = TEST_RESOURCES.resolve("multiple-recipes.json");
        genericImportService.importData(importFilePath.toString());

        ArrayList<RecipeIngredient> ingredients = new ArrayList<>();
        ingredients.add(new RecipeIngredient("Vlašák", 1000000, new Unit("If I see one more type fail im gonna lose it", "kilogram", UnitType.Weight, 0.001), 1));

        assertThat(recipeCrudService.findAll())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        new Recipe( "Krtkův dort","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", RecipeCategory.ZAKUSEK, LocalTime.NOON, 2,
                                ingredients, 1),
                        new Recipe( "Chleba s vlašákem","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", RecipeCategory.ZAKUSEK, LocalTime.NOON, 2,
                                ingredients, 1)
                );
    }

    @Test
    void invalidNameFails() {
        Path importFilePath = TEST_RESOURCES.resolve("invalid-name-recipe.json");

        var stringPath = importFilePath.toString();
        // we want only single invocation in the 'isThrownBy' method so that we don't accidentally catch
        // unwanted exceptions
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> genericImportService.importData(stringPath))
                .withMessageContaining("'Recipe name' length is not between 2 (inclusive) and 150 (inclusive)");
    }

    @Test
    void duplicateGuidFails() {
        Path importFilePath = TEST_RESOURCES.resolve("duplicate-guid-recipes.json");

        var stringPath = importFilePath.toString();
        // we want only single invocation in the 'isThrownBy' method so that we don't accidentally catch
        // unwanted exceptions
        assertThatExceptionOfType(EntityAlreadyExistsException.class)
                .isThrownBy(() -> genericImportService.importData(stringPath))
                .withMessage("Recipe with given guid already exists: 76c59af3-6e9a-4fcb-bd7e-d0a163ed8b45");
    }
}