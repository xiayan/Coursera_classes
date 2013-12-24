#!/usr/bin/python
"""
Node = [number: ['edge list index', explored, finishing(keys to the new list), leader]
"""
import sys
sys.setrecursionlimit(1000000)

t = 0
s = 0

class MyNode:
	def __init__(self, index, explored = False, finish = -1, leader = -1):
		self.index = index
		self.explored = explored
		self.finish = finish
		self.leader = leader

class MyEdge:
	def __init__(self):
		self.head = None
	def configEdge(self, nodes, h):
		self.head = nodes[h]

class MyGraph:
	def __init__(self, nodeList = [], edgeList = []):
		self.nodeList = nodeList
		self.edgeList = edgeList


def DFS(graph, sourceNode):
	sourceNode.explored = True
	global s
	sourceNode.leader = s

	#print "graph", graph.edgeList[sourceNode.index]
	if (graph.edgeList[sourceNode.index] != None):
		for theEdge in graph.edgeList[sourceNode.index]:
		#	print theEdge.head.index
			if theEdge.head.explored == False:
				#print theEdge.head.index
				DFS(graph, theEdge.head)

	global t
	t = t + 1
	sourceNode.finish = t
	print "finish: ", sourceNode.finish

def DFS_Loop(graph):
	global t 	# for finishing times in the 1st pass. # of nodes processed so far
	global s 	# for leaders in 2nd pass. current source vertex
	length = len(graph.nodeList)
	for i in range(length - 1, -1, -1):
		#print "Loop", graph.nodeList[i].index, "i: ", i
		aNode =graph.nodeList[i]
		if aNode != None:
			if aNode.explored == False:
				s = i
				#print aNode.index, graph.nodeList[i].finish
				DFS(graph, aNode)


def Make_Edges(inputFile, nodes, non, reverse, edgeList):
	for line in inputFile:
		edge = line.split()
		first = eval(edge[0])
		second = eval(edge[1])
		if reverse:
			newEdge = MyEdge()
			newEdge.configEdge(nodes, first)
			if not edgeList[second]:
				edgeList[second] = [newEdge]
			else:
				edgeList[second].append(newEdge)
		else:
			newEdge = MyEdge()
			newEdge.configEdge(nodes, second)
			if not edgeList[first]:
				edgeList[first] = [newEdge]
			else:
				edgeList[first].append(newEdge)
	#print "EdgeList", edgeList
	#return edgeList

def Update_NodeList(old_list):
	length = len(old_list)
	newList = [None] * length
	for node in old_list:
		if node != None:
			node.explored = False
			node.leader = 0
			newList[node.finish] = node
	#print "newList:", newList[-20:]
	del old_list
	return newList

def Output_Result(nodeList):
	resultDict = dict()
	for node in nodeList:
		if node != None:
			if node.leader in resultDict:
				resultDict[node.leader] = resultDict[node.leader] + 1
			else:
				resultDict[node.leader] = 1
	print sorted(resultDict.values())[-5:]


def main():
	filename = 'graph2n100scc93.txt'
	inputFile = open(filename, 'r')
	aSet = set()
	for line in inputFile:
		entry = line.split()
		aSet.add(eval(entry[0]))
		aSet.add(eval(entry[1]))

	numberOfNodes = max(aSet)
	inputFile.close()

	# make node list
	nodeList = [None] * (numberOfNodes + 1)
	for i in aSet:
		newNode = MyNode(i)
		nodeList[i] = newNode
	#print "nodeList", nodeList
	inputFile = open(filename, 'r')
	reverseList = [None] * (numberOfNodes + 1)
	Make_Edges(inputFile, nodeList, numberOfNodes, True, reverseList)


	theGraph = MyGraph(nodeList, reverseList)
	print "Graph Done"
	inputFile.close()

	DFS_Loop(theGraph)
	print "First round done"
	# Make new edgelist first
	inputFile = open(filename, 'r')
	del reverseList
	forwardList = [None] * (numberOfNodes + 1)
	Make_Edges(inputFile, nodeList, numberOfNodes, False, forwardList)
	#print "edgelist", theGraph.edgeList
	# scramble up nodeList
	theGraph.nodeList = Update_NodeList(theGraph.nodeList)

	global t
	t = 0
	global s
	s = 0

	DFS_Loop(theGraph)
	del forwardList
	print "Second round done"
	Output_Result(theGraph.nodeList)

	inputFile.close()

main()
