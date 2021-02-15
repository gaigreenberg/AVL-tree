import java.util.Arrays;
import java.util.Collections;

/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with distinct integer keys and info
 *
 */

public class AVLTree {

	private IAVLNode root;
	private IAVLNode minNode;
	private IAVLNode maxNode;

	public static void main(String[] args) {
		String space = "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
		System.out.println("let's rock & roll");
		System.out.println(space);

		int n = 100000;
		
		 Integer[] keys = randomIntArr(n); 
		 int[] keysInt = new int[n];
		 String a = Arrays.toString(keys); 
		 //toString the List or Vector 
		 String[] values= a.substring(1, a.length() - 1).split(", "); 
		 for (int i = 0; i <keys.length; i++) { 
			 keysInt[i] = keys[i];
				} 
		 //int[] keysInt = { 3, 2,4, 1, 5 }; 
		 //String[] values = { "3", "2", "4", "1", "5" };
		 //System.out.println(Arrays.toString(keysInt));
		AVLTree myTree = setTreeFromArrays(keysInt, values);
		//myTree.print2DUtil(myTree.getRoot(), 1);

		
		int count = 0, max = 0;
		
		while (myTree.getMinNode() != null) {
			int c = myTree.delete(myTree.getMinNode().getKey());
			count += c;
			// System.out.println("count =" + count);
			if (c > max) {
				max = c;
			}
		}
		float av =(float) count/n;
		System.out.println("average = " + av);
		myTree.print2DUtil(myTree.getRoot(), 1);
		System.out.println(Arrays.toString(myTree.keysToArray()));
		System.out.println("count =" + count);
		System.out.println("max = " + max);

		System.out.println("done");
	}

	private static AVLTree setTreeFromArrays(int[] keys, String[] values) {
		AVLTree tree = new AVLTree();
		for (int i = 0; i < keys.length; i++)
			tree.insert(keys[i], values[i]);

		return tree;
	}

	private static Integer[] randomIntArr(int n) {
		Integer[] arr = new Integer[n];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = i + 1;
		}
		Collections.shuffle(Arrays.asList(arr));
		return arr;
	}

	private void testJoin() {

		AVLTree myTree = this;

		AVLTree myTree2 = new AVLTree();
		myTree2.insert(15, "twenty");
		myTree2.insert(16, "twenty eight");
		myTree2.insert(17, "twenty one");
		IAVLNode y = myTree2.new AVLNode(14, "fifteen");

		// System.out.println(" tree2 size=" + myTree2.size());
		// System.out.println("min value: " + myTree2.min() + "\nmax value: " +
		// myTree2.max());
		// System.out.println("keys Array second Tree: " +
		// Arrays.toString(myTree2.keysToArray()));
		// System.out.println("info Array second Tree: " +
		// Arrays.toString(myTree2.infoToArray()));

		// myTree2.print2DUtil(myTree2.getRoot(), 10);

		myTree.join(y, myTree2);

		// System.out.println("joint tree size=" + myTree.size());
		// System.out.println("min value: " + myTree.min() + "\nmax value: " +
		// myTree.max());
		System.out.println();
		// System.out.println("keys Array Joined: " +
		// Arrays.toString(myTree.keysToArray()));
		// System.out.println("info Array Joined: " +
		// Arrays.toString(myTree.infoToArray()));

		// myTree.print2DUtil(myTree.getRoot(), 10);
	}

	/**
	 * construct empty AVL tree
	 */
	public AVLTree() {
		root = null;
		minNode = null;
		maxNode = null;

	}

	/**
	 * public boolean empty()
	 *
	 * returns true if and only if the tree is empty
	 *
	 */
	public boolean empty() { // gai
		if (this.getRoot() == null) {
			return true;
		}
		return false;
	}

	public void setRoot(IAVLNode node) {
		this.root = node;
		node.setParent(null);
	}

	/**
	 * public String search(int k)
	 *
	 * returns the info of an item with key k if it exists in the tree otherwise,
	 * returns null
	 */
	public String search(int k) // gai
	{

		IAVLNode node = this.getRoot();

		while (node != null && node.isRealNode()) {

			if (node.getKey() == k) {
				return node.getValue();
			}

			else if (node.getKey() > k) {
				node = node.getLeft();
			}

			else {
				node = node.getRight();
			}
		}
		return null;
	}

	/**
	 * public int insert(int k, String i)
	 *
	 * inserts an item with key k and info i to the AVL tree. the tree must remain
	 * valid (keep its invariants). returns the number of rebalancing operations, or
	 * 0 if no rebalancing operations were necessary. returns -1 if an item with key
	 * k already exists in the tree.
	 */
	public int insert(int k, String i) {
		IAVLNode node = new AVLNode(k, i);

		if (empty()) {
			root = node;
			maxNode = node;
			minNode = node;
			return 0;
		}

		String value = search(k);
		if (value != null) { // there is a key in tree with value k
			return -1;
		}

		else if (k < this.minNode.getKey()) {
			this.minNode = node;
		} else if (this.maxNode.getKey() < k) {
			this.maxNode = node;

		}

		IAVLNode newParent = naiveInsertion(node); // insert new node under it's new parent
		int rebalnces = rebalanceInsert(newParent, 0); // rebalance all tree from father to root if needed
		return rebalnces;

	}

	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there; the tree
	 * must remain valid (keep its invariants). returns the number of rebalancing
	 * operations, or 0 if no rebalancing operations were needed. returns -1 if an
	 * item with key k was not found in the tree.
	 */
	public int delete(int k) {
		//System.out.println("-----------------------------------------");
		//System.out.println("deleting " + k + " from tree:");
		//print2DUtil(this.getRoot(), 1);
		//System.out.println("-----------------------------------------");
		IAVLNode node = findNodeDelete(k);
		if (node == null) {
			return -1;
		}

		if (size() == 1) {
			eraseTree();
			return 0;
		}
		if (size() == 2) {
			if (node.equales(getRoot())) {
				AVLNode newRoot;
				if (node.hasOneChild() == 'R') {
					eraseTree();
					this.insert(node.getRight().getKey(), node.getRight().getValue());
				} else {
					eraseTree();
					this.insert(node.getLeft().getKey(), node.getLeft().getValue());
				}
			}
			return 0;
		}

		IAVLNode parent = naiveDelete(node);

		int parentStatus = getStatus(parent);

		if (parentStatus == 11 || parentStatus == 12 || parentStatus == 21) {
			return 0;
		}

		int rebalances = rebalnanceDeletion(parent, 0);

		return rebalances; // to be replaced by student code
	}

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree, or null if
	 * the tree is empty
	 */
	public String min() // omri
	{
		if (this.empty() == true) {
			return null;
		}
		return this.minNode.getValue();
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree, or null if the
	 * tree is empty
	 */
	public String max() // omri
	{
		if (this.empty() == true) {
			return null;
		}
		return this.maxNode.getValue();
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree, or an empty array
	 * if the tree is empty.
	 */
	public int[] keysToArray() // omri
	{
		if (empty()) {
			return new int[0];
		}

		int[] arr = new int[this.size()];
		IAVLNode n = this.minNode;
		for (int i = 0; i < arr.length; i++) {
			arr[i] = n.getKey();
			n = this.successor(n);
		}
		return arr;
	}

	/**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree, sorted by their
	 * respective keys, or an empty array if the tree is empty.
	 */
	public String[] infoToArray() // omri
	{
		String[] arr = new String[this.size()];
		IAVLNode n = this.minNode;
		for (int i = 0; i < arr.length; i++) {
			arr[i] = n.getValue();
			n = this.successor(n);
		}
		return arr;
	}

	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 *
	 * precondition: none postcondition: none
	 */
	public int size() // gai
	{
		if (empty()) {
			return 0;
		}

		int i = 1;
		IAVLNode node = this.minNode;
		while (this.successor(node) != null) {
			i++;
			node = this.successor(node);
		}

		return i;
	}

	/**
	 * public int getRoot()
	 *
	 * Returns the root AVL node, or null if the tree is empty
	 *
	 * precondition: none postcondition: none
	 */
	public IAVLNode getRoot() // gai
	{
		return this.root;

	}

	/**
	 * public string split(int x)
	 *
	 * splits the tree into 2 trees according to the key x. Returns an array [t1,
	 * t2] with two AVL trees. keys(t1) < x < keys(t2). precondition: search(x) !=
	 * null postcondition: none
	 */
	public AVLTree[] split(int x) {

		if (search(x) == null) {
			insert(x, "node for splitting");
		}
		// this.print2DUtil(getRoot(), 1);

		IAVLNode pivot = findNodeSplit(x);

		AVLTree bigger = setSubTree(pivot.getRight());
		AVLTree smaller = setSubTree(pivot.getLeft());

		if (pivot.equales(getRoot())) {
			AVLTree[] trees = { smaller, bigger };
			return trees;
		}

		IAVLNode parent = pivot.getParent();
		IAVLNode grandParent = parent.getParent();

		while ((grandParent != null)) { // while parent is not root

			if (parent.getKey() < x) {
				AVLTree subLeft = setSubTree(parent.getLeft());
				smaller.join(parent, subLeft);

			}

			else if (parent.getKey() > x) {
				AVLTree subRight = setSubTree(parent.getRight());
				bigger.join(parent, subRight);

			}

			parent = grandParent;
			grandParent = grandParent.getParent();
		}

		// now: parent == root //

		if (parent.getKey() > x) {
			AVLTree t = setSubTree(parent.getRight());
			bigger.join(parent, t);

		} else {
			AVLTree t = setSubTree(parent.getLeft());

			smaller.join(parent, t);
		}

		AVLTree[] trees = { smaller, bigger };
		return trees;
	}

	/**
	 * public join(IAVLNode x, AVLTree t)
	 *
	 * joins t and x with the tree. Returns the complexity of the operation (rank
	 * difference between the tree and t +1) precondition: keys(x,t) < keys() or
	 * keys(x,t) > keys() postcondition: none
	 */
	public int join(IAVLNode x, AVLTree t) {

		if (t.getRoot() == null || t == null && this.empty()) {
			this.insert(x.getKey(), x.getValue());
			return this.getRoot().getHeight() + 1;
		}

		else if (this.empty()) { // if join was called with empty tree and t is not empty
			this.setRoot(t.getRoot());
			this.setMax(getSubTreeMaxNode(this.getRoot()));
			this.setMin(getSubTreeMinNode(this.getRoot()));
			this.insert(x.getKey(), x.getValue());
			return this.getRoot().getHeight() + 1;
		}

		else if (this.empty() && t.empty()) { // if join was called with 2 empty trees and a node x
			this.insert(x.getKey(), x.getValue());
			return 1;
		}

		else {
			int rankDiff = this.getRoot().getHeight() - t.getRoot().getHeight();
			if (rankDiff == 0) { // check if the trees are the same height
				if (this.getRoot().getKey() > t.getRoot().getKey()) { // finding which will be right / left son of x
					x.setRight(this.getRoot());
					this.getRoot().setParent(x);
					x.setLeft(t.getRoot());
					t.getRoot().setParent(x);
				} else {
					x.setLeft(this.getRoot());
					this.getRoot().setParent(x);
					x.setRight(t.getRoot());
					t.getRoot().setParent(x);
				}
				this.setRoot(x);
				this.setMin(getSubTreeMinNode(x));
				this.setMax(getSubTreeMaxNode(x));
				return 1;
			}

			if (rankDiff > 0) { // this tree is bigger
				x.setHeight(t.getRoot().getHeight() + 1);
				if (this.getRoot().getKey() > t.getRoot().getKey()) { // this tree keys are bigger - x will be left to
																		// this.tree
					IAVLNode firstRanked = this.getRoot();
					while (firstRanked.getHeight() >= x.getHeight()) { // finding first node with height <x.rank
						firstRanked = firstRanked.getLeft(); // is the node in this tree that will switch parent to be x
					}
					x.setParent(firstRanked.getParent());
					x.setLeft(t.getRoot());
					x.setRight(firstRanked);
					t.getRoot().setParent(x);
					firstRanked.getParent().setLeft(x);
					firstRanked.setParent(x);
					this.setMin(t.getMinNode());

				}

				else { // this tree keys are smaller - x will be right to this.tree
					IAVLNode firstRanked = this.getRoot();
					while (firstRanked.getHeight() >= x.getHeight()) { // finding first node with height <x.rank
						firstRanked = firstRanked.getRight(); // is the node in this tree that will switch parent to be
																// the new node (x)
					}
					x.setParent(firstRanked.getParent());
					x.setRight(t.getRoot());
					x.setLeft(firstRanked);
					t.getRoot().setParent(x);
					firstRanked.getParent().setRight(x);
					firstRanked.setParent(x);
					this.setMax(t.getMaxNode());
				}

				rebalanceInsert(x.getParent(), 0);
				return rankDiff + 1;
			}

			else { // this.height < t.height case - splits into 2 cases
				x.setHeight(this.getRoot().getHeight() + 1);
				if (this.getRoot().getKey() > t.getRoot().getKey()) { // this tree keys are bigger than t.keys
					IAVLNode firstRanked = t.getRoot();
					while (firstRanked.getHeight() >= x.getHeight()) { // finding first node with height <x.rank
						firstRanked = firstRanked.getRight(); // is the node in this tree that will switch parent to be
																// x
					}
					x.setParent(firstRanked.getParent());
					x.setLeft(firstRanked);
					x.setRight(this.getRoot());
					this.getRoot().setParent(x);
					firstRanked.getParent().setRight(x);
					firstRanked.setParent(x);
					this.setMin(t.getMinNode());
				} else { // this tree keys are smaller than t.keys
					IAVLNode firstRanked = t.getRoot();
					while (firstRanked.getHeight() >= x.getHeight()) { // finding first node with height <x.rank
						firstRanked = firstRanked.getLeft(); // is the node in this tree that will switch parent to be x
					}
					x.setParent(firstRanked.getParent());
					x.setLeft(this.getRoot());
					x.setRight(firstRanked);
					this.getRoot().setParent(x);
					firstRanked.getParent().setLeft(x);
					firstRanked.setParent(x);
					this.setMax(t.getMaxNode());
				}
				this.setRoot(t.getRoot()); // updating this.root to be the new big tree root
				rebalanceInsert(x.getParent(), 0);
				return (rankDiff) * (-1) + 1;
			}
		}
	}

	public IAVLNode getMinNode() { // return the min node in a tree
		return this.minNode;
	}

	public IAVLNode getMaxNode() { //// return the max node in a tree
		return this.maxNode;
	}

	// setting sub functions //

	private void eraseTree() {
		root = null;
		minNode = null;
		maxNode = null;
	}

	private void setMin(IAVLNode min) {
		minNode = min;
	}

	private void setMax(IAVLNode max) {
		maxNode = max;
	}

	// split sub functions //

	private IAVLNode findNodeSplit(int k) {

		IAVLNode node = this.getRoot();
		while (node != null && node.isRealNode()) {
			if (node.getKey() == k) {
				return node;
			}

			else if (node.getKey() > k) {
				node = node.getLeft();
			}

			else {
				node = node.getRight();
			}
		}
		return null;

	}

	/**
	 * private AVLTree setSubTree(IAVLNode node) remove the sub tree from the given
	 * one. return a new AVLtree
	 * 
	 */
	private AVLTree setSubTree(IAVLNode node) {
		AVLTree tree = new AVLTree();

		if (!node.isRealNode()) {
			return tree;
		}

		IAVLNode parent = node.getParent();

		if (nodeSide(node) == 'R') {
			parent.setRight(new AVLNode());
		}

		else {
			parent.setLeft(new AVLNode());
		}

		tree.setRoot(node);
		tree.minNode = getSubTreeMinNode(node);
		tree.maxNode = getSubTreeMaxNode(node);

		return tree;
	}

	// general sub functions //

	/**
	 * Process right child first
	 */
	public void print2DUtil(IAVLNode root, int space) {
		// Base case
		if (root == null)
			return;

		// Increase distance between levels
		space += 10;

		// Process right child first
		print2DUtil(root.getRight(), space);

		// Print current node after space
		// count
		System.out.print("\n");
		for (int i = 10; i < space; i++)
			System.out.print(" ");
		System.out.print(root + "\n");

		// Process left child
		print2DUtil(root.getLeft(), space);
	}

	/**
	 * given a node with key=i return the node with key =j such: j<i && forall key<i
	 * -> j>=key
	 */
	private IAVLNode predecessor(IAVLNode node) { // gai
		if (node.equals(this.minNode)) { // node is the min in tree
			return null;
		}
		IAVLNode n = node.getLeft();
		if (n.getKey() != -1) {
			while (n.getRight().getKey() != -1) { // finds the left son's tree max node
				n = n.getRight();
			}
			return n;
		}
		if (node.getParent().getRight() == node) { // check if node's parent is his predecessor
			return node.getParent();
		}
		n = node.getParent();
		while (n.getParent().getRight() != n) {
			n = n.getParent();
		}
		return n.getParent();
	}

	/**
	 * given a node with key=i return the node with key =j such: i<j && forall key>i
	 * -> j<=key
	 */
	private IAVLNode successor(IAVLNode current) {

		if (current.equals(this.maxNode)) { // no successor
			return null;
		}

		boolean hasRightSon = (current.getRight() != null) && (current.getRight().isRealNode());

		if (hasRightSon) { // has a right child - dive in to minimum of subtree
			return getSubTreeMinNode(current.getRight());
		}

		else { // need to climb
			while (current.getParent() != null && nodeSide(current) != 'L') {
				current = current.getParent();
			}
			return current.getParent();

		}
		/**
		 * (current.equales(this.maxNode)) { // if there is no successor return null; }
		 * 
		 * else if (current.getRight().isRealNode()) { // if has a non-virtual right
		 * child
		 * 
		 * current = getSubTreeMinNode(current.getRight()); // get minimum key of right
		 * sub tree of node }
		 * 
		 * else if (current.getParent().getLeft() != null &&
		 * current.getParent().getLeft().equales(current)) { // current
		 * 
		 * current = current.getParent(); // get parent }
		 * 
		 * else { while (!(current.getParent().getLeft().equales(current))) { // while
		 * not a left child current = current.getParent(); } current =
		 * current.getParent(); }
		 * 
		 * return current;
		 */
	}

	/**
	 * return the minimum node of the subtree T T.root = current
	 */
	public static IAVLNode getSubTreeMinNode(IAVLNode current) {

		while (current.getLeft().isRealNode()) {
			current = current.getLeft();
		}

		return current;
	}

	/**
	 * return the maximum node of the subtree T T.root = current
	 */
	private static IAVLNode getSubTreeMaxNode(IAVLNode current) {

		while (current.getRight().isRealNode()) {
			current = current.getRight();
		}

		return current;
	}

	/**
	 * @pre node.isLeaf == false return 'R' if: node.getParent().getRight() == node
	 *      return 'L' if: node.getParent().getLeft() == node
	 */
	public char nodeSide(IAVLNode node) { // return which son a node is compare to its parent
		IAVLNode parent = node.getParent();
		if (parent == null) {
			return 'N';
		}
		if (parent.getRight().isRealNode()) {
			boolean rightSon = (node.equales(parent.getRight()));
			if (rightSon) {
				return 'R';
			}
		}

		return 'L';
	}

	/**
	 * return the rank difference between node and children
	 * 
	 * @return = a*10 +b -> a: rank difference with left son b: rank difference with
	 *         right son
	 */
	public static int getStatus(IAVLNode node) { // get node
		if (node.isRealNode()) {
			//System.out.println("node : " + node + " height is = " + node.getHeight());
			//System.out.println("right son h= " + node.getRight().getHeight() + "left son : " + node.getLeft().getHeight());
			int res = ((node.getHeight() - node.getLeft().getHeight()) * 10)
					+ (node.getHeight() - node.getRight().getHeight());
			if (res < 0) {
				System.out.println("status<0");
			}
			//System.out.println("res = " + res );
			return res;

		} else {
			return 11;
		}
	}

	// insert sub Functions //

	/**
	 * balance tree from given node
	 * 
	 * @param node           = node need to rebalance
	 * @param rebalanceCount = how much rebalance action have been done already
	 * @return how many rebalance action done recursional only when promote
	 */
	private int rebalanceInsert(IAVLNode node, int rebalanceCount) {

		boolean isRoot = false;
		if (node == this.getRoot()) {
			isRoot = true;
		}

		int status = getStatus(node);

		// boolean isBalanced = (status == 11 || status == 12 || status == 21);
		boolean isPromoteable = (status == 10 || status == 01); // promote and check father
		boolean is20position = (status == 20); // node is a 2-0 node
		boolean is02position = (status == 02); // node is a 0-2 node
		boolean isRotateable = (is02position || is20position); // solved by rotation

		if (isPromoteable) { // case 1
			node.promote();

			if (isRoot) {
				return rebalanceCount + 1;
			} else {
				return rebalanceInsert(node.getParent(), rebalanceCount + 1); // non - terminal (recursion to father)
			}

		}

		else if (isRotateable) { // cases 2-3

			IAVLNode problematic = new AVLNode();
			int sonStatus;

			if (is02position) {

				problematic = node.getLeft(); // son with rank diff 0

				sonStatus = getStatus(problematic);
				if (sonStatus == 12) { // if 12 -> single rotate right
					singleRotateRight(node, problematic);
					return rebalanceCount + 2; // terminal
				} else if (sonStatus == 21) { // if 21 -> double rotate right
					DoubleRotateRightInsert(node, problematic, problematic.getRight());
					return rebalanceCount + 5; // terminal
				}

			} else if (is20position) {

				problematic = node.getRight();

				sonStatus = getStatus(problematic);
				if (sonStatus == 12) { // if 12 -> double rotate left
					DoubleRotateLeftInsert(node, problematic, problematic.getLeft());
					return rebalanceCount + 2; // terminal
				} else if (sonStatus == 21) { // if 21 -> single rotate left
					singleRotateLeft(node, problematic);
					return rebalanceCount + 5; // terminal
				}
			}
		}

		return rebalanceCount;
	}

	/**
	 * insert the node to the position without rebalance the tree return the father
	 * of the node
	 */
	private IAVLNode naiveInsertion(IAVLNode newNode) {

		IAVLNode parent = treePositionInsertion(newNode.getKey());
		newNode.setParent(parent);

		if (parent.getKey() < newNode.getKey()) {

			parent.setRight(newNode);
		} else {
			parent.setLeft(newNode);
		}

		return parent;

	}

	/**
	 * locate the parent of a new node with a given key
	 * 
	 * @pre (search(k) == null) increse size of all nodes in path to new node
	 * @pre (search(k) == null) increse Height of all nodes in path to new node
	 * 
	 */
	public IAVLNode treePositionInsertion(int key) // increse size
	{

		IAVLNode node = this.getRoot();
		IAVLNode parent = null;
		while (node.isRealNode()) {

			parent = node;

			if (key < node.getKey()) {
				if (node.getLeft().isRealNode()) {
					node = node.getLeft();
				} else {
					return node;
				}
			}

			else if (key > node.getKey()) {
				if (node.getRight().isRealNode()) {
					node = node.getRight();
				} else {
					return node;
				}
			}

		}

		return parent;

	}

	/**
	 * right rotate for rebalance after insertion
	 */
	private void singleRotateRight(IAVLNode node1, IAVLNode node2) {
		char sonSide = 'n';
		IAVLNode masterParent = null;
		if (!(this.getRoot().equales(node1))) {
			masterParent = node1.getParent();
			sonSide = this.nodeSide(node1); // save which son of master parent was node 1
		}

		node2.getRight().setParent(node1);
		node1.setLeft(node2.getRight());
		node1.setParent(node2);
		node2.setRight(node1);
		switch (sonSide) {
		case 'L':
			node2.setParent(masterParent);
			masterParent.setLeft(node2);
			break;
		case 'R':
			node2.setParent(masterParent);
			masterParent.setRight(node2);
			break;
		default:
			this.setRoot(node2);
			node2.setParent(null);
			break;
		}
		node1.setHeight(node1.getHeight() - 1);

	}

	/**
	 * left rotate for rebalance after insertion
	 */
	private void singleRotateLeft(IAVLNode node1, IAVLNode node2) {
		char sonSide = 'n';
		IAVLNode masterParent = null;
		if (!this.getRoot().equales(node1)) {
			masterParent = node1.getParent();
			sonSide = this.nodeSide(node1); // save which son of master parent was node 1
		}
		node2.getLeft().setParent(node1);
		node1.setRight(node2.getLeft());
		node1.setParent(node2);
		node2.setLeft(node1);

		switch (sonSide) {
		case 'L':
			node2.setParent(masterParent);
			masterParent.setLeft(node2);
			break;
		case 'R':
			node2.setParent(masterParent);
			masterParent.setRight(node2);
			break;
		default:
			this.setRoot(node2);
			node2.setParent(null);
			break;
		}
		node1.setHeight(node1.getHeight() - 1);

	}

	private void DoubleRotateRightInsert(IAVLNode node1, IAVLNode node2, IAVLNode node3) {
		singleRotateLeft(node2, node3);
		singleRotateRight(node1, node3);
		node3.setHeight(node3.getHeight() + 1);
	}

	/**
	 * double left rotate for rebalance after insertion 1st step: right rotate 2nd
	 * step: left rotate
	 * 
	 * @param node3 = node2.leftSon
	 * @param node2 = node1.rightSon
	 */
	private void DoubleRotateLeftInsert(IAVLNode node1, IAVLNode node2, IAVLNode node3) {
		singleRotateRight(node2, node3);
		singleRotateLeft(node1, node3);
		node3.setHeight(node3.getHeight() + 1);
	}

	// delete sub Functions //

	/**
	 * private IAVLNode findNodeDelete(int k)
	 *
	 * find the node that should be deleted in the tree.
	 * 
	 * @pre no k in keys -> tree.size = @prev tree.size -1
	 */
	public IAVLNode findNodeDelete(int k) {

		IAVLNode node = this.getRoot();

		while (node != null && node.isRealNode()) {
			if (node.getKey() == k) {
				return node;
			}

			else if (node.getKey() > k) {
				node = node.getLeft();
			}

			else {
				node = node.getRight();
			}
		}
		return null;
	}

	private IAVLNode naiveDelete(IAVLNode node) {
		/**
		 * update minNode || maxNode if needed
		 */
		if (node.getKey() == minNode.getKey()) {
			minNode = this.successor(node);
		}

		else if (node.getKey() == maxNode.getKey()) {
			maxNode = this.predecessor(node);
		}

		IAVLNode parent;

		if (getRoot().equales(node)) {
			IAVLNode replacor;
			if (this.successor(node) != null) {
				replacor = this.successor(node);
				parent = replacor.getParent();
				// if (replacor.isLeaf()) {
				// deleteLeaf(replacor, this.nodeSide(replacor));
				// } else { // deleteUnaryNode();
				// parent = deleteUnaryNode(replacor, replacor.hasOneChild(),
				// this.nodeSide(replacor));
				// }
			} else {
				replacor = this.predecessor(node);
				// parent = replacor.getParent();
				// deleteLeaf(replacor, nodeSide(replacor));
			}

			parent = naiveDelete(replacor);

			replaceRoot(replacor);

			// replaceRoot(replacor);

		} else {
			parent = node.getParent();
			char mySideFromParent = nodeSide(node);
			char myOnlyChildSide = node.hasOneChild();

			if (node.isLeaf()) { // 'easy case' - simple deletion
				deleteLeaf(node, mySideFromParent);

			}

			else if (myOnlyChildSide != 'N') { // 'easy case' - simple deletion
				deleteUnaryNode(node, myOnlyChildSide, mySideFromParent);
			}

			else { // 'hard case' - replace with successor
				IAVLNode successor = successor(node);
				deleteHardCase(node, mySideFromParent, successor);
			}
		}
		return parent;

	}

	private void replaceRoot(IAVLNode replacor) {

		IAVLNode oldRoot = getRoot().copy();

		replacor.setLeft(oldRoot.getLeft());
		oldRoot.getLeft().setParent(replacor);

		replacor.setRight(oldRoot.getRight());
		oldRoot.getRight().setParent(replacor);

		replacor.setHeight(oldRoot.getHeight());

		setRoot(replacor);

	}

	/**
	 * successor is unary delete node.successor from tree; insert node.successor
	 * instead of node
	 * 
	 */
	// add Root delete
	private void deleteHardCase(IAVLNode node, char mySideFromParent, IAVLNode successor) {

		char SuccessorChildSide = successor.hasOneChild();
		char SuccessorSideFromParent = nodeSide(successor);

		if (successor.isLeaf()) {
			deleteLeaf(successor, SuccessorSideFromParent);
		} else if (node.equales(this.getRoot()) && node.hasOneChild() != 'N') {

		} else if (SuccessorChildSide == 'R' || SuccessorChildSide == 'L') {
			deleteUnaryNode(successor, successor.hasOneChild(), mySideFromParent);
		}

		successor.setParent(node.getParent());
		successor.setRight(node.getRight());
		successor.setLeft(node.getLeft());
		//successor.promote();

		if (mySideFromParent == 'R') {
			successor.getParent().setRight(successor);
		} else if (mySideFromParent == 'L') {
			successor.getParent().setLeft(successor);
		}

	}

	private void deleteUnaryNode(IAVLNode node, char myOnlyChildSide, char mySideFromParent) {
		IAVLNode parent = node.getParent();

		if (myOnlyChildSide == 'R' && mySideFromParent == 'R') {
			parent.setRight(node.getRight());
			node.getRight().setParent(parent);
			//node.getRight().promote();
		}

		else if (myOnlyChildSide == 'L' && mySideFromParent == 'R') {
			parent.setRight(node.getLeft());
			node.getLeft().setParent(parent);
			//node.getLeft().promote();
		}

		else if (myOnlyChildSide == 'R' && mySideFromParent == 'L') {
			parent.setLeft(node.getRight());
			node.getRight().setParent(parent);
			//node.getRight().promote();
		}

		else if (myOnlyChildSide == 'L' && mySideFromParent == 'L') {
			parent.setLeft(node.getLeft());
			node.getLeft().setParent(parent);
			//node.getLeft().promote();

		}

	}

	private void deleteLeaf(IAVLNode node, char side) {
		if (side == 'R') {
			node.getParent().setRight(new AVLNode());
		} else {
			node.getParent().setLeft(new AVLNode());
		}
	}

	private int rebalnanceDeletion(IAVLNode node, int rebalanceCount) {
		if (node == null) {
			return rebalanceCount;
		}
		//System.out.println("-------------------------------------");
		//System.out.println("tree to rebalance , and node: " + node );
		//print2DUtil(this.getRoot(), 1);
		//System.out.println("-------------------------------------");
		int status = getStatus(node);

		boolean isBalanced = (status == 12 || status == 21 || status == 11);
		boolean isDemotable = (status == 22);
		boolean rightChildProblem = (status == 31);

		if (isBalanced) {
			//System.out.println("balanced");
			return rebalanceCount;
		}

		else if (isDemotable) {
			node.demote();
			if (this.getRoot().equales(node)) {
				return rebalanceCount;
			}
			return rebalnanceDeletion(node.getParent(), rebalanceCount + 1);
		}

		else { // not balanced and not demoteable

			if (rightChildProblem) { // node is a 3-1 node
				int problemChildStatus = getStatus(node.getRight());
				if (problemChildStatus == 11) {
					// RotateAleft - terminal
					leftRotateA(node, node.getRight());
					return rebalanceCount + 1;
				} else if (problemChildStatus == 21) {
					// RotateBleft - non terminal
					leftRotateB(node, node.getRight());
					return rebalnanceDeletion(node.getParent().getParent(), rebalanceCount + 3);

				} else if (problemChildStatus == 12) {
					// doubleRotateleft - non terminal
					doubleRotateLeftDelete(node, node.getRight(), node.getRight().getLeft());
					return rebalnanceDeletion(node.getParent().getParent(), rebalanceCount + 6);
				}
			}

			else { // node is a 1-3 node
				int problemChildStatus = getStatus(node.getLeft());
				if (problemChildStatus == 11) {
					// RotateAright - terminal
					//System.out.println("node in RD is " + node + " status : "+ status);
					rightRotateA(node, node.getLeft());
					return rebalanceCount + 1;
				} else if (problemChildStatus == 12) {
					// RotateBright - non terminal
					rightRotateB(node, node.getLeft());
					return rebalnanceDeletion(node.getParent().getParent(), rebalanceCount + 3);
				} else if (problemChildStatus == 21) {
					// doubleRotateright - non terminal
					doubleRotateRightDelete(node, node.getLeft(), node.getLeft().getRight());
					return rebalnanceDeletion(node.getParent().getParent(), rebalanceCount + 6);
				}
			}
		}
		// for tests! //
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		System.out.println("error: status = " + status + "for:");
		System.out.println("should not have get here");

		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		// System.out.println(node + "is at hieght " + node.getHeight());
		return 0;

	}

	/**
	 * function for after deletion rebalance case 2 rotate left using the rotate
	 * left func with rank update for node 2
	 * 
	 * @param node 2 = node.rightSon
	 */
	private void leftRotateA(IAVLNode node1, IAVLNode node2) {
		this.singleRotateLeft(node1, node2);
		node2.setHeight(node2.getHeight() + 1);
	}

	/**
	 * function for after deletion rebalance case 2 rotate right using the rotate
	 * right func with rank update for node 2
	 * 
	 * @param node 2 = node.leftSon
	 */

	private void rightRotateA(IAVLNode node1, IAVLNode node2) {
		this.singleRotateRight(node1, node2);
		node2.setHeight(node2.getHeight() + 1);
	}

	/**
	 * function for after deletion rebalance case 3 rotate left using the rotate
	 * left func for insert with double downgrade for node.1rank
	 * 
	 * @param node 2 = node.rightSon
	 */
	private void leftRotateB(IAVLNode node1, IAVLNode node2) {
		this.singleRotateLeft(node1, node2);
		node1.setHeight(node1.getHeight() - 1);
	}

	/**
	 * function for after deletion rebalance case 3 rotate right using the rotate
	 * right func for insert with double downgrade for node.1rank
	 * 
	 * @param node 2 = node.leftSon
	 */
	private void rightRotateB(IAVLNode node1, IAVLNode node2) {
		this.singleRotateRight(node1, node2);
		node1.setHeight(node1.getHeight() - 1);
	}

	/**
	 * function for after deletion rebalance case 4 dobule rotate left using: 1st
	 * step: single right rotate for nodes 2,3 2nd step: single left rotate for
	 * nodes 1,3
	 * 
	 * @param node 2 = node1.rightSon
	 * @param node 3 = node2.leftSon
	 */
	private void doubleRotateLeftDelete(IAVLNode node1, IAVLNode node2, IAVLNode node3) {
		this.singleRotateRight(node2, node3);
		this.singleRotateLeft(node1, node3);
		node1.setHeight(node1.getHeight() - 1);
		node3.setHeight(node3.getHeight() + 1);
	}

	/**
	 * function for after deletion rebalance case 4 dobule rotate right using: 1st
	 * step: single left rotate for nodes 2,3 2nd step: single right rotate for
	 * nodes 1,3
	 * 
	 * @param node 2 = node1.leftSon
	 * @param node 3 = node2.rightSon
	 */
	private void doubleRotateRightDelete(IAVLNode node1, IAVLNode node2, IAVLNode node3) {
		this.singleRotateLeft(node2, node3);
		this.singleRotateRight(node1, node3);
		node1.setHeight(node1.getHeight() - 1);
		node3.setHeight(node3.getHeight() + 1);
	}

	// IAVLNode && AVLNode //

	/**
	 * public interface IAVLNode ! Do not delete or modify this - otherwise all
	 * tests will fail !
	 */
	public interface IAVLNode {
		public int getKey(); // returns node's key (for virtuval node return -1)

		public String getValue(); // returns node's value [info] (for virtuval node return null)

		public void setLeft(IAVLNode node); // sets left child

		public IAVLNode getLeft(); // returns left child (if there is no left child return null)

		public void setRight(IAVLNode node); // sets right child

		public IAVLNode getRight(); // returns right child (if there is no right child return null)

		public void setParent(IAVLNode node); // sets parent

		public IAVLNode getParent(); // returns the parent (if there is no parent return null)

		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node

		public void setHeight(int rank); // set the rank of the node

		public int getHeight(); // return the rank of the node

		// our addition to IAVLNode//

		public AVLNode copy(); // return a copy of a node

		public boolean equales(IAVLNode node); // returns true if both node has equal key and equal value

		public boolean isLeaf(); // returns true if the node has two virtual leafs as sons

		public void promote(); // increase rank of node by 1

		public void demote(); // decrease rank of node by 1

		public char hasOneChild(); // return N if node has 0 or 2 real children; 'R' if only Right child;
									// 'L'otherwise
	}

	/**
	 * public class AVLNode If you wish to implement classes other than AVLTree (for
	 * example AVLNode), do it in this file, not in another file. This class can and
	 * must be modified. (It must implement IAVLNode)
	 */
	public class AVLNode implements IAVLNode {

		private int key;
		private String value;
		private IAVLNode rightSon = null;
		private IAVLNode leftSon = null;
		private IAVLNode parent = null;
		private int height;

		/**
		 * construct a virtual leaf
		 */

		public AVLNode() {
			key = -1;
			value = null;
			leftSon = null;
			rightSon = null;
			height = -1;
		}

		public AVLNode(int key, String value) {
			AVLNode virtual1 = new AVLNode();
			AVLNode virtual2 = new AVLNode();
			this.key = key;
			this.value = value;
			leftSon = virtual1;
			rightSon = virtual2;

			virtual1.setParent(this);
			virtual2.setParent(this);

			height = 0;
		}

		public int getKey() {
			return this.key;
		}

		public String getValue() {
			return this.value;
		}

		public void setLeft(IAVLNode node) {
			this.leftSon = node;
		}

		public IAVLNode getLeft() {
			return this.leftSon;
		}

		public void setRight(IAVLNode node) {
			this.rightSon = node;
		}

		public IAVLNode getRight() {
			return this.rightSon;
		}

		public void setParent(IAVLNode node) {
			this.parent = node;
		}

		public IAVLNode getParent() {
			return this.parent;
		}

		public AVLNode copy() {
			AVLNode newNode = new AVLNode(this.getKey(), this.getValue());
			newNode.setHeight(this.getHeight());
			newNode.setLeft(this.getLeft());
			newNode.setParent(this.getParent());
			newNode.setRight(this.getRight());
			return newNode;
		}

		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode() {
			if (this.key != -1) {
				return true;
			}
			return false;
		}

		public boolean isLeaf() {
			AVLNode virtual = new AVLNode();
			boolean hasNoLeft = this.getLeft().equales(virtual);
			boolean hasNoRight = this.getRight().equales(virtual);
			boolean notVirtual = this.isRealNode();

			if (notVirtual && hasNoLeft && hasNoRight) {
				return true;
			}
			return false;
		}

		public int getHeight() {
			return this.height;
		}

		public void setHeight(int rank) {
			this.height = rank;
		}

		public boolean equales(IAVLNode node) {

			int key = node.getKey();
			if (this.key == key) {
				return true;
			}
			return false;
		}

		public void promote() {
			int rank = this.getHeight();
			this.setHeight(rank + 1);
		}

		public void demote() {
			int rank = this.getHeight();
			this.setHeight(rank - 1);
		}

		/**
		 * @return N if node has 0 or 2 real children; 'R' if only Right child;
		 *         'L'otherwise
		 */
		public char hasOneChild() {

			boolean hasRight = !(this.getRight().equales(new AVLNode()));
			boolean hasLeft = !(this.getLeft().equales(new AVLNode()));

			if (Boolean.logicalXor(hasRight, hasLeft)) {
				if (hasRight) {
					return 'R';
				} else if (hasLeft) {
					return 'L';
				}
			}
			return 'N';
		}

		public String toString() {
			String s;
			if (isRealNode()) {
				s = "[" + key + ",'r" + getHeight() + "']";
			} else {
				s = "[VL]";
			}

			return s;

		}

	}

}
