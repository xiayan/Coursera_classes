#!/usr/bin/python
import sys, heapq

class MyNode:
	def __init__(self):
		self.key = 10e9
		self.name = None
	def __lt__(self, other):
		return self.key < other.key

def makeDict(nodeDict, heapList, resultDict, source, inputfile, numberOfNodes):
	for i in range(0, numberOfNodes + 1):
		newNode = MyNode()
		newNode.name = "Node" + str(i)
		resultDict[newNode] = 10e9
		if i == source:
			newNode.key = 0
		heapList[i] = newNode

	for line in inputfile:
		info = line.split()
		number1, number2, number3 = eval(info[0]), eval(info[1]), eval(info[2])
		if not heapList[number1] in nodeDict:
			nodeDict[heapList[number1]] = [(heapList[number2], number3)]
		else:
			nodeDict[heapList[number1]].append((heapList[number2], number3))
	heapq.heapify(heapList)

def dijkstra(nodeDict, heapList, resultDict):
	while heapList:
		theNode = heapq.heappop(heapList)
		if theNode.key != None:
			resultDict[theNode] = theNode.key
			if theNode in nodeDict:
				for (headNode, weight) in nodeDict[theNode]:
					if headNode in heapList:
						headNode.key = min(headNode.key, resultDict[theNode] + weight)
						heapq.heapify(heapList)

def main():
	filename = "test1.txt"
	numberOfNodes = 4
	source = eval(sys.argv[1])
	inputfile = open(filename, 'r')
	nodeDict = dict()
	resultDict = dict()
	heapList = [None] * (numberOfNodes + 1)
	makeDict(nodeDict, heapList, resultDict, source, inputfile, numberOfNodes)

	dijkstra(nodeDict, heapList, resultDict)

	for (key, value) in resultDict.items():
		if key.name != "Node0" and key.name != "Node" + str(source):
			print key.name, value

main()
