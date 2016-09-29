package leo;

import java.util.LinkedList;
import java.util.List;

public class DecisionTreeGenerator {
	SelectAttributeStrategy strategy = null;
	public void setStrategy(SelectAttributeStrategy strategy){
		this.strategy = strategy;
	}
	TreeNode generateDecisionTree(List<Integer[]> examples, List<Integer> attributes){
		int attriNum = examples.get(0).length;
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
		
		//int bestAttri = informationGenCalc.getBestClassifies(examples, attributes);
		int bestAttri = strategy.selectAttributeId(examples, attributes);
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
			result.left  = generateDecisionTree(examplesLeft,  attributesLeft);
		}else{
			TreeNode left =  new TreeNode();
			//???
			left.label = result.label;
			result.left = null;
		}
		if(examplesRight.size() > 0){
			result.right = generateDecisionTree(examplesRight,  attributesRight);
		}else{
			TreeNode right =  new TreeNode();
			//???
			right.label = result.label;
			result.right = null;
		}
		return result;
	}
}
