package leo;
import java.util.List;


public class InformationGenCalc {
	int attriNum = 0;
	void setAttriNum(int attriNum ){
		this.attriNum = attriNum;
	}
	
	InformationGenCalc(int attriNum ){
		this.attriNum = attriNum;
	}
	
	public int counts(List<Integer[]> examples, int colum, int target){
		int count = 0;
		for(Integer[] e : examples){
			if(target==e[colum]) count++;
		}
		return count;
	}
	
	private int countsOnCondition(List<Integer[]> examples, int conditionColum, int conditionValue, int colum, int target){
		int count = 0;
		for(Integer[] e : examples){
			if(e[conditionColum]==conditionValue && target==e[colum]) count++;
		}
		return count;
	}
	
	int getBestClassifies(List<Integer[]> examples, List<Integer> attributes){
		int bestAttr = 0;
		double max = Integer.MIN_VALUE;
		for(Integer attr: attributes){
			double cur = informationGen(examples, attr);
			if(max < cur){
				max = cur;
				bestAttr = attr;
			}
		}
		return bestAttr;
	}
	
	private double entropy(int pos, int neg){
		double result = 0;
		double total = pos + neg;
		double probability = pos/total;
		if(pos!=0){
			result -= probability*(Math.log10(probability)/Math.log10(2.0));
		}
		if(neg!=0){
			result -= (1-probability)*(Math.log10( 1 - probability)/Math.log10(2.0));
		}
		return result;
	}
	
	double informationGen(List<Integer[]> examples, int attributes){
		double result = 0;
		int total = examples.size();
		// calculate entropy of root
		int yPos = counts(examples, attriNum-1, 1);
		int yNeg = total - yPos;
		double entropySef = entropy(yPos,yNeg);
		int totalLeft = counts(examples, attributes, 0);
		int totalRight = total - totalLeft;
		// calculate entropy of left child
		int yPosLeft = countsOnCondition(examples,attributes, 0, attriNum-1, 1);
		int yNegLeft = totalLeft - yPosLeft;
		double entropyLeft = entropy(yPosLeft,yNegLeft);
		// calculate entropy of right child
		int yPosRight = countsOnCondition(examples,attributes, 1, attriNum-1, 1);
		int yNegRight = totalRight - yPosRight;
		double entropyRight = entropy(yPosRight,yNegRight);
		
		result = entropySef;
		result -= (totalLeft/(double)total)*entropyLeft;
		result -= (totalRight/(double)total)*entropyRight;
		
		return result;
	}
}
