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
//typedef Pair Info;

class Item {
    Pair a;
 public:
    Item(): a(0, 0) {}
    Item(int x, int y): a(x, y) {}
    bool operator ==(Item const &b) const {
      return a.get<0>() == b.a.get<0>() &&
             a.get<1>() == b.a.get<1>();
    }

    friend std::size_t hash_value(Item const &p) {
        std::size_t seed = 0;
        boost::hash_combine(seed, p.a.get<0>());
        boost::hash_combine(seed, p.a.get<1>());
        return seed;
    }

    int getValue() { return a.get<0>(); }
    int getWeight() { return a.get<1>(); }
};

typedef Item Info; // 0 is index, 1 is total
typedef vector<Item>::iterator iter; // Item: 0 is value, 1 is weight

int knackpack(unordered_map<Info, int> &, int, int, vector<Item> &);

int main (int argc, const char * argv[]) {
    char filename[] = "knapsack2.txt";
    ifstream input(filename);
    if (!input.good()) {
        cout << "Can't open file" << endl;
        exit(1);
    }

    int numberOfItems;
    int totalWeight;
    vector<Item> data;

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
        data.push_back(Item(value, weight));
    }

    unordered_map<Info, int> map;

    int result = knackpack(map, data.size() - 1, totalWeight, data);
    
    cout << "Result: " << result << endl;
    return 0;
}

int knackpack(unordered_map<Item, int> &m, int i, int w, vector<Item> &data) {
    if (i < 0) return 0;
    Info current(i, w);
    if (m.find(current) != m.end()) return m[current];

    int case2 = -1;
    Item &di = data[i];
    if (w >= di.getWeight())
        case2 = knackpack(m, i - 1, w - di.getWeight(), data) + di.getValue();
    int case1 = knackpack(m, i - 1, w, data);

    int better = max(case1, case2);
    m[Info(i, w)] = better;
    return better;
}
