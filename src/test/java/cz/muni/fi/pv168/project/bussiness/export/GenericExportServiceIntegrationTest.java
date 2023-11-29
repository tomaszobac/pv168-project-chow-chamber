package cz.muni.fi.pv168.project.bussiness.export;


import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.model.Unit;
import cz.muni.fi.pv168.project.business.model.UuidGuidProvider;
import cz.muni.fi.pv168.project.business.service.crud.IngredientCrudService;
import cz.muni.fi.pv168.project.business.service.crud.RecipeCrudService;
import cz.muni.fi.pv168.project.business.service.crud.UnitCrudService;
import cz.muni.fi.pv168.project.business.service.import_export.GenericExportService;
import cz.muni.fi.pv168.project.business.service.import_export.format.BatchJsonExporter;
import cz.muni.fi.pv168.project.business.service.validation.IngredientValidator;
import cz.muni.fi.pv168.project.business.service.validation.RecipeValidator;
import cz.muni.fi.pv168.project.business.service.validation.UnitValidator;
import cz.muni.fi.pv168.project.storage.memory.InMemoryRepository;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link GenericExportService}
 */
class GenericExportServiceIntegrationTest {

    private static final Path PROJECT_ROOT = Paths.get(System.getProperty("project.basedir", "")).toAbsolutePath();
    private static final Path TEST_RESOURCES = PROJECT_ROOT.resolve(Path.of("src", "test", "resources"));

    private final Path exportFilePath = TEST_RESOURCES
            .resolve("output")
            .resolve("test_export" + ".json");

    private GenericExportService genericExportService;
    private RecipeCrudService recipeCrudService;

    @BeforeEach
    void setUp() {
        var recipeRepository = new InMemoryRepository<Recipe>(List.of());
        var recipeValidator = new RecipeValidator();
        var uuidGuidProvider = new UuidGuidProvider();
        recipeCrudService = new RecipeCrudService(recipeRepository, recipeValidator, uuidGuidProvider);

        var ingredientRepository = new InMemoryRepository<>(setUpIngredients());
        var ingredientValidator = new IngredientValidator();
        var ingredientCrudService = new IngredientCrudService(ingredientRepository, ingredientValidator,
                uuidGuidProvider);

        var unitRepository = new InMemoryRepository<>(setUpUnits());
        var unitValidator = new UnitValidator();
        var unitCrudService = new UnitCrudService(unitRepository, unitValidator,
                uuidGuidProvider);

        genericExportService = new GenericExportService(
                recipeCrudService,
                unitCrudService,
                ingredientCrudService,
                List.of(new BatchJsonExporter())
        );
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(exportFilePath)) {
            Files.delete(exportFilePath);
        }
    }

    @Test
    void exportEmpty() throws IOException {
        genericExportService.exportData(exportFilePath.toString());

        assertExportedContent("""
                {"recipes":[],
                "units":[{"name":"kilogram","type":"Weight","conversionToBase":"0.001"}],
                "ingredients":[{"name":"Vlašák","calories":"1000000.0","unit":{"name":"kilogram","type":"Weight","conversionToBase":"0.001"}}]}
                """);
    }

    @Test
    void exportSingleRecipe() throws IOException {
        createRecipes(
                setUpKrtkuvDort()
        );
        genericExportService.exportData(exportFilePath.toString());

        assertExportedContent(
                """
                        {"recipes":[{"name":"Krtkův dort","instructions":"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","category":"ZAKUSEK","time":"12:00","portions":"2","ingredients":[{"name":"Vlašák","calories":"1000000.0","unit":{"name":"kilogram","type":"Weight","conversionToBase":"0.001"},"amount":"1.0"}],"numberOfIngredients":"1"}],
                        "units":[{"name":"kilogram","type":"Weight","conversionToBase":"0.001"}],
                        "ingredients":[{"name":"Vlašák","calories":"1000000.0","unit":{"name":"kilogram","type":"Weight","conversionToBase":"0.001"}}]}
                        """);
    }

    @Test
    void exportMultipleRecipes() throws IOException {
        createRecipes(
                setUpKrtkuvDort(),
                setUpChlebaSVlasakem()
        );
        genericExportService.exportData(exportFilePath.toString());

        assertExportedContent(
                """
                        {"recipes":[{"name":"Krtkův dort","instructions":"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","category":"ZAKUSEK","time":"12:00","portions":"2","ingredients":[{"name":"Vlašák","calories":"1000000.0","unit":{"name":"kilogram","type":"Weight","conversionToBase":"0.001"},"amount":"1.0"}],"numberOfIngredients":"1"},{"name":"Chleba s vlašákem","instructions":"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","category":"ZAKUSEK","time":"12:00","portions":"2","ingredients":[{"name":"Vlašák","calories":"1000000.0","unit":{"name":"kilogram","type":"Weight","conversionToBase":"0.001"},"amount":"1.0"}],"numberOfIngredients":"1"}],
                        "units":[{"name":"kilogram","type":"Weight","conversionToBase":"0.001"}],
                        "ingredients":[{"name":"Vlašák","calories":"1000000.0","unit":{"name":"kilogram","type":"Weight","conversionToBase":"0.001"}}]}
                        """);
    }

    private void assertExportedContent(String expectedContent) throws IOException {
        assertThat(Files.readString(exportFilePath))
                .isEqualToIgnoringNewLines(expectedContent);
    }

    private Unit setUpUnit() {
        return new Unit("If I see one more type fail im gonna lose it", "kilogram", UnitType.Weight, 0.001);
    }

    private ArrayList<Unit> setUpUnits() {
        Unit unit = setUpUnit();
        ArrayList<Unit> units = new ArrayList<>();
        units.add(unit);
        return units;
    }

    private Ingredient setUpIngredient() {
        return new Ingredient("afa5", "Vlašák", 1000000, setUpUnit());
    }

    private RecipeIngredient setUpRecipeIngredient() {
        return new RecipeIngredient("Vlašák", 1000000, setUpUnit(), 1);
    }

    private ArrayList<Ingredient> setUpIngredients() {
        Ingredient ingredient = setUpIngredient();
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        return ingredients;
    }

    private ArrayList<RecipeIngredient> setUpRecipeIngredients() {
        RecipeIngredient ingredient = setUpRecipeIngredient();
        ArrayList<RecipeIngredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        return ingredients;
    }

    private void createRecipes(Recipe... employees) {
        for (Recipe recipe : employees) {
            recipeCrudService.create(recipe);
        }
    }

    private Recipe setUpKrtkuvDort() {
        return new Recipe("76c59af3-6e9a-4fcb-bd7e-d0a163ed8b45", "Krtkův dort", RecipeCategory.ZAKUSEK, LocalTime.NOON, 2,
                setUpRecipeIngredients(), "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    }

    private Recipe setUpChlebaSVlasakem() {
        return new Recipe("dc96a827-b56b-4252-bf24-bb8f25209f3e", "Chleba s vlašákem", RecipeCategory.ZAKUSEK, LocalTime.NOON, 2,
                setUpRecipeIngredients(), "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    }
}