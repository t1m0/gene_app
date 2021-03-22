package com.t1m0.gene.service;

import javax.inject.Inject;

import com.t1m0.gene.repo.GeneRepo;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@QuarkusTest
public class GeneLoaderIT extends AbstractQuarkusTest {

    private static final FileDownloader fileDownloader = mock(FileDownloader.class);
    private static final GzipExtractor gzipExtractor = mock(GzipExtractor.class);

    @Inject
    private GeneLoader geneLoader;

    @Inject
    private GeneRepo geneRepo;

    @BeforeAll
    public static void setup() {
        QuarkusMock.installMockForType(fileDownloader, FileDownloader.class);
        QuarkusMock.installMockForType(gzipExtractor, GzipExtractor.class);
    }

    @Test
    public void testGeneLoad() throws InterruptedException {
        geneLoader.loadGenes(false);
        awaitLoadFinish(geneLoader, 50);
        assertEquals(6L, geneRepo.count());
    }
}
