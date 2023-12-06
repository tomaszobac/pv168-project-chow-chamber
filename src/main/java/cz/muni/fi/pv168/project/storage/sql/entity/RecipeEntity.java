package cz.muni.fi.pv168.project.storage.sql.entity;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Representation of Recipe entity in a SQL database.
 */
public record RecipeEntity(
        Long id,
        String guid,
        long departmentId,
        String firstName,
        String lastName,
        Gender gender,
        LocalDate birthDate) {
    public RecipeEntity(
            Long id,
            String guid,
            long departmentId,
            String firstName,
            String lastName,
            Gender gender,
            LocalDate birthDate) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.departmentId = departmentId;
        this.firstName = Objects.requireNonNull(firstName, "firstName must not be null");
        this.lastName = Objects.requireNonNull(lastName, "lastName must not be null");
        this.gender = Objects.requireNonNull(gender, "gender must not be null");
        this.birthDate = Objects.requireNonNull(birthDate, "birthDate must not be null");
    }

    public RecipeEntity(
            String guid,
            long departmentId,
            String firstName,
            String lastName,
            Gender gender,
            LocalDate birthDate) {
        this(null, guid, departmentId, firstName, lastName, gender, birthDate);
    }
}
