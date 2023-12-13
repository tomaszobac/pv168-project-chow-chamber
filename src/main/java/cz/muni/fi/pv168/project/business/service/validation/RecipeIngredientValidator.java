package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.RecipeIngredient;
import cz.muni.fi.pv168.project.business.service.validation.common.StringLengthValidator;

import java.util.List;

/**
 * RecipeIngredient entity {@link Validator}.
 */
public class RecipeIngredientValidator implements Validator<RecipeIngredient> {

    @Override
    public ValidationResult validate(RecipeIngredient model) {
        var validators = List.of(
                Validator.extracting(recIng -> ((RecipeIngredient) recIng).getRecipe().getGuid(), new StringLengthValidator(2, 150, "Recipe guid")),
                Validator.extracting(recIng -> ((RecipeIngredient) recIng).getIngredient().getGuid(), new StringLengthValidator(2, 150, "Recipe name"))
        );

        return Validator.compose(validators).validate(model);
    }
}