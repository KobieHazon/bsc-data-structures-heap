import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Authors: Kobie Hazon and Itzchak Harel

/**
 * FibonacciHeap
 *
 * An implementation of fibonacci heap over non-negative integers.
 */
public class FibonacciHeap {

	private static int linkNum;
	private static int cutNum;

	private int size;
	private int marked;
	private int treeNum;
	private HeapNode first;
	private HeapNode last;
	private HeapNode beforeMin;

	/**
	 * public boolean empty()
	 *
	 * precondition: none
	 * 
	 * The method returns true if and only if the heap is empty.
	 * 
	 */
	public boolean empty() {
		return first == null; // should be replaced by student code
	}

	/**
	 * public int size()
	 *
	 * Return the number of elements in the heap
	 * 
	 */
	public int size() {
		return this.size;
	}

	/**
	 * public HeapNode findMin()
	 *
	 * Return the node of the heap whose key is minimal.
	 *
	 */
	public HeapNode findMin() {
		return this.beforeMin == null ? this.first : this.beforeMin.next;
	}

	/**
	 * public int[] countersRep()
	 *
	 * Return a counters array, where the value of the i-th entry is the number of
	 * trees of order i in the heap.
	 * 
	 */
	public int[] countersRep() {
		int[] arr = new int[(int) (Math.log(this.size) / (Math.log(1.6))) + 1];
		for (HeapNode temp = this.first; temp != null; temp = temp.next)
			arr[temp.degree]++;
		return arr;
	}

	/**
	 * public HeapNode insert(int key)
	 *
	 * Creates a node (of type HeapNode) which contains the given key, and inserts
	 * it into the heap.
	 */
	public HeapNode insert(int key) {
		HeapNode x = new HeapNode(key);
		if (this.empty()) {
			this.first = x;
			this.last = x;
		} else {
			x.next = this.first;
			if (this.beforeMin == null) {
				if (x.key > this.first.key)
					this.beforeMin = x;
			} else {
				if (x.key < this.beforeMin.next.key)
					this.beforeMin = null;
			}
			this.first = x;
		}
		this.size++;
		this.treeNum++;
		return x;
	}

	/**
	 * public void deleteMin()
	 *
	 * Delete the node containing the minimum key.
	 *
	 */
	public void deleteMin() {
		if (this.empty())
			return;
		FibonacciHeap heap2 = this.findMin().child;
		if (this.findMin().mark)
			marked--;
		if (this.beforeMin == null)
			this.first = this.first.next;
		else {
			if (this.findMin() == this.last)
				this.last = this.beforeMin;
			this.beforeMin.next = this.beforeMin.next.next;
		}
		this.size--;
		this.meld(heap2);
		if (!this.empty())
			this.consolidate();
	}

	/**
	 * public void meld (FibonacciHeap heap2)
	 *
	 * Meld the heap with heap2
	 *
	 */
	public void meld(FibonacciHeap heap2) {
		this.marked += heap2.marked;
		this.size += heap2.size;
		this.treeNum += heap2.treeNum;
		if (!this.empty()) {
			this.last.next = heap2.first;
			if (!heap2.empty()) {
				this.last = heap2.last;
			}
		} else {
			if (!heap2.empty()) {
				this.first = heap2.first;
				this.last = heap2.last;
			}
		}
		for (HeapNode tmp = heap2.first; tmp != null; tmp = tmp.next)
			tmp.parent = null;
	}

	/**
	 * public void consolidate()
	 *
	 * Consolidates the trees of the heap, by using the algorithm shown in class, thus every
	 * possible degree will have at most one tree
	 */
	public void consolidate() {
		Map<Integer, List<HeapNode>> bins = new HashMap<Integer, List<HeapNode>>();
		for (HeapNode tmp = this.first; tmp != null; tmp = tmp.next) {
			if (!bins.containsKey(tmp.degree))
				bins.put(tmp.degree, new ArrayList<HeapNode>());
			bins.get(tmp.degree).add(tmp);
		}
		for (int deg = 0; deg <= (int) (Math.log(this.size) / (Math.log(1.6))) + 1; deg++) {
			while (bins.containsKey(deg) && bins.get(deg).size() > 1) {
				HeapNode newNode = Concat(bins.get(deg).get(0), bins.get(deg).get(1));
				bins.get(deg).remove(0);
				bins.get(deg).remove(0);
				if (!bins.containsKey(deg + 1))
					bins.put(deg + 1, new ArrayList<HeapNode>());
				bins.get(deg + 1).add(newNode);
				if (bins.get(deg).size() == 0)
					bins.remove(deg);
			}
		}
		this.MapToHeap(bins);
	}

	/**
	 * public HeapNode Concat(HeapNode heap1, HeapNode heap2)
	 *
	 * Perform concats between 2 heaps, does so by finding which heap is minimal and adds other heap as
	 * son of minimal heap
	 */
	public HeapNode Concat(HeapNode heap1, HeapNode heap2) {
		FibonacciHeap.linkNum++;
		HeapNode min = heap1.key <= heap2.key ? heap1 : heap2;
		if (min == heap1)
			heap1.insertSon(heap2);
		else
			heap2.insertSon(heap1);
		min.degree++;
		return min;
	}

	/**
	 * public void MapToHeap(Map<Integer, List<HeapNode>> map)
	 *
	 * we perform the consolidate by using a map of integers to list of heapnodes so the keys are the degrees
	 * and the list contains the heaps of said degrees, so after concating (like binary addition),
	 * we need to return the map to our heap representation
	 * 
	 */
	private void MapToHeap(Map<Integer, List<HeapNode>> map) {
		this.last = null;
		this.first = null;
		HeapNode newMin = new HeapNode(Integer.MAX_VALUE);
		for (int deg : map.keySet()) {
			HeapNode oldNode = this.first;
			this.first = map.get(deg).get(0);
			if (oldNode == null)
				this.last = this.first;
			this.first.next = oldNode;
			if (newMin.key > map.get(deg).get(0).key)
				newMin = map.get(deg).get(0);
		}
		this.updateMin(newMin);
		this.treeNum = map.keySet().size();
	}

	/**
	 * public void delete(HeapNode x)
	 *
	 * Deletes the node x from the heap.
	 *
	 */
	public void delete(HeapNode x) {
		if (this.empty())
			return;
		this.decreaseKey(x, x.key - this.findMin().key + 1);
		this.deleteMin();
	}

	/**
	 * public void decreaseKey(HeapNode x, int delta)
	 *
	 * The function decreases the key of the node x by delta. The structure of the
	 * heap should be updated to reflect this chage (for example, the cascading cuts
	 * procedure should be applied if needed).
	 */
	public void decreaseKey(HeapNode x, int delta) {
		x.key -= delta;
		if (x.parent != null && x.key < x.parent.key) {
			HeapNode y = x.parent;
			cut(x, x.parent);
			cascadingCut(y);

		}
		if (x.key < this.findMin().key)
			this.updateMin(x);
	}

	/**
	 * public void cut(HeapNode x, HeapNode y)
	 *
	 * Performs cut between parent heapnode y to son heapnode x,
	 * x is added to root heapnode list
	 * 
	 */
	private void cut(HeapNode x, HeapNode y) {
		removeChild(y, x);
		FibonacciHeap.cutNum++;
		x.next = this.first;
		this.first = x;
		x.parent = null;
		if (x.mark) {
			this.marked--;
			x.mark = false;
		}
	}

	/**
	 * public void removeChild(HeapNode parent, HeapNode child)
	 *
	 * removes child from parent son's list, to do so it needs to search for it first and then
	 * update the pointers
	 * 
	 */
	private void removeChild(HeapNode parent, HeapNode child) {
		HeapNode tmp = parent.child.first;
		if (tmp == child) {
			parent.child.first = parent.child.first.next;
			tmp.parent = null;
		} else {
			while (tmp.next != null) {
				if (tmp.next == child) {
					tmp.next.parent = null;
					tmp.next = tmp.next.next;
					break;
				}
				tmp = tmp.next;
			}
		}
		parent.degree--;
		this.treeNum++;
	}

/**
		 * public void cascadingCut(HeapNode y)
		 *
		 * performs cascading cut recursively as shown in class from heapnode y
		 * 
		 */
	private void cascadingCut(HeapNode y) {
		HeapNode z = y.parent;
		if (z != null) {
			if (!y.mark) {
				y.mark = true;
				this.marked++;
			} else {
				cut(y, z);
				cascadingCut(z);
			}
		}
	}

	/**
	 * public void updateMin(HeapNode x)
	 *
	 * updates the field beforeMin so the function FindMin() will return heapnode x
	 * when called
	 * 
	 */
	private void updateMin(HeapNode x) {
		if (x == this.first)
			this.beforeMin = null;
		for (HeapNode tmp = this.first; tmp.next != null; tmp = tmp.next) {
			if (tmp.next == x) {
				this.beforeMin = tmp;
				break;
			}
		}
	}

	/**
	 * public static int totalLinks()
	 *
	 * This static function returns the total number of link operations made during
	 * the run-time of the program. A link operation is the operation which gets as
	 * input two trees of the same rank, and generates a tree of rank bigger by one,
	 * by hanging the tree which has larger value in its root on the tree which has
	 * smaller value in its root.
	 */
	public static int totalLinks() {
		return linkNum;
	}

	/**
	 * public static int totalCuts()
	 *
	 * This static function returns the total number of cut operations made during
	 * the run-time of the program. A cut operation is the operation which
	 * diconnects a subtree from its parent (during decreaseKey/delete methods).
	 */
	public static int totalCuts() {
		return cutNum;
	}

	/**
	 * public int potential()
	 *
	 * This function returns the current potential of the heap, which is: Potential
	 * = #trees + 2*#marked The potential equals to the number of trees in the heap
	 * plus twice the number of marked nodes in the heap.
	 */
	public int potential() {
		return this.treeNum + 2 * this.marked;
	}

	/**
	 * public class HeapNode
	 * 
	 * If you wish to implement classes other than FibonacciHeap (for example
	 * HeapNode), do it in this file, not in another file
	 * 
	 */
	public class HeapNode {

		private int key;
		private int degree;
		private FibonacciHeap child;
		private HeapNode next;
		private boolean mark = false;
		private HeapNode parent;

		/**
		 * public HeapNode(int key)
		 * 
		 * Contructor for HeapNode, receives key
		 * 
		 */
		public HeapNode(int key) {
			this.key = key;
			this.child = new FibonacciHeap();
		}

		/**
		 * public int getKey()
		 * 
		 * returns field key
		 * 
		 */
		public int getKey() {
			return this.key;
		}

		/**
		 * private void insertSon(HeapNode other)
		 * 
		 * adds heapnode other to list of sons of this heapnode
		 * 
		 */
		private void insertSon(HeapNode other) {
			other.next = this.child.first;
			if (this.child.empty())
				this.child.last = other;
			this.child.first = other;
			other.parent = this;
		}
	}
}
