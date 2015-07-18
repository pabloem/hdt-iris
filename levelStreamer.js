#!/usr/bin/env node
var levelup = require('levelup'),
    levelgraph = require('levelgraph');

var args = process.argv.slice(2);
if (args.length < 1) {
    console.log('usage: levelStreamer database [subject predicate object]');
    return process.exit(1);
}

var db = levelgraph(levelup(args[0])),
    subject = (args[1] == '?') ? undefined : args[1],
    predicate = (args[2] == '?') ? undefined: args[2],
    object = (args[3] == '?') ? undefined: args[3];

var readStream = db.getStream({subject:subject, predicate:predicate, object:object});
readStream.on('data',function(data){
    console.log(data.subject+' '+data.predicate+' '+data.object+' .');
});
readStream.on('end',function() {
    db.close();
});
