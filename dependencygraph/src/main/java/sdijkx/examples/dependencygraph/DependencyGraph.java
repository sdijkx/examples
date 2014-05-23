package sdijkx.examples.dependencygraph;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Create a dependency graph and topological sort the dependies.
 * @author steven
 * @param <T> Generic
 */
public class DependencyGraph<T> {

    
   /**
    * Set of vertices
    */
    final private Set<T> vertices;
    
    /**
     * Map of dependencies
     */
    final private Map<T,List<T>> dependencies;
    
    /**
     * Map of outgoing dependencies used for sorting 
     */
    final private Map<T,List<T>> OUT;
    
    /**
     * Map of incoming dependencies used for sorting 
     */
    final private Map<T,List<T>> IN;
    
    /**
     * Queue with root elements (no dependencies) used for sorting 
     */
    final private Deque<T> rootVertices;
    
    /**
     * Queue with sorted list result of sort
     */
    final private Deque<T> sortResult;
    
    /**
     * Create a new dependency graph
     */
    DependencyGraph() {
        dependencies = new HashMap<>();
        vertices = new HashSet<>();
        sortResult = new LinkedList<>();
        rootVertices = new LinkedList<>();
        IN = new HashMap<>();
        OUT = new HashMap<>();
    }
    
    /**
     * Process the sorted dependency graph. 
     * @return sorted list  
     */
    public List<T> topologicalSort() {
        try {
            prepare();
            sort();
            return new ArrayList<>(sortResult);
        } finally {
            cleanup();
        }
    }    
    
    /**
     * Create a dependency tree. 
     * @return tree with all root nodes and dependencies  
     */
    public TreeNode<T> dependencyTree() {
        
        TreeNode<T> rootNode = new TreeNode<T>(null,null);
        Deque<TreeNode<T>> queue = new LinkedList<>();
        Map<T,List<T>> edges = new HashMap<T,List<T>>();
        for(T n : vertices) {
            if(!hasDepedencies(n)) {
                TreeNode<T> childNode = new TreeNode<T>(n,rootNode);
                rootNode.addChild(childNode);
                queue.addFirst(childNode);
            } else {
               addDependenciesToEdges(n,edges);
            }
        }
        
        while(!queue.isEmpty()) {
            TreeNode<T> current = queue.poll();
            List<TreeNode<T>> children = findChildNodes(current, edges);
            for(TreeNode<T> childNode : children) {
                queue.addLast(childNode);
            }
        }
        return rootNode;
    }
    
    private void addDependenciesToEdges(T n, Map<T,List<T>> edges) {
        List<T> dependencyList = dependencies.get(n);
        for(T m : dependencyList) {
            addEdgeToIndex(edges,m,n);
        }
    }
    
    private List<TreeNode<T>> findChildNodes(TreeNode<T> current, Map<T,List<T>> edges) {
        List<TreeNode<T>> result = new ArrayList<TreeNode<T>>();
        List<T> list = edges.get(current.getItem());
        if(list != null) {
            for(T childItem : list) {
                TreeNode<T> childNode = new TreeNode<T>(childItem,current);
                current.getChildren().add(childNode);
                if(!current.hasParent(childNode.getItem())) {
                    result.add(childNode);
                }
            }
        }
        return result;        
    }
    

    /**
     * Test if an object is in the vertices set.
     * @param obj 
     * @return
     */
    public boolean contains(T obj) {
        return vertices.contains(obj);
    }    
    
    /**
     * Add a dependencies to graph.
     * @param obj
     * @param dependency 
     */
    public void addDependency(T obj, T dependency) {
        if(!contains(obj)) {
            add(obj);
        }
        if(!contains(dependency)) {
            add(dependency);
        }
        dependencies.put(obj,createOrAppend(dependencies.get(obj),dependency));
    }
    
    /**
     * Add a dependencies to graph.
     * @param obj
     * @param dependency
     * @return true if dependency exists, flase otherwise
     */
    public boolean containsDependency(T obj, T dependency) {
        if(dependencies.containsKey(obj)) {
            return dependencies.get(obj).contains(dependency);
        }
        return false;
    }    

    /**
     * remove a dependency from the graph.
     * @param obj
     * @param dependency 
     */
    public void removeDependency(T obj, T dependency) {
        removeEdgeFromIndex(dependencies,obj,dependency);
    }
    
   /**
     * remove a object from the dependency graph.
     * @param obj
     */
    public void remove(T obj) {
        for(List<T> list : dependencies.values()) {
            list.remove(obj);
        }
        dependencies.remove(obj);
        vertices.remove(obj);
    } 
    
    
    /**
     * Add an object to the graph.
     * @param obj 
     */
    private void add(T obj) {
        vertices.add(obj);
    }
    
    /**
     * prepare the graph for sorting.
     */
    private void prepare() {
        for(T n : vertices) {
            if(hasDepedencies(n)) {
                for(T m : dependencies(n)) {
                    addEdge(n,m);
                }
            } else {
                rootVertices.add(n);                
            }
        }   
        if(rootVertices.isEmpty()) {
            throw new IllegalStateException("No root validations found");
        }
    }

    /**
     * clear the dependency graph after sorting.
     */
    private void cleanup() {
        OUT.clear();
        IN.clear();
        rootVertices.clear();
        sortResult.clear();
    }
    
    /**
     * sort the graph topological.
     */
    private void sort() {
        while(!rootVertices.isEmpty()) {
            T  n = rootVertices.removeFirst();
            sortResult.addLast(n);
            List<T> out = findOutgoingEdges(n);
            for(T m : out) {
                removeEdge(n,m);
                if(noIncomingEdges(m)) {
                    rootVertices.add(m);
                }
            }
        }
        if(hasEdges()) {
            throw new IllegalStateException("Cycle detected");
        }
    }

    /**
     * Add an edge to graph.
     * @param n
     * @param m 
     */
    private void addEdge(T n, T m) {
        addEdgeToIndex(OUT,m,n);
        addEdgeToIndex(IN,n,m);
    }
    
    /**
     * Return all outgoing edges for a vertex.
     * @param n
     * @return 
     */
    private List<T> findOutgoingEdges(T n) {
        return copyList(OUT.get(n));
    }
    
    /**
     * Remove an edge from the graph.
     * @param n
     * @param m 
     */
    private void removeEdge(T n, T m) {
        removeEdgeFromIndex(IN,m,n);
        removeEdgeFromIndex(OUT,n,m);
    }

    /**
     * Check if the graph has edges.
     * @return 
     */
    private boolean hasEdges() {
        return !IN.isEmpty();
    }

    /**
     * Check if the incoming edges are empty.
     * @param m
     * @return 
     */
    private boolean noIncomingEdges(T m) {
        return !IN.containsKey(m);
    }    

    /**
     * check if the vertex has dependencies
     * @param n
     * @return 
     */
     private boolean hasDepedencies(T n) {
        return dependencies.containsKey(n);
    }

    /**
     * get dependencies for a vertex
     * @param n
     * @return 
     */
    private List<T> dependencies(T n) {
        return copyList(dependencies.get(n));
    }
    
    /**
     * Add an edge to a map
     * @param E
     * @param n
     * @param m 
     */
    private static <T> void addEdgeToIndex(Map<T,List<T>> E, T n, T m) {
        E.put(n, createOrAppend(E.get(n),m));
    }
    
    /**
     * Remove an edge from a map.
     * @param E
     * @param n
     * @param m 
     */
    private static <T> void removeEdgeFromIndex(Map<T,List<T>> E, T n, T m) {
        List<T> e = E.get(n);
        if(e!=null) {
            e.remove(m);
            if(e.isEmpty()) {
                E.remove(n);
            }            
        }        
    }
    /**
     * Copy a list, return an empty list if list is null
     * @param <T> 
     * @param list
     * @return a copy of the list.
     */
    private static <T> List<T> copyList(List<T> list) {
        if(list!=null) {
            return new ArrayList<T>(list);
        } else {
            return new ArrayList<T>();
        }        
    }    

    /**
    * Add an item to a list, if the list is null create a new list.
    * @param <T> Generic
    * @param list append item to list
    * @param item the item to add
    * @return the list or a new list if the list was null
    */
    private static <T> List<T> createOrAppend(List<T> list, T item) {
        if(list == null ) {
            list = new ArrayList<>();
        }
        list.add(item);
        return list;
    }    
}
