package asm3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.ObjectInputStream.GetField;
import java.lang.ProcessBuilder.Redirect;
import java.util.HashSet;
import java.util.Scanner;

import asm3.graph.Graph;
import asm3.tree.*;

public class index {

	public static void main(String[] args) {
		////tree
		AvlItems avlItems = new AvlItems();
		BstItem bstItem = new BstItem();
			//set for id to let it be unique(it necessary because my bst allow same id)
		HashSet<Integer> ids = new HashSet<Integer>();
			//save info
		HashSet<Employee> employees = new HashSet<index.Employee>();
		///graph
		Graph graph = new Graph();
			//locking file and add content if empty or not available
		File mantranFile = getFile(new File("matran.txt"));
			//read file and vertex and edges to graph
		readFileAndAdd(mantranFile,graph);
		
		Scanner scanner = new Scanner(System.in);
		boolean isContinuesPropram = true;
		while (isContinuesPropram) {
			System.out.println("1.Thêm nhân viên mới vào cây");
			System.out.println("2.Hiển thị danh sách theo thứ tự duyêt dạng Inorder");
			System.out.println("3.Hiển thị danh sách theo thứ tự duyêt dạng chiều rộng");
			System.out.println("4.Tìm nhân viên");
			System.out.println("5.Xóa nhân viên");
			System.out.println("6.Cân bằng cây bst");
			System.out.println("7.Graph:Load đồ thị từ matran.txt và duyệt theo thứ tự depth-first-search");
			System.out.println("8.Graph:Load đồ thị từ matran.txt và xuất ra đường đi và quãng dường ngắn nhất đến mọi điểm");
			System.out.println("10.Thoát khỏi chương trình");
			System.out.print("Bạn chọn: ");
			switch (scanner.nextInt()) {
			case 1:{
				//checker id
				int id;
				do {
					System.out.print("Nhập Id - không trùng: ");
					id = scanner.nextInt();
				} while (!ids.add(id));
				
				System.out.print("Nhập tên: ");
				String name = (new Scanner(System.in)).nextLine();
				System.out.print("Nhập tuổi: ");
				int age = scanner.nextInt();
				
				avlItems.add(new ItemNode(id, name, age));
				bstItem.add(new ItemNode(id, name, age));
				employees.add(new Employee(id, name, age));
				
				System.out.println("Đã Thêm!");
				break;
			}
			case 2:{
				bstItem.display(BstItem.IN_ORDER);
				break;
			}
			case 3:{
				bstItem.display(BstItem.BREADTH_FIRST_SEARCH);
				break;
			}
			case 4:{
				System.out.print("Nhập id nhân viên: ");
				//find and get node
				ItemNode itemNode = bstItem.find(scanner.nextInt());
				System.out.print(itemNode == null ? "Không tìm thấy" : itemNode);
				break;
			}
			case 5:{
				System.out.print("Nhập id nhân viên muốn xóa: ");
				int id = scanner.nextInt();
				//if remove success it mean id exists ....and hanle 
				if(bstItem.remove(id)){
					System.out.println("đã xóa");
					avlItems.remove(id);
					ids.remove(id);
					for (Employee employee : employees) {
						if(employee.id == id){
							employees.remove(employee);
							break;
						}
					}
				}else{
					System.out.println("Không tìm thấy nhân viên!");
				}

				break;
			}
			case 6:{
				//take root from avl, the root of the kind of tree always balances
				bstItem.root = avlItems.root;
				//new tree for avl
				avlItems = new AvlItems();
				for (Employee employee : employees) {
					avlItems.add(new ItemNode(employee.id, employee.name, employee.age));
				}
				break;
			}
			case 7:{
				//dfs
				graph.display("A");
				break;
			}
			case 8:{
				//adjacency linked-list
				graph.display();
				graph.findShortWay("A");
				break;
			}
			case 15:{
				//actually not a function for user, this just show you see what happen in every change in another function
				System.out.println(ids);
				System.out.println(employees);
				System.out.println();
				bstItem.display(bstItem.IN_ORDER);
				System.out.println();
				avlItems.display(avlItems.IN_ORDER);
				System.out.println();
				graph.display();
				
				break;
			}
			
			case 10:{
				//exit
				isContinuesPropram = false;
				break;
			}
			default:
				break;
			}
			System.out.println("\n------------");
		}
	}
	
	public static class Employee{
		int id;
		String name;
		int age;
		public Employee(int id, String name,int age){
			this.id=id;
			this.name = name;
			this.age = age;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return id + " " + name + " " + age;
		}
	}
	public static File getFile(File file){
		try {
			if(file.createNewFile()){
				//// matrix take from assignment
				Writer writer = new FileWriter(file.getPath());
				String values = "A B C D E F G\n "
						+"0 10 -1 -1 -1 -1 8 \n"
						+ "-1 0 2 10 25 80 -1 \n"
						+ "30 -1 0 8 3 20 -1 \n"
						+ "20 -1 -1 0 -1 5 10 \n"
						+ "-1 -1 -1 4 0 -1 -1 \n"
						+ "8 -1 -1 -1 -1 0 5 \n"
						+ "8 -1 -1 -1 -1 -1 0 \n";
				//write to file for have something to do
				writer.write(values);
				System.out.println();
				writer.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
	
	
	public static Graph readFileAndAdd(File file, Graph graph){
		try {
			Scanner reader = new Scanner(file);
			//first line is name vertex
			String[] nameVertex = (reader.nextLine()).split(" ");
			//add vertex
			for (int i = 0; i < nameVertex.length; i++) {
				graph.addVertex(nameVertex[i]);
			}
			//add edges
			for (int i = 0; i < nameVertex.length; i++) {
				for (int j = 0; j < nameVertex.length; j++) {
					graph.addEdge(nameVertex[i], nameVertex[j],Integer.parseInt(reader.next()));
				}
			}
			return graph;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return graph;
	}
		// TODO Auto-generated method stub
//		AvlItems avlItems =  new AvlItems();
//		int[] a = new int[]{50,40,60,55,70,80};
//		for (int i = 0; i < a.length; i++) {
//			avlItems.add(new ItemNode(a[i], "trống"));
//		}
//		System.out.println();
//		avlItems.display(AvlItems.PRE_ORDER);
//		
//		System.out.println();
//		avlItems.display(AvlItems.IN_ORDER);
//
//		System.out.println();
//		avlItems.display(AvlItems.POST_ORDER);
//		System.out.println("-------");

//		BstItem bstItem =  new BstItem();
//		int[] a = new int[]{50,40,60,55,70,80};
//		for (int i = 0; i < a.length; i++) {
//			bstItem.add(new ItemNode(a[i], "trống"));
//		}
//		System.out.println();
//		bstItem.display(AvlItems.PRE_ORDER);
//		
//		System.out.println();
//		bstItem.display(AvlItems.IN_ORDER);
//
//		System.out.println();
//		bstItem.display(AvlItems.POST_ORDER);
//		
//		System.out.println();
//		bstItem.display(AvlItems.BREADTH_FIRST_SEARCH);
//		System.out.println("-------");
		
		
		
//		ArrayList<Integer> asd = new ArrayList<Integer>(){{
//			add(1);
//			add(2);
//		}
//		};
//		System.out.println(asd.remove(0));
//		System.out.println(asd.remove(0));
	

}
