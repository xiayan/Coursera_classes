#ifndef UNIONNODE_H_
#define UNIONNODE_H_

class UnionNode {
private:
    int size;
    UnionNode *root;
public:
    UnionNode() { }
    ~UnionNode() { }
    int getSize() { return size; }
    void setSize(int s) { size = s; }
    UnionNode *getRoot() { return root; }
    void setRoot(UnionNode *r) { root = r; }
};

#endif
