package cs132.vapor.ast;
import cs132.util.SourcePos;
import cs132.vapor.ast.VMemRef;

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

public class VisitorMips<Throwable> extends VInstr.Visitor
// public class VisitorPrinter<P,R,E extends java.lang.Throwable> extends VInstr.VisitorPR
{
//System.out.println("\t" + a.instrIndex + "    " + a.sourcePos + "    " + a.ident );
    String dubtab = "\t\t";

    public VisitorMips()
    {
        ;
    }
    //----------------------------------------

	public void visit(VAssign a)
        throws java.lang.Throwable
    {
        // System.out.println("\tAssign");
        // System.out.println(dubtab + "pos: " + a.sourcePos );
        // System.out.println(dubtab + "dst: " + a.dest.toString());
        // System.out.println(dubtab + "src: " + a.source.toString());

        String source_class = a.source.getClass().toString();
        String dest_temp = a.dest.toString();
        String source_temp = a.source.toString();

        if(dest_temp.indexOf("out") != -1) {
            String loc_str = dest_temp.substring(4, 5);
            int loc_num = Integer.parseInt(loc_str);
            loc_num = loc_num * 4;
            System.out.println("  sw $t9 " + loc_num + "($sp)");
            dest_temp = "$t9";
        }
        
        if(source_class.equals("class cs132.vapor.ast.VLitInt")) {
            System.out.println("  li " + dest_temp + " " + a.source.toString());
            return;
        }

        System.out.println("  move " + a.dest.toString() + " " + a.source.toString());

    }
    //----------------------------------------

    public void visit(VBranch b)
        throws java.lang.Throwable
    {
        // System.out.println("\tBranch");
        // System.out.println(dubtab + "pos: " + b.sourcePos );
        // System.out.println(dubtab + "cmp: " + b.positive);
        // System.out.println(dubtab + "val: " + b.value.toString());
        // System.out.println(dubtab + "jmp: " + b.target.toString());

        String opname = "beqz";

        if(b.positive) {
            opname = "bnez";
        }

        String target_tmp = b.target.toString();

        if(target_tmp.indexOf(":") != -1) {
            // int index = target_tmp.indexOf(":");
            target_tmp = target_tmp.substring(1);
        }

        System.out.println("  " + opname + " " + b.value.toString() + " " + target_tmp);
    }
    //----------------------------------------

    public void visit(VBuiltIn c)
        throws java.lang.Throwable
    {
        // System.out.println("\tBuiltIn");
        // System.out.println(dubtab + "pos: " + c.sourcePos );
        // // dest might be null
        // if(c.dest != null)
        // {
        //     System.out.println(dubtab + "dst: " + c.dest);
        // }
        // System.out.println(dubtab +  "op:  " + c.op.name);
        // for(VOperand oper : c.args)
        // {
        //     System.out.println(dubtab + "arg: " + oper);
        // }
        String opname = "";

        if(c.op.name.equals("LtS")) {
            opname = "slt";
        }
        else if(c.op.name.equals("Add")) {
            opname = "addu";
        }
        else if(c.op.name.equals("Sub")) {
            opname = "subu";
        }
        else if(c.op.name.equals("MulS")) {
            opname = "mult";
        }
        
        if(c.op.name.equals("Error")) //  handle the error function special case
        {
            System.out.println("  la $a0 _str0");
            System.out.println("  j _error");
            return;
        }

        if(c.op.name.equals("HeapAllocZ")) {
            String dst = c.dest.toString();
            String arg = c.args[0].toString();

            System.out.println("  li $a0 " + arg);
            System.out.println("  jal _heapAlloc");
            System.out.println("  move " + dst + " $v0");

            return;
        }

        if(c.op.name.equals("PrintIntS")) {
            String arg = c.args[0].toString();
            System.out.println("  move $a0 " + arg);
            System.out.println("  jal _print");
            return;
        }

        // for(int arg_num = 0; arg_num < c.args.length; arg_num++)
        // {
        //     VOperand oper = c.args[arg_num];
        //     String c_name = oper.getClass().toString();
        //     String builtin_arg = oper.toString();


        // }
        System.out.println(opname + " " + c.dest.toString() + " " + c.args[0].toString() + " " + c.args[1].toString());



        String stor = "sw $t9 " + 0 + "($sp)"; // offset
    }
    //----------------------------------------

	public void visit(VCall c)
        throws java.lang.Throwable
    {
        // System.out.println("\tCall");
        // System.out.println(dubtab + "pos: " + c.sourcePos );
        // // dest might be null
        // if(c.dest != null)
        // {
        //     System.out.println(dubtab + "dst: " + c.dest);
        // }
        // System.out.println(dubtab +  "fun:  " + c.addr.toString());
        // for(VOperand oper : c.args)
        // {
        //     System.out.println(dubtab + "arg: " + oper);
        // }
        String func_temp = c.addr.toString();
        System.out.println("jalr " + func_temp);

    }
    //----------------------------------------

    public void visit(VGoto g)
        throws java.lang.Throwable
    {
        System.out.println("\tGoto");
        System.out.println(dubtab +  "pos: "  + g.sourcePos );
        System.out.println(dubtab +  "dst:  " + g.target.toString());
    }
    //----------------------------------------

    public void visit(VMemRead r)   // ?? Source
        throws java.lang.Throwable
    {
        String r_cast = "";
        int r_offset = 0;

        if(r.source instanceof VMemRef.Global) {
            r_cast = ((VMemRef.Global)r.source).base.toString();
            r_offset = ((VMemRef.Global)r.source).byteOffset;
            // System.out.println("            $mem ref is " + r_cast);
        }
        else if(r.source instanceof VMemRef.Stack) {
            r_cast = ((VMemRef.Stack)r.source).region.toString();
            r_offset = ((VMemRef.Stack)r.source).index;
            // System.out.println("            $array ref is " + r_cast);
        }

        String dest_temp1 = r_cast;
        if(dest_temp1.indexOf("In") != -1) {
            int loc_num = r_offset * 4;
            System.out.println("  lw $t9 " + loc_num + "($fp)");
            return;
        }

        String dest_temp = r.dest.toString();
        if(dest_temp.indexOf(":") != -1) {
            dest_temp = dest_temp.substring(1);
        }

        System.out.println("  lw " + dest_temp + " " + r_offset + "(" + r_cast + ")");
    }
    //----------------------------------------

    public void visit(VMemWrite w)  // ?? Destination
        throws java.lang.Throwable
    {
        // System.out.println("\tMemWrite");
        // System.out.println(dubtab + "pos: " + w.sourcePos );
        // System.out.println(dubtab + "dst: " + w.dest.toString());
        // System.out.println(dubtab + "src: " + w.source.toString());


        String w_cast = "";
        int w_offset = 0;
        if(w.dest instanceof VMemRef.Global) {
            w_cast = ((VMemRef.Global)w.dest).base.toString();
            w_offset = ((VMemRef.Global)w.dest).byteOffset;

            // System.out.println("            $mem ref is " + w_cast);
        }
        else if(w.dest instanceof VMemRef.Stack) {
            w_cast = ((VMemRef.Stack)w.dest).region.toString();
            w_offset = ((VMemRef.Stack)w.dest).index;
            // System.out.println("            $array ref is " + w_cast);
        }

        String dest_temp1 = w_cast;
        if(dest_temp1.indexOf("Out") != -1) {
            int loc_num = w_offset * 4;
            String source1_temp = w.source.toString();
            if(source1_temp.indexOf("$") != -1) {
                System.out.println("  move $t9  " + source1_temp);
            }
            else {
                System.out.println("  li $t9 " + w.source.toString());
            }
            System.out.println("  sw $t9 " + loc_num + "($sp)");
            return;
        }
    

        String source_temp = w.source.toString();

        if(source_temp.indexOf(":") != -1) {
            source_temp = source_temp.substring(1);
        }
        System.out.println("  la $t9 " + source_temp);
        System.out.println("  sw $t9 " +w_offset+ "(" + w_cast + ")");
    }
    //----------------------------------------

    public void visit(VReturn r)
        throws java.lang.Throwable
    {
        // System.out.println("\tReturn");
        // System.out.println(dubtab + "pos: " + r.sourcePos );
        // // dest might be null
        // if(r.value != null)
        // {
        //     System.out.println(dubtab + "val: " + r.value);
        // }
    }
    //----------------------------------------

}