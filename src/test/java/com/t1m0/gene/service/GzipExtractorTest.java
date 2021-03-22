package com.t1m0.gene.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GzipExtractorTest {

    private static final String NOT_EXISTING_FILE = "src/test/resources/not_there.gz";
    private static final String NOT_GZIP_FILE = "src/test/resources/test_data.tsv";
    private static final String OUTPUT_FILE = "test_data.tsv";

    @BeforeEach
    public void beforeEach() throws URISyntaxException, IOException {
        TestHelper.copyTestArchive();
    }

    @Test
    public void testSuccessfulExtraction() {
        File file = extract(TestHelper.TMP_ARCHIVE_FILE);
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testExtractionOfNotExistingFile() {
        File file = extract(NOT_EXISTING_FILE);
        assertFalse(file.exists());
    }

    @Test
    public void testExtractionOfNoneGzipFile() {
        File file = extract(NOT_GZIP_FILE);
        assertFalse(file.exists());
    }

    @AfterEach
    public void afterEach() {
        TestHelper.deleteFile(TestHelper.TMP_ARCHIVE_FILE);
        TestHelper.deleteFile(OUTPUT_FILE);
    }

    private File extract(String archiveFile) {
        GzipExtractor gzipExtractor = new GzipExtractor(archiveFile, OUTPUT_FILE);
        gzipExtractor.extract();
        return new File(OUTPUT_FILE);
    }

}