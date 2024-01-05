package cz.muni.fi.pv168.project.ui.workers;

import cz.muni.fi.pv168.project.business.service.import_export.GenericExportService;
import cz.muni.fi.pv168.project.business.service.import_export.format.Format;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AsyncExporterTest {
    @Mock
    private GenericExportService mockExportService;
    private AsyncExporter asyncExporter;
    private Runnable onFinishRunnable;

    @Before
    public void setUp() {
        onFinishRunnable = mock(Runnable.class);
        mockExportService = mock(GenericExportService.class);
        asyncExporter = new AsyncExporter(mockExportService, onFinishRunnable);
    }

    @Test
    public void testGetFormats() {
        Collection<Format> expectedFormats = List.of(
                new Format("JSON", List.of("json")));
        when(mockExportService.getFormats()).thenReturn(expectedFormats);

        Collection<Format> actualFormats = asyncExporter.getFormats();

        assertEquals(expectedFormats, actualFormats);
        verify(mockExportService).getFormats();
    }

    @Test
    public void testExportProcess() {
        ArgumentCaptor<String> filePathCaptor = ArgumentCaptor.forClass(String.class);

        doNothing().when(mockExportService).exportData(filePathCaptor.capture());
        asyncExporter.exportData("testPath");

        verify(mockExportService, timeout(10000)).exportData(anyString());
        assertEquals("testPath", filePathCaptor.getValue());
    }

    @Test
    public void testProgressUpdates() {
        AtomicInteger simulatedProgress = new AtomicInteger(0);
        doAnswer(invocation -> {
            simulatedProgress.set(50);
            return null;
        }).when(mockExportService).exportData(anyString());

        asyncExporter.exportData("testPath");

        timeout(10000);

        assertEquals(simulatedProgress.get(), asyncExporter.getProgressBar().getValue());
    }
}