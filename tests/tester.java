class Medium
{
    public static void main(String[] args)
    {
        boolean a;
        boolean b;

        a = true;
        b = false;

        if (true  && true)  { System.out.println(1); } else { System.out.println(0); }  //  1  //  TRUE  = true  && true
        if (true && false)  { System.out.println(1); } else { System.out.println(0); }  //  0  //  FALSE = true  && false
        if (false && true)  { System.out.println(1); } else { System.out.println(0); }  //  0  //  FALSE = false && true // <------- WRONG
        if (false && false) { System.out.println(1); } else { System.out.println(0); }  //  0  //  FALSE = false && false

        if (a && true)  { System.out.println(1); } else { System.out.println(0); }  //  1  //  TRUE  = true  && true
        if (a && false) { System.out.println(1); } else { System.out.println(0); }  //  0  //  FALSE = true  && false
        if (b && true)  { System.out.println(1); } else { System.out.println(0); }  //  0  //  FALSE = false && true
        if (b && false) { System.out.println(1); } else { System.out.println(0); }  //  0  //  FALSE = false && false

        if (true  && a) { System.out.println(1); } else { System.out.println(0); }  //  1  //  TRUE  = true  && true
        if (true  && b) { System.out.println(1); } else { System.out.println(0); }  //  0  //  FALSE = true  && false
        if (false && a) { System.out.println(1); } else { System.out.println(0); }  //  0  //  FALSE = false && true
        if (false && b) { System.out.println(1); } else { System.out.println(0); }  //  0  //  FALSE = false && false

        if (a && a) { System.out.println(1); } else { System.out.println(0); }  //  1  //  TRUE  = true  && true
        if (a && b) { System.out.println(1); } else { System.out.println(0); }  //  0  //  FALSE = true  && false
        if (b && a) { System.out.println(1); } else { System.out.println(0); }  //  0  //  FALSE = false && true
        if (b && b) { System.out.println(1); } else { System.out.println(0); }  //  0  //  FALSE = false && false

    }
}
