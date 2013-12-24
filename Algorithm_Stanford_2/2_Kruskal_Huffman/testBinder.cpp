// Testing binder1st and functor object
// The object is to sort the hamming distance according to a node
// the implementation is too complex for the sake of practicing with adaptable classes and functors

#include <boost/algorithm/string.hpp>
#include <boost/dynamic_bitset.hpp>
#include <iostream>
#include <fstream>
#include <vector>
#include <time.h>
#include <math.h>

using namespace std;
using namespace boost;
typedef dynamic_bitset<unsigned> Code;

unsigned int strToBin(string &s, int bits) {
    unsigned int total = 0;
    for (int i = 0; i < bits; i++) {
        if (s.at(i) == '1')
            total += (int)pow(2.0, bits - 1 - i);
    }
    return total;
}

// Defining an "Adaptable" class
// have to typedef these three types and overide the () operator
class hammingDist {
public:
    typedef int result_type;
    typedef Code first_argument_type;
    typedef Code second_argument_type;
    int operator() (Code c1, Code c2) const {
        c1 ^= c2;
        return c1.count();
    }
};

// Functor class that has variables to help sort
template <typename S>
class sortByDist {
private:
    Code &t;
    binder1st<S> sorter; // binary predicate to unary predicate

public:
    sortByDist(Code &i) : t(i), sorter(S(), t) {};
    bool operator() (const Code &c1, const Code &c2) {
        return sorter(c1) < sorter(c2);
    }
};

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

    vector<Code> nm;

    while (!input.eof()) {
        getline(input, line);
        erase_all(line, " ");
        unsigned int decimal = strToBin(line, bits);
        Code currentCode(bits, decimal);
        nm.push_back(currentCode); 
    }
    
    Code &target = nm[0];
    // takes functor classname which contains typedefs
    binder1st<hammingDist> f1(hammingDist(), target);
    cout << "Testing binder1s: ";
    cout << f1(nm[1]) << endl << endl;

    sortByDist<hammingDist> f2(target);
    sort(nm.begin(), nm.end(), f2);

    vector<int>result(numOfNodes);
    transform(nm.begin(), nm.end(), result.begin(), f1);

    bool con = false;
    int c = 0;
    int decision = 0;
    for (vector<int>::iterator i = result.begin(); i != result.end(); i++) {
        if (c++ != 59) {
            cout << *i << endl;
        } else {
            cout << "continue(1) / quit(0): ";
            cin >> decision;
            if (decision) {
                c = 0;
                continue;
            }
            else break;
        }
    }

    input.close();
    nm.clear(); 
    result.clear();
    
    t = clock() - t;
    cout << (float)t / CLOCKS_PER_SEC << " seconds " << endl;

    return 0;
}
