## HDT Iris
Iris is a utility that consolidates an HDT file with some removed and aded triples in levelGraph databases
into a single HDT file.
It requires a configuration file with locations to the following executable programs:
* levelStreamer - A specially coded streaming script to query levelgraph databases.
* rdf2hdt - The main utility converting RDF triples into an HDT file.
* hdt2rdf - The utility to convert the source HDT file into RDF triples that are passed to rdf2hdt.

If any of the paths for these programs are improperly set, the program will fail to run.
