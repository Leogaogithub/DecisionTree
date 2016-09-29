package leo;

import java.util.List;

public interface SelectAttributeStrategy {
	public int selectAttributeId(List<Integer[]> examples, List<Integer> attributes);
}
