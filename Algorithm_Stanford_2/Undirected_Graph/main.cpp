//
//  main.cpp
//  prim
//
//  Created by Yan Xia on 12/18/12.
//  Copyright (c) 2012 Yan Xia. All rights reserved.
//

#include <iostream>
#include <fstream>
#include <boost/algorithm/string.hpp>
#include <time.h>
#include "yxGraph.h"

using namespace boost;
using namespace std;

int main(int argc, const char * argv[])
{
    std::clock_t t = clock();
    
    std::ifstream input("edges.txt");
    if (!input.good()) {
        std::cout << "Can't open file" << std::endl;
        exit(1);
    }
    
    std::string line;
    vector<string> strs;
    getline(input, line);
    split(strs, line, is_any_of(" "), token_compress_on);
    int numberN = atoi(strs[0].c_str());
    int numberE = atoi(strs[1].c_str());
    
    yxGraph graph(numberN, numberE);
    
    while (!input.eof()) {
        getline(input, line);
        split(strs, line, is_any_of(" "), token_compress_on);
        int a = atoi(strs[0].c_str()) - 1;
        int b = atoi(strs[1].c_str()) - 1;
        int w = atoi(strs[2].c_str());
        graph.addEdge(a, b, w);
    }
    
    graph.primMST2();
    
    t = clock() - t;
    cout << "Total time: " << (float)t / CLOCKS_PER_SEC << endl;
    return 0;
}
