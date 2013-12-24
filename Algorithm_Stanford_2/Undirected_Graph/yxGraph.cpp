//
//  yxGraph.cpp
//  prim
//
//  Created by Yan Xia on 12/18/12.
//  Copyright (c) 2012 Yan Xia. All rights reserved.
//

#include "yxGraph.h"
#include "node_heap.h"
using std::cout;
using std::endl;

yxGraph::yxGraph(int n, int e) : numberN(n), nodes(n), numberE(0) {
    edges = new edge[e];
    nh = new node_heap(n);
    nodes[0].score = 0;
    nodes[0].visited = true;
    
    for (vector<node>::iterator iter = nodes.begin(); iter != nodes.end(); iter++)
        nh->push(&(*iter));
}

yxGraph::~yxGraph() {
    delete nh;
    delete[] edges;
}

void yxGraph::addEdge(int a, int b, int w) {
    edge newEdge(&nodes[a], &nodes[b], w);
    edges[numberE] = newEdge;
    nodes[a].addEdge(edges[numberE]);
    nodes[b].addEdge(edges[numberE++]);
}

void yxGraph::displayGraph() {
    vector<node>::iterator iter;
    for (iter = nodes.begin(); iter != nodes.end(); iter++)
        iter->displayEdge();
}

void yxGraph::primMST1() {
    int total = 0;
    for (int counter = 1;  counter < numberN; counter++) {
        int min = 99999999;
        node *winner;
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes[i].visited == true) {
                vector<edge *> &es = nodes[i].edges;
                for (int j = 0; j < es.size(); j++) {
                    if (es[j]->other(&nodes[i])->visited == false) {
                        if (es[j]->weight < min) {
                            min = es[j]->weight;
                            winner = es[j]->other(&nodes[i]);
                        }
                    }
                }
            }
        }
        total += min;
        winner->visited = true;
    }
    
    cout << total << endl;
}

void yxGraph::primMST2() {
    // This process iterate exactly number of nodes - 1 rounds
    int totalScore = 0;
    for (int i = 0; i < numberN; i++) {
        node *current = nh->top();
        current->visited = true;
        nh->pop();
        totalScore += current->get_score();
        
        // update the neigbors of local winner
        vector<edge *> &es = current->edges;
        for (int j = 0; j < es.size(); j++) {
            node *head = es[j]->other(current);
            if (head->visited == false) {
                int newScore = std::min(es[j]->weight, head->score);
                head->score = newScore;
                nh->update(head);
            }
        }
    }
    cout << totalScore << endl;
}