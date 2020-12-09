package asm3.tree;

import java.util.ArrayList;

///////// every thing same with binary-search-tree 
///////// but this is Avl Tree so except balance after "add" and "remove", there is no balance function for user

public class AvlItems  {
	public static final int PRE_ORDER = 1;
	public static final int IN_ORDER = 2;
	public static final int POST_ORDER = 3;
	public static final int BREADTH_FIRST_SEARCH = 4;

	public ItemNode root;
	public int size = 0;
	
	public AvlItems(){
		this.root = null;
		this.size = 0;
	}
	
	public void add(ItemNode itemNode){
		if(itemNode == null){return;};
		if(size == 0){
			this.root = itemNode;
			size++;
		}else{	
			addRecursion(itemNode,root);
			size++;
		}
	}
	public boolean display(int type){
		if(this.root == null) return false;
		
		switch (type) {
		case PRE_ORDER:{
			preOrder(this.root);
			break;
		}
		case IN_ORDER:{
			inOrder(this.root);
			break;
		}
		case BREADTH_FIRST_SEARCH:{
			breadthSearch();
			break;
		}
		default:
			postOrder(this.root);
			break;
		}
		
		return true;
	}
	public boolean remove(int id){
		System.out.println("remove:" + id);
		recRemove(id,this.root);
		return true;
	}
	private int addRecursion(ItemNode itemNode,ItemNode position){
//		System.out.println("outside " + position);
		if(position.id < itemNode.id){
			if(position.rightChild == null){
				position.rightChild = itemNode;
				itemNode.parent = position;
				if(position.deep<=0) position.deep = 1;
				return 1;
			}else{
				int hight = addRecursion(itemNode,position.rightChild)+1;
				//System.out.println("gọi về của " +itemNode.id + " cho " + position.id  + " " + hight);
				if(!(position.deep>hight)) position.deep = hight;
				
//				if (hightLeft == hightRight) return hight;
//				System.out.println("ở đây ko cân = " + position + position.rightChild.deep + "/" + position.leftChild.deep);
				//check balance
				if(Math.abs((position.leftChild != null ? position.leftChild.deep : -1)
						-(position.rightChild != null ? position.rightChild.deep : -1))-1>0){
					rebalance("right",itemNode,position);
					return hight-1;
				}
				return hight;
			}
		}else{
			if(position.leftChild == null){
				position.leftChild = itemNode;
				itemNode.parent = position;
				if(position.deep<=0) position.deep = 1;
				return 1;
			}else{
				int hight = addRecursion(itemNode,position.leftChild)+1;
				if(!(position.deep>hight)) position.deep = hight;
				//check balance
				if(Math.abs((position.leftChild != null ? position.leftChild.deep : -1)
						-( position.rightChild != null ? position.rightChild.deep : -1))-1>0){
					rebalance("left",itemNode,position);
					return hight-1;
				}
				
				return hight;
			}

		}
	}
	private int recRemove(int id, ItemNode position){
		if(position == null) return -1;
		//phần xữ lý sao khi tìm thấy
		if(position.id == id){
			//case: deepest node
			if(position.deep == 0){
				if(position == root){
					this.root = null;
				}else{
					if(id>position.parent.id){
						position.parent.rightChild = null;
					}else{
						position.parent.leftChild = null;
					}
				}
				this.size--;
				return -1;
			}
			if(position.deep == 1){

				ItemNode B = position.leftChild;
				ItemNode C = position.rightChild;
				ItemNode par = position.parent;
				boolean isRightChildOfPar = false;
				if(par == null){
					this.root = (B != null ? B : C);
					this.root.parent = null;
				}else{
					if(par.id < (B != null ? B.id : C.id)){
						par.rightChild = (B != null ? B : C);
						par.rightChild.parent = par;
						isRightChildOfPar = true;
					}else{
						par.leftChild = (B != null ? B : C);
						par.leftChild.parent = par;
					}
				}
				ItemNode newPosition = (par == null) ? root : (isRightChildOfPar ? par.rightChild : par.leftChild);
				if((B == null && newPosition == C )|| (C == null && newPosition == B)){
					newPosition.rightChild = newPosition.leftChild = null;
					this.size--;
					return B != null ? B.deep : C.deep;
				}else{
					B.deep++;
					B.rightChild = C;
					C.parent = B;
					this.size--;
					return B.deep;
				}	
			}
			//high 2+ case
			ItemNode nodeReplaceNodeRemoved = position.leftChild;
			System.out.println(nodeReplaceNodeRemoved);
			while (nodeReplaceNodeRemoved.rightChild != null) {
				nodeReplaceNodeRemoved = nodeReplaceNodeRemoved.rightChild;
			}
			
			int hight = recRemove(nodeReplaceNodeRemoved.id, position);

			ItemNode parOldPos = position.parent,
					leftOldPos = position.leftChild,
					rightOldPos= position.rightChild;
			int 	deepOldPos = position.deep;
			
			if(parOldPos != null){
				nodeReplaceNodeRemoved.parent = parOldPos;
				if(parOldPos.id < nodeReplaceNodeRemoved.id){
					parOldPos.rightChild = nodeReplaceNodeRemoved;
				}else{
					parOldPos.leftChild = nodeReplaceNodeRemoved;
				}
			}else{
				this.root = nodeReplaceNodeRemoved;
				nodeReplaceNodeRemoved.parent = null;
			}
			
			nodeReplaceNodeRemoved.leftChild = leftOldPos;
			if(nodeReplaceNodeRemoved.leftChild != null ){leftOldPos.parent = nodeReplaceNodeRemoved;}
			nodeReplaceNodeRemoved.rightChild = rightOldPos;
			if(nodeReplaceNodeRemoved.rightChild != null ){rightOldPos.parent = nodeReplaceNodeRemoved;}
			nodeReplaceNodeRemoved.deep = deepOldPos;
			
			return hight;
		}else if(id > position.id){ // phần tìm -....- lấy hight -> set hight -> căn bằng
			int hight = recRemove(id,position.rightChild)+1;
			if(hight > (position.leftChild == null ? -1 : position.leftChild.deep)) position.deep = hight;
			//check căn bằng
			if(Math.abs((position.leftChild != null ? position.leftChild.deep : -1)
					-(position.rightChild != null ? position.rightChild.deep : -1))-1>0){
				return rebalance("left",null,position);				
			}
			
			return position.deep;
		}else{ // phần tìm -....- lấy hight -> set hight -> căn bằng
			int hight = recRemove(id,position.leftChild)+1;

			if(hight > (position.rightChild == null ? -1 : position.rightChild.deep)) position.deep = hight;
			if(Math.abs((position.leftChild != null ? position.leftChild.deep : -1)
					-(position.rightChild != null ? position.rightChild.deep : -1))-1>0){
				return rebalance("right",null,position);
				 
			}
			
			return position.deep;
		}
	}
	
	public void preOrder(ItemNode itemNode){
		System.out.println(itemNode 
				+ " deep:" + itemNode.deep 
				+ " par:" + (itemNode.parent==null?"I'm Root!":itemNode.parent.id)
				+ " L:" + (itemNode.leftChild==null?null:itemNode.leftChild.id) 
				+ " R:" + (itemNode.rightChild==null?null:itemNode.rightChild.id));	
		if(itemNode.leftChild != null){
			preOrder(itemNode.leftChild);
		}
		if(itemNode.rightChild != null){
			preOrder(itemNode.rightChild);
		}
	}
	
	public void inOrder(ItemNode itemNode){
		if(itemNode.leftChild != null){
			inOrder(itemNode.leftChild);
		}
		System.out.println(itemNode 
				+ " deep:" + itemNode.deep 
				+ " par:" + (itemNode.parent==null?"I'm Root!":itemNode.parent.id)
				+ " L:" + (itemNode.leftChild==null?null:itemNode.leftChild.id) 
				+ " R:" + (itemNode.rightChild==null?null:itemNode.rightChild.id));
		if(itemNode.rightChild != null){
			inOrder(itemNode.rightChild);
		}
	}
	public void postOrder(ItemNode itemNode){
		if(itemNode.leftChild != null){
			preOrder(itemNode.leftChild);
		}
		if(itemNode.rightChild != null){
			preOrder(itemNode.rightChild);
		}
		System.out.println(itemNode 
				+ " deep:" + itemNode.deep 
				+ " par:" + (itemNode.parent==null?"I'm Root!":itemNode.parent.id)
				+ " L:" + (itemNode.leftChild==null?null:itemNode.leftChild.id) 
				+ " R:" + (itemNode.rightChild==null?null:itemNode.rightChild.id));	
	}
	@SuppressWarnings("serial")
	private void breadthSearch(){
		ArrayList<ItemNode> list = new ArrayList<ItemNode>(){};
		list.add(this.root);
		while (!list.isEmpty()) {
			ItemNode node = list.get(0);
			if(node.leftChild != null ) list.add(node.leftChild);
			if(node.rightChild != null ) list.add(node.rightChild);
			System.out.println(node);
			list.remove(0);
		}
	}

	//a,b,c => 10,20,30
	//d,x,y unordered
	// final result of balance processing 
	//      B
	//     / \
	//    A   C
	private int rebalance(String sideHigher,ItemNode itemNode,ItemNode position){
		ItemNode a,b,c,d,x,y;
		x = y = d = null;
		ItemNode parent = position.parent;
		boolean is_b_bottom = false;// if true ? x,y is exists : d is exist
		if(sideHigher == "left"){
			/////////    left
			c = position;
			if((c.leftChild.leftChild != null ? c.leftChild.leftChild.deep : -1) 
					> (c.leftChild.rightChild != null ? c.leftChild.rightChild.deep : -1)  ){
//				System.out.println("p-l-l");
				//set case
				b = c.leftChild;
				a = b.leftChild;
				d = b.rightChild;
				//reset high
				c.deep-=2;
			}else{
//				System.out.println("p-l-r");
				//set case
				is_b_bottom = true;
				a = c.leftChild;
				b = a.rightChild;
				x = b.leftChild;
				y = b.rightChild;
				//reset high
				c.deep-=2;
				a.deep--;
				b.deep++;
			}
		}else{
			/////////    right
			a = position;
			if((a.rightChild.rightChild != null ? a.rightChild.rightChild.deep : -1) 
					> (a.rightChild.leftChild != null ? a.rightChild.leftChild.deep : -1)  ){
//				System.out.println("p-r-r");
				b = a.rightChild;
				c = b.rightChild;
				d = b.leftChild;
				
				a.deep-=2;
			}else{
//				System.out.println("p-r-l");
				is_b_bottom = true;
				c = a.rightChild;
				b = c.leftChild;
				x = b.leftChild;
				y = b.rightChild;
				
				a.deep-=2;
				b.deep++;
				c.deep--;
			}
		}
			
		if (parent == null){
			this.root = b;
		}else{
			if(position.id > parent.id){
				parent.rightChild = b;
			}else{
				parent.leftChild = b;
			}
		}
		b.parent = parent;
		b.leftChild = a;
		b.rightChild = c;
		a.parent = b;
		c.parent = b;
		
		if(is_b_bottom){
			a.rightChild = x;
			if(x != null) x.parent = a;
			c.leftChild = y;
			if(y != null) y.parent = c;
		}else if(sideHigher == "left"){
			c.leftChild = d;
			if(d != null) d.parent = c;
		}else{
			a.rightChild = d;
			if(d != null) d.parent = a;
		}
		return b.deep;
	}
	
	public ItemNode find(int id){
		ItemNode checker = this.root;
		while(checker != null){
			if(checker.id == id){
				return checker;
			}else if(checker.id < id){
				checker = checker.rightChild;
			}else{
				checker = checker.leftChild;
			}
		}
		return null;
	}
}
