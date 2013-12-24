//Min distance: 26442.7(round down to 26442)
#include <iostream>
#include <fstream>
#include <vector>
#include <math.h>
#include <boost/algorithm/string.hpp>
#include <time.h>

using namespace std;
using namespace boost;

static int length = 32;

bool  get_bit(int, int);
int   set_bit(int, int);
int clear_bit(int, int);
int  get_next(int);
int upper_limit(int);

int main(int argc, const char *argv[]) {
    clock_t t = clock();

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
    
    int total_size = upper_limit(length - 1);

    const int r_size = total_size;
    const int r_l = length;
    float **result = new float *[r_size];
    for (int i = 0; i < r_size; i++)
        result[i] = new float[r_l];

    typedef pair<float, float> coord;
    vector<coord> coords;
    
    while(!input.eof()) {
        getline(input, line);
        split(strs, line, is_any_of(" "), token_compress_on);
        coord new_coord;
        new_coord.first = atof(strs[0].c_str());
        new_coord.second = atof(strs[1].c_str());
        coords.push_back(new_coord);
    }

    const int d_l = length;
    float **dists = new float *[d_l];
    for (int i = 0; i < d_l; i++) {
        dists[i] = new float[d_l];
    }

    for (int i = 0; i < length; i++) {
        for (int j = 0; j < length; j++) {
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

    int smallest = 0;
    register int largest;
    register int current;
    
    register bool contain;
    register int excluded;
    register float min;
    register bool is_set;

    for (int m = 1; m < length; m++) {
        smallest = smallest * 2 + 1;
        largest = upper_limit(m);
        current = smallest;
        cout << "group size = " << m + 1 << endl;
        while (current <= largest) {
            
            for (int j = 0; j < length - 1; j++) {
                contain = get_bit(current, j);
                if (!contain) continue;
                excluded = clear_bit(current, j);
                if (excluded == 0) {
                    min = dists[0][j + 1];
                } else {
                    min = 999999999999.9;
                    for (int k = 0; k < length - 1; k++) {
                        is_set = get_bit(excluded, k);
                        if (!is_set) continue;
                        float local_dist = result[excluded - 1][k] + dists[k + 1][j + 1];
                        if (local_dist < min) min = local_dist;
                    }
                }
                result[current - 1][j] = min;
            }
            current = get_next(current);
        }
    }

    min = 999999999999.9;
    for (int j = 0; j < length - 1; j++) {
        float local = result[largest - 1][j] + dists[j + 1][0];
        if (local < min) min = local;
    }
    
    for (int i = 0; i < r_size; i++) {
        delete[] result[i];
    }
    delete[] result;

    for (int i = 0; i < d_l; i++)
        delete[] dists[i];
    delete[] dists;

    cout << "Min distance: " << min << endl;

    t = clock() - t;
    cout << (float) t / CLOCKS_PER_SEC << " seconds" << endl;
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
    int result = ~((1 << (length -1 - n)) - 1);
    int mask = (1 << (length - 1)) - 1;
    return result & mask;
}
