package asm3.tree;

public class ItemNode extends Item{
	//public Itemnode defaultValue....
	public ItemNode parent;
	public ItemNode leftChild;
	public ItemNode rightChild;
	public int deep;

	public ItemNode(int id, String name,int age) {
		super(id, name, age);
		this.parent = this.leftChild = this.rightChild = null;
		this.deep = 0;
	}

}
