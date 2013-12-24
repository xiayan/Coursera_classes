// Clusters: 16508

#include <boost/unordered_map.hpp>
#include <boost/algorithm/string.hpp>
#include <boost/dynamic_bitset.hpp>
#include <iostream>
#include <fstream>
#include <vector>
#include <time.h>
#include "UnionFind.h"

using namespace std;
using namespace boost;

int ipow( int base, int exponent)
{
    if (exponent == 0) return 1;  // base case;
    int temp = ipow(base, exponent/2);
    if (exponent % 2 == 0)
        return temp * temp; 
    else
        return (base * temp * temp);
}

unsigned int strToBin(string &s, int bits) {
    unsigned int total = 0;
    for (int i = 0; i < bits; i++) {
        if (s.at(i) == '1')
            total += (int)pow(2.0, bits - 1 - i);
    }
    return total;
}

/*
namespace boost {
    template <typename B, typename A>
    std::size_t hash_value(const boost::dynamic_bitset<B, A>& bs) {
        std::vector<B, A> v;
        boost::to_block_range(bs, std::back_inserter(v));
        return boost::hash_value(v);
    }
}
*/

// have to define hash values otherwise cannot use hashmap with dynamic_bitset
namespace boost {
    unsigned long hash_value(const boost::dynamic_bitset<unsigned> &bs) {
        return bs.to_ulong();
    }
}

int main(int argc, const char *argv[]) {
    clock_t t = clock();

    const char *filename = "clustering2.txt";
    ifstream input(filename);
    if (!input.good()) {
        cout << "Cannot open file" << endl;
        exit(1);
    }
    
    vector<string> strs;
    string line;
    getline(input, line);
    split(strs, line, boost::is_any_of(" "), boost::token_compress_on);
    int numOfNodes = atoi(strs[0].c_str());
    int bits       = atoi(strs[1].c_str());

    typedef dynamic_bitset<unsigned> Code;
    unordered_map<Code, int> map;
    vector<Code> nm;

    int counter = 0;
    int merges = 0;
    while (!input.eof()) {
        getline(input, line);
        erase_all(line, " ");
        unsigned int decimal = strToBin(line, bits);
        Code currentCode(bits, decimal);
        if (map.find(currentCode) != map.end()) {
            merges++;
            continue;
        }
        nm.push_back(currentCode); 
        map[currentCode] = counter++;
    }
    input.close();

    int distinct = nm.size();
    
    //BOOST_FOREACH(um::value_type i, map) {
    //    std::cout<<i.first<<", "<<i.second<<"\n";
    //}
    
    UnionFind uf(numOfNodes);

    // spacing = 1
    for (int i = 0; i < distinct; i++) {
        Code &current = nm[i];
        for (int j = 0; j < bits; j++) {
            Code mutation = current;
            mutation.flip(j);

            if (map.find(mutation) != map.end()) {
                int other = map.at(mutation);
                if (!uf.connected(i, other)) {
                    uf.connect(i, other);
                    merges++;
                }
            }
        }
    }

    // spacing = 2
    for (int i = 0; i < distinct; i++) {
        Code &current = nm[i];
        for (int j = 0; j < bits; j++) {
            Code mut1 = current;
            mut1.flip(j);

            for (int k = j + 1; k < bits; k++) {
                Code mut2 = mut1;
                mut2.flip(k);

                if (map.find(mut2) != map.end()) {
                    int other = map.at(mut2);
                    if (!uf.connected(i, other)) {
                        uf.connect(i, other);
                        merges++;
                    }
                }
            }
        }
    }
    
    cout << "Merged: " << merges << endl; 
    cout << "Clusters: " << numOfNodes - merges << endl;

    nm.clear();
    map.clear();
    
    t = clock() - t;
    cout << (float)t / CLOCKS_PER_SEC << " seconds " << endl;

    return 0;
}
