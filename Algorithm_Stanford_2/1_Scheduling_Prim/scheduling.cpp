// Minus: Total Score: 69119377652
// Ratio: Total Score: 67311454237

// Practiced sorting using both functor and function pointer

#include <iostream>
#include <fstream>
#include <boost/algorithm/string.hpp>
#include <vector>
#include <inttypes.h>

using namespace std;

struct job {
    int weight;
    int length;
    float w_over_l;
    float w_minus_l;
};
typedef vector<job> job_list;

// functor classes
class ratioFunctor {
public:
    bool operator() (const job& j1, const job& j2) {
        if  (j1.w_over_l > j2.w_over_l) {
            return true;
        } else if (j1.w_over_l < j2.w_over_l) {
            return false;
        } else {
            return j1.weight > j2.weight;
        }
    }
};

class minusFunctor {
public:
    bool operator() (const job &j1, const job &j2) {
        if  (j1.w_minus_l > j2.w_minus_l) {
            return true;
        } else if (j1.w_minus_l < j2.w_minus_l) {
            return false;
        } else {
            return j1.weight > j2.weight;
        }
    }
};

// Function Declarations
bool ratioCompare(const job &, const job &);
bool minusCompare(const job &, const job &);
void constructJobList(ifstream &, job_list &);
uint64_t calculateTotalScore(job_list &, bool (*pf)(const job &, const job &));
template<typename Function>
uint64_t functorScore(job_list &jl, Function f);

int main(int argc, const char * argv[]) {
    string filename = "jobs.txt";
    ifstream inputFile(filename.c_str());
    job_list alist;
    constructJobList(inputFile, alist);

    uint64_t minusScore = calculateTotalScore(alist, minusCompare);
    uint64_t ratioScore = calculateTotalScore(alist, ratioCompare);
    uint64_t mScore = functorScore<minusFunctor>(alist, minusFunctor());
    uint64_t rScore = functorScore<ratioFunctor>(alist, ratioFunctor());
    
    cout << "Total Minus Score: " << minusScore << endl; 
    cout << "Total Ratio Score: " << ratioScore << endl;
    cout << "Functor Minus : " << mScore << endl;
    cout << "Function Ratio : " << rScore << endl;
    return 0;
}

bool ratioCompare(const job &j1, const job &j2) {
    if  (j1.w_over_l > j2.w_over_l) {
        return true;
    } else if (j1.w_over_l < j2.w_over_l) {
        return false;
    } else {
        return j1.weight > j2.weight;
    }
}

bool minusCompare(const job &j1, const job &j2) {
    if  (j1.w_minus_l > j2.w_minus_l) {
        return true;
    } else if (j1.w_minus_l < j2.w_minus_l) {
        return false;
    } else {
        return j1.weight > j2.weight;
    }
}

void constructJobList(ifstream &input, job_list &alist) {
    using namespace boost::algorithm;
    string line;
    vector<string> strs;

    if (!input.good()) {
        cout << "Can't open file" << endl;
        exit(1);
    }
    
    getline(input, line);
    int numberOfJobs = atoi(line.c_str());

    while (! input.eof()) {
        getline(input, line);
        trim(line);
        split(strs, line, boost::is_any_of(" "), boost::token_compress_on);
        
        if (strs.size() < 2) { continue; }
        
        job newJob;
        newJob.weight = atoi(strs[0].c_str());
        newJob.length = atoi(strs[1].c_str());
        newJob.w_over_l  = (float)newJob.weight / (float)newJob.length;
        newJob.w_minus_l = (float)newJob.weight - (float)newJob.length;
        alist.push_back(newJob);
        strs.clear();
    }

    if (alist.size() != numberOfJobs) {
        cout << "Number of jobs different than specification" << endl;
        exit(-1);
    }
}

// Use function pointer to sort the vector using different parameters
uint64_t calculateTotalScore(job_list &jl, bool (*pf)(const job &j2, const job &j1)) {
    sort(jl.begin(), jl.end(), pf);
    unsigned int runningTime = 0;
    uint64_t totalScore = 0;
    for (int i = 0; i < jl.size(); i++) {
        job &currentJob = jl[i];
        runningTime += currentJob.length;
        totalScore += runningTime * currentJob.weight;
    }
    return totalScore;
}

template <typename Function>
uint64_t functorScore(job_list &jl, Function f) {
    sort(jl.begin(), jl.end(), f);
    unsigned int runningTime = 0;
    uint64_t totalScore = 0;
    for (int i = 0; i < jl.size(); i++) {
        job &currentJob = jl[i];
        runningTime += currentJob.length;
        totalScore += runningTime * currentJob.weight;
    }
    return totalScore;
}
