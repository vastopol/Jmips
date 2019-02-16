class Main {
	public static void main(String[] a){
		System.out.println(new A().run());
	}
}

class A {
	public int run() {
		int[] a;
		int b;
		a = new int[20];
		b = a.length;
		return b;
	}
}

