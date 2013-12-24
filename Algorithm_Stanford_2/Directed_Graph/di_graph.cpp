//
//  di_graph.cpp
//  prim
//
//  Created by Yan Xia on 12/18/12.
//  Copyright (c) 2012 Yan Xia. All rights reserved.
//

#include "di_graph.h"
#include "node_heap.h"
#include <boost/lexical_cast.hpp>

using std::cout;
using std::endl;

di_graph::di_graph(int n, int e) : numberN(n), nodes(n + 1), numberE(0) {
    //edges = new di_edge[e];
    for (int i =0; i < nodes.size(); i++)
        nodes[i].name = boost::lexical_cast<string>(i);
}

di_graph::~di_graph() {
    //delete nh;
    //delete[] edges;
}

void di_graph::addEdge(int a, int b, int w) {
    di_edge new_edge(&nodes[a], &nodes[b], w);
    //edges[numberE] = new_edge;
    //edges.push_back(new_edge);
    //nodes[a].add_out_edge(edges[numberE++]);
    nodes[a].add_out_edge(new_edge);
    //nodes[b].add_in_edge(edges[numberE++]);
}

void di_graph::remove_start_node() {
    
}

void di_graph::displayGraph() {
    vector<node>::iterator iter;
    for (iter = nodes.begin(); iter != nodes.end(); iter++)
        iter->displayEdge();
}

//void di_graph::primMST() {
//    // This process iterate exactly number of nodes - 1 rounds
//    int totalScore = 0;
//    for (int i = 0; i < numberN; i++) {
//        node *current = nh->top();
//        current->visited = true;
//        nh->pop();
//        totalScore += current->get_score();
//        
//        // update the neigbors of local winner
//        vector<edge *> &es = current->edges;
//        for (int j = 0; j < es.size(); j++) {
//            node *head = es[j]->other(current);
//            if (head->visited == false) {
//                int newScore = std::min(es[j]->weight, head->score);
//                head->score = newScore;
//                nh->update(head);
//            }
//        }
//    }
//    cout << totalScore << endl;
//}

void di_graph::reset_graph(bool reset_score) {
    register int max = 999999999;
    for (vector<node>::iterator it = nodes.begin(); it != nodes.end(); it++) {
        if (reset_score) it->set_score(max);
        it->dij_score = max;
        it->visited = false;
        it->pre = 0;
    }
}

void di_graph::dijkstra(int s) {
    node_heap *nh = new node_heap(numberN);
    
    reset_graph(false);
    nodes[s].dij_score = 0;
    nodes[s].visited = true;
    
    for (int i = 0; i < nodes.size() - 1; i++) {
        nh->push(&nodes[i]);
    }

    // This process iterate exactly number of nodes - 1 rounds
    for (int i = 0; i < numberN; i++) {
        node *current = nh->top();
        current->visited = true;
        nh->pop();
        int current_score = current->dij_score;
        
        // update the neigbors of local winner
        vector<di_edge> &es = current->out_edges;
        for (int j = 0; j < es.size(); j++) {
            node *head = es[j].get_head();
            if (head->visited == false) {
                head->dij_score = std::min(es[j].weight + current_score, head->dij_score);
                nh->update(head);
            }
        }
    }

    delete nh;
}

bool di_graph::bellman_ford(int s, bool reset_score) {
    reset_graph(reset_score);
    nodes[s].score = 0;

    for (int k = 0; k < nodes.size() - 1; k++) {
        for (int i = 0; i < nodes.size(); i++) {
            vector<di_edge> &es = nodes[i].out_edges;
            for (int j = 0; j < es.size(); j++) {
                node *t = es[j].get_tail();
                node *h = es[j].get_head();
                if (t->score + es[j].weight < h->score) {
                    h->score = t->score + es[j].weight;
                    h->pre = t;
                }
            }
        }
    }

    for (int i = 0; i < nodes.size(); i++) {
        vector<di_edge> &es = nodes[i].out_edges;
        for (int j = 0; j < es.size(); j++) {
            node *t = es[j].get_tail();
            node *h = es[j].get_head();
            if (t->score + es[j].weight < h->score) {
                cout << "Graph contains a negative-weigth cycle";
                return false;
            }
        }
    }
    return true;
}

bool di_graph::johnson(int **result) {
    // add source node
    for (int i = 0; i < nodes.size() - 1; i++) {
        di_edge new_edge(&nodes[i], &nodes.back(), 0);
        nodes.back().add_out_edge(new_edge);
    }
    
    bool no_circle = bellman_ford(numberN, false);
    if (!no_circle) return false;

    for (int i = 0; i < nodes.size() - 1; i++) {
        vector<di_edge> &es = nodes[i].out_edges;
        for (int j = 0; j < es.size(); j++) {
            node *t = es[j].get_tail();
            node *h = es[j].get_head();
            es[j].weight += (t->score - h->score);
        }
    }

    for (int i = 0; i < nodes.size() - 1; i++) {
        dijkstra(i);
        for (int j = 0; j < nodes.size() - 1; j++) {
            if (nodes[j].dij_score != 999999999)
                result[i][j] = nodes[j].dij_score - nodes[i].score + nodes[j].score;
            else
                result[i][j] = 999999999;
        }
    }

    return true;
}

bool di_graph::floyd_warshall() {
    return true;
}

int di_graph::sp_shortest_path(int s, int d) {
    if (s == d) return 0;
    bellman_ford(s - 1, true);
    //dijkstra(s - 1);
    return nodes[d - 1].score;
}
