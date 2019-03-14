import cs132.util.ProblemException;
import cs132.vapor.parser.VaporParser;
import cs132.vapor.ast.VaporProgram;
import cs132.vapor.ast.VBuiltIn.Op;

import cs132.util.*;
import cs132.vapor.ast.*;
import cs132.vapor.parser.*;

import java.io.*;
import java.util.*;

class VM2M
{
    public static void main(String[] args)
        throws IOException, Throwable
    {
        InputStream in = System.in;
        PrintStream err = System.err;

        VaporProgram prog = parseVapor(in, err);

        VisitorMips mips_visitor = new VisitorMips();

        mips_grab(prog, mips_visitor);

    }

    public static void mips_grab(VaporProgram  prog, VisitorMips vdatav)
        throws Throwable
    {
      VFunction[] fns = prog.functions;       // All the functions in this program
      VDataSegment[] dat = prog.dataSegments; // All the data segments in this program
      String[] reg = prog.registers;

      HashMap<String,ArrayList<String>> vtabs = new HashMap<String,ArrayList<String>>();


      System.out.println(".data");
      System.out.println();

      for (VDataSegment d : dat)
      {
          VOperand.Static[] vals = d.values;
          ArrayList<String> arlst = new ArrayList<String>();
          String vname = d.ident;
          vname = vname.substring(vname.indexOf("vmt"), vname.length() - 1);
          for(VOperand v : vals) // add to the map
          {
              arlst.add(v.toString());
          }
          if(!arlst.isEmpty())
          {
              vtabs.put(d.ident,arlst);
          }
          else{
              vtabs.put(d.ident,arlst);
          }
      }

      // print vtable
      for (Map.Entry<String,ArrayList<String>> entry : vtabs.entrySet())
      {
          System.out.println(entry.getKey() + ":");
          ArrayList<String> l = entry.getValue();
          for( String s : l )
          {
              String s_tmp = s.substring(1);            
              System.out.println("  " + s_tmp);
          }
          System.out.println("");
      }

      System.out.println();
      System.out.println(".text");
      System.out.println();
      System.out.println("  jal Main");
      System.out.println("  li $v0 10");
      System.out.println("  syscall");
      System.out.println();

      for(VFunction f: prog.functions) {
        System.out.println(f.ident.toString() + ":");
        // System.out.println(f.stack.in + " " + f.stack.out + " " + f.stack.local);
        int in = f.stack.in;
        int out = f.stack.out;
        int local = f.stack.local;
        int stack_ints = (out*4) + (local*4) + 8;
        VCodeLabel[] lbl = f.labels;
        HashMap<Integer,String> lmap = new HashMap<Integer,String>();

        for (VCodeLabel l : lbl)
        {
            // System.out.println(tab + "pos: " + l.sourcePos + "    " + l.ident );
            lmap.put(l.sourcePos.line, l.ident);
        }

        // sw $fp -8($sp)
        // move $fp $sp
        // subu $sp $sp 20
        // sw $ra -4($fp)

        System.out.println("  sw $fp -8($sp)");
        System.out.println("  move $fp $sp");
        System.out.println("  subu $sp $sp " + stack_ints);
        System.out.println("  sw $ra -4($fp)");

        VInstr[] bdy = f.body;
        for(VInstr vi: bdy) {
          int t = vi.sourcePos.line-1;
          // System.out.println("");
          // for(String s : lmap.values())
          // {
          //     System.out.println(s);
          // }
          // System.out.println("");

          while(lmap.containsKey(t))  // if the sourcePos + 1 = sourcePos of a label then print the label
          {
              System.out.println(lmap.get(t)+":");
              t--;
          }
          go_visit(vdatav, vi);
        }

        System.out.println("  lw $ra -4($fp)");
        System.out.println("  lw $fp -8($fp)");
        System.out.println("  addu $sp $sp " + stack_ints);
        System.out.println("  jr $ra");
        System.out.println();
      }

      System.out.println();
      String aux = "_print:\nli $v0 1   # syscall: print integer\nsyscall\nla $a0 _newline\nli $v0 4   # syscall: print string\nsyscall\njr $ra\n\n_error:\nli $v0 4   # syscall: print string\nsyscall\nli $v0 10  # syscall: exit\nsyscall\n\n_heapAlloc:\nli $v0 9   # syscall: sbrk\nsyscall\njr $ra\n\n.data\n.align 0\n_newline: .asciiz \"\\n\"\n_str0: .asciiz \"null pointer\"\n_str1: .asciiz \"array index out of bounds\"";
    System.out.println(aux);

    }

    public static void go_visit(VInstr.Visitor v,VInstr vi)
    throws Throwable
  {
    String classy = vi.getClass().toString();
    if(classy.equals("class cs132.vapor.ast.VAssign"))
    {
        v.visit((cs132.vapor.ast.VAssign)vi);
    }
    else if(classy.equals("class cs132.vapor.ast.VBranch"))
    {
        v.visit((cs132.vapor.ast.VBranch)vi);
    }
    else if(classy.equals("class cs132.vapor.ast.VBuiltIn"))
    {
        v.visit((cs132.vapor.ast.VBuiltIn)vi);
    }
    else if(classy.equals("class cs132.vapor.ast.VCall"))
    {
        v.visit((cs132.vapor.ast.VCall)vi);
    }
    else if(classy.equals("class cs132.vapor.ast.VGoto"))
    {
        v.visit((cs132.vapor.ast.VGoto)vi);
    }
    else if(classy.equals("class cs132.vapor.ast.VMemRead"))
    {
        v.visit((cs132.vapor.ast.VMemRead)vi);
    }
    else if(classy.equals("class cs132.vapor.ast.VMemWrite"))
    {
        v.visit((cs132.vapor.ast.VMemWrite)vi);
    }
    else if(classy.equals("class cs132.vapor.ast.VReturn"))
    {
        v.visit((cs132.vapor.ast.VReturn)vi);
    }
    else
    {
        System.out.println("error: '" + vi.getClass().toString() + "'");
    }
  }

    public static VaporProgram parseVapor(InputStream in, PrintStream err) throws IOException {
      Op[] ops = {
        Op.Add, Op.Sub, Op.MulS, Op.Eq, Op.Lt, Op.LtS,
        Op.PrintIntS, Op.HeapAllocZ, Op.Error,
      };

      boolean allowLocals = false;
      String[] registers = {
        "v0", "v1",
        "a0", "a1", "a2", "a3",
        "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7",
        "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7",
        "t8",
      };
      boolean allowStack = true;

      VaporProgram tree;
      try {
        tree = VaporParser.run(new InputStreamReader(in), 1, 1,
                               java.util.Arrays.asList(ops),
                               allowLocals, registers, allowStack);
      }
      catch (ProblemException ex) {
        err.println(ex.getMessage());
        return null;
      }

      return tree;
    }
}