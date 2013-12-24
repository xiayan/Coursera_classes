//
//  node.cpp
//  prim
//
//  Created by Yan Xia on 12/18/12.
//  Copyright (c) 2012 Yan Xia. All rights reserved.
//

#include "node.h"

//void node::add_in_edge(di_edge &e) {
//    in_edges.push_back(&e);
//}

void node::add_out_edge(di_edge &e) {
    out_edges.push_back(e);
}

void node::displayEdge() {
    vector<di_edge>::iterator iter;
    for (iter = out_edges.begin(); iter != out_edges.end(); iter++)
        iter -> displayEdge();
    
    std::cout << std::endl;
}
