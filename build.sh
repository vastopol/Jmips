#!/bin/bash

# Compiler build and test script

#========================================
# MAIN
#========================================

# Global Variables
JTB_JAR="../stuff/jtb.jar"          # Tree Builder
MINI_J="../stuff/minijava.jj"       # Tree Builder Input
VAPOR_I="../stuff/vapor.jar"        # Vapor Interpreter
VAPOR_P="../stuff/vapor-parser.jar" # Vapor Parser
MARS="../stuff/mars.jar"            # MIPS Interpreter

# Driver for user subroutines, called at bottom
function main()
{
    init_p1
    # check_p1
    stack_p1
    clean_p1
}

#========================================
# PHASE 1 - Type Checking
#========================================

# Create skeleton MiniJava parser and generate syntax tree classes and visitor classes, etc...
function init_p1()
{
    if ( test -d p1 ) ; then
        echo "Phase1 build exists"
        return
    fi
    echo "Creating initial Phase1 build"
    mkdir p1
    cd p1
    java -jar $JTB_JAR $MINI_J
    javacc jtb.out.jj
    touch Typecheck.java
    cd ..
}

# testing type checking
function check_p1()
{
    P1_FILE="../stuff/Factorial.java"
    cd p1
    echo "Compiling Phase1"
    javac Typecheck.java
    echo "Checking "$P1_FILE
    java Typecheck $P1_FILE
    cd ..
}

# testing type checking
function stack_p1()
{
    P1_FILE="../stuff/Factorial.java"
    cd p1
    echo "Compiling Phase1"
    javac StackTest.java
    echo "Checking "$P1_FILE
    java StackTest $P1_FILE
    cd ..
}

# remove class files
function clean_p1()
{
    cd p1;         rm *.class
    cd visitor;    rm *.class; cd ..
    cd syntaxtree; rm *.class; cd ..
    cd struct;     rm *.class; cd ..
    cd ..
}

#========================================

# Call main driver function
main
