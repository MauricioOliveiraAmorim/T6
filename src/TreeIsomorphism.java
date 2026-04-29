import java.util.*;

public class TreeIsomorphism {
    
    private Graph graph;
    private int V;
    private List<Integer> centers;
    private String canonicalEncoding;
    private boolean isValidTree;
    private String validationMessage;

    public TreeIsomorphism(Graph graph) {
        this.graph = graph;
        this.V = graph.V();
        this.centers = new ArrayList<>();
        this.isValidTree = false;
        
        validateTree();
        if (isValidTree) {
            findCenters();
            computeCanonicalEncoding();
        }
    }

    /**
     * Valida se o grafo é uma árvore:
     * 1. Deve ter exatamente V-1 arestas
     * 2. Deve ser conexo (reachável de qualquer vértice)
     * 3. Não deve ter ciclos
     */
    private void validateTree() {
        // Verificar número de arestas
        if (graph.E() != graph.V() - 1) {
            validationMessage = "Graph does not have V-1 edges (Expected: " + (graph.V() - 1) + ", Found: " + graph.E() + ")";
            return;
        }

        // Verificar conectividade usando BFS
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        visited.add(0);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            for (int w : graph.adj(v)) {
                if (!visited.contains(w)) {
                    visited.add(w);
                    queue.add(w);
                }
            }
        }

        if (visited.size() != graph.V()) {
            validationMessage = "Graph is not connected. Visited: " + visited.size() + " of " + graph.V() + " vertices";
            return;
        }

        // Se tem V-1 arestas e é conexo, é uma árvore
        isValidTree = true;
        validationMessage = "Valid tree";
    }

    /**
     * Encontra o(s) centro(s) da árvore usando o método de "peeling" (remoção de folhas)
     */
    private void findCenters() {
        if (!isValidTree) return;

        // Copiar graus dos vértices
        List<Integer> degree = new ArrayList<>();
        Set<Integer> active = new HashSet<>();
        for (int i = 0; i < V; i++) {
            degree.add(graph.degree(i));
            active.add(i);
        }

        // Inicializar fila de folhas (vértices com grau 1)
        Queue<Integer> leaves = new LinkedList<>();
        for (int i = 0; i < V; i++) {
            if (degree.get(i) == 1) {
                leaves.add(i);
            }
        }

        // Remover folhas camada por camada até encontrar o(s) centro(s)
        int remaining = active.size();
        while (remaining > 2) {
            int leafCount = leaves.size();
            remaining -= leafCount;

            for (int i = 0; i < leafCount; i++) {
                int leaf = leaves.poll();
                active.remove(leaf);
                for (int neighbor : graph.adj(leaf)) {
                    if (active.contains(neighbor)) {
                        degree.set(neighbor, degree.get(neighbor) - 1);
                        if (degree.get(neighbor) == 1) {
                            leaves.add(neighbor);
                        }
                    }
                }
            }
        }

        // Adicionar apenas os vértices remanescentes ativos como centros
        for (int v : active) {
            centers.add(v);
        }

        Collections.sort(centers);
    }

    /**
     * Computa a codificação canônica da árvore usando enraizamento em cada centro
     * e seleção lexicograficamente mínima
     */
    private void computeCanonicalEncoding() {
        if (!isValidTree || centers.isEmpty()) return;

        // Gerar codificações para cada possível raiz
        List<String> encodings = new ArrayList<>();

        for (int center : centers) {
            String encoding = encodeFromRoot(center);
            encodings.add(encoding);
        }

        // Selecionar a codificação lexicograficamente mínima
        Collections.sort(encodings);
        canonicalEncoding = encodings.get(0);
    }

    /**
     * Gera a codificação de árvore enraizada usando DFS
     * Formato: (subárvore1 subárvore2 ...) onde subárvores são ordenadas lexicograficamente
     */
    private String encodeFromRoot(int root) {
        Map<Integer, String> memo = new HashMap<>();
        return encode(root, -1, memo);
    }

    private String encode(int v, int parent, Map<Integer, String> memo) {
        if (memo.containsKey(v)) {
            return memo.get(v);
        }

        List<String> childEncodings = new ArrayList<>();

        for (int w : graph.adj(v)) {
            if (w != parent) {
                childEncodings.add(encode(w, v, memo));
            }
        }

        // Ordenar as codificações dos filhos em ordem lexicográfica
        Collections.sort(childEncodings);

        // Construir a string de codificação
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (String encoding : childEncodings) {
            sb.append(encoding);
        }
        sb.append(")");

        String result = sb.toString();
        memo.put(v, result);
        return result;
    }

    // Getters
    public boolean isValidTree() {
        return isValidTree;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public List<Integer> getCenters() {
        return new ArrayList<>(centers);
    }

    public String getCanonicalEncoding() {
        return canonicalEncoding;
    }

    /**
     * Compara duas árvores verificando se as codificações canônicas são iguais
     */
    public static boolean areIsomorphic(TreeIsomorphism tree1, TreeIsomorphism tree2) {
        // Ambas devem ser árvores válidas
        if (!tree1.isValidTree() || !tree2.isValidTree()) {
            return false;
        }

        // Devem ter o mesmo número de vértices
        if (tree1.graph.V() != tree2.graph.V()) {
            return false;
        }

        // Comparar codificações canônicas
        return tree1.getCanonicalEncoding().equals(tree2.getCanonicalEncoding());
    }
}
