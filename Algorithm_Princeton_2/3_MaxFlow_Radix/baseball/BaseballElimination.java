public class BaseballElimination {
    private ST<String, Integer> sToI;
    private ST<Integer, String> iToS;
    private int numOfTeams;
    private int s;
    private int t;
    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] g;
    
    // caching
    private String  currentTeam;
    private boolean currentElim;
    private Stack<String> certificate;

    public BaseballElimination(String filename) {        
        In teamInfo = new In(filename);

        String line = null;
        String[] tokens = null;

        line = teamInfo.readLine();
        numOfTeams = Integer.parseInt(line);
        s = numOfTeams + numOfTeams * (numOfTeams - 1) / 2;
        t = s + 1;
        
        sToI = new ST<String, Integer>();
        iToS = new ST<Integer, String>();
        w = new int[numOfTeams];
        l = new int[numOfTeams];
        r = new int[numOfTeams];
        g = new int[numOfTeams][numOfTeams];
        currentTeam = "";
        currentElim = false;
        certificate = new Stack<String>();

        int counter = 0;
        int games = numOfTeams;
        Integer[] indexes;

        while (teamInfo.hasNextLine()) {
            line = teamInfo.readLine().trim();
            tokens = line.split(" +");
            if (tokens.length < 4) continue;
            int id = counter;
            sToI.put(tokens[0], counter);
            iToS.put(counter++, tokens[0]);
            
            w[id] = Integer.parseInt(tokens[1]);
            l[id] = Integer.parseInt(tokens[2]);
            r[id] = Integer.parseInt(tokens[3]);

            for (int i = 0; i < numOfTeams; i++)
                g[id][i] = Integer.parseInt(tokens[4 + i]);
        }
    }
    
    public int numberOfTeams() {
        return numOfTeams;
    }

    public Iterable<String> teams() {
        return sToI.keys();
    }

    public int wins(String team) {
        if (!sToI.contains(team)) {
            throw new java.lang.IllegalArgumentException();
        }
        int index = sToI.get(team);
        return w[index];
    }

    public int losses(String team) {
        if (!sToI.contains(team)) {
            throw new java.lang.IllegalArgumentException();
        }        
        int index = sToI.get(team);
        return l[index];
    }

    public int remaining(String team) {
        if (!sToI.contains(team)) {
            throw new java.lang.IllegalArgumentException();
        }        
        int index = sToI.get(team);
        return r[index];
    }

    public int against(String team1, String team2) {
        if (!sToI.contains(team1) || !sToI.contains(team2)) {
            throw new java.lang.IllegalArgumentException();
        }        
        int id1 = sToI.get(team1);
        int id2 = sToI.get(team2);
        return g[id1][id2];
    }
    
    private boolean trivial(int x) {
        certificate = new Stack<String>();
        currentElim = false;
        for (int i = 0; i < numOfTeams; i++) {
            if (w[x] + r[x] < w[i]) {
                certificate.push(iToS.get(i));
                currentElim = true;
                break;
            }
        }
        return currentElim;
    }

    private void maxFlow(int x) {
        int X = w[x] + r[x];
        int counter = numOfTeams;
        certificate = new Stack<String>();
        currentElim = false;

        FlowNetwork G = new FlowNetwork(t + 1);
        for (int k = 0; k < numOfTeams; k++) {
            for (int j = k+1; j < numOfTeams; j++) {
                if (k == x || j == x) continue;
                if (g[k][j] == 0) continue;
                G.addEdge(new FlowEdge(s, counter, g[k][j]));
                G.addEdge(new FlowEdge(counter, k, Double.POSITIVE_INFINITY));
                G.addEdge(new FlowEdge(counter, j, Double.POSITIVE_INFINITY));
                counter++;
            }
            G.addEdge(new FlowEdge(k, t, X - w[k]));
        }

        FordFulkerson ff = new FordFulkerson(G, s, t);
        for (FlowEdge e : G.adj(s)) {
            if (e.flow() != e.capacity()) currentElim = true;
        }

        for (int k = 0; k < numOfTeams; k++)
            if (ff.inCut(k)) certificate.push(iToS.get(k));

        if (certificate.isEmpty()) certificate = null;
    }

    public boolean isEliminated(String team) {
        if (!sToI.contains(team)) {
            throw new java.lang.IllegalArgumentException();
        }        
        if (currentTeam == team) return currentElim;
        
        currentTeam = team;
        int index = sToI.get(team);
        boolean eliminated = trivial(index);
        if (!eliminated)
            maxFlow(index);
        return currentElim;
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!sToI.contains(team)) {
            throw new java.lang.IllegalArgumentException();
        }        
        if (currentTeam == team)
            return certificate;

        currentTeam = team;
        int index = sToI.get(team);
        boolean eliminated = trivial(index);

        if (!eliminated) {
            maxFlow(index);
        }
        return certificate;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
