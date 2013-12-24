//
//  node.h
//  prim
//
//  Created by Yan Xia on 12/18/12.
//  Copyright (c) 2012 Yan Xia. All rights reserved.
//

#ifndef NODE_H_
#define NODE_H_

#include <iostream>
#include <vector>
#include "di_edge.h"

using std::vector;
using std::string;

class node {
private:
    friend class di_graph;
    string name;
    int score;
    int dij_score;
    int index;
    bool visited;
    //vector<di_edge *> in_edges;
    vector<di_edge> out_edges;
    node *pre;

public:
    node() : score(999999999), dij_score(999999999), visited(false), index(0), pre(0) {}
    //void add_in_edge(di_edge &);
    void add_out_edge(di_edge &);
    void displayEdge();
    void set_score(int s) { score = s; }
    int  get_score()      { return score; }
    void set_index(int i) { index = i; }
    int  get_index()      { return index; }
    bool operator<(node const &rhs) const { return (dij_score < rhs.dij_score); }
};

#endif /* defined(__prim__node__) */
