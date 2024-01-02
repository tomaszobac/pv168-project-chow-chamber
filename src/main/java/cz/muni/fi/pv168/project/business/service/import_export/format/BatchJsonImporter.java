package cz.muni.fi.pv168.project.business.service.import_export.format;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.muni.fi.pv168.project.business.service.import_export.batch.Batch;
import cz.muni.fi.pv168.project.business.service.import_export.batch.BatchImporter;

public class BatchJsonImporter implements BatchImporter {
    private static final Format FORMAT = new Format("JSON", List.of("json"));
    @Override
    public Format getFormat() {
        return FORMAT;
    }
    @Override
    public Batch importBatch(String filePath) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            return objectMapper.readValue(new File(filePath), Batch.class);
        } catch (JsonParseException e) {
            // Handle issues related to file reading
            throw new RuntimeException("Error parsing JSON in the file: " + filePath, e);
        } catch (JsonMappingException e) {
            // Handle issues related to JSON parsing
            throw new RuntimeException("Error mapping JSON to Java objects in the file: " + filePath, e);
        } catch (IOException e) {
            // Handle issues related to JSON mapping
            throw new RuntimeException("Error reading the file: " + filePath, e);
        }
    }
}
