package cs132.vapor.ast;
import cs132.util.SourcePos;

import java.lang.Throwable; // need for each visit function

import java.io.*;
import java.util.*;
import Graph.*;

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

public class VisitorVaporM<Throwable> extends VInstr.Visitor
// public class VisitorVaporM<P,R,E extends java.lang.Throwable> extends VInstr.VisitorPR
{

    String dubtab = "  ";
    // jank
    public static int rnum = 0;
    static String rname = "$t";
    String lastreg = "";

    Stack<String> stk = new Stack<String>();

    public static HashMap<String,String> vartoreg; // currently set inside of the data_grab() in main()

    public LinearScan all_mappings;
    public Map<String, String> curr_vartoreg_mappings;
    public Vector<String> func_args;
    public int end_line;
    public String current_func_name;

    public VisitorVaporM()
    {
        all_mappings = null;
        curr_vartoreg_mappings = null;
        func_args = null;
        end_line = 0;
        current_func_name = null;
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

    public String register_allocate(String k, int start_line) {
        //Find register within current_vartoreg_mappings
        String lastreg = "^";

        if(curr_vartoreg_mappings.containsKey(k)) {
            lastreg = curr_vartoreg_mappings.get(k);
            if(lastreg.indexOf("$t") == -1) {
                String var_reg = all_mappings.get_first_mapping(k, start_line, this.end_line, curr_vartoreg_mappings);
                System.out.println("  " + var_reg + " = " + lastreg);
                curr_vartoreg_mappings.replace(k, var_reg);
                lastreg = var_reg;
            }
        } //If not found, retrieve the mapping within LinearScan and put the mapping into current_vartoreg_mappings
        else {
            String var_reg = all_mappings.get_first_mapping(k, start_line, this.end_line, curr_vartoreg_mappings);
            curr_vartoreg_mappings.put(k, var_reg);
            lastreg = var_reg;
        }

        return lastreg;
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
            // System.out.println("aaha!!!!");
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

        lastreg = register_allocate(a.dest.toString(), a.dest.sourcePos.line);

        String source_reg = a.source.toString();
        String source_class = a.source.getClass().toString();
        
        if(!source_class.equals("class cs132.vapor.ast.VLitInt") && !source_class.equals("class cs132.vapor.ast.VLabelRef")) {
            source_reg = register_allocate(a.source.toString(), a.source.sourcePos.line);
        }

        System.out.println(dubtab + lastreg + " = " + source_reg );    // jank

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
        // if(!stk.empty())
        // {
        //     // System.out.println(stk.peek());
        //     bbb = stk.peek(); // change from pop
        // }
        // else if(vartoreg.containsKey(val) && vartoreg.get(val) != "" )
        // {
        //     bbb = vartoreg.get(val);
        // }

        bbb = register_allocate(b.value.toString(), b.sourcePos.line);
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

            lastreg = register_allocate(c.dest.toString(), c.sourcePos.line);
            String heap_arg = c.args[0].toString();
            String heap_arg_class = c.args[0].getClass().toString();

            if(!heap_arg_class.equals("class cs132.vapor.ast.VLitInt")) {
                heap_arg = register_allocate(heap_arg, c.sourcePos.line);
            }
            System.out.println("  " + lastreg + " = HeapAllocZ(" + heap_arg + ")");
            stk.push(lastreg);  // push on stack probably used by the memwrite
            return;
        }


        str += c.op.name + "(";
        for(int arg_num = 0; arg_num < c.args.length; arg_num++)
        {
            VOperand oper = c.args[arg_num];
            // System.out.println(dubtab + "arg: " + oper);
            // str = str + oper + " ";
            String c_name = oper.getClass().toString();
            String builtin_arg = oper.toString();
            if(!c_name.equals("class cs132.vapor.ast.VLitInt") && !c_name.equals("class cs132.vapor.ast.VLabelRef")) {
                builtin_arg = register_allocate(oper.toString(), oper.sourcePos.line);
            }
            str += builtin_arg;

            if(arg_num < c.args.length - 1) {
                str+= " ";
            }
            // str = str + lastreg + " ";  // jank1

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

            lastreg = register_allocate(c.dest.toString(), c.dest.sourcePos.line);

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
        // System.out.println(dubtab +  "fun:  " + c.addr.toString());

        // printme();
        String calling_reg = c.addr.toString();
        String calling_class = c.addr.getClass().toString();
        
        //calling_reg.indexOf(":") == -1
        //!calling_class.equals("class cs132.vapor.ast.VAddr$Label")
        if(!calling_class.equals("class cs132.vapor.ast.VAddr$Label")) {
            calling_reg = register_allocate(c.addr.toString(), c.sourcePos.line);
        }

        for(int argi = 0; argi < c.args.length; argi++)
        {
            VOperand oper = c.args[argi];
            // System.out.println(dubtab + "arg: " + oper);
            // String argv = vartoreg.get(oper.toString());
            String arg_class = oper.getClass().toString();
            String arg_reg = "";

            if(arg_class.equals("class cs132.vapor.ast.VLitInt") || arg_class.equals("class cs132.vapor.ast.VLabelRef")){
                arg_reg = oper.toString();
            }
            else {
                arg_reg  = register_allocate(oper.toString(), oper.sourcePos.line);
            }


            if(argi < 4) {

                System.out.println("  $a" + argi + " = " + arg_reg);
            }
            else {
                int out_ind = argi - 4;
                System.out.println("  out[" + out_ind + "] = " + arg_reg);
            }
            // System.out.println( "  $a" + Integer.toString(argi) + " = " + argv );
        }


        // String freg = "";
        // if(!stk.empty())
        // {
        //     // System.out.println(stk.peek());
        //     freg = stk.peek();
        // }

        String retreg = rname + Integer.toString(rnum);
        rnum++;
        stk.push(retreg);   // put ret val on the stack

        System.out.println("  call " + calling_reg );

        String dest_reg = "";
        if(c.dest != null)
        {
            dest_reg = register_allocate(c.dest.toString(), c.sourcePos.line);
            System.out.println("  " + dest_reg + " = $v0");
            // System.out.println(dubtab + "dst: " + c.dest);
        }


        // System.out.println(dubtab + "call " + freg);
        // System.out.println(dubtab + retreg + " = $v0");  // set a reg to the special return register

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

        String r_cast = "";
        int r_offset = 0;
        String c_name = "";

        if(r.source instanceof VMemRef.Global) {
            r_cast = ((VMemRef.Global)r.source).base.toString();
            r_offset = ((VMemRef.Global)r.source).byteOffset;
            c_name = "Global";
            // System.out.println("            $mem ref is " + r_cast);
        }
        else if(r.source instanceof VMemRef.Stack) {
            r_cast = ((VMemRef.Stack)r.source).region.toString();
            r_offset = ((VMemRef.Global)r.source).byteOffset;
            c_name = "Stack";
            // System.out.println("            $array ref is " + r_cast);
        }


        // printme();


        String retreg = rname + Integer.toString(rnum);
        rnum++;
        stk.push(retreg);   // put ret val on the stack
        // System.out.println(dubtab +  retreg + " = [ " + memoref  + "]");

        String dst = r.dest.toString();
        vartoreg.replace(dst,retreg);

        memoref = r_cast;

        if(!c_name.equals("Stack")) {
            memoref = register_allocate(r_cast, r.sourcePos.line);
        }

        if(r_offset > 0) {
            memoref = memoref + "+" + r_offset;
        } 

        retreg = register_allocate(r.dest.toString(), r.sourcePos.line);
        System.out.println("  " + retreg + " = [" + memoref + "]");

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
        String w_cast = "";
        int w_offset = 0;
        if(w.dest instanceof VMemRef.Global) {
            w_cast = ((VMemRef.Global)w.dest).base.toString();
            w_offset = ((VMemRef.Global)w.dest).byteOffset;

            // System.out.println("            $mem ref is " + w_cast);
        }
        else if(w.dest instanceof VMemRef.Stack) {
            w_cast = ((VMemRef.Stack)w.dest).region.toString();
            w_offset = ((VMemRef.Global)w.dest).byteOffset;
            // System.out.println("            $array ref is " + w_cast);
        }

        String memoref = "";
        if(!stk.empty())
        {
            // System.out.println(stk.peek());
            memoref = stk.peek(); // changed from pop
        }

        // printme();
        memoref = register_allocate(w_cast, w.sourcePos.line);
        String c_name = w.source.getClass().toString();
        String source_reg = w.source.toString();

        if(!c_name.equals("class cs132.vapor.ast.VLitInt") && !c_name.equals("class cs132.vapor.ast.VLabelRef")) {
            source_reg = register_allocate(w.source.toString(), w.sourcePos.line);
        }

        if(w_offset > 0) {
            memoref = memoref + "+" + w_offset;
        } 




        System.out.println("  " + "[" + memoref  + "] = " + source_reg);
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