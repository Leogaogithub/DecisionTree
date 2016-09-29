package leo;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomAttributeSelectionStrategy implements SelectAttributeStrategy {

		@Override
		public int selectAttributeId(List<Integer[]> examples,List<Integer> attributes) {							
			int size = attributes.size();
			Random random = new Random();
			//Collections.shuffle(attributes);
			int idx = random.nextInt(size);
			return  attributes.get(idx);	
			//return attributes.get(0);
		}	
}
