class Tester
{
    public static void main(String[] args)
    {
        int val;
        A as;
        as = new A();

        // val = as.f(1,2);
        // System.out.println(val);

        // as.x = 1;    // ERROR
        // System.out.println(as.x);
    }
}

class A
{
    int x;
    int my_int;
    boolean my_bool;

    public int f(int a, int b)
    {
        int c;
        c = b + a;
        // return 5;   // broken if returns a literal
        return c;
    }
}

