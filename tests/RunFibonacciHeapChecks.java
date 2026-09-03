public class RunFibonacciHeapChecks {
    public static void main(String[] args) {
        testInsertFindMinAndDeleteMin();
        testMeldDecreaseKeyAndDelete();
        System.out.println("All Fibonacci heap checks passed");
    }

    private static void testInsertFindMinAndDeleteMin() {
        FibonacciHeap heap = new FibonacciHeap();
        check(heap.empty(), "new heap is empty");
        checkEquals(0, heap.size(), "empty heap size");
        checkEquals(null, heap.findMin(), "empty heap has no min");

        heap.insert(7);
        heap.insert(3);
        heap.insert(9);
        check(!heap.empty(), "heap with inserts is not empty");
        checkEquals(3, heap.size(), "heap size after inserts");
        checkEquals(3, heap.findMin().getKey(), "find minimum after inserts");
        checkEquals(3, heap.potential(), "three roots before consolidation");

        heap.deleteMin();
        checkEquals(2, heap.size(), "size after delete-min");
        checkEquals(7, heap.findMin().getKey(), "new min after delete-min");
        check(heap.countersRep().length > 0, "counters representation exists for non-empty heap");
    }

    private static void testMeldDecreaseKeyAndDelete() {
        FibonacciHeap left = new FibonacciHeap();
        FibonacciHeap.HeapNode ten = left.insert(10);
        FibonacciHeap.HeapNode thirty = left.insert(30);

        FibonacciHeap right = new FibonacciHeap();
        right.insert(5);
        right.insert(20);
        left.meld(right);
        checkEquals(4, left.size(), "melded heap size");
        checkEquals(5, left.findMin().getKey(), "melded heap min");

        left.decreaseKey(thirty, 28);
        checkEquals(2, left.findMin().getKey(), "decrease-key can create new min");

        left.delete(ten);
        checkEquals(3, left.size(), "size after deleting a node handle");
        checkEquals(null, findByDeleting(left, 10), "deleted key is absent from remaining heap");
    }

    private static Integer findByDeleting(FibonacciHeap heap, int key) {
        while (!heap.empty()) {
            int current = heap.findMin().getKey();
            if (current == key) {
                return current;
            }
            heap.deleteMin();
        }
        return null;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void checkEquals(Object expected, Object actual, String message) {
        if (expected == null) {
            if (actual != null) {
                throw new AssertionError(message + ": expected null but got " + actual);
            }
            return;
        }
        if (!expected.equals(actual)) {
            throw new AssertionError(message + ": expected " + expected + " but got " + actual);
        }
    }
}
