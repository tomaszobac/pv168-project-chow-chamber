package cz.muni.fi.pv168.project.business.service.validation;

import cz.muni.fi.pv168.project.business.model.Ingredient;
import cz.muni.fi.pv168.project.business.service.validation.common.StringLengthValidator;

import java.util.List;

import static cz.muni.fi.pv168.project.business.service.validation.Validator.extracting;

/**
 * Ingredient entity {@link Validator}.
 */
public class IngredientValidator implements Validator<Ingredient> {

    @Override
    public ValidationResult validate(Ingredient model) {
        var validators = List.of(
                extracting(Ingredient::getName, new StringLengthValidator(2, 150, "Ingredient name"))
        );

        return Validator.compose(validators).validate(model);
    }
}
