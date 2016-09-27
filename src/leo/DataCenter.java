package leo;
import java.util.LinkedList;
import java.util.List;

public class DataCenter {
	List<Integer[]> trainData;
	List<Integer[]> testData;
	String attriNames[] = null;
	List<Integer> attributs = null;	
	int attriNum = 0;
	
	DataCenter(){
		trainData = new LinkedList<>();
		testData = new LinkedList<>();		
		attriNames = null;
		attriNum = 0;
	}
	
	void update(){
		attributs = new LinkedList<>();
		for(int i = 0; i < attriNum-1; i++){
			attributs.add(i);
		}
	}
	
	public String toString(){
		StringBuilder result = new StringBuilder();
		for(String attriName : attriNames){
			result.append(attriName);
			result.append("\t");
		}
		result.append("\n");
		for(Integer record[] : trainData){
			for(Integer i: record){
				result.append(i);
				result.append("\t");
			}
			result.append("\n");			
		}
		return result.toString();
	}	

	public void parseFile(String name, boolean isTrainData){		
		List<String> lines = DataReader.readLines(name);
		String line0= UtilityTool.regularingLine(lines.get(0));
		attriNames = line0.split(" ");
		attriNum = attriNames.length;
		lines.remove(0);				
		for(String line: lines){
			if(UtilityTool.isValidLine(line)){
				line= UtilityTool.regularingLine(line);
				String [] args = line.split(" ");
				Integer record[] = new Integer[attriNum];
				int i = 0;
				for(String arg: args){
					record[i++] = Integer.parseInt(arg);
				}
				if(isTrainData){
					trainData.add(record);
				}
				else{
					testData.add(record);
				}				
			}
		}			
	}
}
