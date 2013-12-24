//
//  node_heap.h
//  prim
//
//  Created by Yan Xia on 12/19/12.
//  Copyright (c) 2012 Yan Xia. All rights reserved.
//

#ifndef __prim__node_heap__
#define __prim__node_heap__

#include <iostream>
#include "node.h"

class node_heap {
private:
    node **heap;
    int capacity;
    int number;
    int parent(int);
    int leftchild(int);
    int rightchild(int);
    node * parentNode(node *);
    node * largerChild(node *);
    void exch(node *, node *);
    void swim(node *);
    void sink(node *);
public:
    node_heap(int);
    ~node_heap();
    void push(node *);
    void update(node *);
    node * top();
    void pop();
};

#endif /* defined(__prim__node_heap__) */