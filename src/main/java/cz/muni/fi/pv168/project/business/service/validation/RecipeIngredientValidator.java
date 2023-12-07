package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.service.validation.common.EntityExistsValidator;

import java.util.List;

/**
 * RecipeIngredient entity {@link Validator}.
 */
public class RecipeIngredientValidator implements Validator<RecipeIngredient> {

    @Override
    public ValidationResult validate(RecipeIngredient model) {
        var validators = List.of(
                Validator.extracting(RecipeIngredient::getRecipe, new EntityExistsValidator("Recipe")),
                Validator.extracting(RecipeIngredient::getIngredient, new EntityExistsValidator("Ingredient"))
        );

        return Validator.compose(validators).validate(model);
    }
}