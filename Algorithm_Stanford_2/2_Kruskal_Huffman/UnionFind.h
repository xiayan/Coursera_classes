#ifndef UNIONFIND_H_
#define UNIONFIND_H_

#include "UnionNode.h"

class UnionFind {
private:
    UnionNode *nodes;

public:
    UnionFind(int size);
    ~UnionFind();
    UnionNode *findRoot(int a);
    bool connected(int a, int b);
    void connect(int a, int b);
};

#endif
