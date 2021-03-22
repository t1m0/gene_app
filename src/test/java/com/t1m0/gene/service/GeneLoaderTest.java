package com.t1m0.gene.service;

import java.util.concurrent.CountDownLatch;

import com.t1m0.gene.model.Gene;
import com.t1m0.gene.repo.GeneRepo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class GeneLoaderTest {

    private static final String TEMP_FILE = "./src/test/resources/test_data.tsv";
    private static final String NOT_EXISTING_FILE = "./not_existing.tsv";

    private final FileDownloader fileDownloader = mock(FileDownloader.class);
    private final GzipExtractor gzipExtractor = mock(GzipExtractor.class);
    private final GeneRepo geneRepo = mock(GeneRepo.class);
    private final GeneMapper geneMapper = mock(GeneMapper.class);

    private final GeneLoader geneLoader = new GeneLoader(fileDownloader, gzipExtractor, geneRepo, geneMapper, TEMP_FILE, 20);

    @Test
    public void testSuccessfulLoading() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        when(geneMapper.map(any())).thenReturn(new Gene());
        doAnswer(invocationOnMock -> {
            countDownLatch.countDown();
            return null;
        }).when(geneRepo).saveAll((anyList()));
        geneLoader.loadGenes(true);
        countDownLatch.await();
        Thread.sleep(50);
        assertEquals(6, geneLoader.getGeneLoaderInfo().getSuccessfulGenes());
        assertEquals(0, geneLoader.getGeneLoaderInfo().getFailedGenes());
        assertNotNull(geneLoader.getGeneLoaderInfo().getLoadFinished());
        assertFalse(geneLoader.getGeneLoaderInfo().isCurrentlyRunning());
        assertFalse(geneLoader.isLoadRunning());
        verify(fileDownloader).download();
        verify(gzipExtractor).extract();
        verify(geneMapper, times(6)).map(any());
        verify(geneRepo, times(1)).saveAll(any());
        verify(geneRepo, never()).save(any());
    }

    @Test
    public void testSuccessfulLoadingWithFailedMapping() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        when(geneMapper.map(any())).thenReturn(new Gene(), new Gene(), null, new Gene(), null, new Gene());
        doAnswer(invocationOnMock -> {
            countDownLatch.countDown();
            return null;
        }).when(geneRepo).saveAll((anyList()));
        geneLoader.loadGenes(true);
        countDownLatch.await();
        assertEquals(4, geneLoader.getGeneLoaderInfo().getSuccessfulGenes());
        assertEquals(2, geneLoader.getGeneLoaderInfo().getFailedGenes());
        verify(fileDownloader).download();
        verify(gzipExtractor).extract();
        verify(geneMapper, times(6)).map(any());
        verify(geneRepo, times(1)).saveAll(any());
        verify(geneRepo, never()).save(any());
    }

    @Test
    public void testParallelExecution() {
        when(geneMapper.map(any())).thenReturn(new Gene());
        doAnswer(invocationOnMock -> {
            Thread.sleep(500);
            return null;
        }).when(fileDownloader).download();
        geneLoader.loadGenes(true);
        assertThrows(RuntimeException.class, () -> geneLoader.loadGenes(true));
    }

    @Test
    public void testLoadWithNotExistingFile() throws InterruptedException {
        GeneLoader geneLoader = new GeneLoader(fileDownloader, gzipExtractor, geneRepo, geneMapper, NOT_EXISTING_FILE, 20);
        geneLoader.loadGenes(true);
        // quick sleep to account for async execution
        Thread.sleep(100);
        assertEquals(0, geneLoader.getGeneLoaderInfo().getSuccessfulGenes());
        assertEquals(0, geneLoader.getGeneLoaderInfo().getFailedGenes());
        assertNotNull(geneLoader.getGeneLoaderInfo().getLoadFinished());
        assertFalse(geneLoader.getGeneLoaderInfo().isCurrentlyRunning());
        assertFalse(geneLoader.isLoadRunning());
        verify(fileDownloader).download();
        verify(gzipExtractor).extract();
        verify(geneMapper, never()).map(any());
    }
}