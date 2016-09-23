package leo;

public class DecisionTree {
	
	TreeNode root = null;
	int attriNum = 0;
	DataCenter dCenter = null;
	
	DecisionTree(){
		root = null;
		dCenter = new DataCenter();
	}

	void parseFile(String filename, boolean isTrainData){
		dCenter.parseFile(filename, isTrainData);
		dCenter.update();		
		this.attriNum = dCenter.attriNum;
	}
	
	public static void main(String[] args) {
		DecisionTree dTree = new DecisionTree();		
		dTree.parseFile("./data/train2.dat", true);	
		dTree.parseFile("./data/test2.dat", false);	
		dTree.run();
		
		
	}
	
	void run(){
		ID3Algorithm id3Algorithm = new ID3Algorithm();
		root = id3Algorithm.ID3(dCenter.trainData, dCenter.attributs);
		print(root, 0);
		line("");
		line("Pre-Pruned Accuracy");
		line(getSummary());
		line("\n\n");
		print(root, 0);
		line("");
		line("Post-Pruned Accuracy");
		line(getSummary());
		line("");
	}
	
	void print(TreeNode node, int level){
		StringBuilder cur = new StringBuilder();
		for(int i = 0; i< level; i++){
			cur.append("|  ");			
		}
		cur.append(dCenter.attriNames[node.attrId]);
		cur.append(" = ");	
		int length = cur.length();
		cur.append(0 + " : ");		
		if(node.left == null){
			cur.append(node.label);
			line(cur.toString());
		}
		
		if(node.left!=null){
			if(node.left.hasChild()){
				line(cur.toString());
				print(node.left, level+1);
			}else{
				cur.append(node.left.label);
				line(cur.toString());
			}
		}
		
		cur.setLength(length);
		cur.append(1 + " : ");
		
		if(node.right == null){
			cur.append(node.label);
			line(cur.toString());
		}
		
		if(node.right!=null){
			if(node.right.hasChild()){
				line(cur.toString());
				print(node.right, level+1);
			}else{
				cur.append(node.right.label);
				line(cur.toString());
			}
		}
		
	}
	
	void line(String str){
		System.out.println(str);
	}
	
	void prune(){
		
	}
	
	int getTotalLeafNode(TreeNode node){
		if(node == null) return 0;
		if(!node.hasChild()) return 1;		
		return getTotalLeafNode(node.left)+getTotalLeafNode(node.right);
	}
	
	int getTotalNode(TreeNode node){
		if(node == null) return 0;				
		return 1+ getTotalNode(node.left)+getTotalNode(node.right);
	}
	
	public String getSummary(){
		StringBuilder summry = new StringBuilder();
		Performance performance = new Performance(root);
		summry.append("------------------------------------------------------------\n");
		summry.append("Number of training instances = ");
		summry.append(dCenter.trainData.size()+"\n");
		summry.append("Number of training attributes = ");
		summry.append(dCenter.trainData.get(0).length + "\n");		
		summry.append("Total number of nodes in the tree = ");
		summry.append(getTotalNode(root)+ "\n");
		summry.append("Number of leaf nodes in the tree = ");
		summry.append(getTotalLeafNode(root)+ "\n");
		summry.append("Accuracy of the model on the training dataset = ");
		summry.append(performance.getAccuracy(dCenter.trainData)*100 + "%\n");
		
		summry.append("\nNumber of testing instances = ");
		summry.append(dCenter.testData.size()+ "\n");
		summry.append("Number of testing attributes = ");
		summry.append(dCenter.testData.get(0).length+ "\n");
		summry.append("Accuracy of the model on the testing dataset = ");
		summry.append(performance.getAccuracy(dCenter.testData)*100 + "%\n");		
		return summry.toString();
	}
	
	
}
