#!/usr/bin/python

def main():
	filename = 'stat.txt'
	inputFile = open(filename, 'r')

	resultDict = dict()
	for node in inputFile:
		number = eval(node)
		if number in resultDict:
			resultDict[number] = resultDict[number] + 1
		else:
			resultDict[number] = 1
	print (sorted(resultDict.values())[-5:])

main()
