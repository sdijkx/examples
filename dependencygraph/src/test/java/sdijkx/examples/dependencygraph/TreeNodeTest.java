package sdijkx.examples.dependencygraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TreeNodeTest {

    @Test
    public void testHasNullParent() {
        TreeNode treeNode = new TreeNode(null, null);
        assertFalse(treeNode.hasParent(null));

        TreeNode childNode = new TreeNode(new Object(), treeNode);
        assertFalse(childNode.hasParent(new Object()));
        assertTrue(childNode.hasParent(null));
    }

    @Test
    public void testHasParent() {
        Object root = new Object();
        TreeNode treeNode = new TreeNode(root, null);
        TreeNode childNode = new TreeNode(new Object(), treeNode);
        assertFalse(childNode.hasParent(new Object()));
        assertFalse(childNode.hasParent(null));
        assertTrue(childNode.hasParent(root));
    }

    @Test
    public void testChildren() {
        TreeNode treeNode = new TreeNode(null, null);
        assertNotNull(treeNode.getChildren());
        assertEquals(0, treeNode.getChildren().size());
        treeNode.addChild(new TreeNode(new Object(), treeNode));
        assertEquals(1, treeNode.getChildren().size());
    }

    @Test
    public void testGetItem() {
        Object root = new Object();
        TreeNode treeNode = new TreeNode(root, null);
        assertEquals(root, treeNode.getItem());
    }
}
