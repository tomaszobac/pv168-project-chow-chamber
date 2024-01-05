/*
package cz.muni.fi.pv168.project.ui.workers;

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
import cz.muni.fi.pv168.project.business.service.import_export.GenericExportService;

import cz.muni.fi.pv168.project.business.service.import_export.GenericExportService;
import cz.muni.fi.pv168.project.business.service.import_export.format.BatchJsonExporter;
import cz.muni.fi.pv168.project.business.service.validation.IngredientValidator;
import cz.muni.fi.pv168.project.business.service.validation.RecipeIngredientValidator;
import cz.muni.fi.pv168.project.business.service.validation.RecipeValidator;
import cz.muni.fi.pv168.project.business.service.validation.UnitValidator;
import cz.muni.fi.pv168.project.storage.sql.IngredientSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.RecipeIngredientSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.RecipeSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.UnitSqlRepository;
import cz.muni.fi.pv168.project.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.project.storage.sql.entity.mapper.EntityMapper;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;
import cz.muni.fi.pv168.project.ui.model.enums.UnitType;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

*/
/**
 * Integration tests for {@link GenericExportService}
 *//*

public class ExporterTest {

    private static final Path PROJECT_ROOT = Paths.get(System.getProperty("project.basedir", "")).toAbsolutePath();
    private static final Path TEST_RESOURCES = PROJECT_ROOT.resolve(Path.of("src", "test", "resources"));

    private final Path exportFilePath = TEST_RESOURCES
            .resolve("test_export" + ".json");

    private GenericExportService genericExportService;
    private RecipeCrudService recipeCrudService;

    @BeforeEach
    void setUp() {
        var recipeRepository = new RecipeSqlRepository(mock(DataAccessObject.class), mock(EntityMapper.class));
        var recipeValidator = new RecipeValidator();
        var uuidGuidProvider = new UuidGuidProvider();
        recipeCrudService = new RecipeCrudService(recipeRepository, recipeValidator, uuidGuidProvider);

        var ingredientRepository = new IngredientSqlRepository(mock(DataAccessObject.class), mock(EntityMapper.class));
        var ingredientValidator = new IngredientValidator();
        var ingredientCrudService = new IngredientCrudService(ingredientRepository, ingredientValidator,
                uuidGuidProvider);

        var unitRepository = new UnitSqlRepository(mock(DataAccessObject.class), mock(EntityMapper.class));
        var unitValidator = new UnitValidator();
        var unitCrudService = new UnitCrudService(unitRepository, unitValidator,
                uuidGuidProvider);

        var recipeIngredientRepository = new RecipeIngredientSqlRepository(mock(DataAccessObject.class), mock(EntityMapper.class));
        var recipeIngredientValidator = new RecipeIngredientValidator();
        var recipeIngredientCrudService = new RecipeIngredientCrudService(recipeIngredientRepository, recipeIngredientValidator,
                uuidGuidProvider);

        genericExportService = new GenericExportService(
                recipeCrudService,
                unitCrudService,
                ingredientCrudService,
                recipeIngredientCrudService,
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
    public void exportEmpty() throws IOException {
        genericExportService.exportData(exportFilePath.toString());

        assertExportedContent("""
                {"recipes":[],
                "units":[{"guid":"If I see one more type fail im gonna lose it","name":"kilogram","type":"Weight","conversionToBase":"0.001"}],
                "ingredients":[{"guid":"afa5","name":"Vlašák","calories":"1000000.0","unit":{"name":"kilogram","type":"Weight","conversionToBase":"0.001"}}],
                "recipeIngredients":[{"recipeGuid":"Vlašák","ingredientGuid":"1000000","unit":{"guid":"If I see one more type fail im gonna lose it","name":"kilogram","type":"Weight","conversionToBase":"0.001"},"amount":"100.0"}]}
                """);
    }

    @Test
    public void exportSingleRecipe() throws IOException {
        createRecipes(
                setUpKrtkuvDort()
        );
        genericExportService.exportData(exportFilePath.toString());

        assertExportedContent(
                """
                        {"recipes":[{"guid":"76c59af3-6e9a-4fcb-bd7e-d0a163ed8b45","name":"Krtkův dort","instructions":"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","category":"ZAKUSEK","time":"12:00","portions":"2"}],
                        "units":[{"guid":"If I see one more type fail im gonna lose it","name":"kilogram","type":"Weight","conversionToBase":"0.001"}],
                        "ingredients":[{"guid":"afa5","name":"Vlašák","calories":"1000000.0","unit":{"name":"kilogram","type":"Weight","conversionToBase":"0.001"}}],
                        "recipeIngredients":[{"recipeGuid":"Vlašák","ingredientGuid":"1000000","unit":{"guid":"If I see one more type fail im gonna lose it","name":"kilogram","type":"Weight","conversionToBase":"0.001"},"amount":"100.0"}]}
                        """);
    }

    @Test
    public void exportMultipleRecipes() throws IOException {
        createRecipes(
                setUpKrtkuvDort(),
                setUpChlebaSVlasakem()
        );
        genericExportService.exportData(exportFilePath.toString());

        assertExportedContent(
                """
                        {"recipes":[{"guid":"76c59af3-6e9a-4fcb-bd7e-d0a163ed8b45","name":"Krtkův dort","instructions":"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","category":"ZAKUSEK","time":"12:00","portions":"2"},{"guid":"dc96a827-b56b-4252-bf24-bb8f25209f3e","name":"Chleba s vlašákem","instructions":"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","category":"ZAKUSEK","time":"12:00","portions":"2"}],
                        "units":[{"guid":"If I see one more type fail im gonna lose it","name":"kilogram","type":"Weight","conversionToBase":"0.001"}],
                        "ingredients":[{"guid":"afa5","name":"Vlašák","calories":"1000000.0","unit":{"name":"kilogram","type":"Weight","conversionToBase":"0.001"}}],
                        "recipeIngredients":[{"recipeGuid":"Vlašák","ingredientGuid":"1000000","unit":{"guid":"If I see one more type fail im gonna lose it","name":"kilogram","type":"Weight","conversionToBase":"0.001"},"amount":"100.0"}]}
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
        return new RecipeIngredient("guid13344", setUpChlebaSVlasakem(), mock(Ingredient.class), setUpUnit(), 100);
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
        return new Recipe("76c59af3-6e9a-4fcb-bd7e-d0a163ed8b45", "Krtkův dort", RecipeCategory.ZAKUSEK, LocalTime.NOON, 2, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    }

    private Recipe setUpChlebaSVlasakem() {
        return new Recipe("dc96a827-b56b-4252-bf24-bb8f25209f3e", "Chleba s vlašákem", RecipeCategory.ZAKUSEK, LocalTime.NOON, 2, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    }
}*/
