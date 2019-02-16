class Main {
	public static void main(String[] a){
		System.out.println(new A().run());
	}
}

class A {
	public int run() {
		int[] a;
		int b;
		int c;
		a = new int[20];
		b = a.length;
		// a[4] = 7;
		// c = 0 - 1;
		a[4] = 8;
		c = a[4];
		return c;
	}
}
