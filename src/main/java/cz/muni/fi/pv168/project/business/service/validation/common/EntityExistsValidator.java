package cz.muni.fi.pv168.project.business.service.validation.common;

import cz.muni.fi.pv168.project.business.model.Entity;
import cz.muni.fi.pv168.project.business.service.validation.ValidationResult;

import java.util.Objects;

public class EntityExistsValidator extends PropertyValidator<Entity> {

    public EntityExistsValidator(String name) {
        super(name);
    }

    @Override
    public ValidationResult validate(Entity model) {
        var result = new ValidationResult();

        if (Objects.isNull(model)) {
            result.add("%s is null".formatted(getName()));
        }

        return result;
    }
}

