CREATE TABLE IF NOT EXISTS "Recipe"
(
    `id`                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `guid`                  VARCHAR      NOT NULL UNIQUE,
    `name`                  VARCHAR(150) NOT NULL,
    `category`              VARCHAR(150) NOT NULL,
    `time`                  TIME         NOT NULL,
    `portions`              INT          NOT NULL,
    `numberOfIngredients`   INT   NOT NULL,
    `instructions`          LONGTEXT      NOT NULL,
    `createdAt`             TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "Ingredient"
(
    `id`        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `guid`      VARCHAR         NOT NULL UNIQUE,
    `name`      VARCHAR(150)    NOT NULL,
    `calories`  DOUBLE          NOT NULL UNIQUE, //TODO: Shouldn't this be NOT UNIQUE?
    `unit`      OBJECT          NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "Unit"
(
    `id`                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `guid`              VARCHAR         NOT NULL UNIQUE,
    `name`              VARCHAR(150)    NOT NULL,
    `type`              VARCHAR(150)    NOT NULL UNIQUE,
    `conversionToBase`  DOUBLE          NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "RecipeIngredient"
(
    `id`                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `guid`              VARCHAR         NOT NULL UNIQUE,
    `recipeGuid`        VARCHAR         NOT NULL,
    `ingredientGuid`    VARCHAR         NOT NULL,
    `unit`              OBJECT          NOT NULL,
    `amount`            DOUBLE          NOT NULL
)
