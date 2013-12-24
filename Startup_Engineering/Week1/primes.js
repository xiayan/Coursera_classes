#!/usr/bin/env node

var fs = require('fs');
var numbers = 600;
var marked = [];

for (var i = 0; i < numbers+1; i++)
    marked[i] = true;

for (var j = 2; j < Math.ceil(Math.sqrt(numbers))+1; j++) {
    if (!marked[j]) continue;
    for (var k = j*j; k < numbers+1; k+=j)
        marked[k] = false;
}

result = []
for (var i = 2; i < numbers+1; i++) {
    if (marked[i] == true)
        result.push(i);
}

var line = result.slice(0, 100).join(',') + '\n';
var outfile = "primes.txt";
fs.writeFileSync(outfile, line);

