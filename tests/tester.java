class Tester
{
    public static void main(String[] args)
    {
        A as;
        as = new A();

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
        return 5;   // broke if returns a literal
    }

    public boolean setX(int y)
    {
        x = y;
        return true;   // broke if returns a literal
    }
}

class B extends A
{
    int k;

    public boolean setK(int w)
    {
        k = w;
        return true;   // broke if returns a literal
    }
}