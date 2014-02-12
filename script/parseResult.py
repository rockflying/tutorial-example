#! /usr/bin/python

import sys

dir=sys.argv[1]
filename=sys.argv[2];

path = dir + "/" + filename;

f = open(path);
lines = f.readlines();
f.close;

new_filename = "step1_" + filename;
new_file = open(dir + "/" + new_filename, 'w');


for line in lines:
	line = line.strip();
	if line.startswith("Found a flow to sink"):
		new_file.write(line);
	elif line.startswith("-"):
		new_file.write(line);
	elif line.startswith("on Path"):
		new_file.write(line);
	else:
		print line;
	new_file.write("\n");

new_file.close();
