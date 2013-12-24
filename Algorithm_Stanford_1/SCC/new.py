#!/usr/bin/python
"""
implement adjacency list as dictionary. Representing nodes as key, edges as value list
Node: 875714
Answer: 434821,968,459,313,211
"""

import sys, thread, threading, time
sys.setrecursionlimit(100000)
thread.stack_size(2**27)

t = 0
s = 0
filename = "graph12n850000scc779032.txt"
numberOfNodes = 850000

class MyNode(object):
	def __init__(self):
		self.explored = False
		self.finish = 0
		self.leader = 0
		self.index = 0

def DFS(aNode, edgeDict):
	global s
	aNode.explored = True
	aNode.leader = s
	if aNode in edgeDict:
		for neighbor in edgeDict[aNode]:
			if neighbor.explored == False:
				DFS(neighbor, edgeDict)

	global t
	t = t + 1
	aNode.finish = t


def DFS_Loop(nodeList, reverseDict):
	global s
	global numberOfNodes
	for i in range(numberOfNodes, 0, -1):
		aNode = nodeList[i]
		if (aNode != None) and (aNode.explored == False):
			s = i
			DFS(aNode, reverseDict)

def Output_Result(nodeList):
	resultDict = dict()
	for node in nodeList:
		if node != None:
			if node.leader in resultDict:
				resultDict[node.leader] = resultDict[node.leader] + 1
			else:
				resultDict[node.leader] = 1
	theList = sorted(resultDict.values())[-5:]
	theList.reverse()
	answer = ""
	numofSCC = len(theList)
	for i in range(5):
		if i == 0:
			answer = answer + str(theList[i])
		elif i > 0 and i < numofSCC:
			answer = answer + "," + str(theList[i])
		else:
			answer = answer + ",0"
	print answer


def main():
	begin = time.clock()
	global numberOfNodes
	global filename

	nodeList = [None] * (numberOfNodes + 1)
	for i in range(numberOfNodes + 1):
		newNode = MyNode()
		newNode.index = i
		nodeList[i] = newNode

	print "start making adjacency list"
	forwardDict = dict()
	reverseDict = dict()
	inputFile = open(filename, 'r')
	for line in inputFile:
		anEdge = line.split()
		first = eval(anEdge[0])
		second = eval(anEdge[1])
		headNode = nodeList[first]
		tailNode = nodeList[second]
		if not (headNode in forwardDict):
			forwardDict[headNode] = [tailNode]
		else:
			forwardDict[headNode].append(tailNode)
		if not (tailNode in reverseDict):
			reverseDict[tailNode] = [headNode]
		else:
			reverseDict[tailNode].append(headNode)
	inputFile.close()
	print "Done making adjacency list"

	print "First pass started"
	t1 = threading.Thread(target = DFS_Loop, args = (nodeList, reverseDict))
	t1.start()      # start the scc thread
	t1.join()       # and wait for it to finish
	print "First pass finished"

	global t
	global s
	t = 0
	s = 0
	newList = [None] * (numberOfNodes + 1)
	print "making new list"
	for node in nodeList:
		node.explored = False
		node.leader = 0
		newIndex = node.finish
		newList[newIndex] = node
	print "done making new list"

	print "Second pass started"
	t2 = threading.Thread(target = DFS_Loop, args = (newList, forwardDict))
	t2.start()      # start the scc thread
	t2.join()       # and wait for it to finish
	print "Second pass finished"

	Output_Result(newList)
	print "Total time: ", time.clock() - begin

main()
