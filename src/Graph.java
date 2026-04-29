import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Graph {
    private final int V;
    private int E;
    private final List<List<Integer>> adj;
    public Graph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.V = V;
        this.E = 0;
        adj = new ArrayList<>();
        for (int v = 0; v < V; v++) {
            adj.add(new ArrayList<>());
        }
    }

    public Graph(Scanner in) {
        if (in == null) throw new IllegalArgumentException("argument is null");
        try {
            this.V = in.nextInt();
            if (V < 0) throw new IllegalArgumentException("number of vertices in a Graph must be non-negative");
            adj = new ArrayList<>();
            for (int v = 0; v < V; v++) {
                adj.add(new ArrayList<>());
            }
            int E = in.nextInt();
            if (E < 0) throw new IllegalArgumentException("number of edges in a Graph must be non-negative");
            for (int i = 0; i < E; i++) {
                int v = in.nextInt();
                int w = in.nextInt();
                validateVertex(v);
                validateVertex(w);
                addEdge(v, w);
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException("invalid input format in Graph constructor", e);
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        E++;
        adj.get(v).add(w);
        adj.get(w).add(v);
    }

    public List<Integer> adj(int v) {
        validateVertex(v);
        return adj.get(v);
    }

    public int degree(int v) {
        validateVertex(v);
        return adj.get(v).size();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " vertices, " + E + " edges\n");
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (int w : adj.get(v)) {
                s.append(w + " ");
            }
            s.append("\n");
        }
        return s.toString();
    }
}