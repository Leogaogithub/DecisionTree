package leo;
import java.util.List;

public class Performance {
	TreeNode dTreeRoot;	
	public Performance(TreeNode root) {
		dTreeRoot = root;		
	}
	
	double getAccuracy( List<Integer[]> data){
		double total = data.size();
		int correctNum = 0;
		int outid = data.get(0).length-1;
		double accuracy = 0;
		for(Integer[]d : data){
			int pre = predit(d);
			if(pre == d[outid]){
				correctNum++;
			}						
		}
		accuracy = correctNum/total;
		return accuracy;
	}	
	
	int predit(Integer[] example){
		TreeNode node = dTreeRoot;
		int pred = 0;
		while(node != null){
			int attriId = node.attrId;
			pred = node.label;
			if(example[attriId] == 1){
				node = node.right;				
			}else{
				node = node.left;
			}			
		}
		return pred;
	}	
}
