class Medium
{
    public static void main(String[] args)
    {
        boolean a; boolean b;
        boolean c; boolean d;
        int low; int high;

        a = true;   b = true;
        c = false;  d = false;
        low = 1;    high = 1024;

        // IF BRANCHES

        if (true)   { System.out.println(1); } else { System.out.println(0); }  //  1  //  TRUE  = true
        if (false)  { System.out.println(1); } else { System.out.println(0); }  //  0  //  FALSE = false

        if (a)  { System.out.println(1); } else { System.out.println(0); }  //  1  //  TRUE  = true
        if (c)  { System.out.println(1); } else { System.out.println(0); }  //  0  //  FALSE = false

        if (low  < high) { System.out.println(1); } else { System.out.println(0); }  //  1  //  TRUE  = 1 < 1024
        if (high < low)  { System.out.println(1); } else { System.out.println(0); }  //  0  //  FALSE = 1024 < 1

        if(c){
            System.out.println(0);
            if (b)  { System.out.println(1); } else { System.out.println(2); }
        }
        else{
            System.out.println(3);
            if (d)  { System.out.println(4); } else { System.out.println(5); }
        }

        // WHILE LOOPS

        // print 1, 2, 4, 8, 16, ... , 1024
        while ( low < high ) { System.out.println(low); low = low * 2; }
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