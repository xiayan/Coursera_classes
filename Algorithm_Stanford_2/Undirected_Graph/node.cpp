//
//  node.cpp
//  prim
//
//  Created by Yan Xia on 12/18/12.
//  Copyright (c) 2012 Yan Xia. All rights reserved.
//

#include "node.h"

void node::addEdge(edge &e) {
    edges.push_back(&e);
}

void node::displayEdge() {
    vector<edge *>::iterator iter;
    for (iter = edges.begin(); iter != edges.end(); iter++)
        (*iter) -> displayEdge();
    
    std::cout << std::endl;
}
