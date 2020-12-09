package asm3.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public final class Graph {
	
	LinkedList<Vertex> linkedListVerText = new LinkedList<Vertex>();
	//set to be unique
	HashSet<String> vertexs = new HashSet<String>();
	HashSet<String> edges = new HashSet<String>();
	
	///constructor for what?
	
	//if just have start node, we will show the way to get everywhere
	public void findShortWay(String start){
		//ruturn if not exist
		if(!hasVertex(start)) return;
		ArrayList<RowDijkstraAlgorithmPenal> penal = DijkstraAlgorithm(findVertex(start));
		for (RowDijkstraAlgorithmPenal row : penal) {
			row.displayShortestWay();
			System.out.println();
		}

	}
	//but if we have destination, it simpler
	public void findShortWay(String start, String destination){
		if(!(hasVertex(start) && hasVertex(destination))) return;
		ArrayList<RowDijkstraAlgorithmPenal> penal = DijkstraAlgorithm(findVertex(start));
		for (RowDijkstraAlgorithmPenal row : penal) {
			if(row.vertex.nameVertex.equals(destination)) row.displayShortestWay();
		}
		System.out.println();
	}
	
	//best important part in this algorithm is node browsing sequence
	//current node still have adjacent node?
	//adjacent node was visited?
	//this edge to adjacent node is shortest?
	//what is next node now?
	private ArrayList<RowDijkstraAlgorithmPenal> DijkstraAlgorithm(Vertex startVertex ){

		ArrayList<Vertex> nodeUnvisiteds = new ArrayList<Vertex>();
		ArrayList<Vertex> nodeVisiteds = new ArrayList<Vertex>();
		
		ArrayList<RowDijkstraAlgorithmPenal> panel = new ArrayList<RowDijkstraAlgorithmPenal>();
		
		nodeUnvisiteds.add(startVertex);
		//now is't start vertex just call get(0) when we need start place
		RowDijkstraAlgorithmPenal currentRow = new RowDijkstraAlgorithmPenal(startVertex,0,null,null);
		panel.add(currentRow);

		
		
		Vertex currentPosition = nodeUnvisiteds.get(0);
		int indexCurrentPosition = 0;
		int indexOfNextPosition = 0;
		
		System.out.println("--------");// handle
		while (nodeUnvisiteds.size() != 0) {
			if(currentPosition.lklDirectedEdge.size != 0){//neighborVertex
				/// add neighbor vertex if it has't been visited 
				DirectedEdgeNode directedEdgeNode = currentPosition.lklDirectedEdge.head;
				int shortestDistanceToNextNode = 99999999;
				while (directedEdgeNode != null) {
					boolean isVisitedNode = false;
					//is node visited ?
					for (Vertex vertex : nodeVisiteds) if(vertex.nameVertex.equals(directedEdgeNode.toVertext.nameVertex)) isVisitedNode = true;
//					for (Vertex vertex : nodeUnvisiteds) if(vertex.nameVertex.equals(directedEdgeNode.toVertext.nameVertex)) isVisitedNode = true;
					
					if(!isVisitedNode){
						boolean isNodeAdded = false;
						for (Vertex vertex : nodeUnvisiteds) {
							if(vertex.nameVertex.equals(directedEdgeNode.toVertext.nameVertex)) {
								isNodeAdded = true;
							}
						}
						if(!isNodeAdded){
							//add new node
							nodeUnvisiteds.add(directedEdgeNode.toVertext);
							panel.add(new RowDijkstraAlgorithmPenal(
									directedEdgeNode.toVertext,
									currentRow.shortestDistance+directedEdgeNode.distance,
									currentPosition,
									currentRow));
							//locking for next node
							if(directedEdgeNode.distance < shortestDistanceToNextNode){
								shortestDistanceToNextNode = directedEdgeNode.distance;
								indexOfNextPosition = nodeUnvisiteds.size()-2;
							}
						}else{//// this case is neighbor node was added but has't been visited
							if(directedEdgeNode.distance < shortestDistanceToNextNode){
								shortestDistanceToNextNode = directedEdgeNode.distance;
								int getIndex = nodeUnvisiteds.indexOf(directedEdgeNode.toVertext);
								indexOfNextPosition = getIndex < indexCurrentPosition ? getIndex : getIndex -1;
							}
							for (RowDijkstraAlgorithmPenal row : panel) {
								if(row.vertex == directedEdgeNode.toVertext){
//									System.out.println(currentRow);
									if((currentRow.shortestDistance + directedEdgeNode.distance) < row.shortestDistance){
										row.shortestDistance = currentRow.shortestDistance + directedEdgeNode.distance;
										row.priviousRows = currentRow;
										row.priVertex = currentRow.vertex;
									}
								}
							}
						}
					}
					directedEdgeNode = directedEdgeNode.nextDirectedEdgeNode;
				}
			}
			if(!(indexOfNextPosition == -1)){//still have next point
				nodeVisiteds.add(nodeUnvisiteds.remove(indexCurrentPosition));
				currentPosition = nodeUnvisiteds.get(indexOfNextPosition);
				indexCurrentPosition = indexOfNextPosition;
			}else if(nodeUnvisiteds.size() > 1 ){
				nodeVisiteds.add(nodeUnvisiteds.remove(indexCurrentPosition));
				currentPosition = nodeUnvisiteds.get(nodeUnvisiteds.size()-1);
				indexCurrentPosition = nodeUnvisiteds.size()-1;
			}else{
				nodeVisiteds.add(nodeUnvisiteds.remove(indexCurrentPosition));
			}
			for (RowDijkstraAlgorithmPenal row : panel) {
				if(row.vertex == currentPosition){currentRow = row;break;}
			}
			indexOfNextPosition = -1;
			
		}
		//final result
		return panel;
	}
	
	public class RowDijkstraAlgorithmPenal{
		Vertex vertex;
		int shortestDistance;
		Vertex priVertex;
		RowDijkstraAlgorithmPenal priviousRows;
		public RowDijkstraAlgorithmPenal(Vertex verText){
			this.vertex = verText;
			shortestDistance = 9999999;
			priVertex = null;
			priviousRows = null;
		}
		public RowDijkstraAlgorithmPenal(Vertex verText,int shortestDistance,Vertex priVertex,RowDijkstraAlgorithmPenal priviousRows){
			this.vertex = verText;
			this.shortestDistance = shortestDistance;
			this.priVertex = priVertex;
			this.priviousRows = priviousRows;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return vertex + " " + shortestDistance + " " + priVertex + "\n";
		}
		public void displayShortestWay(){
			recDisplayShortestWay(this);
		}
		private void recDisplayShortestWay(RowDijkstraAlgorithmPenal row){
			if(row == null)return;
			recDisplayShortestWay(row.priviousRows);
			System.out.print(row.priviousRows == null ? row.vertex.nameVertex : " -> " + row.vertex.nameVertex);
		}
	}
	
	public boolean addVertex(String nameVertext){
		if(vertexs.add(nameVertext)){
			linkedListVerText.add(new Vertex(nameVertext));
			return true;
		}
		return false;		
	}
	private boolean removeVertex(String nameVertext){////////////////// cần sửa
		for (Vertex vertex : linkedListVerText) {
			if(vertex.nameVertex.equals(nameVertext)){
				linkedListVerText.remove(vertex);
				return true;
			}
		}
		return false;
	}
	
	public void addEdge(String fromVertex, String toVertex, int distance){
		if( fromVertex.equals(toVertex)
				|| distance < 1 //0 is common 1 location and -1 is not exists
				|| (!(hasVertex(fromVertex) && hasVertex(toVertex)))
				|| hasEdge(fromVertex, toVertex)) return;
		edges.add(fromVertex+toVertex);
		(findVertex(fromVertex)).lklDirectedEdge.add(new DirectedEdgeNode(findVertex(toVertex), distance));
	}
	///////////remove edge
	
	public Vertex findVertex(String name){
		for (Vertex vertex : linkedListVerText) {
			if(vertex.nameVertex.equals(name))return vertex;
		}
		return null;
	}
	
	
	public boolean hasVertex(String name){
		for (String nameVertex : vertexs) {
			if(nameVertex.equals(name))return true;
		}
		return false;
	}
	public boolean hasEdge(String name){
		for (String nameEdge : edges) {
			if(nameEdge.equals(name))return true;
		}
		return false;
	}
	public boolean hasEdge(String A, String B){
		for (String nameEdge : edges) {
			if(nameEdge.equals(A+B))return true;
		}
		return false;
	}
	//adjacent linked-list style
	public boolean display(){
		if(linkedListVerText.size() == 0){return false;}
		for (Vertex vertex : linkedListVerText) {
			System.out.print(vertex.nameVertex + "\t");
			vertex.lklDirectedEdge.display();
			System.out.println();
		}
		return true;
	}
	//depth-fisrt-search style if parameter is vertex
	public boolean display(String nameVertex){
		if(linkedListVerText.size() == 0){return false;}
		Vertex firstNode = findVertex(nameVertex);
		if(firstNode == null){return false;}
		System.out.println();
		recDisplay(firstNode, new HashSet<Vertex>());
		return true;
//		System.out.println();
		
	}
	private void recDisplay(Vertex vertex, HashSet<Vertex> visitedVertexs){
		if(visitedVertexs.add(vertex)){
			System.out.print(vertex.nameVertex+" ");
			if(vertex.lklDirectedEdge.size == 0){return;}
			DirectedEdgeNode edges = vertex.lklDirectedEdge.head;
			while (edges!=null) {//add stack 
				recDisplay(edges.toVertext, visitedVertexs);
				edges = edges.nextDirectedEdgeNode;
			}
		}
	}
	
	private class Vertex{
		String nameVertex;
		LinkedListDirectedEdge lklDirectedEdge;
		public Vertex(String nameVertex){
			this.nameVertex = nameVertex;
			lklDirectedEdge = new LinkedListDirectedEdge();
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return nameVertex;
		}
	}
	// 
	private class DirectedEdge{
		public Vertex toVertext;
		public int distance;
		public DirectedEdge(Vertex target, int distance){
			this.toVertext = target;
			this.distance = distance;
		}
	}
	private class DirectedEdgeNode extends DirectedEdge{
		
		DirectedEdgeNode nextDirectedEdgeNode;
		
		public DirectedEdgeNode(Vertex target, int distance) {
			super(target, distance);
			nextDirectedEdgeNode = null;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
//			return super.toString();
			return toVertext + "|" + distance;
		}
	}
	private class LinkedListDirectedEdge {
		
		DirectedEdgeNode head;
		int size;
		
		public LinkedListDirectedEdge(){
			head = null;
			size = 0;
		}
		
		public void add(DirectedEdgeNode directedEdgeNode){
			if(head == null){
				head = directedEdgeNode;
				size++;
				return;
			}else{
				DirectedEdgeNode lastNode = this.head;
				while (lastNode != null) {
					if(lastNode.nextDirectedEdgeNode == null) break;
					lastNode=lastNode.nextDirectedEdgeNode;
					
				}
				lastNode.nextDirectedEdgeNode = directedEdgeNode;
				size++;
			}
		}
		
		public void remove(DirectedEdgeNode directedEdgeNode){
			if(directedEdgeNode == null || head == null)	{return;}
			if(directedEdgeNode == head)	{head=head.nextDirectedEdgeNode; return;}
			DirectedEdgeNode position = head;
			while (position != null) {
				if(position.nextDirectedEdgeNode == directedEdgeNode){
					position.nextDirectedEdgeNode = position.nextDirectedEdgeNode.nextDirectedEdgeNode;
					size--;
					return;
				}
				position = position.nextDirectedEdgeNode;
			};
			System.out.println("ko tìm thấy");
		}
		
		public void display(){
			DirectedEdgeNode node = this.head;
			if(node == null){System.out.println("rỗng");return;}
			while (node != null) {
				System.out.print(node + " ");
				node=node.nextDirectedEdgeNode;
			}
		}
	}
	
}
