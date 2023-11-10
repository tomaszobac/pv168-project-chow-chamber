package cz.muni.fi.pv168.project.business.service.export.format;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class BatchJsonImporter {
    private static final Format FORMAT = new Format("JSON", List.of("json"));

    @Override
    public Format getFormat() {
        return FORMAT;
    }

    @Override
    public Batch importBatch(String filePath) {
        var departments = new HashMap<String, Department>();

        try(var reader = Files.newBufferedReader(Path.of(filePath))) {
            var employees = reader.lines()
                    .map(line -> parseEmployee(departments, line))
                    .toList();

            return new Batch(departments.values(), employees);
        } catch (IOException e) {
            throw new DataManipulationException("Unable to read file", e);
        }
    }

    private Employee parseEmployee(HashMap<String, Department> departments, String line) {
        var fields = line.split(SEPARATOR);
        var department = parseDepartment(departments, fields[5]);

        return new Employee(
                fields[0],
                fields[1],
                fields[2],
                Gender.valueOf(fields[3]),
                LocalDate.parse(fields[4]),
                department
        );
    }

    private Department parseDepartment(HashMap<String, Department> departments, String departmentInfo) {
        String[] fields = departmentInfo.split(DEPARTMENT_SEPARATOR);
        return departments.computeIfAbsent(fields[0], num -> new Department(fields[0], fields[2], fields[1]));
    }
}
