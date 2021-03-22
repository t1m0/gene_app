package com.t1m0.gene.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.BadRequestException;

import com.t1m0.gene.model.Gene;
import com.t1m0.gene.repo.GeneRepo;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class GeneLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneLoader.class);

    private final GeneLoaderInfo geneLoaderInfo = new GeneLoaderInfo();
    private final ReentrantLock reentrantLock = new ReentrantLock();

    private final FileDownloader fileDownloader;
    private final GzipExtractor gzipExtractor;
    private final GeneRepo geneRepo;
    private final GeneMapper geneMapper;
    private final String tmpFileName;
    private final int batchSize;

    @Inject
    public GeneLoader(FileDownloader fileDownloader,
                      GzipExtractor gzipExtractor,
                      GeneRepo geneRepo,
                      GeneMapper geneMapper,
                      @ConfigProperty(name = "com.t1m0.gene.gene.tmpFile") final String tmpFileName,
                      @ConfigProperty(name = "com.t1m0.gene.gene.batchSize") final int batchSize) {
        this.fileDownloader = fileDownloader;
        this.gzipExtractor = gzipExtractor;
        this.geneRepo = geneRepo;
        this.geneMapper = geneMapper;
        this.tmpFileName = tmpFileName;
        this.batchSize = batchSize;
    }

    public void loadGenes(boolean downloadNew) {
        if (!reentrantLock.isLocked()) {
            new Thread(() -> {
                startLoad();
                if (downloadNew) {
                    fileDownloader.download();
                    gzipExtractor.extract();
                }
                truncateTable();
                processFile();
                stopLoad();
            }).start();
        } else {
            throw new BadRequestException("There is already a load running!");
        }
    }

    public synchronized GeneLoaderInfo getGeneLoaderInfo() {
        return this.geneLoaderInfo;
    }

    public boolean isLoadRunning() {
        return this.reentrantLock.isLocked();
    }

    private void startLoad() {
        reentrantLock.lock();
        LOGGER.info("Starting gene load.");
        getGeneLoaderInfo().start();
    }

    private void truncateTable() {
        LOGGER.info("Removing existing gene entries!");
        int removedGenes = geneRepo.truncate();
        LOGGER.info("Removed {} gene entries", removedGenes);
    }

    private void processFile() {
        File file = new File(tmpFileName);
        Stream<String> lineStream = null;
        try {
            if (!file.exists()) {
                throw new FileNotFoundException("Temp file not found for data loda!");
            }
            LOGGER.info("Start loading gene information from '{}' into the database", tmpFileName);
            List<Gene> geneBatch = new ArrayList<>(batchSize);
            lineStream = Files.lines(file.toPath());
            lineStream.skip(1)
                    .map(l -> l.split("\t"))
                    .forEach(values -> loadGene(values, geneBatch));
            if (!geneBatch.isEmpty()) {
                persistBatch(geneBatch);
            }
            LOGGER.info("Finished loading gene information to database.");
        } catch (IOException e) {
            LOGGER.error("Failed to load genes!", e);
        } finally {
            if (lineStream != null) {
                lineStream.close();
            }
        }
    }

    private void loadGene(String[] values, List<Gene> geneBatch) {
        Gene gene = geneMapper.map(values);
        if (gene != null) {
            geneBatch.add(gene);
        } else {
            geneLoaderInfo.incrementFailed();
        }
        if (geneBatch.size() >= batchSize) {
            persistBatch(geneBatch);
        }
    }

    private void persistBatch(List<Gene> geneBatch) {
        try {
            long start = System.currentTimeMillis();
            geneRepo.saveAll(geneBatch);
            long duration = System.currentTimeMillis() - start;
            LOGGER.info("Inserted {} genese in {}ms", batchSize, duration);
            geneLoaderInfo.setSuccessfulGenes(geneLoaderInfo.getSuccessfulGenes() + geneBatch.size());
        } catch (Exception e) {
            LOGGER.error("Failed to save batch!", e);
            geneLoaderInfo.setFailedGenes(geneLoaderInfo.getFailedGenes() + geneBatch.size());
        }
        geneBatch.clear();
    }

    private void stopLoad() {
        getGeneLoaderInfo().setCurrentlyRunning(false);
        getGeneLoaderInfo().setLoadFinished(ZonedDateTime.now());
        Duration duration = Duration.between(getGeneLoaderInfo().getLoadStart(), getGeneLoaderInfo().getLoadFinished());
        LOGGER.info("Finished gene load in {} minutes.", (duration.getSeconds() / 60));
        reentrantLock.unlock();
    }

    public class GeneLoaderInfo {
        private boolean currentlyRunning = false;
        private int successfulGenes = 0;
        private int failedGenes = 0;
        private ZonedDateTime loadStart;
        private ZonedDateTime loadFinished;

        public boolean isCurrentlyRunning() {
            return currentlyRunning;
        }

        public void setCurrentlyRunning(boolean currentlyRunning) {
            this.currentlyRunning = currentlyRunning;
        }

        public int getSuccessfulGenes() {
            return successfulGenes;
        }

        public void setSuccessfulGenes(int successfulGenes) {
            this.successfulGenes = successfulGenes;
        }

        public int getFailedGenes() {
            return failedGenes;
        }

        public void setFailedGenes(int failedGenes) {
            this.failedGenes = failedGenes;
        }

        public void incrementSuccessful() {
            this.successfulGenes++;
        }

        public void incrementFailed() {
            this.failedGenes++;
        }

        public void start() {
            this.currentlyRunning = true;
            this.successfulGenes = 0;
            this.failedGenes = 0;
            this.loadStart = ZonedDateTime.now();
            this.loadFinished = null;
        }

        public ZonedDateTime getLoadStart() {
            return loadStart;
        }

        public void setLoadStart(ZonedDateTime loadStart) {
            this.loadStart = loadStart;
        }

        public ZonedDateTime getLoadFinished() {
            return loadFinished;
        }

        public void setLoadFinished(ZonedDateTime loadFinished) {
            this.loadFinished = loadFinished;
        }
    }
}
