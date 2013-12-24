//Min distance: 26442.7
#include <iostream>
#include <fstream>
#include <vector>
#include <math.h>
#include <boost/algorithm/string.hpp>

using namespace std;
using namespace boost;

static int length = 32;

bool  get_bit(int, int);
int   set_bit(int, int);
int clear_bit(int, int);
int  get_next(int);
int upper_limit(int);

int main() {
    const char *filename = "tsp.txt";
    ifstream input(filename);
    if (!input.good()) {
        cout << "Cannot open file " << filename << endl;
    }

    vector<string> strs;
    string line;
    getline(input, line);
    split(strs, line, is_any_of(" "), token_compress_on);
    length = atoi(strs[0].c_str());
    
    int total_size = upper_limit(length);
    vector< vector<float> > result(total_size + 1);
    for (int i = 0; i < total_size + 1; i++)
        result[i] = vector<float>(length + 1);

    typedef pair<float, float> coord;
    vector<coord> coords;
    coord dummy(0.0, 0.0);
    coords.push_back(dummy);
    
    while(!input.eof()) {
        getline(input, line);
        split(strs, line, is_any_of(" "), token_compress_on);
        coord new_coord;
        new_coord.first = atof(strs[0].c_str());
        new_coord.second = atof(strs[1].c_str());
        coords.push_back(new_coord);
    }

    vector< vector<float> > dists(length + 1);
    for(int i = 0; i < length + 1; i++)
        dists[i] = vector<float>(length + 1);

    for (int i = 1; i < length + 1; i++) {
        for (int j = 1; j < length + 1; j++) {
            if (i == j)
                dists[i][j] = 0.0;
            else {
                coord &a = coords[i];
                coord &b = coords[j];
                float dist = (a.first - b.first) * (a.first - b.first) + (a.second - b.second) * (a.second - b.second);
                dists[i][j] = sqrt(dist);
            }
        }
    }

    // handle base case
    for (int i = 2; i < total_size + 1; i++) {
        result[i][1] = 999999999999.9;
    }
    result[1][1] = 0.0;

    int smallest = 1;
    int largest;
    int current;
    
    for (int m = 2; m < length + 1; m++) {
        smallest = smallest * 2 + 1;
        largest = upper_limit(m);
        current = smallest;
        cout << "group size = " << m << endl;
        while (current <= largest) {
            for (int j = 1; j < length; j++) {
                bool contain = get_bit(current, j);
                if (!contain) continue;
                int excluded = clear_bit(current, j);
                float min = 999999999999.9;
                for (int k = 0; k < length; k++) {
                    bool is_set = get_bit(excluded, k);
                    if (!is_set) continue;
                    //previous = map[sig(excluded, k + 1)];
                    float local_dist = result[excluded][k + 1] + dists[k + 1][j + 1];
                    if (local_dist < min) min = local_dist;
                }
                //map[sig(current, j + 1)] = min;
                result[current][j + 1] = min;
            }
            
            do {
                current = get_next(current);
            } while (!get_bit(current, 0));
        }
    }

    float min = 999999999999.9;
    
    for (int j = 1; j < length; j++) {
        //float local = map[sig(largest, j + 1)] + dists[j + 1][1];
        float local = result[largest][j + 1] + dists[j + 1][1];
        if (local < min) min = local;
    }

    cout << "Min distance: " << min << endl;

    return 0;
}

bool get_bit(int num, int i) {
    return ((num & (1 << i)) != 0);
}

int set_bit(int num, int i) {
    return num | (1 << i);
}

int clear_bit(int num, int i) {
    int mask = ~(1 << i);
    return num & mask;
}

int get_next(int n) {
    int c = n;
    int c0 = 0;
    int c1 = 0;

    while (((c & 1) == 0 && c != 0)) {
        c0++;
        c >>= 1;
    }

    while ((c & 1) == 1) {
        c1++;
        c >>= 1;
    }

    if (c0 + c1 == 31 || c0 + c1 == 0)
        return -1;

    int p = c0 + c1;
    
    n |= (1 << p);
    n &= ~((1 << p) - 1);
    n |= (1 << (c1 - 1)) - 1;
    return n;
}

int upper_limit(int n) {
    int result = ~((1 << (length - n)) - 1);
    int mask = (1 << length) - 1;
    return result & mask;
}
