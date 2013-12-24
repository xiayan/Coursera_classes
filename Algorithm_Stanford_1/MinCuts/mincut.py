#!/usr/bin/python
import string, copy, math
from random import choice

def makeGraph(inputFile):
    Graph = {}
    for vertex in inputFile:
        list = string.split(vertex)
        # First input is the vertex, following inputs are the neiboring nodes
        Graph[list[0]] = list[1:]
    return Graph

def pickEdge(G):
    # Pick a random edge
    tail = choice(G.keys())
    head = choice(G[tail])
    
    return tail, head

def mergeGraph(G, tail, head):
    tailEdges = G[tail]
    headEdges = G.pop(head)

    # merge edges
    G[tail] = headEdges + tailEdges
    
    # replacing the poped head in all vertices
    valueIter = G.itervalues()
    for value in valueIter:
        while head in value:
            value[value.index(head)] = tail
            
    # remove self-loop
    while tail in G[tail]:
        G[tail].pop(G[tail].index(tail))
    
def ranContraction(G):
    while len(G) > 2:
        tail, head = pickEdge(G)
        mergeGraph(G, tail, head)
    return len(G[tail])
        
def main():
    inputFile = open('kargerAdj.txt', 'r')
    Graph = makeGraph(inputFile)
    
    numberofNodes = len(Graph)
    # Do n**2 * log(n) iterations.  The probablity of failure is 1/n
    numberofIter = int(numberofNodes ** 2 * math.log(numberofNodes))

    print "Total Simulation: ", numberofIter, "times"
    
    minCut = 1e10
    for i in range(numberofIter):
        tempG = copy.deepcopy(Graph)
        cut = ranContraction(tempG)
        if cut < minCut:
            minCut = cut
            
    print "the min cut is: ", str(minCut)

main()