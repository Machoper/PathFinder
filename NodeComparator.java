//Yixuan Qian

import java.util.Comparator;

public class NodeComparator implements Comparator<Node>{

	@Override
	public int compare(Node n1, Node n2) {
		if (n1.Fvalue > n2.Fvalue) {
			return 1;
		}
		else if (n1.Fvalue < n2.Fvalue) {
			return -1;
		}
		else return 0;
	}

}
