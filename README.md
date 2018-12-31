# test_179e

Java to MIPS compiler

## Phases

* Phase1: Type Checking
* Phase2: Intermediate Code Generation
* Phase3: Register Allocation
* Phase4: Activation Records and Instruction Selection

## Dependencies

* JavaCC (Java Compiler Compiler) parser generator.
  Given a context free grammer, JavaCC generates a parser.

* JTB (Java Tree Builder) jar file jtb.jar
  JTB is a syntax tree builder to be used with JavaCC.  
  Given a JavaCC grammar file, it automatically generates syntax tree and visitor classes and an annotated JavaCC grammer to build syntax trees.

* MiniJava Grammer for JavaCC minijava.jj
  The context free grammer of MiniJava that is input to JTB and JavaCC.

* Vapor Interpreter vapor.jar

* Vapor Parser vapor-parser.jar
  The source code vapor-parser-source.tar.gz
  the java doc vapor-parser-javadoc.tar.gz

* MIPS Interpreter mars.jar
