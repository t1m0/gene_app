package com.t1m0.gene.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class GzipExtractor {

    private static final Logger LOGGER = LoggerFactory.getLogger(GzipExtractor.class);

    private final String downloadFileName;
    private final String tmpFileName;

    @Inject
    public GzipExtractor(@ConfigProperty(name = "com.t1m0.gene.gene.downloadFile") final String downloadFileName, @ConfigProperty(name = "com.t1m0.gene.gene.tmpFile") final String tmpFileName) {
        this.downloadFileName = downloadFileName;
        this.tmpFileName = tmpFileName;
    }

    public void extract() {
        byte[] buffer = new byte[1024];
        FileInputStream fileIn = null;
        GZIPInputStream gZIPInputStream = null;
        FileOutputStream fileOutputStream = null;
        File downloadFile = new File(downloadFileName);
        try {
            LOGGER.info("Starting decompression of '{}'", downloadFileName);
            fileIn = new FileInputStream(downloadFile);
            gZIPInputStream = new GZIPInputStream(fileIn);
            fileOutputStream = new FileOutputStream(tmpFileName);
            int bytes_read;
            while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bytes_read);
            }
            gZIPInputStream.close();
            fileOutputStream.close();
            LOGGER.info("Finished decompression.");
            LOGGER.info("Deleting downloaded file '{}'", downloadFile);
            downloadFile.delete();
        } catch (IOException e) {
            LOGGER.error("Failed to decompress!", e);
        } finally {
            try {
                if (fileIn != null) {
                    fileIn.close();
                }
                if (gZIPInputStream != null) {
                    gZIPInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("Failed to close stream!", e);
            }
        }
    }
}
