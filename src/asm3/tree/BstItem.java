package asm3.tree;

import java.util.ArrayList;

public class BstItem {
	public static final int PRE_ORDER = 1;
	public static final int IN_ORDER = 2;
	public static final int POST_ORDER = 3;
	public static final int BREADTH_FIRST_SEARCH = 4;

	public ItemNode root;
	public int size = 0;
	
	public void add(ItemNode itemNode){
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
		if(find(id)==null){return false;}
		recRemove(id,this.root);
		return true;
	}
	private int addRecursion(ItemNode itemNode,ItemNode position){
//		System.out.println("outside " + position);
		if(position.id < itemNode.id){
			if(position.rightChild == null){
				//set child
				position.rightChild = itemNode;
				//reset parent
				itemNode.parent = position;
				//reset deep
				if(position.deep<=0) position.deep = 1;
				return 1;
			}else{
				int hight = addRecursion(itemNode,position.rightChild)+1;
				if(!(position.deep>hight)) position.deep = hight;
				return hight;
			}
		}else{
			//same with the right
			if(position.leftChild == null){
				position.leftChild = itemNode;
				itemNode.parent = position;
				if(position.deep<=0) position.deep = 1;
				return 1;
			}else{
				int hight = addRecursion(itemNode,position.leftChild)+1;
				if(!(position.deep>hight)) position.deep = hight;
				return hight;
			}

		}
	}
	//use stack to handle this function
	//node removed will be replace by node biggest in left side 
	//but if don't have left side, node replace is will be right child. Nothing happen if removed node is top of tree(deep = 0)
	//then, check balance after set hight
	private int recRemove(int id, ItemNode position){
		if(position == null) return -1;
		//phần xữ lý sao khi tìm thấy
		if(position.id == id){
			//case: top of tree
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
		}else if(id > position.id){ // phần tìm -....- lấy hight -> set hight
			int hight = recRemove(id,position.rightChild)+1;
			if(hight > (position.leftChild == null ? -1 : position.leftChild.deep)) position.deep = hight;
			return position.deep;
		}else{
			int hight = recRemove(id,position.leftChild)+1;

			if(hight > (position.rightChild == null ? -1 : position.rightChild.deep)) position.deep = hight;
//			System.out.println("l-pos:" + position.id + " pos.deep:" + position.deep + " hight:" + hight);
			return position.deep;
		}
	}
	
	public void preOrder(ItemNode itemNode){
//		System.out.println(itemNode 
//				+ " deep:" + itemNode.deep 
//				+ " par:" + (itemNode.parent==null?"I'm Root!":itemNode.parent.id)
//				+ " L:" + (itemNode.leftChild==null?null:itemNode.leftChild.id) 
//				+ " R:" + (itemNode.rightChild==null?null:itemNode.rightChild.id));	
		System.out.println(itemNode);
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
//		System.out.println(itemNode);
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
		System.out.println(itemNode);
//		System.out.println(itemNode 
//				+ " deep:" + itemNode.deep 
//				+ " par:" + (itemNode.parent==null?"I'm Root!":itemNode.parent.id)
//				+ " L:" + (itemNode.leftChild==null?null:itemNode.leftChild.id) 
//				+ " R:" + (itemNode.rightChild==null?null:itemNode.rightChild.id));	
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
