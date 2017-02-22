// Yixuan Qian

import world.Robot;

import world.World;

import java.awt.*;
import java.util.*;

public class MyRobot extends Robot {
    boolean isUncertain = false;
    Comparator<Node> comp = new NodeComparator();
    PriorityQueue<Node> openSet = new PriorityQueue<Node>(comp);
    ArrayList<Node> closedSet = new ArrayList<Node>();
    Point start;
    Point end;
    int Hvalues[][];
    int row;
    int column;
    ArrayList<Node> path = new ArrayList<Node>();
    ArrayList<Node> openSetList = new ArrayList<Node>();
    
    public static void main(String[] args) {
        try {
        	//World myWorld1 = new World("myInputFile1.txt", true);
        	//World myWorld2 = new World("myInputFile2.txt", true);
        	//World myWorld3 = new World("myInputFile3.txt", false);
			//World myWorld4 = new World("myInputFile4.txt", false);
        	//World myWorld5 = new World("Puzzle1.txt", false);
        	World myWorld6 = new World("Puzzle2.txt", false);
			MyRobot robot = new MyRobot();
			            
			robot.addToWorld(myWorld6);
			//robot.addToWorld(myWorld2);
			//robot.addToWorld(myWorld3);
			//robot.addToWorld(myWorld4);
			//robot.addToWorld(myWorld5);
			
			robot.travelToDestination();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    @Override
    public void addToWorld(World world) {
        isUncertain = world.getUncertain();
        start = world.getStartPos();
        end = world.getEndPos();
        computeHvalues(world);
        super.addToWorld(world);
    }
    
    @Override
    public void travelToDestination() {
    	Point p = super.getPosition();
    	Node fd = null;
    	Node current = new Node(p, 0, Hvalues[p.x][p.y], null);
    	
    	if (isUncertain == true) {
			fd = UnCertain(current);
        }
        else {
			fd = Certain(current);
        }
		reconstruct_path(fd);
    }

    
	public ArrayList<Node> getAdjacent(Node node){
		ArrayList<Node> adj = new ArrayList<Node>();
		int i = node.point.x;
		int j = node.point.y;
		int straightCost = 1000;
		int diagonalCost = 1414;
		if(i-1 >= 0){
			Point p1 = new Point(i-1,j);
			String s1 = super.pingMap(p1);
			if(!s1.equals("X")){
				int H = Hvalues[i-1][j];
				Node parent = node;
				int G = parent.Gvalue + straightCost;
				Node n = new Node(p1,G,H,parent);
				adj.add(n);
			}
			if(j-1 >= 0){
				Point p2 = new Point(i-1,j-1);
				String s2 = super.pingMap(p2);
				if(!s2.equals("X")){
					int H = Hvalues[i-1][j-1];
					Node parent = node;
					int G = parent.Gvalue + diagonalCost;
					Node n = new Node(p2,G,H,parent);
					adj.add(n);
				}
			}
		}
		if(j+1 < column){
			Point p1 = new Point(i,j+1);
			String s1 = super.pingMap(p1);
			if(!s1.equals("X")){
				int H = Hvalues[i][j+1];
				Node parent = node;
				int G = parent.Gvalue + straightCost;
				Node n = new Node(p1,G,H,parent);
				adj.add(n);
			}
			if(i-1 >= 0){
				Point p2 = new Point(i-1,j+1);
				String s2 = super.pingMap(p2);
				if(!s2.equals("X")){
					int H = Hvalues[i-1][j+1];
					Node parent = node;
					int G = parent.Gvalue + diagonalCost;
					Node n = new Node(p2,G,H,parent);
					adj.add(n);
				}
			}
		}
		if(i+1 < row){
			Point p1 = new Point(i+1,j);
			String s1 = super.pingMap(p1);
			if(!s1.equals("X")){
				int H = Hvalues[i+1][j];
				Node parent = node;
				int G = parent.Gvalue + straightCost;
				Node n = new Node(p1,G,H,parent);
				adj.add(n);
			}
			if(j+1 < column){
				Point p2 = new Point(i+1,j+1);
				String s2 = super.pingMap(p2);
				if(!s2.equals("X")){
					int H = Hvalues[i+1][j+1];
					Node parent = node;
					int G = parent.Gvalue + diagonalCost;
					Node n = new Node(p2,G,H,parent);
					adj.add(n);
				}
			}
		}
		if(j-1 >= 0){
			Point p1 = new Point(i,j-1);
			String s1 = super.pingMap(p1);
			if(!s1.equals("X")){
				int H = Hvalues[i][j-1];
				Node parent = node;
				int G = parent.Gvalue + straightCost;
				Node n = new Node(p1,G,H,parent);
				adj.add(n);
			}
			if(i+1 < row){
				Point p2 = new Point(i+1,j-1);
				String s2 = super.pingMap(p2);
				if(!s2.equals("X")){
					int H = Hvalues[i+1][j-1];
					Node parent = node;
					int G = parent.Gvalue + diagonalCost;
					Node n = new Node(p2,G,H,parent);
					adj.add(n);
				}
			}
		}
		return adj;
	}
    
    public void reach(Node node) {
    	path.add(node);
    	while(node.parent != null) {
    		path.add(node.parent);
    		node = node.parent;
    	}
    	for (int i = path.size()-1; i >= 0; i--) {
    		super.move(path.get(i).point);
    	}
    }
    
    public Node UnCertain(Node node) {
    	path = new ArrayList<Node>();
    	reach(node);
    	ArrayList<Node> potentials = getAdjacent(node);
    	
		for(int i = 0; i< potentials.size(); i++){
			Node potential = potentials.get(i);
			
			if(potential.point.equals(end)){
				return new Node(end,0,0,potential);
			}
			if(inClosedSet(potential)){
				continue;
			}
			if(inOpenSet(potential)){
				Node current = getPotential(potential);
				if(potential.Gvalue + node.Gvalue < potential.Fvalue){
					current.parent = node;
				}	
			}
			else {
				openSet.add(potential);
				openSetList.add(potential);
			}
		}
		for (int i = 1; i < path.size(); i++) {
			super.move(path.get(i).point);
		}
		node = openSet.poll();
		closedSet.add(node);
		openSetList.remove(node);
		return UnCertain(node);	
	}
	
	public Node Certain(Node node){
		ArrayList<Node> potentials = getAdjacent(node);		
		for(int i=0;i<potentials.size();i++){
			Node potential = potentials.get(i);
			if(potential.point.equals(end)){
				return new Node(end,0,0,potential);
			}
			if(inClosedSet(potential)){
				continue;
			}
			if(inOpenSet(potential)){
				Node incumbent = getPotential(potential);
				if(potential.Gvalue + node.Gvalue < potential.Fvalue){
					incumbent.parent = node;
				}	
			}
			else {
				openSet.add(potential);
				openSetList.add(potential);
			}
		}
		node = openSet.poll();
		closedSet.add(node);
		openSetList.remove(node);
		return Certain(node);
	}
	
	public void reconstruct_path(Node node){
		ArrayList<Node> path = new ArrayList<Node>();
		while(node.parent != null){
			path.add(node.parent);
			node = node.parent;
		}
		for(int i = path.size()-2; i >= 0; i--){
			super.move(path.get(i).point);
		}
	}
	
	public boolean inOpenSet(Node node){
		for(int i=0; i<openSetList.size();i++){
			if(openSetList.get(i).point.x == node.point.x 
					&& openSetList.get(i).point.y == node.point.y) {
				return true;
			}
		}
		return false;
	}
	
	public boolean inClosedSet(Node node) {
		for(int i=0; i<closedSet.size();i++){
			if(closedSet.get(i).point.x == node.point.x 
					&& closedSet.get(i).point.y == node.point.y){
				return true;
			}
		}
		return false;
	}

	public Node getPotential(Node node){
		for(int i = 0; i < openSetList.size(); i++){
			if(openSetList.get(i).point.x == node.point.x 
					&& openSetList.get(i).point.y == node.point.y) {
				return openSetList.get(i);
			}
		}
		return null;
	}
	
	public int[][] computeHvalues(World world){
		Point destination = world.getEndPos();
		row = world.numRows();
		column = world.numCols();
 		Hvalues = new int[row][column];
 		for(int i = 0; i < row; i++){
 			for(int j = 0; j < column; j++){
 				Hvalues[i][j] = (int) (Math.abs(destination.getX()-i) 
 						+ Math.abs(destination.getY()-j));
 			}
 		}
		return Hvalues;
	}
}