/*
 * =====================================================================================
 *
 *       Filename:  branchBound.cpp
 *
 *    Description:  solving 0-1 knackpack problem with branch and bound algorithm
 *
 *        Version:  1.0
 *        Created:  06/23/2013 10:26:24
 *       Revision:  none
 *       Compiler:  llvm
 *
 *         Author:  yanxia
 *
 * =====================================================================================
 */
#include <stdlib.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <deque>

using namespace std;

struct node {
    int val;
    int wgt;
    float frac;
    int rank;
    node(int v = 0, int w = 0, int r = 0) : val(v), wgt(w), rank(r) { frac = 0.0; }
    void print() { cout << val << " " << wgt << " " << frac << " " << rank << endl; }
};

struct record {
    const int idx;
    bool mark;
    int value;
    int room;
    int estimate;
    record(int i, int m, int v, int r, int e) : idx(i), mark(m), value(v), \
                                                room(r), estimate(e) {}
    void print() {
        cout << idx << "  " << (mark == true) << "  " << value << "  " << room << "  " << estimate << endl;
    }
};

bool rankNodes(const node &n1, const node &n2) {
    // sort the nodes according to value/weight
    long double r1 = (long double)(n1.val) / (long double)(n1.wgt);
    long double r2 = (long double)(n2.val) / (long double)(n2.wgt);
    return r1 > r2;
}

// function definitions
void solver(vector<node> &items, int N, int W);
int  calBound(vector<node> &items, int s, int &e, int estimate);
int iterativeSolver(vector<node> &items, int s, bool marked[], int room);
void printResult(int winner, bool x[], int N);

int main(int argc, const char *argv[]) {
    const char *inputFN = argv[1];
    ifstream inFile(inputFN);
    if (!inFile.good()) exit(1);
    int N, W;
    inFile >> N >> W;
    vector<node> allItems;
    allItems.push_back(node(100000, 1, 0));

    int v, w;
    int r = 1;
    while (inFile >> v >> w) {
        node newNode(v, w, r++);
        allItems.push_back(newNode);
    }

    sort(allItems.begin(), allItems.end(), rankNodes);

    // for (int i = 0; i < allItems.size(); i++)
    //     allItems[i].print();

    solver(allItems, N, W);

    return 0;
}

void solver(vector<node> &items, int N, int W) {
    bool marked[N+1];
    for (int i = 1; i < N+1; i++)
        marked[i] = false;

    // calculate initial bounds
    int initial = 0;
    int room = W;
    int last, frac;
    for (int i = 1; i < items.size(); i++) {
        const node current = items[i];
        if (room >= current.wgt)
            initial += current.val;
        else
            initial += (float(room) / float(current.wgt) * current.val);
        room -= current.wgt;
        if (room <= 0) {
            last = i;
            items[last].frac = 1.0 + float(room) / float(current.wgt);
            break;
        }
    }

    // do depth first search on stack
    deque<record> frames;
    // index -1, unmarked and marked
    record f(0, false, 0, W, initial);
    int winner = -1;
    bool x[N+1];
    frames.push_back(f);

    while (!frames.empty()) {
        record cRecord = frames.back();
        // cRecord.print();
        frames.pop_back();
        // cRecord.print();
        node &thisNode = items[cRecord.idx];
        node &nextNode = items[cRecord.idx+1];
        marked[thisNode.rank] = cRecord.mark;

        // solve it by dynamic programming if size is small
        if ((items.size() - cRecord.idx) * cRecord.room <= 2000000) {
            // cout << "dynamic\n";
            int bestV = iterativeSolver(items, cRecord.idx+1, marked, cRecord.room);
            // cout << "bestV: " << bestV << endl;
            if (winner < bestV + cRecord.value) {
                winner = bestV + cRecord.value;
                for (int j = 1; j < N+1; j++)
                    x[j] = marked[j];
            }
            // for (int i = cRecord.idx+1; i < items.size(); i++)
            //     marked[items[i].rank] = false;
            continue;
        }

        // calculate estimate without next one
        int newE = calBound(items, cRecord.idx+1, last, cRecord.estimate);
        if (winner <= newE) {
        // if (winner <= initial) {
            record inu(cRecord.idx+1, false, cRecord.value, cRecord.room, newE);
            frames.push_back(inu);
        }

        // if (cRecord.room >= nextNode.wgt && cRecord.estimate >= winner) {
        if (cRecord.room >= nextNode.wgt) {
            record inm(cRecord.idx+1, true, cRecord.value+nextNode.val, \
                    cRecord.room - nextNode.wgt, cRecord.estimate);
            frames.push_back(inm);
        }

        if (cRecord.room <= nextNode.wgt && winner > 0 && winner < cRecord.value) {
            winner = cRecord.value;
            for (int j = 1; j < N+1; j++)
                x[j] = marked[j];
        }
    }
    printResult(winner, x, N);
    frames.clear();
}

int calBound(vector<node> &items, int s, int &e, int estimate) {
    // greedy method to calculate the bounds.
    // TODO: use linear programming to find optimal bounds
    if (s == items.size() - 1) return estimate;

    int newE, extra;
    if (s == e) {
        newE = 0;
        extra = items[s].wgt;
    } else {
        newE = estimate - items[s].val - items[e].val * items[e].frac;
        extra = items[s].wgt + (items[e].wgt) * (items[e].frac);
    }
    for (int i = e; i < items.size(); i++) {
        const node current = items[i];
        if (extra >= current.wgt)
            newE += current.val;
        else
            newE += (float(extra) / float(current.wgt) * current.val);
        extra -= current.wgt;
        if (extra <= 0) {
            e = i;
            items[e].frac = 1.0 + float(extra) / float(current.wgt);
            break;
        }
    }
    return newE;
}

int iterativeSolver(vector<node> &items, int s, bool marked[], int room) {
    int numberOfItems = items.size() - s;
    int totalWeight = room;

    int result[numberOfItems+1][totalWeight+1];
    for (int k = 0; k <= totalWeight; k++)
        result[0][k] = 0;

    for (int i = 1; i < numberOfItems+1; i++) {
        node &current = items[s+i-1];
        for (int w = 0; w < totalWeight+1; w++) {
            int case1 = result[i - 1][w];
            int case2 = (w >= current.wgt) ? \
                        (result[i - 1][w - current.wgt] + current.val) : 0;
            int entry = max(case1, case2);
            result[i][w] = entry;
        }
    }
    // trace back assignment from the result
    int idx = numberOfItems;
    int w   = totalWeight;
    for (; idx > 0; idx--) {
        node &thisNode = items[s+idx-1];
        if (result[idx][w] == result[idx-1][w]) {
            marked[thisNode.rank] = false;
        } else {
            marked[thisNode.rank] = true;
            w -= thisNode.wgt;
        }
    }

    return result[numberOfItems][totalWeight];
}

void printResult(int winner, bool x[], int N) {
    cout << winner << " " << "0" << endl;
    cout << x[1];
    for (int i = 2; i < N+1; i++)
        cout << " " << x[i];
    cout << endl;
}
