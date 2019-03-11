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
    public static int rnum = 0;
    static String rname = "$t";
    String lastreg = "";

    Stack<String> stk = new Stack<String>();

    public static HashMap<String,String> vartoreg; // currently set inside of the data_grab() in main()

    public VisitorData()
    {
        ;
    }

    public void printme()
    {
        System.out.println("Vars map to regs");
        for(String name : vartoreg.keySet())
        {
            System.out.println(name + " -> " + vartoreg.get(name));
        }
    }

    //----------------------------------------

	public void visit(VAssign a)
        throws java.lang.Throwable
    {
        //printme();
        // System.out.println("\tAssign");
        // System.out.println(dubtab + "pos: " + a.sourcePos );
        // System.out.println(dubtab + "dst: " + a.dest.toString());
        // System.out.println(dubtab + "src: " + a.source.toString());

        // System.out.println(dubtab + a.dest.toString() + " = " + a.source.toString() );
        // System.out.println("a" + lastreg);

        String dst = a.dest.toString();
        if(vartoreg.containsKey(dst) && vartoreg.get(dst) != "" ) // not used ??
        {
            System.out.println("aaha!!!!");
            lastreg = vartoreg.get(dst);
        }
        else
        {
            // System.out.println("baha!!!!");
            lastreg = rname + Integer.toString(rnum);
            vartoreg.replace(dst,lastreg);
            rnum++;
        }

        // System.out.println("a " + lastreg);

        if(lastreg != "")
        {
            stk.push(lastreg);
        }

        System.out.println(dubtab + lastreg + " = " + a.source.toString() );    // jank

    }
    //----------------------------------------

    public void visit(VBranch b)
        throws java.lang.Throwable
    {
        // System.out.println(dubtab + "pos: " + b.sourcePos );
        // System.out.println("\tBranch");
        // System.out.println(dubtab + "cmp: " + b.positive);
        // System.out.println(dubtab + "val: " + b.value.toString());
        // System.out.println(dubtab + "jmp: " + b.target.toString());
        // System.out.println(dubtab + brnch + " " + b.value.toString() + " goto " + b.target.toString());
        //printme();

        String brnch = "if";
        if(b.positive == false)
        {
            brnch += "0";
        }

        String val = b.value.toString();
        String bbb = "";
        if(!stk.empty())
        {
            // System.out.println(stk.peek());
            bbb = stk.peek(); // change from pop
        }
        else if(vartoreg.containsKey(val) && vartoreg.get(val) != "" )
        {
            bbb = vartoreg.get(val);
        }
        System.out.println(dubtab + brnch + " " + bbb + " goto " + b.target.toString());
    }
    //----------------------------------------

    public void visit(VBuiltIn c)
        throws java.lang.Throwable
    {
        String str = "";

        // System.out.println("\tBuiltIn");
        // printme();
        // System.out.println(dubtab + "pos: " + c.sourcePos );
        // System.out.println(dubtab +  "op:  " + c.op.name);

        if(c.op.name.equals("Error")) //  handle the error function special case
        {
            System.out.println("  Error(" + c.args[0] + ")");
            return;
        }

        if(c.op.name.equals("HeapAllocZ")) //  handle the error function special case
        {
            lastreg = rname + Integer.toString(rnum);   // jank
            rnum++;
            String dst = c.dest.toString();
            vartoreg.replace(dst,lastreg);
            System.out.println("  " + lastreg + " = HeapAllocZ(" + c.args[0] + ")");
            stk.push(lastreg);  // push on stack probably used by the memwrite
            return;
        }

        str += c.op.name + "(";
        for(VOperand oper : c.args)
        {
            // System.out.println(dubtab + "arg: " + oper);
            // str = str + oper + " ";

            // str = str + lastreg + " ";  // jank1

            String dst = c.op.name;
            if(!stk.empty())
            {
                // System.out.println("clapper"); // common

                String treg = stk.pop();
                str = str + treg + " ";  // jank2
            }
            else if(vartoreg.containsKey(dst) && vartoreg.get(dst) != "")   // ??? not used??
            {
                System.out.println("slapper");  // never goes here
                System.out.print("d " + str + "\n");
                System.out.print("e " + oper + "\n");

                lastreg = rname + Integer.toString(rnum);   // jank
                String astr = lastreg + " = " + oper + "\n";
                System.out.print(dubtab + astr);  // declare tmp reg for immediate

                // System.out.print("b " + str + "\n");
                rnum++;
                // stk.push(lastreg); // ?
                str = str + " " + lastreg + " ";  // jank2
                // System.out.print("c " + str + "\n");
            }
            else
            {
                // System.out.println("flapper"); // common
                // System.out.print("f " + str + "\n");
                // System.out.print("g " + oper + "\n");
                // printme();
                //
                // System.out.println(oper.getClass()); // can be either a litInt or vvarreflocal

                String classy = oper.getClass().toString();
                if(classy.equals("class cs132.vapor.ast.VVarRef$Local"))
                {
                    // System.out.println("jankolator");

                    lastreg = rname + Integer.toString(rnum);   // jank

                    // System.out.println(lastreg);
                    // System.out.println(((VVarRef.Local)oper).ident);
                    // printme();

                    String nameo = vartoreg.get(((VVarRef.Local)oper).ident);
                    String astr = lastreg + " = " + nameo + "\n";  // ????????
                    rnum++;
                    System.out.print(dubtab + astr);  // declare tmp reg for immediate

                    // System.out.print("b " + str + "\n");
                    // stk.push(lastreg); // ?
                    str = str + " " + lastreg + " ";  // jank2
                    // System.out.print("c " + str + "\n");
                }
                else    // integer literal
                {

                    // lastreg = rname + Integer.toString(rnum);   // jank
                    // String astr = lastreg + " = " + oper + "\n";
                    // rnum++;
                    // System.out.print(dubtab + astr);  // declare tmp reg for immediate
                    //
                    // // System.out.print("b " + str + "\n");
                    // // stk.push(lastreg); // ?
                    // str = str + " " + lastreg + " ";  // jank2
                    // // System.out.print("c " + str + "\n");

                    str = str + " " + oper.toString() + " "; // put the integer directly in the call
                }
            }
        }
        str+=")";

        // dest might be null
        if(c.dest != null)
        {
            // System.out.println(dubtab + "dst: " + c.dest);
            // str = c.dest + " = ";

            // System.out.println("b" + lastreg);
            // lastreg = rname + Integer.toString(rnum);   // jank
            // System.out.println("b" + lastreg);
            // stk.push(lastreg);
            // System.out.println(dubtab + rname + Integer.toString(rnum) + " = " );  // jank1

            // str = rname + Integer.toString(rnum) + " = " + str;  // jank2
            // str = lastreg + " = " + str;  // jank2
            // rnum++;

            String dst = c.dest.toString();
            if(vartoreg.containsKey(dst) && vartoreg.get(dst) != "" )
            {
                lastreg = vartoreg.get(dst);
            }
            else
            {
                lastreg = rname + Integer.toString(rnum);
                vartoreg.replace(dst,lastreg);
                rnum++;
            }

            str = lastreg + " = " + str;  // jank2
        }

        System.out.println(dubtab + str);
    }
    //----------------------------------------

	public void visit(VCall c)
        throws java.lang.Throwable
    {
        // System.out.println("<<<<<<<<<<<<<<<<<<<<");
        // System.out.println(dubtab + "Call");
        // System.out.println(dubtab + "pos: " + c.sourcePos );

        // dest might be null
        if(c.dest != null)
        {
            // System.out.println(dubtab + "dst: " + c.dest);
        }
        // System.out.println(dubtab +  "fun:  " + c.addr.toString());

        // printme();

        int argi = 0;
        for(VOperand oper : c.args)
        {
            // System.out.println(dubtab + "arg: " + oper);
            String argv = vartoreg.get(oper.toString());
            if(argv == null)
            {
                argv = oper.toString();
            }
            System.out.println( "  $a" + Integer.toString(argi) + " = " + argv );
            argi++;
        }


        String freg = "";
        if(!stk.empty())
        {
            // System.out.println(stk.peek());
            freg = stk.peek();
        }

        String retreg = rname + Integer.toString(rnum);
        rnum++;
        stk.push(retreg);   // put ret val on the stack

        System.out.println(dubtab + "call " + freg);
        System.out.println(dubtab + retreg + " = $v0");  // set a reg to the special return register

        // System.out.println(">>>>>>>>>>>>>>>>>>>>");
    }
    //----------------------------------------

    public void visit(VGoto g)
        throws java.lang.Throwable
    {
        // System.out.println("\tGoto");
        // System.out.println(dubtab + "pos: " + g.sourcePos );

        // System.out.println(dubtab +  "dst:  " + g.target.toString());

        //printme();

        System.out.println(dubtab + "goto " + g.target.toString());
    }
    //----------------------------------------

    public void visit(VMemRead r)   // ?? Source
        throws java.lang.Throwable
    {
        // System.out.println("<<<<<<<<<<<<<<<<<<<<");
        // System.out.println("\tMemRead");
        // System.out.println(dubtab + "pos: " + r.sourcePos );
        //
        // System.out.println(dubtab + "dst: " + r.dest.toString());
        // System.out.println(dubtab + "src: " + r.source.toString());

        String memoref = "";
        if(!stk.empty())
        {
            // System.out.println("stk");
            // System.out.println(stk.peek());
            memoref = stk.peek(); // change from pop
        }


        // printme();


        String retreg = rname + Integer.toString(rnum);
        rnum++;
        stk.push(retreg);   // put ret val on the stack
        System.out.println(dubtab +  retreg + " = [ " + memoref  + "]");

        String dst = r.dest.toString();
        vartoreg.replace(dst,retreg);

        // System.out.println(">>>>>>>>>>>>>>>>>>>>");
    }
    //----------------------------------------

    public void visit(VMemWrite w)  // ?? Destination
        throws java.lang.Throwable
    {
        // System.out.println("\tMemWrite");
        // System.out.println(dubtab + "pos: " + w.sourcePos );
        //
        // System.out.println(dubtab + "dst: " + w.dest.toString());
        // System.out.println(dubtab + "src: " + w.source.toString());

        String memoref = "";
        if(!stk.empty())
        {
            // System.out.println(stk.peek());
            memoref = stk.peek(); // changed from pop
        }

        // printme();

        System.out.println(dubtab + "[" + memoref  + "] = " + w.source.toString());
    }
    //----------------------------------------

    public void visit(VReturn r)
        throws java.lang.Throwable
    {
        // System.out.println(dubtab + "Return");
        // System.out.println(dubtab + "pos: " + r.sourcePos );

        // if(r.value != null) // dest might be null
        // {
        //     System.out.println(dubtab + "val: " + r.value);
        // }

        //printme();

        System.out.println(dubtab + "ret");
    }
    //----------------------------------------

}