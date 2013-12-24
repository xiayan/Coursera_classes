//
//  edge.h
//  prim
//
//  Created by Yan Xia on 12/18/12.
//  Copyright (c) 2012 Yan Xia. All rights reserved.
//

#ifndef DI_EDGE_H_
#define DI_EDGE_H_

#include <iostream>
class node;

class di_edge {
    friend class di_graph;
    node *tail;
    node *head;
    int weight;
    
public:
    di_edge(node *t = 0, node *h = 0, int w = 0) : tail(t), head(h), weight(w) {}
    node * get_tail() const { return tail; }
    node * get_head() const { return head; }
    int get_weight() const { return weight; }
    void displayEdge() const { std::cout << weight << std::endl; }
};

#endif
