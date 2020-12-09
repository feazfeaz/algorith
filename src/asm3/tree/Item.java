package asm3.tree;

public class Item {

	int id;
	String name;
	int age;
	
	public Item(int id,String name,int age){
		this.id = id;
		this.name = name;
		this.age = age;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.id + " " + this.name + " " + this.age;
	}
}
