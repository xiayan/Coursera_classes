//
//  edge.h
//  prim
//
//  Created by Yan Xia on 12/18/12.
//  Copyright (c) 2012 Yan Xia. All rights reserved.
//

#ifndef __prim__edge__
#define __prim__edge__

#include <iostream>
class node;

class edge {
    friend class yxGraph;
    node *a_end;
    node *b_end;
    int weight;
    
public:
    edge(node *a = 0, node *b = 0, int w = 0) : a_end(a), b_end(b), weight(w) {}
    node * other(node *n) { return (n == a_end) ? b_end : a_end; }
    void displayEdge() const { std::cout << weight << std::endl; }
};

#endif /* defined(__prim__edge__) */
