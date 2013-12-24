#include "UnionFind.h"

UnionFind::UnionFind(int size) {
    nodes = new UnionNode[size];
    for (int i = 0; i < size; i++) {
        UnionNode *current = &nodes[i];
        current->setSize(1);
        current->setRoot(current);
    }
}

UnionFind::~UnionFind() {
    delete[] nodes;
}

UnionNode *UnionFind::findRoot(int a) {
    UnionNode *result = &nodes[a];
    while (result != result->getRoot()) {
        result = result->getRoot();
    }
    return result;
}

bool UnionFind::connected(int a, int b) {
    UnionNode *r1 = findRoot(a);
    UnionNode *r2 = findRoot(b);
    return r1 == r2;
}

void UnionFind::connect(int a, int b) {
    int sizeA = nodes[a].getSize();
    int sizeB = nodes[b].getSize();
    UnionNode *rA = findRoot(a);
    UnionNode *rB = findRoot(b);

    if (sizeA >= sizeB) {
        rB->setRoot(rA);
        rA->setSize(sizeA + sizeB);
    } else {
        rA->setRoot(rB);
        rB->setSize(sizeA + sizeB);
    }
}
