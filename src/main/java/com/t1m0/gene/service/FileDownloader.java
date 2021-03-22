package com.t1m0.gene.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class FileDownloader {

    private static final int BUFFER_SIZE = 8192;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloader.class);

    private final String url;
    private final String downloadFile;

    @Inject
    public FileDownloader(@ConfigProperty(name = "com.t1m0.gene.gene.source") final String url, @ConfigProperty(name = "com.t1m0.gene.gene.downloadFile") final String downloadFile) {
        this.url = url;
        this.downloadFile = downloadFile;
    }

    public void download() {
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            LOGGER.info("Starting download of '{}'", url);
            URL url = new URL(this.url);
            URLConnection conn = url.openConnection();
            inputStream = conn.getInputStream();
            outputStream = new FileOutputStream(downloadFile);
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                LOGGER.debug("Still downloading ...");
            }
            outputStream.close();
            inputStream.close();
            LOGGER.info("Finished download.");
        } catch (IOException e) {
            LOGGER.error("Failed to download!", e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("Failed to close connection!", e);
            }
        }
    }


}
