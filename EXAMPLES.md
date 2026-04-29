# Execution Examples and Analysis

## Example 1: Isomorphic Paths

### Input Files
**iso-path4-a.txt**:
```
4
3
0 1
1 2
2 3
```
Linear path: 0-1-2-3

**iso-path4-b.text**:
```
4
3
0 2
2 1
1 3
```
Same structure but different labeling: 0-2-1-3

### Execution
```bash
java -cp bin Main dados/iso-path4-a.txt dados/iso-path4-b.text
```

### Output Analysis

Tree 1:
- Vertices: 4, Edges: 3 ✓
- Structure: 0-1-2-3 (linear path)
- Validation: Valid tree ✓
- Centers: [1, 2] (two middle vertices)
- Canonical Encoding: `((())())`

Tree 2:
- Vertices: 4, Edges: 3 ✓
- Structure: 0-2-1-3 (linear path, different labeling)
- Validation: Valid tree ✓
- Centers: [1, 2]
- Canonical Encoding: `((())())`

**Result**: ISOMORPHIC ✓
- Same number of vertices
- Identical canonical encodings
- Encodings match despite different vertex labels

---

## Example 2: Non-Isomorphic (Star vs Path)

### Input Files
**nao-iso-estrela5.txt** (5-vertex star):
```
5
4
0 1
0 2
0 3
0 4
```
Star centered at vertex 0

**nao-iso-path5.txt** (5-vertex path):
```
5
4
0 1
1 2
2 3
3 4
```
Linear path: 0-1-2-3-4

### Execution
```bash
java -cp bin Main dados/nao-iso-path5.txt dados/nao-iso-estrela5.txt
```

### Output Analysis

Tree 1 (Star):
- Structure: One central vertex connected to 4 leaves
- Center: vertex 0 (highest degree = 4)
- Encoding: `((()()()))`
  - Rooted at 0, all 4 children are leaves
  - Each leaf: `()`
  - Total: `(` + `()()()()` + `)` = `((()()()))`

Tree 2 (Path):
- Structure: Linear 0-1-2-3-4
- Centers: [2] (middle vertex)
- Encoding: `(((()))())`
  - Rooted at 2:
    ```
         2
        / \
       1   3
       |   |
       0   4
    ```
  - Left subtree (rooted at 1): `((()))`
  - Right subtree (rooted at 3): `(())`
  - Combined: `(` + `((()))` + `(())` + `)` = `(((()))())`

**Result**: NOT ISOMORPHIC ✓
- Different canonical encodings reveal different structures
- Star has one central hub; path is linear

---

## Example 3: Isomorphic Trees with Single Center

### Input Files
**unico-centro-a.txt**:
```
7
6
0 1
1 2
2 3
2 4
4 5
4 6
```
Structure:
```
0-1-2-{3, 4-{5,6}}
```

**unico-centro-b.txt**:
```
7
6
0 2
1 2
2 5
3 5
4 5
5 6
```
Different labeling but same structure

### Execution
```bash
java -cp bin Main dados/unico-centro-a.txt dados/unico-centro-b.txt
```

### Output Analysis

Tree 1:
- Centers: [2] (single center)
- Rooted at 2:
  ```
        2
       /|\
      1 3 4
      |    |\
      0    5 6
  ```
- Encoding: `((()())(())())`

Tree 2:
- Centers: [2]
- Same structure with relabeled vertices
- Encoding: `((()())(())())`

**Result**: ISOMORPHIC ✓
- Single center found in both
- Identical canonical encodings despite different labelings

---

## Example 4: Invalid Input - Cycle

### Input
**ciclo2.txt**:
```
3
3
0 1
1 2
2 0
```
Forms a 3-cycle: 0-1-2-0 (not a tree!)

### Execution
```bash
java -cp bin Main dados/ciclo2.txt dados/iso-path4-a.txt
```

### Output
```
Validation: Graph does not have V-1 edges (Expected: 2, Found: 3)
ERROR: First tree is invalid!
Reason: Graph does not have V-1 edges (Expected: 2, Found: 3)
Comparison aborted.
```

**Caught**: ✓ Correctly identified as not a tree (3 edges instead of 2)

---

## Example 5: Invalid Input - Disconnected

### Input
**desconexo.txt**:
```
4
3
0 1
1 2
3 3
```
Two components: {0-1-2} and {3-self-loop}

### Execution
```bash
java -cp bin Main dados/desconexo.txt dados/iso-path4-a.txt
```

### Output
```
Validation: Graph is not connected. Visited: 3 of 4 vertices
ERROR: Second tree is invalid!
Reason: Graph is not connected. Visited: 3 of 4 vertices
```

**Caught**: ✓ Correctly identified as not connected

---

## Canonical Encoding Algorithm Visualization

### Tree Structure
```
    1
   / \
  0   2
      |
      3
```

### Rooted at vertex 1:
```
Step 1: DFS from 1
  - Visit 0 (leaf): encode(0, parent=1) = ()
  - Visit 2 (internal): 
    - Visit 3 (leaf): encode(3, parent=2) = ()
    - Children of 2: [3]
    - Sorted: [()]
    - encode(2, parent=1) = (())
  - Children of 1: [0, 2]
  
Step 2: Sort child encodings at root
  - Child encodings: [(), (())]
  - Sorted lexicographically: [(), (())]
  
Step 3: Final encoding
  - Combine sorted: () + (()) = ()(())
  - Wrap: (()(()))
  
Result: (()())
```

### Root at vertex 2:
```
Same structure but rooted differently
Would generate different encoding
Then we select lexicographically minimum from all possible roots
```

---

## Center Finding Algorithm Visualization

### Example: 5-vertex path 0-1-2-3-4

Initial state:
```
Degrees: [1, 2, 2, 2, 1]
Leaves (degree=1): {0, 4}
```

Iteration 1:
```
Remove leaves: 0, 4
Active vertices: {1, 2, 3}
Update degrees:
  - degree[1] = 2 - 1 = 1
  - degree[3] = 2 - 1 = 1
New leaves: {1, 3}
Remaining: 3 vertices
```

Iteration 2:
```
Remove leaves: 1, 3
Active vertices: {2}
Update degrees:
  - degree[2] = 2 - 2 = 0
Remaining: 1 vertex
```

Stop (remaining ≤ 2):
- Center(s): {2}

---

## Key Insights

1. **Canonical Encoding is independent of**:
   - Vertex labels (relabeling preserves structure)
   - Edge insertion order (encoding comes from structure)
   - Which center is chosen (lexicographically minimum encoding is consistent)

2. **Path vs Star recognition**:
   - Path: Many levels of nesting
   - Star: Single level with many children

3. **Center detection**:
   - Guarantees 1 or 2 centers per tree
   - Determines the "center" root(s) for encoding
   - Helps normalize the encoding across different labelings
