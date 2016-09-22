public class TreeNode {	
	int num[] = null;
	int label = 0;
	int attrId = 0;
	//List<TreeNode> childern;
	TreeNode left = null;
	TreeNode right = null;
	TreeNode(){
		num = new int[2];
		label = 0;
		attrId = 0;
		//childern = new LinkedList<TreeNode>();
		left = null;
		right = null;
	}
	
	boolean hasChild(){
		return (left!=null) || (right!=null);
	}	
}
