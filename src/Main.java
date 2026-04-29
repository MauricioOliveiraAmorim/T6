import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // Definir arquivos de teste
        String file1 = "dados/nao-iso-estrela5.txt";
        String file2 = "dados/nao-iso-path5.txt";
        
        // Se argumentos forem passados, usar eles em vez dos padrões
        if (args.length >= 2) {
            file1 = args[0];
            file2 = args[1];
        }

        // Carregar e processar primeiro grafo
        System.out.println("========================================");
        System.out.println("TREE ISOMORPHISM CHECKER");
        System.out.println("========================================\n");

        System.out.println("Loading Tree 1 from: " + file1);
        Graph graph1 = loadGraph(file1);
        TreeIsomorphism tree1 = new TreeIsomorphism(graph1);
        displayTreeInfo(1, graph1, tree1);

        // Validar primeira árvore
        if (!tree1.isValidTree()) {
            System.out.println("\n❌ ERROR: First tree is invalid!");
            System.out.println("Reason: " + tree1.getValidationMessage());
            System.out.println("\nComparison aborted.");
            System.exit(1);
        }

        System.out.println("\n----------------------------------------\n");

        // Carregar e processar segundo grafo
        System.out.println("Loading Tree 2 from: " + file2);
        Graph graph2 = loadGraph(file2);
        TreeIsomorphism tree2 = new TreeIsomorphism(graph2);
        displayTreeInfo(2, graph2, tree2);

        // Validar segunda árvore
        if (!tree2.isValidTree()) {
            System.out.println("\n❌ ERROR: Second tree is invalid!");
            System.out.println("Reason: " + tree2.getValidationMessage());
            System.out.println("\nComparison aborted.");
            System.exit(1);
        }

        // Comparar isomorfismo
        System.out.println("\n========================================");
        System.out.println("ISOMORPHISM RESULT");
        System.out.println("========================================\n");

        boolean isomorphic = TreeIsomorphism.areIsomorphic(tree1, tree2);

        if (isomorphic) {
            System.out.println("✓ ISOMORPHIC - The two trees are isomorphic!");
            System.out.println("\nCanonical representations match:");
            System.out.println("  Encoding 1: " + tree1.getCanonicalEncoding());
            System.out.println("  Encoding 2: " + tree2.getCanonicalEncoding());
        } else {
            System.out.println("✗ NOT ISOMORPHIC - The two trees are NOT isomorphic.");
            System.out.println("\nCanonical representations differ:");
            System.out.println("  Encoding 1: " + tree1.getCanonicalEncoding());
            System.out.println("  Encoding 2: " + tree2.getCanonicalEncoding());
        }

        System.out.println("\n========================================");
    }

    /**
     * Exibe informações completas sobre uma árvore
     */
    private static void displayTreeInfo(int treeNumber, Graph graph, TreeIsomorphism tree) {
        System.out.println("\nTree " + treeNumber + ":");
        System.out.println("---------");

        // Exibir propriedades básicas
        System.out.println("Vertices: " + graph.V());
        System.out.println("Edges: " + graph.E());

        // Exibir lista de adjacência
        System.out.println("\nAdjacency List:");
        System.out.print(graph.toString());

        // Validação
        System.out.println("Validation: " + tree.getValidationMessage());

        if (tree.isValidTree()) {
            // Exibir centros encontrados
            List<Integer> centers = tree.getCenters();
            System.out.print("Center(s): ");
            if (centers.size() == 1) {
                System.out.println(centers.get(0));
            } else {
                System.out.println(centers);
            }

            // Exibir codificação canônica
            System.out.println("Canonical Encoding: " + tree.getCanonicalEncoding());
        }
    }

    /**
     * Carrega um grafo a partir de um arquivo
     */
    private static Graph loadGraph(String filename) throws FileNotFoundException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filename);
        }

        Scanner in = new Scanner(file);
        Graph graph = new Graph(in);
        in.close();
        return graph;
    }
}
