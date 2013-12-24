#include <iostream>
#include <fstream>
#include <queue>
#include <boost/algorithm/string.hpp>
#include <boost/unordered_map.hpp>
#include <boost/lexical_cast.hpp>

using namespace std;
using namespace boost;
int times = 0;
int source = 0;

struct node {
    int idx;
    int finish;
    int leader;
    bool visited;
    node(int i = 0, int f = 0, int s = 0, bool v = false) : idx(i), finish(f), leader(s), visited(v) {}
};

void dfs_loop(vector<node> &nodes, vector<node *> &nodes_p, unordered_map<int, vector<int> > &graph);
void dfs(vector<node> &nodes, unordered_map<int, vector<int> > &graph, int s);
void compile_result(vector<node> &nodes);

bool compare_node_p(node * a, node *b) {
    return a->finish < b->finish;
}

int main(int argc, const char *argv[]) {
    const char *filename = argv[1];
    
    ifstream input(filename);
    string line;
    vector<string> strs;
    if (!input.good()) {
        cout << "Can't open file" << endl;
        exit(1);
    }

    getline(input, line);
    int num_nodes = atoi(line.c_str());
    vector<node> nodes(num_nodes);
    vector<node *> nodes_p(num_nodes);
    for (int i = 0; i < nodes.size(); i++) {
        nodes[i].idx = i;
        nodes_p[i] = &nodes[i];
    }

    unordered_map<int, vector<int> > graph;
    unordered_map<int, vector<int> > r_graph;

    while (!input.eof()) {
        getline(input, line);
        split(strs, line, is_any_of(" "), token_compress_on);
        int a_end = atoi(strs[0].c_str()) - 1;
        int b_end = atoi(strs[1].c_str()) - 1;
        graph[a_end].push_back(b_end);
        r_graph[b_end].push_back(a_end);
    }

    dfs_loop(nodes, nodes_p, r_graph);

    for (int i = 0; i < nodes.size(); i++) {
        nodes[i].visited = false;
        nodes[i].leader = 0;
    }
    
    sort(nodes_p.begin(), nodes_p.end(), compare_node_p);

    dfs_loop(nodes, nodes_p, graph);

    compile_result(nodes);

    return 0;
}

void dfs_loop(vector<node> &nodes, vector<node *> &nodes_p, unordered_map<int, vector<int> > &graph) {
    times = 0;
    source = 0;
    for (int j = nodes_p.size() - 1; j >= 0; j--) {
        int c = nodes_p[j]->idx;
        if (!nodes[c].visited) {
            source = c;
            dfs(nodes, graph, c);
        }
    }
}

void dfs(vector<node> &nodes, unordered_map<int, vector<int> > &graph, int c) {
    nodes[c].visited = true;
    nodes[c].leader = source;

    vector<int> &neighbors = graph[c];
    for (int i = 0; i < neighbors.size(); i++) {
        int a = neighbors[i];
        if (!nodes[a].visited) 
            dfs(nodes, graph, a);
    }

    nodes[c].finish = ++times;
}

void compile_result(vector<node> &nodes) {
    unordered_map<int, int> result;
    for (int i = 0; i < nodes.size(); i++) {
        int s = nodes[i].leader;
        if (result.find(s) == result.end())
           result[s] = 1;
        else
            result[s]++;
    }
    
    priority_queue<int, vector<int>, greater<int> > queue;
    unordered_map<int, int>::iterator it;
    for (it = result.begin(); it != result.end(); it++) {
        int total = it->second;
        if (queue.size() < 5)
            queue.push(total);
        else {
            if (queue.top() < total) {
                queue.pop();
                queue.push(total);
            }
        }
    }
    
    string r = "";
    while (!queue.empty()) {
        r = lexical_cast<string>(queue.top()) + "," + r;
        queue.pop();
    }
    cout << r << endl;
}
