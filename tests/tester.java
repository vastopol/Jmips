class Easy
{
    public static void main(String[] args)
    {
        int a;
        int b;
        int c;
        int d;

        a = 1;
        b = 2;
        c = 2;
        System.out.println(1);      //   1
        System.out.println(a);      //   1   <-------- BROKEN
        System.out.println(2 - 3);  //  -1
        System.out.println(2 + 3);  //   5
        System.out.println(2 * 3);  //   6
        System.out.println(c - 5);  //  -3
        System.out.println(c + 5);  //   7
        System.out.println(c * 5);  //   10
        System.out.println(5 - c);  //   3
        System.out.println(5 + c);  //   7
        System.out.println(5 * c);  //   10
        System.out.println(a - b);  //  -1
        System.out.println(a + b);  //   3
        System.out.println(a * b);  //   2
        System.out.println(c - b);  //   0
        System.out.println(c + b);  //   4
        System.out.println(c * b);  //   4

        d = 6 + 5;
        d = d + 1;
        System.out.println(d);  //  12

        d = a + b;
        d = d + c;
        System.out.println(d);  //  5
    }

}

class A {
    int x;
    public int f(int a, int b){
        int c;
        c = b + a;
        return a;
    }
}

class B extends A {
    public int g(int b){
        return b;
    }
}

class C extends B {
    public int g(int c){
        return c;
    }
}

class D extends C {
    int y;
    public int f(int d){
        return d;
    }
}

