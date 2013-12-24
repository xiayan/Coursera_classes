//
//  node_heap.cpp
//  prim
//
//  Created by Yan Xia on 12/19/12.
//  Copyright (c) 2012 Yan Xia. All rights reserved.
//

#include "node_heap.h"

int node_heap::parent(int i) {
    int p = (i - 1) / 2;
    return (p >= 0) ? p : -1;
}

int node_heap::leftchild(int i) {
    int l = 2 * i + 1;
    return (l < number) ? l : -1;
}

int node_heap::rightchild(int i) {
    int r = 2 * i + 2;
    return (r < number) ? r : -1;
}

node_heap::node_heap(int c) : capacity(c), number(0) {
    heap = new node *[c];
}

node_heap::~node_heap() {
    delete[] heap;
}

void node_heap::exch(node *a, node *b) {
    int index_a = a->get_index();
    int index_b = b->get_index();
    
    heap[index_a] = b;
    heap[index_b] = a;
    a->set_index(index_b);
    b->set_index(index_a);
}

node * node_heap::parentNode(node * n) {
    int index_n = n->get_index();
    int index_p = parent(index_n);
    if (index_p == -1) return 0;
    return heap[index_p];
}

node * node_heap::largerChild(node * n) {
    int index_n = n->get_index();
    int index_l = leftchild(index_n);
    int index_r = rightchild(index_n);
    if (index_l == -1) return 0;
    if (index_r == -1) return heap[index_l];
    node *l = heap[index_l];
    node *r = heap[index_r];
    node *w = ((*l) < (*r)) ? l : r;
    return w;
}

void node_heap::swim(node *n) {
    node *p = parentNode(n);
    if (p == 0) return;

    if ((*n) < (*p)) {
        exch(n, p);
        swim(n);
    } else {
        return;
    }
}

void node_heap::sink(node *n) {
    node *w = largerChild(n);
    if (w == 0) return;

    if ((*w) < (*n)) {
        exch(n, w);
        sink(n);
    } else {
        return;
    }
}

void node_heap::push(node *n) {
    if (number == capacity) {
        std::cout << "Heap full" << std::endl;
        return;
    }
    n->set_index(number);
    heap[number++] = n;
    swim(n);
}

void node_heap::update(node *n) {
    node *p = parentNode(n);
    node *c = largerChild(n);
    if (p != 0 && (*n) < (*p)) swim(n);
    else if (c != 0 && (*c) < (*n)) sink(n);
    else return;
}

node * node_heap::top() {
    return heap[0];
}

void node_heap::pop() {
    exch(heap[0], heap[--number]);
    sink(heap[0]);
    heap[number] = 0;
}