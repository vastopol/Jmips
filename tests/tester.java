class AAAA_Class
{
    public static void main(String[] str_arg)
    {
        System.out.println(new BBBB_Class().MAGIC_Func(10));
    }
}

class CCCC_Class
{
    int n;

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
            a = 1;
        else
            a = num * (this.MAGIC_Func(num-1));
        //a = b[c]; we can catch type errors for array lookups
        // a = b[1]; works
        // a = b[a]; works
        // b = new int[a]; works
        // b = new int[c]; catchs error
        // b = new int[1]; works
        // CCCC_Class cee;
        // cee = new CCCC_Class(); WORKS!
        // cee = new b(); CATCHS
        // a = a.length; CATCHS and works properly
        // a = a*a; WORKS
        //  b[a] = c; //CATCHS AND WORKS PROPERLY
        // a[1] = 1;
        a = c;
        return a;
    }
}
