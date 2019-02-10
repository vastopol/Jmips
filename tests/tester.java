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
        System.out.println(1);  // 1 correct
        System.out.println(a);  // 1 correct
        System.out.println(2 - 3); // -1 correct
        System.out.println(2 + 3); // 5 correct
        System.out.println(2 * 3); // 6 correct
        System.out.println(c - 5); // -3 correct
        System.out.println(c + 5); // 7 correct
        System.out.println(c * 5); // 10 correct
        System.out.println(5 - c); // 3 correct
        System.out.println(5 + c); // 7 correct
        System.out.println(5 * c); // 10 correct
        System.out.println(a - b); // -1 correct
        System.out.println(a + b); // 3 correct
        System.out.println(a * b); // 2 correct
        System.out.println(c - b); // 0 correct
        System.out.println(c + b); // 4 correct
        System.out.println(c * b); // 4 correct
        d = 6 + 5;
        d = d + 1;
        System.out.println(d); // 12 correct
        d = a + b;
        d = d + c;
        System.out.println(d); // 5 correct
    }
}

