#!/usr/bin/python
from string import join
from time import clock

def main():
	t1 = clock()
	filename = "HashInt.txt"
	inputfile = open(filename, 'r')
	numberDict = dict()
	for line in inputfile:
		number = eval(line)
		if not (number in numberDict):
			numberDict[number] = None
	inputfile.close()

	resultList = [None] * 9
	testfile = open("test.txt", 'r')
	index = 0
	for line in  testfile:
		dest = eval(line)
		for number in numberDict.keys():
			if (dest - number) in numberDict:
				resultList[index] = '1'
				break
		if resultList[index] == None:
			resultList[index] = '0'
		index = index + 1
	print join(resultList, "")
	testfile.close()
	print clock() - t1
main()