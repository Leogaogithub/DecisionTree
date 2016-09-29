package leo;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import javax.sound.sampled.Line;

public class DecisionTree {	
	TreeNode root = null;
	int attriNum = 0;
	DataCenter dCenter = null;
	double prunrate = 0;
	
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
		if(args.length !=2){
			System.out.println("Usage: java -jar DecisionTree.jar  [train_data] [test_data]");
			return;
		}
		String trainFileName = args[0];
		String testFileName = args[1];	
		//String trainFileName = "./data/train.dat";
		//String testFileName = "./data/test.dat";
		DecisionTree dTree = new DecisionTree();
		//dTree.prunrate = Double.parseDouble(args[2]);
		//dTree.prunrate = 0.2;
		dTree.parseFile(trainFileName, true);	
		dTree.parseFile(testFileName, false);		
		//dTree.runPruneUsingID3();	
		dTree.runCompareID3andRandom();
	}
	
	void runCompareID3andRandom(){
		DecisionTreeGenerator decisionTreeGenerator = new DecisionTreeGenerator();
		ID3Strategy id3Strategy = new ID3Strategy();
		decisionTreeGenerator.setStrategy(id3Strategy);
		root = decisionTreeGenerator.generateDecisionTree(dCenter.trainData, dCenter.attributs);
		print(root, 0);
		line("");
		line("ID3 Accuracy");
		line(getSummary());
		line("\n\n");
		
		RandomAttributeSelectionStrategy randomStrategy = new RandomAttributeSelectionStrategy();
		decisionTreeGenerator.setStrategy(randomStrategy);
		root = decisionTreeGenerator.generateDecisionTree(dCenter.trainData, dCenter.attributs);
		prune();
		print(root, 0);
		line("");
		line("Random Attribute Selection Accuracy");
		line(getSummary());
		line("");
		
	}
	
	
	
	void runPruneUsingID3(){
		DecisionTreeGenerator decisionTreeGenerator = new DecisionTreeGenerator();
		ID3Strategy id3Strategy = new ID3Strategy();
		decisionTreeGenerator.setStrategy(id3Strategy);
		root = decisionTreeGenerator.generateDecisionTree(dCenter.trainData, dCenter.attributs);
		print(root, 0);
		line("");
		line("Pre-Pruned Accuracy");
		line(getSummary());
		line("\n\n");
		
		prune();
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
		int totalNode = getTotalNode(root);
		int prunsize = (int)(prunrate*totalNode);
		updateTreeNodeId();
		Random random = new Random();
		Set<Integer> prunNodeSet = new HashSet<>();
		while(prunNodeSet.size() != prunsize){
			prunNodeSet.add(2+random.nextInt(totalNode+1));
		}	
		for(Integer nodeId : prunNodeSet){
			pruneNode(nodeId);
		}
	}
	
	void pruneNode(int nodeId){
		TreeNode node = searchNodeFromId(nodeId);
		if(node==null) return;
		TreeNode parent = node.parent;
		parent.label = parent.num[0] > parent.num[1] ? 0: 1;
		if(node==parent.right){
			parent.right = null;
		}else{
			parent.left = null;
		}		
	}
	// if nodeId does not exist, return null
	TreeNode searchNodeFromId(int nodeId){
		TreeNode node = root;
		Queue<TreeNode> queue = new LinkedList<TreeNode>();
		queue.add(root);		
		while(!queue.isEmpty()){			
			node = queue.poll();
			if(node==null) continue;
			if(node.nodeId == nodeId) return node;
			if(node.nodeId > nodeId) break; 			
			queue.add(node.left);
			queue.add(node.right);						
		}
		return null;
	}
	
	//update id and parent field
	void updateTreeNodeId(){
		TreeNode node = root;
		Queue<TreeNode> queue = new LinkedList<TreeNode>();
		queue.add(root);
		int idx = 1;
		while(!queue.isEmpty()){			
			node = queue.poll();			
			node.nodeId = idx;						
			if(node.left!=null){
				node.left.parent = node;
				queue.add(node.left);
			}
			if(node.right!=null){
				node.right.parent = node;
				queue.add(node.right);
			}
			idx++;			
		}
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
	
	int getSumDepthLeafNode(TreeNode node, int deep){
		if(node == null) return 0;
		if(!node.hasChild()) return deep;		
		return getSumDepthLeafNode(node.left, deep+1)+getSumDepthLeafNode(node.right,deep+1);
	}	
	
	
	public String getSummary(){
		StringBuilder summry = new StringBuilder();
		double averageDepth = getSumDepthLeafNode(root, 0)/(double)getTotalLeafNode(root);
		
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
		summry.append("Average	Depth  = ");
		summry.append(averageDepth + "\n");
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
