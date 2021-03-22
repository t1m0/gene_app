package com.t1m0.gene.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

@Path(GeneLoaderEndpoint.ENDPOINT)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GeneLoaderEndpoint {

    public static final String ENDPOINT = "/geneloader";

    private final GeneLoader geneLoader;

    public GeneLoaderEndpoint(GeneLoader geneLoader) {
        this.geneLoader = geneLoader;
    }

    @PUT
    public void loadGenes(@QueryParam("downloadNew") @DefaultValue("true") boolean downloadNew) {
        try {
            geneLoader.loadGenes(downloadNew);
        } catch (WebApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException(e);
        }
    }

    @GET
    public GeneLoader.GeneLoaderInfo getInfo() {
        return geneLoader.getGeneLoaderInfo();
    }
}
