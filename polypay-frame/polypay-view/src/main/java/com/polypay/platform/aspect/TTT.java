package com.polypay.platform.aspect;

public class TTT {
	
	public static void main(String[] args) {
		
		//4
		String a = "hello";
		String b = "word";
		System.out.println(a +"-"+ b); //hello-word
		String temp =  null;
		temp = a;
		a = b;
		b = temp;
		System.out.println(a +"-"+ b);  //word-hello
		
		
		//5
		//同步表示单线程操作一般不做异步处理都是同步，一般用于需要实时展示的功能
		//异步表示多线程操作，一般用于 不需要实时展示的功能 比如发送短信等等
		
		//8
		//mysql : select * from A limit 31,10
		//oracle : select row_num,* from A having row_num >=31 and  row_num<=40
		
		
		
		//9
		// select count(ID),ID from A group by ID having count(ID)>=3
		
		
		
		//10
		// start() 表示 线程启动    run()表示线程启动后执行的方法
		
	}

}
