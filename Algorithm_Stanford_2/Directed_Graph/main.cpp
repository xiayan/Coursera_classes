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
#include "di_graph.h"

using namespace boost;
using namespace std;

int main(int argc, const char * argv[])
{
    std::clock_t t = clock();
    const char *filename = "g3.txt";
    std::ifstream input(filename);
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

    di_graph graph(numberN, numberE);

    while (!input.eof()) {
        getline(input, line);
        split(strs, line, is_any_of(" "), token_compress_on);
        int a = atoi(strs[0].c_str()) - 1;
        int b = atoi(strs[1].c_str()) - 1;
        int w = atoi(strs[2].c_str());
        graph.addEdge(a, b, w);
    }

    // Test Dijkstra's shortest path
    //for (int i = 1; i < numberN + 1; i++) {
    //    for (int j = 1; j < numberN + 1; j++) {
    //        int dist = graph.sp_shortest_path(i, j);
    //        cout << i << " -> " << j << ": " << dist << endl;
    //    }
    //    cout << endl;
    //}

    int **result = new int*[numberN];
    for (int i = 0; i < numberN; i++) {
        result[i] = new int[numberN];
    }

    bool no_circle = graph.johnson(result);

    cout << "no_circle: " << no_circle << endl;

    int min = 999999999;
    if (!no_circle) exit(1);

    for (int i = 0; i < numberN; i++) {
        for (int j = 0; j < numberN; j++) {
            if (result[i][j] < min) min = result[i][j];
        }
    }

    cout << "min: " << min << endl;

    for (int i = 0; i < numberN; i++) {
        delete[] result[i];
    }
    delete[] result;

    t = clock() - t;
    cout << "Total time: " << (float)t / CLOCKS_PER_SEC << endl;
    return 0;
}
