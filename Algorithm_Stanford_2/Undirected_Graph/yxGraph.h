//
//  yxGraph.h
//  prim
//
//  Created by Yan Xia on 12/18/12.
//  Copyright (c) 2012 Yan Xia. All rights reserved.
//

#ifndef __prim__yxGraph__
#define __prim__yxGraph__

#include <iostream>
#include "node.h"
#include "edge.h"
#include <boost/heap/fibonacci_heap.hpp>

class node_heap;
class yxGraph{
    vector<node> nodes;
    edge *edges;
    node_heap *nh;
    int numberN;
    int numberE;
    
public:
    yxGraph(int, int);
    ~yxGraph();
    void addEdge(int a, int b, int w);
    void displayGraph();
    int get_E() { return numberE; }
    void primMST1();
    void primMST2();
};

#endif /* defined(__prim__yxGraph__) */
