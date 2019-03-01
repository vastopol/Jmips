package cs132.vapor.ast;
import cs132.util.SourcePos;

import java.lang.Throwable; // need for each visit function

import java.io.*;

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

    String dubtab = "\t\t";

    public VisitorData()
    {
        ;
    }
    //----------------------------------------

	public void visit(VAssign a)
        throws java.lang.Throwable
    {
        System.out.println("\tAssign");
        // System.out.println(dubtab + "dst: " + a.dest.toString());
        // System.out.println(dubtab + "src: " + a.source.toString());

        System.out.println(dubtab + a.dest.toString() + " = " + a.source.toString() );
    }
    //----------------------------------------

    public void visit(VBranch b)
        throws java.lang.Throwable
    {
        System.out.println("\tBranch");
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

        System.out.println("\tBuiltIn");
        // dest might be null
        if(c.dest != null)
        {
            // System.out.println(dubtab + "dst: " + c.dest);
            str = c.dest + " = ";
        }
        // System.out.println(dubtab +  "op:  " + c.op.name);
        str += c.op.name + "(";
        for(VOperand oper : c.args)
        {
            // System.out.println(dubtab + "arg: " + oper);
            str = str + oper + " ";
        }
        str+=")";
        System.out.println(dubtab + str);
    }
    //----------------------------------------

	public void visit(VCall c)
        throws java.lang.Throwable
    {
        System.out.println("\tCall");
        // dest might be null
        if(c.dest != null)
        {
            System.out.println(dubtab + "dst: " + c.dest);
        }
        System.out.println(dubtab +  "fun:  " + c.addr.toString());
        for(VOperand oper : c.args)
        {
            System.out.println(dubtab + "arg: " + oper);
        }
    }
    //----------------------------------------

    public void visit(VGoto g)
        throws java.lang.Throwable
    {
        System.out.println("\tGoto");
        System.out.println(dubtab +  "dst:  " + g.target.toString());
    }
    //----------------------------------------

    public void visit(VMemRead r)   // ?? Source
        throws java.lang.Throwable
    {
        System.out.println("\tMemRead");
        System.out.println(dubtab + "dst: " + r.dest.toString());
        System.out.println(dubtab + "src: " + r.source.toString());
    }
    //----------------------------------------

    public void visit(VMemWrite w)  // ?? Destination
        throws java.lang.Throwable
    {
        System.out.println("\tMemWrite");
        System.out.println(dubtab + "dst: " + w.dest.toString());
        System.out.println(dubtab + "src: " + w.source.toString());
    }
    //----------------------------------------

    public void visit(VReturn r)
        throws java.lang.Throwable
    {
        System.out.println("\tReturn");
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