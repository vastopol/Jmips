#!/bin/bash

# Mini Java Compiler build and test script
# Modular procedural shell script with each phase separated

#========================================
# MAIN
#========================================

# Global Variables
JTB_JAR="../stuff/jtb.jar"          # Tree Builder
MINI_J="../stuff/minijava.jj"       # Tree Builder Input
VAPOR_I="../stuff/vapor.jar"        # Vapor Interpreter
VAPOR_P="../stuff/vapor-parser.jar" # Vapor Parser
MARS="../stuff/mars.jar"            # MIPS Interpreter

# Driver for all the test harnesses, main is called at bottom
# each phase has a specific test harness procedure, test harnesses are commented out as needed
function main()
{
    do_p1
    # do_p2
    # do_p3
    # do_p4
}

#========================================
# PHASE 1 - Type Checking
#========================================

# phase1 test harness
function do_p1()
{
    init_p1
    check_p1
    clean_p1
}

# Create skeleton MiniJava parser and generate syntax tree classes and visitor classes, etc...
function init_p1()
{
    echo
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
    P1_FILE="../tests/tester.java"
    cd p1
    echo "Compiling Phase1"
    javac Typecheck.java
    echo "Checking "$P1_FILE; echo
    java Typecheck $P1_FILE;  echo
    cd ..
}

# remove class files
function clean_p1()
{
    cd p1;         rm *.class
    cd visitor;    rm *.class; cd ..
    cd syntaxtree; rm *.class; cd ..
    cd struct;     rm *.class; cd ..
    cd toolbox;    rm *.class; cd ..
    cd ..
}

#========================================
# PHASE 2 -
#========================================

# phase1 test harness
function do_p2()
{
    echo "Phase 2"
}

#========================================
# PHASE 3 - xxx
#========================================

# phase1 test harness
function do_p3()
{
    echo "Phase 3"
}

#========================================
# PHASE 4 - xxx
#========================================

# phase1 test harness
function do_p4()
{
    echo "Phase 4"
}

#========================================
# RUN DRIVER
#========================================

main
