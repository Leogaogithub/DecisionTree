import java.util.LinkedList;
import java.util.List;


public class DecisionTree {
	
	TreeNode root = null;
	int attriNum = 0;
	DataCenter dCenter = null;
	
	DecisionTree(){
		root = null;
		dCenter = new DataCenter();
	}

	void parseFile(String filename){
		dCenter.parseFile(filename, true);
		dCenter.update();		
		this.attriNum = dCenter.attriNum;
	}
	
	public static void main(String[] args) {
		DecisionTree dTree = new DecisionTree();		
		dTree.parseFile("./data/train2.dat");		
		dTree.run();
		dTree.print(dTree.root, 0);
	}
	
	void run(){
		root = ID3(dCenter.trainData, dCenter.attributs);
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
	
	TreeNode ID3(List<Integer[]> examples, List<Integer> attributes){
		//root attach attribute
		//leaf don't attach attribute
		TreeNode result =  new TreeNode();		
		InformationGenCalc informationGenCalc = new InformationGenCalc(attriNum);
		result.num[0] = informationGenCalc.counts(examples, attriNum-1, 0);
		result.num[1] = examples.size() - result.num[0];
		//If all examples are positive, Return the single-node tree Root, with label = +.
		if(result.num[0]==examples.size()){
			result.label = 0;
			return result;
		}
		//If all examples are negative, Return the single-node tree Root, with label = -.
		if(result.num[1]==examples.size()){
			result.label = 1;
			return result;
		}
		//If number of predicting attributes is empty, then Return the single node tree Root,
	    //with label = most common value of the target attribute in the examples.
		if(attributes.size() <= 0){
			result.label = result.num[0] > result.num[1] ? 0:1;
			return result;
		}
		
		int bestAttri = informationGenCalc.getBestClassifies(examples, attributes);	
		result.attrId = bestAttri;		
		result.label = result.num[0] > result.num[1] ? 0:1;		
		List<Integer[]> examplesLeft = new LinkedList<Integer[]>();
		List<Integer[]> examplesRight = new LinkedList<Integer[]>();
		
		for(Integer[] example: examples){
			if(example[bestAttri] == 1){
				examplesRight.add(example);
			}else{
				examplesLeft.add(example);
			}
		}
		
		LinkedList<Integer> attributesLeft = (LinkedList<Integer>) ((LinkedList<Integer>)attributes).clone();
		Integer xInteger = bestAttri;
		attributesLeft.remove(xInteger);
		LinkedList<Integer> attributesRight = (LinkedList<Integer>) attributesLeft.clone();
		if(examplesLeft.size() > 0){
			result.left  = ID3(examplesLeft,  attributesLeft);
		}else{
			TreeNode left =  new TreeNode();
			//???
			left.label = result.label;
			result.left = null;
		}
		if(examplesRight.size() > 0){
			result.right = ID3(examplesRight,  attributesRight);
		}else{
			TreeNode right =  new TreeNode();
			//???
			right.label = result.label;
			result.right = null;
		}
		return result;
	}
}
