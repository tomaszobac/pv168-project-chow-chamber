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
                Validator.extracting(RecipeIngredient::getRecipeGuid, new StringLengthValidator(2, 150, "Recipe guid")),
                Validator.extracting(RecipeIngredient::getIngredientGuid, new StringLengthValidator(2, 150, "Recipe name"))
        );

        return Validator.compose(validators).validate(model);
    }
}