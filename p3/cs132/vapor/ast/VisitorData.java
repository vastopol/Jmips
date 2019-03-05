package cs132.vapor.ast;
import cs132.util.SourcePos;

import java.lang.Throwable; // need for each visit function

import java.io.*;
import java.util.*;

// SEE THE VInstr.java file

/**
 * Visitor for instruction nodes, where each visitor function takes a parameter and
 * returns a value.
 *
 * @param <P>
 *     The type of the extra parameter taken by each visitor function.
 * @param <R>
 *     The type of the value returned by each visitor function.
 * @param <E>
 *     The exception type that each node is allowed to throw.  If you don't want checked
 *     exceptions here, use {@link java.lang.Throwable}.
 */

public class VisitorData<Throwable> extends VInstr.Visitor
// public class VisitorPrinter<P,R,E extends java.lang.Throwable> extends VInstr.VisitorPR
{

    String dubtab = "  ";
    // jank
    static int rnum = 0;
    static String rname = "$t";
    String lastreg = "";

    Stack<String> stk = new Stack<String>();

    public VisitorData()
    {
        ;
    }
    //----------------------------------------

	public void visit(VAssign a)
        throws java.lang.Throwable
    {
        // System.out.println("\tAssign");

        // System.out.println(dubtab + "dst: " + a.dest.toString());
        // System.out.println(dubtab + "src: " + a.source.toString());

        // System.out.println(dubtab + a.dest.toString() + " = " + a.source.toString() );

        // System.out.println("a" + lastreg);
        lastreg = rname + Integer.toString(rnum);
        // System.out.println("a" + lastreg);
        if(lastreg != "")
        {
            stk.push(lastreg);
        }
        System.out.println(dubtab + rname + Integer.toString(rnum) + " = " + a.source.toString() );    // jank
        rnum++;
    }
    //----------------------------------------

    public void visit(VBranch b)
        throws java.lang.Throwable
    {
        // System.out.println("\tBranch");

        // System.out.println(dubtab + "cmp: " + b.positive);
        // System.out.println(dubtab + "val: " + b.value.toString());
        // System.out.println(dubtab + "jmp: " + b.target.toString());

        String brnch = "if";
        if(b.positive == false)
        {
            brnch += "0";
        }
        System.out.println(dubtab + brnch + " " + b.value.toString() + " goto " + b.target.toString());
    }
    //----------------------------------------

    public void visit(VBuiltIn c)
        throws java.lang.Throwable
    {
        String str = "";

        // System.out.println("\tBuiltIn");
        // System.out.println(dubtab +  "op:  " + c.op.name);
        str += c.op.name + "(";
        for(VOperand oper : c.args)
        {
            // System.out.println(dubtab + "arg: " + oper);
            // str = str + oper + " ";

            // str = str + lastreg + " ";  // jank1

            if(!stk.empty())
            {
                String treg = stk.pop();
                str = str + treg + " ";  // jank2
            }
            else
            {
                lastreg = rname + Integer.toString(rnum);   // jank
                String astr = lastreg + " = " + oper + "\n";
                System.out.print(dubtab + astr);  // declare tmp reg for immediate

                // System.out.print("b " + str + "\n");
                rnum++;
                stk.push(lastreg);
                str = str + " " + lastreg ;  // jank2
                // System.out.print("c " + str + "\n");
            }
        }
        str+=")";

        // dest might be null
        if(c.dest != null)
        {
            // System.out.println(dubtab + "dst: " + c.dest);
            // str = c.dest + " = ";

            // System.out.println("b" + lastreg);
            lastreg = rname + Integer.toString(rnum);   // jank
            // System.out.println("b" + lastreg);
            stk.push(lastreg);
            // System.out.println(dubtab + rname + Integer.toString(rnum) + " = " );  // jank1

            // str = rname + Integer.toString(rnum) + " = " + str;  // jank2
            str = lastreg + " = " + str;  // jank2
            rnum++;
        }

        System.out.println(dubtab + str);
    }
    //----------------------------------------

	public void visit(VCall c)
        throws java.lang.Throwable
    {
        // System.out.println("\tCall");
        // dest might be null
        if(c.dest != null)
        {
            // System.out.println(dubtab + "dst: " + c.dest);
        }
        // System.out.println(dubtab +  "fun:  " + c.addr.toString());

        for(VOperand oper : c.args)
        {
            // System.out.println(dubtab + "arg: " + oper);
        }
    }
    //----------------------------------------

    public void visit(VGoto g)
        throws java.lang.Throwable
    {
        // System.out.println("\tGoto");

        // System.out.println(dubtab +  "dst:  " + g.target.toString());

        System.out.println(dubtab + "goto " + g.target.toString());
    }
    //----------------------------------------

    public void visit(VMemRead r)   // ?? Source
        throws java.lang.Throwable
    {
        // System.out.println("\tMemRead");

        // System.out.println(dubtab + "dst: " + r.dest.toString());
        // System.out.println(dubtab + "src: " + r.source.toString());
    }
    //----------------------------------------

    public void visit(VMemWrite w)  // ?? Destination
        throws java.lang.Throwable
    {
        // System.out.println("\tMemWrite");

        // System.out.println(dubtab + "dst: " + w.dest.toString());
        // System.out.println(dubtab + "src: " + w.source.toString());
    }
    //----------------------------------------

    public void visit(VReturn r)
        throws java.lang.Throwable
    {
        // System.out.println("\tReturn");
        // dest might be null
        if(r.value != null)
        {
            // System.out.println(dubtab + "val: " + r.value);
            System.out.println(dubtab + "ret " + r.value);
        }
        else
        {
            System.out.println(dubtab + "ret");
        }
    }
    //----------------------------------------

}