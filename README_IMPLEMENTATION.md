# Tree Isomorphism Checker

## Overview
This program implements **canonical encoding** to determine if two trees are isomorphic. It follows the strict requirements of the assignment without using external graph libraries.

## Algorithm Overview

### 1. Tree Validation
Before any processing, the program validates that each input is a valid tree:
- **Must have V-1 edges**: A tree with V vertices must have exactly V-1 edges
- **Must be connected**: All vertices must be reachable from any starting vertex (verified via BFS)
- **No cycles**: Verified implicitly by the above two conditions

### 2. Center Detection
Uses the **leaf peeling algorithm** to find the center(s) of the tree:
1. Start with all degree-1 vertices (leaves) in a queue
2. Remove leaves layer by layer
3. When 1 or 2 vertices remain, they are the center(s)

A tree has exactly 1 or 2 centers:
- **1 center**: Trees with odd height
- **2 centers**: Trees with even height (centers are adjacent)

### 3. Canonical Encoding
The core technique for isomorphism checking:
1. For each center found, compute an encoding by rooting the tree at that center
2. Use DFS to recursively encode all subtrees
3. Sort child encodings lexicographically at each step
4. Choose the **lexicographically minimum** encoding among all centers

**Format**: `(e₁e₂...eₙ)` where e₁, e₂, ... are sorted child encodings

**Example**: A path 0-1-2-3 with centers {1,2}:
```
Rooted at 1:     Rooted at 2:
    1                2
   / \              / \
  0   2            1   3
      |            |
      3            0
Encoding: ((())())  =  ((())())  (same!)
```

### 4. Isomorphism Verdict
Two trees are isomorphic if:
- Both are valid trees
- Both have the same number of vertices
- Their canonical encodings are identical

## Input Format
Standard format (compatible with algs4 library):
```
V E
v₁ w₁
v₂ w₂
...
vₑ wₑ
```

- First line: V (number of vertices) and E (number of edges)
- Next E lines: pairs of vertices representing edges (0-indexed)

Example (4-vertex path):
```
4
3
0 1
1 2
2 3
```

## Usage
```bash
java Main <file1> <file2>
```

## Output Components

1. **Tree 1 Analysis**
   - Vertices and edges count
   - Adjacency list
   - Validation status
   - Center(s) if valid
   - Canonical encoding if valid

2. **Tree 2 Analysis**
   - Same components as Tree 1

3. **Isomorphism Result**
   - `ISOMORPHIC` or `NOT ISOMORPHIC`
   - Encoding comparison

If either tree is invalid, the program aborts comparison and reports the reason.

## Test Cases

### Provided Test Files
- `iso-path4-a.txt`, `iso-path4-b.text`: Two 4-vertex paths (ISOMORPHIC)
- `unico-centro-a.txt`, `unico-centro-b.txt`: Two 7-vertex trees with 1 center (ISOMORPHIC)
- `nao-iso-path5.txt`: 5-vertex path (NOT ISOMORPHIC)
- `nao-iso-estrela5.txt`: 5-vertex star (NOT ISOMORPHIC to path)

### Running Tests
```bash
# Isomorphic test
java -cp bin Main dados/iso-path4-a.txt dados/iso-path4-b.text

# Non-isomorphic test
java -cp bin Main dados/nao-iso-path5.txt dados/nao-iso-estrela5.txt

# Invalid input test
java -cp bin Main dados/ciclo2.txt dados/iso-path4-a.txt
```

## Error Handling
- **Too few arguments**: "Usage: java Main <file1> <file2>"
- **File not found**: Program terminates with error message
- **Invalid graph format**: Graph constructor throws exception
- **Not an edge count mismatch**: "Graph does not have V-1 edges"
- **Disconnected graph**: "Graph is not connected. Visited: X of Y vertices"
- **Tree validation fails**: Program aborts comparison and reports specific reason

## Implementation Details

### Graph.java (Provided)
- Adjacency list representation
- double-counts edges for undirected graphs
- Validation on vertex indices

### TreeIsomorphism.java (Core Logic)
- `validateTree()`: Checks V-1 edges and connectivity
- `findCenters()`: Implements leaf peeling algorithm
- `encodeFromRoot(root)`: Generates canonical encoding from a given root
- `areIsomorphic(tree1, tree2)`: Static method for comparison

### Main.java (Entry Point)
- Command-line argument parsing
- File loading and graph construction
- Output formatting and isomorphism verdict

## Correctness Guarantees
✓ Uses only canonical encoding (no BFS/DFS heuristics for final verdict)
✓ No external graph libraries used
✓ Proper tree validation (V-1 edges + connectivity)
✓ Correct center detection (1 or 2 centers)
✓ Canonical encoding ensures isomorphism detection is independent of:
  - Input file format (as long as it's valid)
  - Edge insertion order
  - Vertex labeling (relabeling preserves canonical form)
✓ Clear output showing validation, center(s), and encoding for transparency
