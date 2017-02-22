//Yixuan Qian

import java.awt.Point;

public class Node {
	Point point;
	int Gvalue;
	int Hvalue;
	int Fvalue;
	Node parent;
	
	public Node(Point pt, int g, int h, Node p) {
		this.point = pt;
		this.Gvalue = g;
		this.Hvalue = h;
		this.Fvalue = h + g;
		this.parent = p;
	}

}
