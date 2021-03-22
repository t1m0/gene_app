package com.t1m0.gene.service;

import static org.junit.jupiter.api.Assertions.fail;

public class AbstractQuarkusTest {
    protected void awaitLoadFinish(GeneLoader geneLoader, int maxWaits) throws InterruptedException {
        int waitCount = 0;
        do {
            Thread.sleep(5000);
            if (!geneLoader.isLoadRunning()) {
                break;
            }
            waitCount++;
        } while (waitCount < maxWaits);
        if (waitCount >= maxWaits) {
            fail("Load didn't finish in time");
        }
    }
}
