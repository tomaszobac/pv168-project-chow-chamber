package cz.muni.fi.pv168.project.bussiness.crud;

import cz.muni.fi.pv168.project.business.model.GuidProvider;
import cz.muni.fi.pv168.project.business.model.Recipe;
import cz.muni.fi.pv168.project.business.repository.Repository;
import cz.muni.fi.pv168.project.business.service.crud.EntityAlreadyExistsException;
import cz.muni.fi.pv168.project.business.service.crud.RecipeCrudService;
import cz.muni.fi.pv168.project.business.service.validation.RecipeValidator;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.project.ui.model.enums.RecipeCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeCrudServiceUnitTest {
    private RecipeCrudService recipeCrudService;
    private Repository<Recipe> recipeRepository;
    private RecipeValidator recipeValidator;
    private GuidProvider guidProvider;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        recipeRepository = Mockito.mock(Repository.class);
        recipeValidator = Mockito.mock(RecipeValidator.class);
        guidProvider = Mockito.mock(GuidProvider.class);
        recipeCrudService = new RecipeCrudService(recipeRepository, recipeValidator, guidProvider);
    }

    @Test
    void createWithGuidSucceeds() {
        var employee = createRecipeInstance("1");

        when(recipeValidator.validate(employee))
                .thenReturn(ValidationResult.success());

        var result = recipeCrudService.create(employee);

        assertThat(result).isEqualTo(ValidationResult.success());
        verify(recipeRepository, times(1))
                .create(employee);
    }

    @Test
    void createWithoutGuidSucceeds() {
        var recipe = createRecipeInstance(null);
        var newGuid = "15165156165";
        var expectedRecipe = createRecipeInstance(newGuid);

        when(guidProvider.newGuid())
                .thenReturn(newGuid);

        when(recipeValidator.validate(recipe))
                .thenReturn(ValidationResult.success());

        var result = recipeCrudService.create(recipe);

        assertThat(result).isEqualTo(ValidationResult.success());
        verify(recipeRepository, times(1))
                .create(refEq(expectedRecipe));
    }

    @Test
    void createValidationError() {
        var employee = createRecipeInstance("1");

        when(recipeValidator.validate(employee))
                .thenReturn(ValidationResult.failed("validation failed"));

        var result = recipeCrudService.create(employee);

        assertThat(result).isEqualTo(ValidationResult.failed("validation failed"));
        verify(recipeRepository, times(0))
                .create(employee);
    }

    @Test
    void createFailsForDuplicateGuid() {
        var employee = createRecipeInstance("1");

        when(recipeValidator.validate(employee))
                .thenReturn(ValidationResult.success());
        when(recipeRepository.existsByGuid("1"))
                .thenReturn(true);

        assertThatExceptionOfType(EntityAlreadyExistsException.class)
                .isThrownBy(() -> recipeCrudService.create(employee))
                .withMessage("Recipe with given guid already exists: 1");
    }

    @Test
    void updateWithGuidSucceeds() {
        var employee = createRecipeInstance("1");

        when(recipeValidator.validate(employee))
                .thenReturn(ValidationResult.success());

        var result = recipeCrudService.update(employee);

        assertThat(result).isEqualTo(ValidationResult.success());
        verify(recipeRepository, times(1))
                .update(employee);
    }

    @Test
    void updateValidationError() {
        var employee = createRecipeInstance("1");

        when(recipeValidator.validate(employee))
                .thenReturn(ValidationResult.failed("validation failed"));

        var result = recipeCrudService.update(employee);

        assertThat(result).isEqualTo(ValidationResult.failed("validation failed"));
        verify(recipeRepository, times(0))
                .update(employee);
    }

    @Test
    void deleteByGuid() {
        recipeCrudService.deleteByGuid("guid");

        verify(recipeRepository, times(1))
                .deleteByGuid("guid");
    }

    @Test
    void findAll() {
        var expectedEmployeeList = List.of(createRecipeInstance("1"));
        when(recipeRepository.findAll())
                .thenReturn(expectedEmployeeList);

        var foundEmployees = recipeCrudService.findAll();

        assertThat(foundEmployees).isEqualTo(expectedEmployeeList);
    }

    @Test
    void deleteAll() {
        recipeCrudService.deleteAll();

        verify(recipeRepository, times(1))
                .deleteAll();
    }

    private static Recipe createRecipeInstance(String guid) {
        return new Recipe(
                guid,
                "My first recipe",
                RecipeCategory.HLAVNI_JIDLO,
                LocalTime.NOON,
                1,
                "Hello there traveler"
        );
    }
}
