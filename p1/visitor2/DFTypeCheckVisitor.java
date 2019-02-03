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
   String msgsendcaller;
   Struct msgsendstruct;
   Vector<String> expr_type_vec;
   Vector<String> expr_name_vec;
   public boolean typechecks = true;

   public DFTypeCheckVisitor(Map<String, Map<String,Struct>> m) {
      symbol_table = m;
      expr_type_vec = new Vector<String>();
      expr_name_vec = new Vector<String>();
      msgsendstruct = null;
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
      current_function = "main";
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
          String func_key = current_function + " " + current_class;
          Struct method_struct = method_struct2.get(func_key);
          if(method_struct == null) {
              method_struct = method_struct2.get(current_function);
          }
        //   System.out.println(method_struct.getName() + " " + func_key + " sup its methoddeclaration");
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
      func_key = current_function + " " + current_class;
      Map<String, Struct> method_table = symbol_table.get(func_key);
      Vector<String> inames = new Vector<String>();

      for(String i: method_table.keySet()) {
         inames.add(method_table.get(i).getName());
      }
      if(inames.size() > 1) {
        if(!helper.distinct(inames)) {
            typechecks = false;
            System.out.println("Typecheck error in methoddeclaration 6");
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
            func_key = current_function + " " + current_class;
            Struct id = symbol_table.get(func_key).get(tmp22);
            if(id == null) {
                id = symbol_table.get(current_class).get(tmp22);
                if(id == null) {
                  System.out.println("Typecheck error in methoddeclaration7");
                  typechecks = false;
                  return;
                }
            }

            if(id.get_className() != tmp11) {
              System.out.println("Typecheck error in methoddeclaration8 " + tmp1 + " " + id.get_className());
              typechecks = false;
              return;
            }
        }
        else {
            System.out.println("Typecheck error in methoddeclaration9 ");
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
      String func_key = current_function + " " + current_class;
      if(tmp1 != tmp2) {
          if(tmp1 == "object") {
              Struct id = symbol_table.get(func_key).get(tmp11);
              if(id == null) {
                  id = symbol_table.get(current_class).get(tmp11);
                  if(id == null) {
                    Struct prnt = symbol_table.get("Global").get(current_class);
                    if(prnt.getParent() != "") {
                        id = symbol_table.get(prnt.getParent()).get(tmp11);
                        if(id == null) {
                            System.out.println("Typecheck error in assignment1");
                        }
                    }
                    else {
                        System.out.println("Typecheck error in assignment2");
                        typechecks = false;
                        return;
                    }
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
      msgsendcaller = tmp1;
      System.out.println(curr_id);
      String func_key = current_function + " " + current_class;
      Struct curr = symbol_table.get("Global").get(tmp1); // if primary is a class
      if(curr == null) {
        // System.out.println(tmp1 + " Current class in msgsnd is " + current_class + " current func in msgsend " + current_function + " or maybe " + func_key);
        curr = symbol_table.get(func_key).get(tmp1); //if primary is an object in the function
        if(curr == null) {;
            curr = symbol_table.get(current_class).get(tmp1);// if primary is a class with a parent
            if(curr == null) {
                Struct prnt = symbol_table.get("Global").get(current_class);
                if(prnt.getParent() != "") { 
                    curr = symbol_table.get(prnt.getParent()).get(tmp1); //if the parent exists then get function from parent
                    if(curr == null) {
                        System.out.println("Typecheck error in message send 1");
                    }
                }
                else {
                    System.out.println("Typecheck error in message send 1.5");
                    typechecks = false;
                    return;
                }
            }
        }
      }
      msgsendstruct = curr;

    //   System.out.println(expr_type);
      n.f1.accept(this);
      n.f2.accept(this);
      String tmp2 = curr_id;
      String rettype = "";
      Struct methodn = null;
      if(curr.getType() == "class") { //if curr is a class retrieves return type and method struct
          for(Struct i: curr.getMethods()) {
              if(tmp2 == i.getName()) {
                  rettype = i.get_returnType();
                  methodn = i;
              }
          }
      }
      else if(curr.getType() == "object") {
        Struct cname = symbol_table.get("Global").get(curr.get_className()); //if curr is an object, retrieves class of object and then return type and method struct
        if(cname == null) {
            System.out.println("Typecheck error in message send2");
            typechecks = false;
            return;
        }
        for(Struct i: cname.getMethods()) { //
            if(tmp2 == i.getName()) {
                rettype = i.get_returnType();
                methodn = i;
            }
        } 
      }
      else if(curr.getType() == "function") { //if curr is a function ??
        Struct cname = symbol_table.get("Global").get(curr.get_returnType());
        if(cname == null) {
            System.out.println("Typecheck error in message send3");
            typechecks = false;
            return;
        }

        for(Struct i: cname.getMethods()) {
            if(tmp2 == i.getName()) {
                rettype = i.get_returnType();
                methodn = i;
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
      if(methodn != null) { //if the method exists then
          if(methodn.getParams().size() > 0) { //if it has parameters
            for(int i = 0; i < expr_type_vec.size(); i++) {
                System.out.println(i + " " + expr_type_vec.elementAt(i));
            }
            if(expr_type_vec.size() == methodn.getParams().size()) { //check if sizes match
              for(Struct i: methodn.getParams()) {
                  System.out.println(i.getType());
              }
              System.out.println("");
              for(String i: expr_type_vec) {
                  System.out.println(i);
              }
              System.out.println(expr_name_vec.size() + " " + methodn.getParams().size() + " " + expr_type_vec.size());
               //  for(String i: expr_type_vec) {
               //      for(Struct j: methodn.getParams()) {
               //          if(i != j.getType()) { //check if types match
               //              System.out.println("Typecheck error in messagesend5 " + i + " " + j.getType());
               //              typechecks = false;
               //              return;
               //          }
               //      }
               //  }
               Vector<Struct> methodps = methodn.getParams();
               for(int i = 0; i < expr_type_vec.size(); i++) {
                  if(expr_type_vec.elementAt(i) != methodps.elementAt(i).getType()) {
                     if(methodps.elementAt(i).getType() == "object") {
                        if(expr_type_vec.elementAt(i) != methodps.elementAt(i).get_className()) {
                           System.out.println("Typecheck error in messagesend5 " + expr_type_vec.elementAt(i) + " " + methodps.elementAt(i));
                           typechecks = false;
                           return;
                        }
                     }
                     else {
                        System.out.println("Typecheck error in messagesend5.5 " + expr_type_vec.elementAt(i) + " " + methodps.elementAt(i));
                        typechecks = false;
                        return;
                     }

                  }
               }
            }
            else {
                System.out.println("Typecheck error in messagesend6 " + expr_type_vec.size() + " " + methodn.getParams().size() + " " + methodn.getName() + " " + methodn.getType());
                for(Struct i: methodn.getParams()) {
                    System.out.println(i.getType());
                }
                System.out.println("");
                for(String i: expr_name_vec) {
                    System.out.println(i);
                }
                typechecks = false;
                return;
            }   
          }
    }
    else {
        System.out.println("Typecheck error in messagesend7");
        typechecks = false;
        return;
    }
      n.f5.accept(this);
      msgsendcaller = "";
      msgsendstruct = null;
      expr_type = rettype;
   }

   /**
    * f0 -> Expression()
    * f1 -> ( ExpressionRest() )*
    */
   public void visit(ExpressionList n) {
       expr_type_vec.clear();
      n.f0.accept(this);
      expr_type_vec.add(expr_type);
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
      expr_type_vec.add(expr_type);
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
    System.out.println("in identifier \""+ current_class + "\" \"" + current_function + "\" \"" + n.f0.toString() +"\"");
    for(String i: symbol_table.keySet()) {
      for(String j: symbol_table.get(i).keySet()) {
          if(n.f0.toString() == symbol_table.get(i).get(j).getName()) {
              id = symbol_table.get(i).get(j);
          }
      }
  }
      if(current_function != null && current_function != "") {
         String func_key = current_function + " " + current_class;
         System.out.println("funckey is " + func_key);
         id = symbol_table.get(func_key).get(n.f0.toString()); //get current id from current function map
         if(id == null && current_class != null && current_class != "") {
            id = symbol_table.get(current_class).get(n.f0.toString()); //get current id from class map
            if(id == null) {
               Struct prnt = symbol_table.get("Global").get(current_class);
               if(prnt != null && prnt.getParent() != "") {
                  id = symbol_table.get(prnt.getParent()).get(n.f0.toString());
                  if(id == null) {
                     id = symbol_table.get("Global").get(n.f0.toString());
                  }
               }
               else {
                  for(String i: symbol_table.keySet()) {
                     for(String j: symbol_table.get(i).keySet()) {
                         if(n.f0.toString() == symbol_table.get(i).get(j).getName()) {
                             id = symbol_table.get(i).get(j);
                         }
                     }
                 }
               }
            }
         }
      }
      if(id == null && msgsendcaller != "" && msgsendcaller != null) {
         // System.out.println("Type error in identifier in stupid " + n.f0.toString() + " " + current_class + " " + msgsendcaller + " " + current_function);
         if(msgsendstruct != null && msgsendstruct.getType() == "object") {
            // System.out.println("stupid " + msgsendstruct.getName() + " " + msgsendstruct.get_className());
            Struct prnt = symbol_table.get("Global").get(msgsendstruct.get_className());
            if(prnt != null) {
               id = symbol_table.get(prnt.getName()).get(n.f0.toString());
               // System.out.println("stupid id is " + id.getName());
            }
         }
      }

      if(id == null)
      {
          typechecks = false;
          System.out.println("Type error in identifier2 " + n.f0.toString() + " " + current_class + " " + msgsendcaller + " " + current_function);
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
