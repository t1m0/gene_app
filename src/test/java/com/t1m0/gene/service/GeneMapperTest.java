package com.t1m0.gene.service;

import com.t1m0.gene.model.Gene;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class GeneMapperTest {

    private static final String GOOD_LINE = "7\t5692769\tNEWENTRY\t-\t-\t-\t-\t-\tRecord to support submission of GeneRIFs for a gene not in Gene (Azotirhizobium caulinodans.  Use when strain, subtype, isolate, etc. is unspecified, or when different from all specified ones in Gene.).\tother\t-\t-\t-\t-\t20201128\t-";
    private static final String SHORT_LINE = "7\t5692769\tNEWENTRY\t-\t-\t-\t-\t-other\t-\t-\t-\t-\t20201128\t-";
    private static final String NOT_NUMBER_LINE = "NOT_A_NUMBER\t5692769\tNEWENTRY\t-\t-\t-\t-\t-\t-\tother\t-\t-\t-\t-\t20201128\t-";

    private final GeneMapper geneMapper = new GeneMapper();

    @Test
    public void testSuccessfulMapping() {
        Gene gene = mapLine(GOOD_LINE);
        assertNotNull(gene);
    }

    @Test
    public void testNotEnoughValues() {
        assertNull(mapLine(SHORT_LINE));
    }

    @Test
    public void testNumberFormatException() {
        assertNull(mapLine(NOT_NUMBER_LINE));
    }

    private Gene mapLine(String line) {
        String[] values = line.split("\t");
        return geneMapper.map(values);
    }
}