#  Prerequisites for building and running
- Maven
- JDK11
- MQSQL
#  Build
```
mvn clean install
```
#  Run
Launch MYSQL (if it's not already running)
```
mysqld
```
Launch Application
```
mvn quarkus:dev
```
#  Use application
## Trigger gene load
This will perform the following actions: 
- wipe out the gene table in the configured database
- load gene data from ftp://ftp.ncbi.nlm.nih.gov/gene/DATA/GENE_INFO/All_Data.gene_info.gz
- load gene data into the database
```
curl -X PUT http://localhost:8080/geneloader
```
