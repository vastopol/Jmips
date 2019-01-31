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
    init_p1   # if no build exists make one
    check_p1  # custom type check
    clean_p1  # rm .class files
    test_p1   # make the .tgz file and use run script
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

# testing type checking on a custom file
function check_p1()
{
    P1_FILE="../tests/tester.java"
    cd p1
    echo "Compiling Phase1"; echo
    javac Typecheck.java
    echo "Checking "$P1_FILE; echo
    echo "See p1_logfile for trace"; echo
    java Typecheck $P1_FILE > ../p1_logfile.txt
    echo
    cd ..
}

# remove class files form the build
function clean_p1()
{
    cd p1;         rm *.class
    cd visitor;    rm *.class; cd ..
    cd syntaxtree; rm *.class; cd ..
    cd struct;     rm *.class; cd ..
    cd toolbox;    rm *.class; cd ..
    cd ..
}

# run the grading script with all the included test cases
# it expects a tar file named "hw1.tgz" to be used with the "run" script
function test_p1()
{
    if [ -e hw1  ] ; then
        echo "Deleteing old hw1 folder"
        rm -rf hw1
    fi

    if [ -e hw1.tgz  ] ; then
        echo "Deleteing old hw1 tarball"
        rm -rf hw1.tgz
    fi

    mkdir hw1

    cp -r p1/struct/  hw1
    cp -r p1/toolbox/ hw1
    cp p1/Typecheck.java hw1

    cp p1/visitor/DFStackVisitor.java hw1
    cp p1/visitor/DFStackVisitor2.java hw1
    cp p1/visitor/DFTypeCheckVisitor.java hw1

    tar zcvf hw1.tgz hw1

    cd tests/Phase1Tester

    # source run SelfTestCases ../../hw1.tgz

    echo "Delete the hw1/ && hw1.tgz && tests/Phase1Tester/Output/ if not needed"


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
