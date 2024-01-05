package cz.muni.fi.pv168.project.util;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cz.muni.fi.pv168.project.business.service.import_export.format.Format;

public class FilterTest {
    private Filter jsonFilter;

    @Before
    public void setUp() {
        jsonFilter = new Filter(new Format("JSON", List.of("json")));
    }

    @Test
    public void testDecorateFilename() {
        String filenameWithoutExtension = "data";
        String filenameWithExtension = "data.json";

        assertEquals("data.json", jsonFilter.decorate(filenameWithoutExtension));
        assertEquals(filenameWithExtension, jsonFilter.decorate(filenameWithExtension));
    }

    @Test
    public void testAcceptFilesAndDirectories() {
        File directory = mock(File.class);
        when(directory.isDirectory()).thenReturn(true);

        File jsonFile = mock(File.class);
        when(jsonFile.isDirectory()).thenReturn(false);
        when(jsonFile.getName()).thenReturn("data.json");

        File txtFile = mock(File.class);
        when(txtFile.isDirectory()).thenReturn(false);
        when(txtFile.getName()).thenReturn("data.txt");

        assertTrue(jsonFilter.accept(directory));
        assertTrue(jsonFilter.accept(jsonFile));
        assertFalse(jsonFilter.accept(txtFile));
    }

    @Test
    public void testGetDescription() {
        String expectedJsonDescription = "JSON (json.*)";
        assertEquals(expectedJsonDescription, jsonFilter.getDescription());
    }
}

