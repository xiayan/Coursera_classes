// Answer: null, null, -19

#include <iostream>
#include <vector>
#include <fstream>
#include <boost/algorithm/string.hpp>
#include <inttypes.h>
#include <limits>

using namespace std;
using namespace boost;

int main(int argc, const char *argv[]) {
    
    const char *filename = argv[1];
    ifstream input(filename);
    if (!input.good()) {
        cout << "Can't open input file" << endl;
        exit(1);
    }

    string line;
    vector<string> strs;
    getline(input, line);
    // get number of nodes and edges
    split(strs, line, is_any_of(" "), token_compress_on);
    int num_node = atoi(strs[0].c_str());
    
    const int dimension = num_node;
    int64_t result[dimension + 1][dimension + 1];

    for (int i = 1; i < dimension + 1; i++)
        for (int j = 1; j < dimension + 1; j++) {
            if (i == j) result[i][j] = 0;
            else result[i][j] = 999999999;
        }
    

    while(!input.eof()) {
        getline(input, line);
        split(strs, line, is_any_of(" "), token_compress_on);
        if (strs.size() < 3) continue;
        int64_t tail = atoi(strs[0].c_str());
        int64_t head = atoi(strs[1].c_str());
        int64_t wght = atoi(strs[2].c_str());
        result[tail][head] = wght;
    }

    for (int k = 1; k < dimension + 1; k++) {
        for (int i = 1; i < dimension + 1; i++) {
            for (int j = 1; j < dimension + 1; j++) {
                result[i][j] = min(result[i][j], result[i][k] + result[k][j]);
                if (i == j && result[i][j] < 0) {
                    cout << "Negative Cycles " << result[i][j] << endl;
                    cout << "Halt at: " << i << " " << j << endl;
                    exit(1);
                }
            }
        }
    }
    
    // This is to compute the requirement for the homework.  Not necessary for typical floyd-warshall algorithm
    int64_t min_dist = 999999999;
    for (int i = 1; i < dimension + 1; i++) {
        for (int j = 1; j < dimension + 1; j++) {
            if (result[i][j] < min_dist) {
                min_dist = result[i][j];
            }
        }
    }

    cout << "min dist: " << min_dist << endl;
    return 0;
}
