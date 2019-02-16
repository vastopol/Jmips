class Main {
	public static void main(String[] a){
		System.out.println(new A().run());
	}
}

class A {
	public int run() {
<<<<<<< HEAD
		//System.out.println(42);
		int[] a;
		int b;
		a = new int[20];
		b = a.length;
		return 99;
=======
		int a;
		int b;
		a = this.helper(12);
		b = this.helper(15);
		return a + b;
	}

	public int helper(int param) {
		int x;
		x = param;
		param = param + 1;
		System.out.println(x);
		return x;
>>>>>>> master
	}
}

