#!/usr/bin/env node
var levelup = require('levelup'),
    levelgraph = require('levelgraph'),
    readline = require('readline');

var args = process.argv.slice(2);
if (args.length < 1) {
  console.log('usage: levelStreamer database [subject predicate object]');
  return process.exit(1);
}

var db = levelgraph(levelup(args[0]));

var rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});
var counter = 0,
attList = ['subject','predicate','object'],
query = {};
rl.on('line',function(line) {
  query[attList[counter]] = line == '' ? undefined : line;
  counter += 1;
  if(counter < attList.length) return ;
  counter = 0;

  var readStream = db.getStream(query);
  readStream.on('data',function(data){
    console.log(data.subject+' '+data.predicate+' '+data.object+' .');
  });
  readStream.on('end',function() {
    console.log('');
  });
});
