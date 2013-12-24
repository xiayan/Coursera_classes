// The recurrence is A[i, w] = max(A[i - 1, w], A[i - 1, w - wi] + vi)
// Knapsack1: 2493893
#include <iostream>
#include <fstream>
#include <boost/tuple/tuple.hpp>
#include <boost/tuple/tuple_comparison.hpp>
#include <boost/algorithm/string.hpp>
#include <vector>

using namespace std;
using namespace boost;


struct Pair {
    int value;
    int weight;
    Pair(int v, int w) : value(v), weight(w) {}
};

int main (int argc, const char * argv[]) {
    char filename[] = "Knapsack1.txt";
    ifstream input(filename);
    if (!input.good()) {
        cout << "Can't open file" << endl;
        exit(1);
    }

    int numberOfItems;
    int totalWeight;
    vector<Pair> data;

    string line;
    vector<string> strs;
    getline(input, line);
    split(strs, line, is_any_of(" "), token_compress_on);
    numberOfItems = atoi(strs[1].c_str());
    totalWeight = atoi(strs[0].c_str());

    while (!input.eof()) {
        getline(input, line);
        split(strs, line, is_any_of(" "), token_compress_on);
        int value = atoi(strs[0].c_str());
        int weight = atoi(strs[1].c_str());
        data.push_back(Pair(value, weight));
    }
    
    int result[numberOfItems + 1][totalWeight + 1];

    for (int i = 1; i < numberOfItems + 1; i++) {
        Pair &current = data[i - 1];
        for (int w = 0; w < totalWeight + 1; w++) {
            int case1 = result[i - 1][w];
            int case2 = (w >= current.weight) ? (result[i - 1][w - current.weight] + current.value) : 0;
            int entry = max(case1, case2);
            result[i][w] = entry;
        }
    }

    //cout << "Result: " << *max_element(result[numberOfItems], result[numberOfItems] + totalWeight + 1) << endl;

    cout << result[numberOfItems][totalWeight] << endl;

    return 0;
}
