package com.t1m0.gene.service;

import java.io.File;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class FileDownloaderIT {

    private static final String URL = "ftp://ftp.ncbi.nlm.nih.gov/gene/DATA/GENE_INFO/All_Data.gene_info.gz";
    private static final String ARCHIVE_FILE = "All_Data.gene_info.gz";

    @Test
    @Disabled
    public void testDataLoad() {
        FileDownloader fileDownloader = new FileDownloader(URL, ARCHIVE_FILE);
        fileDownloader.download();
        File file = new File(ARCHIVE_FILE);
        assertTrue(file.exists());
        TestHelper.deleteFile("All_Data.gene_info.gz");
    }


}