class AAAA_Class
{
    public static void main(String[] str_arg)
    {
        System.out.println(new BBBB_Class().MAGIC_Func(10));
    }
}

class BBBB_Class
{
    int x;
    int[] y;
    boolean z;

    public int MAGIC_Func(int num)
    {
        int a;
        int[] b;
        boolean c;
        if (num < 1)
        {
            a = 1;
        }
        else
        {
            a = num * (this.MAGIC_Func(num-1));
        }
        //a = b[c]; we can catch type errors for array lookups
        // a = b[1]; works
        // a = b[a]; works
        // b = new int[a]; works
        // b = new int[c]; //catches error
        // b = new int[1]; works
        // CCCC_Class cee;
        // cee = new CCCC_Class(); WORKS!
        // cee = new b(); CATCHES
        // a = a.length;// CATCHES and works properly
        // a = a*a; WORKS
        //  b[a] = c; //CATCHES AND WORKS PROPERLY
        // a[1] = 1;
        // a = c; CATCHES AND WORKS PROPERLY
        return a;
    }

    public int retY()
    {
        return x;
    }

}

class CCCC_Class 
{

    int n;

    public int foo(int s) 
    {
        BBBB_Class bvar;
        bvar = new BBBB_Class();
        System.out.println(bvar.retY());
        
        return s;
    }

}

class DDDD_Class extends BBBB_Class
{
    int n;
    int n;

    public boolean funk(boolean tf)
    {
        return tf;
    }
}
