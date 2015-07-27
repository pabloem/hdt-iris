#!/usr/bin/env node
var levelup = require('levelup'),
    levelgraph = require('levelgraph'),
    readline = require('readline'),
    N3 = require('n3');

var args = process.argv.slice(2);
if (args.length < 1) {
  console.log('usage: levelStreamer database');
  return process.exit(1);
}

var db = levelgraph(levelup(args[0]));

var rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

var parser = N3.Parser(/*{format: 'N-Triples'}*/);
var writer = N3.Writer({format: 'N-Triples'});
var counter = 0,
query = {};
rl.on('line',function(line) {
  if(line == '') {
    // Do something about this
  }
  //console.log(line);
  parser.parse(line,function(e,t,pre) {
    //console.log(t);
    if(t == null && line != '') return;
    query.subject = (!t || t.subject == '?') ? undefined : t.subject;
    query.predicate = (!t || t.predicate == '?') ? undefined : t.predicate;
    query.object = (!t || t.object == '?') ? undefined : t.object;

    //console.log(query);
    var readStream = db.getStream(query);
    readStream.on('data',function(data){
      writer.addTriple(data);
      //console.log(data.subject+' '+data.predicate+' '+data.object+' .');
    });
    readStream.on('end',function() {
      writer.end(function(e,r){console.log(r);});
      writer = N3.Writer({format: 'N-Triples'});
      //console.log('');
    });
  });
});
