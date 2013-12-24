// The maximum spacing of a 4 clustering is 106

#include <iostream>
#include <fstream>
#include <vector>
#include <boost/algorithm/string.hpp>
#include <time.h>
#include "UnionFind.h"

using namespace std;
using namespace boost;

struct UnionEdge {
    int aEnd;
    int bEnd;
    int weight;
};

bool compareEdge(const UnionEdge &e1, const UnionEdge &e2) {
    return e1.weight < e2.weight;
}

int main(int argc, char *argv[]) {
    clock_t t = clock();

    int clusters = 4;
    char filename[] = "clustering1.txt";
    ifstream input(filename);
    if (!input.good()) cout << "Cannot open file" << endl;

    vector<UnionEdge> edges;
    vector<string> strs;
    string line;
    int numOfNodes;
    getline(input, line);
    numOfNodes = atoi(line.c_str());
    while (!input.eof()) {
        getline(input, line);
        split(strs, line, boost::is_any_of(" "), boost::token_compress_on);
        unsigned int size = strs.size();
        if (size != 3) continue;
        else {
            UnionEdge newEdge;
            newEdge.aEnd = atoi(strs[0].c_str()) - 1;
            newEdge.bEnd = atoi(strs[1].c_str()) - 1;
            newEdge.weight = atoi(strs[2].c_str());
            edges.push_back(newEdge);
        }
    }
    input.close();

    sort(edges.begin(), edges.end(), compareEdge);
    
    UnionFind uf(numOfNodes);
    int l = 0;
    int merges = 0;
    for (; l < edges.size(); l++) {
        int a = edges[l].aEnd;
        int b = edges[l].bEnd;
        if (!uf.connected(a, b)) {
            uf.connect(a, b);
            merges++;
        }
        if (merges == numOfNodes - clusters) break;
    }
    
    for (; l < edges.size(); l++) {
        int a = edges[l].aEnd;
        int b = edges[l].bEnd;
        if (!uf.connected(a, b))
            break;
    }
    
    cout << "L is: " << l << endl;

    cout << "The maximum spacing of a " << clusters << " clustering is " << edges[l].weight << endl;

    edges.clear();

    t = clock() - t;
    cout << (float) t / CLOCKS_PER_SEC << " seconds" << endl;
    return 0;
}
