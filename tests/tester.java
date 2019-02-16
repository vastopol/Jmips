// class Factorial{
//     public static void main(String[] a){
//         System.out.println(new Fac().ComputeFac(10));
//     }
// }
//
// class Fac {
//     public int ComputeFac(int num){
//         int num_aux ;
//         if (num < 1)
//             num_aux = 1 ;
//         else
//             num_aux = num * (this.ComputeFac(num-1)) ;
//         return num_aux ;
//     }
// }

class Main {
	public static void main(String[] a){
		System.out.println(new A().run());
	}
}

class A {
	public int run() {
		//System.out.println(42);
		int[] a;
		int b;
		a = new int[20];
		b = a.length;
		return 99;
	}
}
