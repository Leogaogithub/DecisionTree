package leo;

import java.util.List;

public class ID3Strategy implements SelectAttributeStrategy {

	@Override
	public int selectAttributeId(List<Integer[]> examples,
			List<Integer> attributes) {
		int attriNum = examples.get(0).length;						
		InformationGenCalc informationGenCalc = new InformationGenCalc(attriNum);
		return  informationGenCalc.getBestClassifies(examples, attributes);		
	}
}
