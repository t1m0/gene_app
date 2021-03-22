package com.t1m0.gene.service;

import javax.inject.Inject;

import com.t1m0.gene.model.Gene;
import com.t1m0.gene.repo.GeneRepo;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;


@QuarkusTest
class GeneLoaderEndpointTest extends AbstractQuarkusTest {

    private static final FileDownloader fileDownloader = mock(FileDownloader.class);
    private static final GzipExtractor gzipExtractor = mock(GzipExtractor.class);
    private static final GeneRepo geneRepo = mock(GeneRepo.class);
    private static final GeneMapper geneMapper = mock(GeneMapper.class);

    @Inject
    private GeneLoader geneLoader;

    @BeforeAll
    public static void setup() {
        QuarkusMock.installMockForType(fileDownloader, FileDownloader.class);
        QuarkusMock.installMockForType(gzipExtractor, GzipExtractor.class);
        QuarkusMock.installMockForType(geneRepo, GeneRepo.class);
        QuarkusMock.installMockForType(geneMapper, GeneMapper.class);
        when(geneMapper.map(any())).thenReturn(new Gene());
    }

    @BeforeEach
    public void beforeEach() {
        reset(geneRepo);
    }


    @Test
    public void testGeneLoad() throws InterruptedException {
        given()
                .when().put(GeneLoaderEndpoint.ENDPOINT)
                .then()
                .statusCode(204);
        awaitLoadFinish(geneLoader, 20);
        verify(geneRepo, times(1)).saveAll(any());
    }

    @Test
    public void testParallelExecution() throws InterruptedException {
        doAnswer(invocationOnMock -> {
            Thread.sleep(500);
            return null;
        }).when(fileDownloader).download();
        given()
                .when().put(GeneLoaderEndpoint.ENDPOINT)
                .then()
                .statusCode(204);
        given()
                .when().put(GeneLoaderEndpoint.ENDPOINT)
                .then()
                .statusCode(400);
        awaitLoadFinish(geneLoader, 20);
        verify(geneRepo, times(1)).saveAll(any());
    }

}