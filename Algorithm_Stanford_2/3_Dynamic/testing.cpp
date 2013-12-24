// The recurrence is A[i, w] = max(A[i - 1, w], A[i - 1, w - wi] + vi)
// Knapsack1: 2493893
// Knapsack2: 2595819
#include <iostream>
#include <fstream>
#include <boost/unordered_map.hpp>
#include <boost/tuple/tuple.hpp>
#include <boost/tuple/tuple_comparison.hpp>
#include <boost/algorithm/string.hpp>

using namespace std;
using namespace boost;

typedef boost::tuples::tuple<int, int> Pair;
typedef Pair Info;

struct ihash : unary_function<Pair, size_t> {
    size_t operator() (Pair const &t) const {
        return t.get<0>() * 10 + t.get<1>();
    }
};

int knackpack(unordered_map<Info, int, ihash> &, int, int, vector<Pair> &);

int main (int argc, const char * argv[]) {
    char filename[] = "knapsack2.txt";
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
    cout << numberOfItems << endl;

    while (!input.eof()) {
        getline(input, line);
        split(strs, line, is_any_of(" "), token_compress_on);
        int value = atoi(strs[0].c_str());
        int weight = atoi(strs[1].c_str());
        data.push_back(Pair(value, weight));
    }

    unordered_map<Info, int, ihash> map;

    int result = knackpack(map, data.size() - 1, totalWeight, data);
    
    cout << "Result: " << result << endl;
    return 0;
}

int knackpack(unordered_map<Pair, int, ihash> &m, int i, int w, vector<Pair> &data) {
    if (i < 0) return 0;
    Info current(i, w);
    if (m.find(current) != m.end()) return m[current];

    int case2 = -1;
    Pair &di = data[i];
    if (w >= di.get<1>())
        case2 = knackpack(m, i - 1, w - di.get<1>(), data) + di.get<0>();
    int case1 = knackpack(m, i - 1, w, data);

    int better = max(case1, case2);
    m[Info(i, w)] = better;
    return better;
}
