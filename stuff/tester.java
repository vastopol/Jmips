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
            a = 1;
        else
            a = num * (this.MAGIC_Func(num-1));
        return a;
    }
}
