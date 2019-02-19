class Main_Class
{
    public static void main(String[] str_arg)
    {
        A a;
        B b;
        C c;
        D d;

        a = new A();
        b = new B();
        c = new C();
        d = new D();

        System.out.println(a.f(1));

        System.out.println(b.f(1));
        System.out.println(b.g(1));

        System.out.println(c.f(1));
        System.out.println(c.g(1));

        System.out.println(d.f(1));
        System.out.println(d.g(1));
    }
}

class A {
    int x;
    public int f(int a){
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
        return c+1;
    }
}

class D extends C {
    int y;
    public int f(int a){
        return a+1;
    }
}