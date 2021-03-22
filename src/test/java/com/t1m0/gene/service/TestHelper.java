package com.t1m0.gene.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;

public class TestHelper {

    public static final String ARCHIVE_FILE = "test_data.gz";
    public static final String TMP_ARCHIVE_FILE = "test_data.gz";

    private static final String EXISTING_FILE = "src/test/resources/test_data.gz";
    private static final String NOT_EXISTING_FILE = "src/test/resources/not_there.gz";
    private static final String NOT_GZIP_FILE = "src/test/resources/test_data.tsv";
    public static final String OUTPUT_FILE = "test_data.tsv";

    private TestHelper() {
    }

    public static void copyTestArchive() throws URISyntaxException, IOException {
        File original = new File(GzipExtractor.class.getClassLoader().getResource(ARCHIVE_FILE).toURI());
        File copied = new File(TMP_ARCHIVE_FILE);
        FileUtils.copyFile(original, copied);
    }

    public static boolean deleteFile(String fileName) {
        return new File(fileName).delete();
    }
}
