package com.t1m0.gene.model;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "allgenes")
public class Gene {

    @Id
    @Column
    private int gene_id;
    @Column
    private int tax_id;
    @Column
    private String symbol;
    @Column
    private String locus_tag;
    @Column(length = 1024)
    private String synonyms;
    @Column(length = 1024)
    private String dbxrefs;
    @Column
    private String chromosome;
    @Column
    private String map_location;
    @Column(length = 2048)
    private String description;
    @Column
    private String type_of_gene;
    @Column
    private String sfna;
    @Column(length = 512)
    private String fnfna;
    @Column
    private String nomenclature_status;
    @Column
    private String other_designations;
    @Column
    private ZonedDateTime mod_date;
    @Column(length = 512)
    private String feature_type;

    public int getGene_id() {
        return gene_id;
    }

    public void setGene_id(int gene_id) {
        this.gene_id = gene_id;
    }

    public int getTax_id() {
        return tax_id;
    }

    public void setTax_id(int tax_id) {
        this.tax_id = tax_id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getLocus_tag() {
        return locus_tag;
    }

    public void setLocus_tag(String locus_tag) {
        this.locus_tag = locus_tag;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public String getDbxrefs() {
        return dbxrefs;
    }

    public void setDbxrefs(String dbxrefs) {
        this.dbxrefs = dbxrefs;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getMap_location() {
        return map_location;
    }

    public void setMap_location(String map_location) {
        this.map_location = map_location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType_of_gene() {
        return type_of_gene;
    }

    public void setType_of_gene(String type_of_gene) {
        this.type_of_gene = type_of_gene;
    }

    public String getSfna() {
        return sfna;
    }

    public void setSfna(String sfna) {
        this.sfna = sfna;
    }

    public String getFnfna() {
        return fnfna;
    }

    public void setFnfna(String fnfna) {
        this.fnfna = fnfna;
    }

    public String getNomenclature_status() {
        return nomenclature_status;
    }

    public void setNomenclature_status(String nomenclature_status) {
        this.nomenclature_status = nomenclature_status;
    }

    public String getOther_designations() {
        return other_designations;
    }

    public void setOther_designations(String other_designations) {
        this.other_designations = other_designations;
    }

    public ZonedDateTime getMod_date() {
        return mod_date;
    }

    public void setMod_date(ZonedDateTime mod_date) {
        this.mod_date = mod_date;
    }

    public String getFeature_type() {
        return feature_type;
    }

    public void setFeature_type(String feature_type) {
        this.feature_type = feature_type;
    }
}
