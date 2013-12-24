//
//  node.h
//  prim
//
//  Created by Yan Xia on 12/18/12.
//  Copyright (c) 2012 Yan Xia. All rights reserved.
//

#ifndef __prim__node__
#define __prim__node__

#include <iostream>
#include <vector>
#include "edge.h"

using std::vector;
using std::string;

class node {
private:
    friend class yxGraph;
    int score;
    int index;
    bool visited;
    vector<edge *> edges;
public:
    node() : score(99999999), visited(false), index(0) {}
    void addEdge(edge &);
    void displayEdge();
    void set_score(int s) { score = s; }
    int  get_score()      { return score; }
    void set_index(int i) { index = i; }
    int  get_index()      { return index; }
    bool operator<(node const &rhs) const { return (score < rhs.score); }
};

#endif /* defined(__prim__node__) */