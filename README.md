## HDT Iris
Iris is a utility that consolidates an HDT file with some removed and aded triples in levelGraph databases
into a single HDT file.
It requires a configuration file with locations to the following executable programs:
* levelStreamer - A specially coded streaming script to query levelgraph databases.
* rdf2hdt - The main utility converting RDF triples into an HDT file.
* hdt2rdf - The utility to convert the source HDT file into RDF triples that are passed to rdf2hdt.

If any of the paths for these programs are improperly set, the program will fail to run.

It is recommended to test the installation of this program manually before jumping in to use it along with the
`LiveHdtDatasource` in the LinkedDataFragments/Server.js.

#### Submodules, and building
We use the repository Java-BloomFilter as a submodule in this project. We also have a small Node.js script to retrieve
information from the auxiliary datasets. To be able to compile the consolidator, you should install the
Node.js dependencies:
```
npm install
```
Also, you should download, and build the submodules.
```
git submodule init
git submodule update
cd Java-BloomFilter
ant
cd ../
```
And finally, you'll be able to compile and run the hdt consolidator.
```
make
```
#### Usage
Once the program has been built, you must add the `rdf2hdt`, `hdt2rdf`, and `levelStreamer` properties to the configuration
file in the directory `conf/`. If these are all ready, you can run the consolidator as follows:
```
./bin/consolidate.sh addedTriples removedTriples oldHdt newHdt
```
Where `addedTriples`, and `removedTriples` are levelgraph databases, `oldHdt` is the original, existing HDT file; and
`newHdt` is the hdt file that will be created.
#### Common problems
Some implementations of `rdf2hdt` have shown to work incorrectly. Specifically, not 'noticing' when a named pipe is closed,
therefore waiting, hanging until the named pipe is `touch`-ed manually.
