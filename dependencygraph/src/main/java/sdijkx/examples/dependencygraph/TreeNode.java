package sdijkx.examples.dependencygraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Node of a dependency tree
 */
public class TreeNode<T> {
    
    /**
     * The data item.
     */
    private final T item;
    
    /**
     * Parent node of tree, null if it is the root
     */
    private final TreeNode<T> parent;
    
    /**
     * Children of tree node
     */
    private final List<TreeNode<T>> children;
    
    /**
     * Create a tree node for an item and parent.
     * @param item
     * @param parent 
     */
    public TreeNode(T item, TreeNode<T> parent) {
        this.item = item;
        this.parent = parent;
        this.children = new ArrayList<TreeNode<T>>();
    }
    
    /**
     * Add a child tree node
     * @param child 
     */
    public void addChild(TreeNode<T> child) {
        this.children.add(child);
    }

    /**
     * Get the item of the node
     * @return item
     */
    public T getItem() {
        return item;
    }

    /**
     * Get all children
     * @return list of child nodes
     */
    public List<TreeNode<T>> getChildren() {
        return children;
    }
    
    /**
     * check if the parentItem exists in one of the parent nodes.
     * @param parentItem, item to search
     * @return true if it exists false if not.
     */
    public boolean hasParent(T parentItem) {
        if(parent == null) {
            return false;
        }
        if( parent.getItem()==null && parentItem == null ) {
            return true;
        }
        if( parent.getItem()!=null && parent.getItem().equals(parentItem)) {
            return true;
        }
        return parent.hasParent(parentItem);
    }
}
