#!/bin/bash

# Compiler build and test script

#----------------------------------------

# Global Variables
JTB_JAR="../stuff/jtb.jar"          # Tree Builder
MINI_J="../stuff/minijava.jj"       # Tree Builder Input
VAPOR_I="../stuff/vapor.jar"        # Vapor Interpreter
VAPOR_P="../stuff/vapor-parser.jar" # Vapor Parser
MARS="../stuff/mars.jar"            # MIPS Interpreter

#----------------------------------------

PROJ="p1" # phase1

# Driver for user subroutines, called at bottom
function main()
{
    # set -e
    # init_cc
    test_cc
    clean_cc
}

#----------------------------------------

# Create skeleton MiniJava parser and generate syntax tree classes and visitor classes, etc...
function init_cc()
{
    if ( test -d $PROJ ) ; then
        echo "Phase1 build exists"
        return
    fi
    mkdir $PROJ
    cd $PROJ
    java -jar $JTB_JAR $MINI_J
    javacc jtb.out.jj
    touch Typecheck.java
    cd ..
}

# testing type checking
function test_cc()
{
    cd $PROJ
    echo "Compiling"
    javac Typecheck.java
    echo "Testing"
    java Typecheck ../stuff/Factorial.java
    cd ..
}

# remove class files
function clean_cc()
{
    cd $PROJ;      rm *.class
    cd visitor;    rm *.class; cd ..
    cd syntaxtree; rm *.class; cd ..
    cd ..
}

#----------------------------------------

# Call main driver function
main
