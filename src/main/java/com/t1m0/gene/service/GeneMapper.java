package com.t1m0.gene.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.enterprise.context.ApplicationScoped;

import com.t1m0.gene.model.Gene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class GeneMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneMapper.class);

    public Gene map(String[] values) {
        if (values == null || values.length < 16) {
            return null;
        }
        try {
            Gene gene = new Gene();
            gene.setTax_id(loadInt(values, 0));
            gene.setGene_id(loadInt(values, 1));
            gene.setSymbol(values[2]);
            gene.setLocus_tag(values[3]);
            gene.setSynonyms(values[4]);
            gene.setDbxrefs(values[5]);
            gene.setChromosome(values[6]);
            gene.setMap_location(values[7]);
            gene.setDescription(values[8]);
            gene.setType_of_gene(values[9]);
            gene.setNomenclature_status(values[12]);
            gene.setOther_designations(values[13]);
            gene.setMod_date(loadTimeStamp(values, 14));
            gene.setFeature_type(values[15]);
            return gene;
        } catch (NumberFormatException e) {
            LOGGER.error("Failed to format number, line will be ignored!", e);
            return null;
        }
    }

    private int loadInt(String[] values, int index) {
        return Integer.parseInt(values[index]);
    }

    private ZonedDateTime loadTimeStamp(String[] values, int index) {
        long l = Long.parseLong(values[index]);
        Instant instant = Instant.ofEpochMilli(l);
        return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
