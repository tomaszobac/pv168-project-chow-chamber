package cz.muni.fi.pv168.project.business.service.import_export.format;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
            return objectMapper.readValue(new File(filePath), Batch.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
