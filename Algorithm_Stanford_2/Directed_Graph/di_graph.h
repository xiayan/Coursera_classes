//
//  yxGraph.h
//  prim
//
//  Created by Yan Xia on 12/18/12.
//  Copyright (c) 2012 Yan Xia. All rights reserved.
//

#ifndef DI_GRAPH_
#define DI_GRAPH_

#include <iostream>
#include "node.h"
#include "di_edge.h"
#include <boost/heap/fibonacci_heap.hpp>

class node_heap;
class di_graph{
    vector<node> nodes;
    //di_edge *edges;
    //vector<di_edge> edges; // edge is owned by nodes
    //node_heap *nh; 
    int numberN;
    int numberE;
    
public:
    di_graph(int, int);
    ~di_graph();
    void addEdge(int a, int b, int w);
    void remove_start_node();
    void displayGraph();
    int get_E() { return numberE; }
    void reset_graph(bool);
    void dijkstra(int s);
    bool bellman_ford(int s, bool reset_score);
    bool johnson(int **);
    bool floyd_warshall();
    int sp_shortest_path(int s, int d);
    //void primMST();
};

#endif /* defined(__prim__yxGraph__) */
