package visitor2;
import visitor.Visitor;
import syntaxtree.*;
import java.util.*;
import struct.*;
import toolbox.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class DFTypeCheckVisitor implements Visitor {
   //
   // Auto class visitors--probably don't need to be overridden.
   //
   Map<String,Map<String,Struct>> symbol_table;
   String current_class;
   String current_function;
   String expr_type;
   String curr_id;
   public boolean typechecks = true;

   public DFTypeCheckVisitor(Map<String, Map<String,Struct>> m) {
      symbol_table = m;
   }

   public void visit(NodeList n) {
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this);
   }

   public void visit(NodeListOptional n) {
      if ( n.present() )
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
            e.nextElement().accept(this);
   }

   public void visit(NodeOptional n) {
      if ( n.present() )
         n.node.accept(this);
   }

   public void visit(NodeSequence n) {
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); )
         e.nextElement().accept(this);
   }

   public void visit(NodeToken n) { }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> MainClass()
    * f1 -> ( TypeDeclaration() )*
    * f2 -> <EOF>
    */
   public void visit(Goal n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> "public"
    * f4 -> "static"
    * f5 -> "void"
    * f6 -> "main"
    * f7 -> "("
    * f8 -> "String"
    * f9 -> "["
    * f10 -> "]"
    * f11 -> Identifier()
    * f12 -> ")"
    * f13 -> "{"
    * f14 -> ( VarDeclaration() )*
    * f15 -> ( Statement() )*
    * f16 -> "}"
    * f17 -> "}"
    */
   public void visit(MainClass n) {
      n.f0.accept(this);
      n.f1.accept(this);
      current_class = curr_id;
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      current_function = curr_id;
      n.f7.accept(this);
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      n.f11.accept(this);
      n.f12.accept(this);
      n.f13.accept(this);
      n.f14.accept(this);
      n.f15.accept(this);
      n.f16.accept(this);
      n.f17.accept(this);
      current_function = "";
      current_class = "";
   }

   /**
    * f0 -> ClassDeclaration()
    *       | ClassExtendsDeclaration()
    */
   public void visit(TypeDeclaration n) {
      n.f0.accept(this);
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "{"
    * f3 -> ( VarDeclaration() )*
    * f4 -> ( MethodDeclaration() )*
    * f5 -> "}"
    */
   public void visit(ClassDeclaration n) {
      n.f0.accept(this);
      n.f1.accept(this);
      current_class = curr_id;
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      current_class = "";
   }

   /**
    * f0 -> "class"
    * f1 -> Identifier()
    * f2 -> "extends"
    * f3 -> Identifier()
    * f4 -> "{"
    * f5 -> ( VarDeclaration() )*
    * f6 -> ( MethodDeclaration() )*
    * f7 -> "}"
    */
   public void visit(ClassExtendsDeclaration n) {
      n.f0.accept(this);
      n.f1.accept(this);
      current_class = curr_id;
      n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      current_class = "";
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    * f2 -> ";"
    */
   public void visit(VarDeclaration n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
    * f0 -> "public"
    * f1 -> Type()
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( FormalParameterList() )?
    * f5 -> ")"
    * f6 -> "{"
    * f7 -> ( VarDeclaration() )*
    * f8 -> ( Statement() )*
    * f9 -> "return"
    * f10 -> Expression()
    * f11 -> ";"
    * f12 -> "}"
    */
   public void visit(MethodDeclaration n) {
      n.f0.accept(this);
      n.f1.accept(this);
      String tmp1 = expr_type;
      String tmp11 = curr_id;
    //   System.out.println("Type is " + expr_type);
      n.f2.accept(this);
      System.out.println(curr_id);
      current_function = curr_id;
        //   System.out.println("current class is " + current_class + " current function is " + current_function);
          Map<String, Struct> method_struct2 = symbol_table.get(current_class);
          if(method_struct2 == null) {
              typechecks = false;
              System.out.println("Typecheck error in methoddeclaration1");
              return;
          }
          Struct method_struct = method_struct2.get(current_function);
          Vector<String> pnames = new Vector<String>();
          if(method_struct == null) {
              typechecks = false;
              System.out.println("Typecheck error in methoddeclaration2");
              return;
          }
    
          if(method_struct.getParams().size() > 1) {

            for(Struct i: method_struct.getParams()) {
               pnames.add(i.getName());
             }
    
            if(!helper.distinct(pnames)) {
              typechecks = false;
                 System.out.println("Typecheck error in methoddeclaration3");
                 return;
            }
        }

      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      Map<String, Struct> method_table = symbol_table.get(current_function);
      Vector<String> inames = new Vector<String>();

      for(String i: method_table.keySet()) {
         inames.add(method_table.get(i).getName());
      }
      if(inames.size() > 1) {
        if(!helper.distinct(inames)) {
            typechecks = false;
            System.out.println("Typecheck error in methoddeclaration 4");
            for (String i: inames) {
                System.out.println("identifier " + i);
            }
            return;
         }
      }
      n.f8.accept(this);
      n.f9.accept(this);
      n.f10.accept(this);
      String tmp2 = expr_type;
      String tmp22 = curr_id;
      if(tmp1 != tmp2) {
        if(tmp2 == "object") {
            Struct id = symbol_table.get(current_function).get(tmp22);
            if(id == null) {
                id = symbol_table.get(current_class).get(tmp22);
                if(id == null) {
                  System.out.println("Typecheck error in methoddeclaration5");
                  typechecks = false;
                  return;
                }
            }

            if(id.get_className() != tmp11) {
              System.out.println("Typecheck error in methoddeclaration6 " + tmp1 + " " + id.get_className());
              typechecks = false;
              return;
            }
        }
        else {
            System.out.println("Typecheck error in methoddeclaration7 ");
            typechecks = false;
            return;
        }
      }
      n.f11.accept(this);
      n.f12.accept(this);
      current_function = "";
   }

   /**
    * f0 -> FormalParameter()
    * f1 -> ( FormalParameterRest() )*
    */
   public void visit(FormalParameterList n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * f0 -> Type()
    * f1 -> Identifier()
    */
   public void visit(FormalParameter n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * f0 -> ","
    * f1 -> FormalParameter()
    */
   public void visit(FormalParameterRest n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * f0 -> ArrayType()
    *       | BooleanType()
    *       | IntegerType()
    *       | Identifier()
    */
   public void visit(Type n) {
      n.f0.accept(this);
   }

   /**
    * f0 -> "int"
    * f1 -> "["
    * f2 -> "]"
    */
   public void visit(ArrayType n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      expr_type = "int[]";
   }

   /**
    * f0 -> "boolean"
    */
   public void visit(BooleanType n) {
      n.f0.accept(this);
      expr_type = "boolean";
   }

   /**
    * f0 -> "int"
    */
   public void visit(IntegerType n) {
      n.f0.accept(this);
      expr_type = "int";
   }

   /**
    * f0 -> Block()
    *       | AssignmentStatement()
    *       | ArrayAssignmentStatement()
    *       | IfStatement()
    *       | WhileStatement()
    *       | PrintStatement()
    */
   public void visit(Statement n) {
      n.f0.accept(this);
   }

   /**
    * f0 -> "{"
    * f1 -> ( Statement() )*
    * f2 -> "}"
    */
   public void visit(Block n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

   /**
    * f0 -> Identifier()
    * f1 -> "="
    * f2 -> Expression()
    * f3 -> ";"
    */
   public void visit(AssignmentStatement n) {
      n.f0.accept(this);
      String tmp1 = expr_type;
      String tmp11 = curr_id;
      n.f1.accept(this);
      n.f2.accept(this);
      String tmp2 = expr_type;
      String tmp22 = curr_id;
      n.f3.accept(this);
      System.out.println(tmp1 + " " + tmp11 + " = " + tmp2 + " " + tmp22);
      if(tmp1 != tmp2) {
          if(tmp1 == "object") {
              Struct id = symbol_table.get(current_function).get(tmp11);
              if(id == null) {
                  id = symbol_table.get(current_class).get(tmp11);
                  if(id == null) {
                    System.out.println("Typecheck error in assignment1");
                    typechecks = false;
                    return;
                  }
              }

              if(id.get_className() != tmp2) {
                System.out.println("Typecheck error in assignment2");
                typechecks = false;
                return;
              }
          }
      }
   }

   /**
    * f0 -> Identifier()
    * f1 -> "["
    * f2 -> Expression()
    * f3 -> "]"
    * f4 -> "="
    * f5 -> Expression()
    * f6 -> ";"
    */
   public void visit(ArrayAssignmentStatement n) {
      n.f0.accept(this);
      if(expr_type != "int[]") {
        typechecks = false;
        System.out.println("Typecheck error in arrayassignment1");
        return;
     }
      n.f1.accept(this);
      n.f2.accept(this);
      if(expr_type != "int") {
        typechecks = false;
        System.out.println("Typecheck error in arrayassignment2");
        return;
     }
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      if(expr_type != "int") {
        typechecks = false;
        System.out.println("Typecheck error in arrayassignment3");
        return;
     }
      n.f6.accept(this);
   }

   /**
    * f0 -> "if"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    * f5 -> "else"
    * f6 -> Statement()
    */
   public void visit(IfStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      if(expr_type != "boolean") {
        typechecks = false;
        System.out.println("Typecheck error in ifstatement");
        return;
     }
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      n.f6.accept(this);
   }

   /**
    * f0 -> "while"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> Statement()
    */
   public void visit(WhileStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      if(expr_type != "boolean") {
        typechecks = false;
        System.out.println("Typecheck error in whilestatement");
        return;
     }
      n.f3.accept(this);
      n.f4.accept(this);
   }

   /**
    * f0 -> "System.out.println"
    * f1 -> "("
    * f2 -> Expression()
    * f3 -> ")"
    * f4 -> ";"
    */
   public void visit(PrintStatement n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      if(expr_type != "int") {
        typechecks = false;
        System.out.println("Type error in print");
        return;
     }
      n.f3.accept(this);
      n.f4.accept(this);
   }

   /**
    * f0 -> AndExpression()
    *       | CompareExpression()
    *       | PlusExpression()
    *       | MinusExpression()
    *       | TimesExpression()
    *       | ArrayLookup()
    *       | ArrayLength()
    *       | MessageSend()
    *       | PrimaryExpression()
    */
   public void visit(Expression n) {
      n.f0.accept(this);
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "&&"
    * f2 -> PrimaryExpression()
    */
   public void visit(AndExpression n) {
      n.f0.accept(this);
      if(expr_type != "boolean") {
        typechecks = false;
        System.out.println("Typecheck error in and1");
        return;
     }
      n.f1.accept(this);
      n.f2.accept(this);
      if(expr_type != "boolean") {
        typechecks = false;
        System.out.println("Typecheck error in and2");
        return;
     }
     expr_type = "boolean";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "<"
    * f2 -> PrimaryExpression()
    */
   public void visit(CompareExpression n) {
      n.f0.accept(this);
      if(expr_type != "int") {
        typechecks = false;
        System.out.println("Typecheck error in lessthan1");
        return;
     }
      n.f1.accept(this);
      n.f2.accept(this);
      if(expr_type != "int") {
        typechecks = false;
        System.out.println("Typecheck error in lessthan2");
        return;
     }
     expr_type = "boolean";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "+"
    * f2 -> PrimaryExpression()
    */
   public void visit(PlusExpression n) {
      n.f0.accept(this);
      if(expr_type != "int") {
        typechecks = false;
        System.out.println("Typecheck error in plus1");
        return;
     }
      n.f1.accept(this);
      n.f2.accept(this);
      if(expr_type != "int") {
        typechecks = false;
        System.out.println("Typecheck error in plus2");
        return;
     }
     expr_type = "int";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "-"
    * f2 -> PrimaryExpression()
    */
   public void visit(MinusExpression n) {
      n.f0.accept(this);
      if(expr_type != "int") {
        typechecks = false;
        System.out.println("Typecheck error in minus1");
        return;
     }
      n.f1.accept(this);
      n.f2.accept(this);
      if(expr_type != "int") {
        typechecks = false;
        System.out.println("Typecheck error in minus2");
        return;
     }
     expr_type = "int";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "*"
    * f2 -> PrimaryExpression()
    */
   public void visit(TimesExpression n) {
      n.f0.accept(this);
      if(expr_type != "int") {
        typechecks = false;
        System.out.println("Typecheck error in times1");
        return;
     }
      n.f1.accept(this);
      n.f2.accept(this);
      if(expr_type != "int") {
        typechecks = false;
        System.out.println("Typecheck error in times2");
        return;
     }
     expr_type = "int";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "["
    * f2 -> PrimaryExpression()
    * f3 -> "]"
    */
   public void visit(ArrayLookup n) {
      n.f0.accept(this);
      if(expr_type != "int[]") {
        typechecks = false;
        System.out.println("Type error in arraylookup1");
        return;
     }
      n.f1.accept(this);
      n.f2.accept(this);
      if(expr_type != "int") {
        typechecks = false;
        System.out.println("Type error in arraylookup2");
        return;
     }
      n.f3.accept(this);
      expr_type = "int";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> "length"
    */
   public void visit(ArrayLength n) {
      n.f0.accept(this);
      if(expr_type != "int[]") {
        typechecks = false;
        System.out.println("Typecheck error in array length");
        return;
     }
      n.f1.accept(this);
      n.f2.accept(this);
      expr_type = "int";
   }

   /**
    * f0 -> PrimaryExpression()
    * f1 -> "."
    * f2 -> Identifier()
    * f3 -> "("
    * f4 -> ( ExpressionList() )?
    * f5 -> ")"
    */
   public void visit(MessageSend n) {
      n.f0.accept(this);
      String tmp1 = curr_id;
      Struct curr = symbol_table.get("Global").get(tmp1);
      if(curr == null) {
        curr = symbol_table.get(current_function).get(tmp1);
        if(curr == null) {;
            curr = symbol_table.get(current_class).get(tmp1);
            if(curr == null) {
                System.out.println("Typecheck error in message send 1 " + tmp1 + " " + current_class + " " + current_function);
                typechecks = false;
                return;
            }
        }
      }
    //   System.out.println(expr_type);
      n.f1.accept(this);
      n.f2.accept(this);
      String tmp2 = curr_id;
      String rettype = "";
      if(curr.getType() == "class") {
          for(Struct i: curr.getMethods()) {
              if(tmp2 == i.getName()) {
                  rettype = i.get_returnType();
              }
          }
      }
      else if(curr.getType() == "object") {
        Struct cname = symbol_table.get("Global").get(curr.get_className());
        if(cname == null) {
            System.out.println("Typecheck error in message send2");
            typechecks = false;
            return;
        }
        for(Struct i: cname.getMethods()) {
            if(tmp2 == i.getName()) {
                rettype = i.get_returnType();
            }
        } 
      }
      else if(curr.getType() == "function") {
        Struct cname = symbol_table.get("Global").get(curr.get_returnType());
        if(cname == null) {
            System.out.println("Typecheck error in message send3");
            typechecks = false;
            return;
        }

        for(Struct i: cname.getMethods()) {
            if(tmp2 == i.getName()) {
                rettype = i.get_returnType();
            }
        }
      }
      else {
          System.out.println("wtf error in messagesend4 " + tmp1 + " " + tmp2 + " " + current_class + " " + current_function);
          typechecks = false;
          return;
      }
      n.f3.accept(this);
      n.f4.accept(this);
      n.f5.accept(this);
      expr_type = rettype;
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public void visit(ExpressionList n) {
      n.f0.accept(this);
    //   System.out.println("Current function in exprlist is " + current_function + " and expr is " + curr_id);
      n.f1.accept(this);
   }

   /**
    * f0 -> ","
    * f1 -> Expression()
    */
   public void visit(ExpressionRest n) {
      n.f0.accept(this);
      n.f1.accept(this);
   }

   /**
    * f0 -> IntegerLiteral()
    *       | TrueLiteral()
    *       | FalseLiteral()
    *       | Identifier()
    *       | ThisExpression()
    *       | ArrayAllocationExpression()
    *       | AllocationExpression()
    *       | NotExpression()
    *       | BracketExpression()
    */
   public void visit(PrimaryExpression n) {
      n.f0.accept(this);
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public void visit(IntegerLiteral n) {
      n.f0.accept(this);
      expr_type = "int";
   }

   /**
    * f0 -> "true"
    */
   public void visit(TrueLiteral n) {
      n.f0.accept(this);
      expr_type = "boolean";
   }

   /**
    * f0 -> "false"
    */
   public void visit(FalseLiteral n) {
      n.f0.accept(this);
      expr_type = "boolean";
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public void visit(Identifier n) {
      n.f0.accept(this);
      Struct id = null;
      for(String i: symbol_table.keySet()) {
          for(String j: symbol_table.get(i).keySet()) {
              if(n.f0.toString() == symbol_table.get(i).get(j).getName()) {
                  id = symbol_table.get(i).get(j);
              }
          }
      }
      if(id == null)
      {
          typechecks = false;
          System.out.println("Type error in identifier");
          return;
      }
      if(id.getType() == "function") {
        //   System.out.println(id.getName() + " is a function " + id.get_returnType());
          expr_type = id.get_returnType();
      }
      else {
          expr_type = id.getType();
      }
      curr_id = id.getName();
   }

   /**
    * f0 -> "this"
    */
   public void visit(ThisExpression n) {
      n.f0.accept(this);
      expr_type = "object";
      curr_id = current_class;
   }

   /**
    * f0 -> "new"
    * f1 -> "int"
    * f2 -> "["
    * f3 -> Expression()
    * f4 -> "]"
    */
   public void visit(ArrayAllocationExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      if(expr_type != "int") {
        typechecks = false;
        System.out.println("Typecheck error in arrayallocationexpr");
        return;
     }
      n.f4.accept(this);
      expr_type = "int[]";
   }

   /**
    * f0 -> "new"
    * f1 -> Identifier()
    * f2 -> "("
    * f3 -> ")"
    */
   public void visit(AllocationExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      if(expr_type != "class") {
          typechecks = false;
          System.out.println("allocation expression error");
          return;
      }
      n.f2.accept(this);
      n.f3.accept(this);
      expr_type = "object";
   }

   /**
    * f0 -> "!"
    * f1 -> Expression()
    */
   public void visit(NotExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      if(expr_type == "boolean") {
        expr_type = "boolean";
        }
      else {
        typechecks = false;
        System.out.println("Type error in not expression");
        return;
      }
   }

   /**
    * f0 -> "("
    * f1 -> Expression()
    * f2 -> ")"
    */
   public void visit(BracketExpression n) {
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
   }

}
