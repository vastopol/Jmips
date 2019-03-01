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
        System.out.println(dubtab + "dst: " + a.dest.toString());
        System.out.println(dubtab + "src: " + a.source.toString());
    }
    //----------------------------------------

    public void visit(VBranch b)
        throws java.lang.Throwable
    {
        System.out.println(dubtab + "cmp: " + b.positive);
        System.out.println(dubtab + "val: " + b.value.toString());
        System.out.println(dubtab + "jmp: " + b.target.toString());
    }
    //----------------------------------------

    public void visit(VBuiltIn c)
        throws java.lang.Throwable
    {
        // dest might be null
        if(c.dest != null)
        {
            System.out.println(dubtab + "dst: " + c.dest);
        }
        System.out.println(dubtab +  "op:  " + c.op.name);
        for(VOperand oper : c.args)
        {
            System.out.println(dubtab + "arg: " + oper);
        }
    }
    //----------------------------------------

	public void visit(VCall c)
        throws java.lang.Throwable
    {
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
        System.out.println(dubtab +  "dst:  " + g.target.toString());
    }
    //----------------------------------------

    public void visit(VMemRead r)   // ?? Source
        throws java.lang.Throwable
    {
        System.out.println(dubtab + "dst: " + r.dest.toString());
        System.out.println(dubtab + "src: " + r.source.toString());
    }
    //----------------------------------------

    public void visit(VMemWrite w)  // ?? Destination
        throws java.lang.Throwable
    {
        System.out.println(dubtab + "dst: " + w.dest.toString());
        System.out.println(dubtab + "src: " + w.source.toString());
    }
    //----------------------------------------

    public void visit(VReturn r)
        throws java.lang.Throwable
    {
        // dest might be null
        if(r.value != null)
        {
            System.out.println(dubtab + "val: " + r.value);
        }
    }
    //----------------------------------------

}