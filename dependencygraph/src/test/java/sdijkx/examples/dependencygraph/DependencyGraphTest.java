package sdijkx.examples.dependencygraph;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class DependencyGraphTest {

    /**
     * Test of addDependency method, of class DependencyGraph.
     */
    @Test
    public void testAddDependency() {
        final Object obj = new String("vertex1");
        final Object dependency = new String("dependency");
        DependencyGraph instance = new DependencyGraph();
        instance.addDependency(obj, dependency);
        assertTrue(instance.contains(obj));
        assertTrue(instance.contains(dependency));
        assertTrue(instance.containsDependency(obj, dependency));
    }

    /**
     * Test of removeDependency method, of class DependencyGraph.
     */
    @Test
    public void testRemoveDependency() {
        final Object obj = new String("vertex1");
        final Object dependency = new String("dependency");
        DependencyGraph instance = new DependencyGraph();
        instance.addDependency(obj, dependency);
        assertTrue(instance.containsDependency(obj, dependency));
        instance.removeDependency(obj, dependency);
        assertTrue(instance.contains(obj));
        assertTrue(instance.contains(dependency));
        assertFalse(instance.containsDependency(obj, dependency));

        instance.removeDependency(null, null);
        instance.removeDependency(obj, null);
        instance.removeDependency(null, obj);

    }

    /**
     * Test of process method, of class DependencyGraph.
     */
    @Test
    public void testTopologicalSort() {
        final Object obj = new String("vertex1");
        final Object vertices[] = new Object[]{new String("v1"), new String("v2"), new String("v3"), new String("v4"), new String("v5")};
        DependencyGraph instance = new DependencyGraph();
        instance.addDependency(vertices[0], vertices[1]);
        instance.addDependency(vertices[1], vertices[2]);
        instance.addDependency(vertices[1], vertices[3]);
        instance.addDependency(vertices[3], vertices[2]);
        instance.addDependency(vertices[4], vertices[2]);
        final List results = instance.topologicalSort();
        assertEquals(vertices[2], results.get(0));
        assertEquals(vertices[3], results.get(1));
        assertEquals(vertices[4], results.get(2));
        assertEquals(vertices[1], results.get(3));
        assertEquals(vertices[0], results.get(4));
    }

    /**
     * Test of process method, of class DependencyGraph.
     */
    @Test
    public void testDisjoint() {
        System.out.println("process");
        final Object obj = new String("vertex1");
        final Object vertices[] = new Object[]{new String("v1"), new String("v2"), new String("v3"), new String("v4"), new String("v5")};
        DependencyGraph instance = new DependencyGraph();
        instance.addDependency(vertices[0], vertices[1]);
        instance.addDependency(vertices[0], vertices[2]);
        instance.addDependency(vertices[3], vertices[4]);
        final List results = instance.topologicalSort();
        assertEquals(vertices[1], results.get(0));
        assertEquals(vertices[2], results.get(1));
        assertEquals(vertices[4], results.get(2));
        assertEquals(vertices[0], results.get(3));
        assertEquals(vertices[3], results.get(4));
    }

    /**
     * Test of detect cysle in process method, of class DependencyGraph.
     */
    @Test(expected = Exception.class)
    public void testDetectCycle() {
        final Object vertices[] = new Object[]{new String("v1"), new String("v2"), new String("v3"), new String("v4")};
        DependencyGraph instance = new DependencyGraph();
        instance.addDependency(vertices[0], vertices[1]);
        instance.addDependency(vertices[1], vertices[2]);
        instance.addDependency(vertices[2], vertices[0]);
        instance.addDependency(vertices[2], vertices[3]);
        List results = instance.topologicalSort();
    }

    /**
     * Test sort with empty dependencies.
     */
    @Test(expected = Exception.class)
    public void testNoRootValidationsFound() {
        DependencyGraph instance = new DependencyGraph();
        List results = instance.topologicalSort();
        assertTrue(results.isEmpty());
    }

    /**
     * Test sort with empty dependencies.
     */
    @Test
    public void testNoDependencies() {
        final Object vertices[] = new Object[]{new String("v1"), new String("v2")};
        DependencyGraph instance = new DependencyGraph();
        instance.addDependency(vertices[0], vertices[1]);
        instance.removeDependency(vertices[0], vertices[1]);
        List results = instance.topologicalSort();
        assertEquals(2, results.size());
    }

    /**
     * Test of contains dependency
     */
    @Test
    public void testContainsDepecency() {
        final Object vertices[] = new Object[]{new String("v1"), new String("v2")};
        DependencyGraph instance = new DependencyGraph();
        instance.addDependency(vertices[0], vertices[1]);
        assertTrue(instance.containsDependency(vertices[0], vertices[1]));
    }

    /**
     * Test of remove
     */
    @Test
    public void testRemove() {
        final Object vertices[] = new Object[]{new String("v1"), new String("v2")};
        DependencyGraph instance = new DependencyGraph();
        instance.addDependency(vertices[0], vertices[1]);
        instance.remove(vertices[0]);
        assertFalse(instance.containsDependency(vertices[0], vertices[1]));
        assertFalse(instance.contains(vertices[0]));
        assertTrue(instance.contains(vertices[1]));
    }

    /**
     * Test of contains dependency tree
     */
    @Test
    public void testDependencyTree() {
        final Object vertices[] = new Object[]{new String("v1"), new String("v2"), new String("v3"), new String("v4")};
        DependencyGraph instance = new DependencyGraph();
        instance.addDependency(vertices[1], vertices[0]);
        instance.addDependency(vertices[1], vertices[2]);
        instance.addDependency(vertices[2], vertices[0]);
        instance.addDependency(vertices[2], vertices[3]);
        TreeNode tree = instance.dependencyTree();
        assertEquals(2, tree.getChildren().size());
    }

    /**
     * Test of contains dependency tree
     */
    @Test
    public void testDependencyTreeWithCycle() {
        final Object vertices[] = new Object[]{new String("v1"), new String("v2"), new String("v3"), new String("v4")};
        DependencyGraph instance = new DependencyGraph();
        instance.addDependency(vertices[0], vertices[1]);
        instance.addDependency(vertices[1], vertices[2]);
        instance.addDependency(vertices[2], vertices[0]);
        instance.addDependency(vertices[2], vertices[3]);
        TreeNode tree = instance.dependencyTree();
        assertEquals(1, tree.getChildren().size());
    }

    /**
     * Test of contains dependency tree
     */
    @Test
    public void testDependencyTreeWithoutDependencies() {
        final Object vertices[] = new Object[]{new String("v1"), new String("v2")};
        DependencyGraph instance = new DependencyGraph();
        instance.addDependency(vertices[0], vertices[1]);
        instance.removeDependency(vertices[0], vertices[1]);
        TreeNode tree = instance.dependencyTree();
        assertEquals(2, tree.getChildren().size());
    }

}
