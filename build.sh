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

# Driver for user subroutines, called at bottom
function main()
{
    set -e

    build_init

    test_p1
}

#----------------------------------------

# Create MiniJava parser and generate syntax tree classes and visitor classes
function build_init()
{
    if ( test -d p1/ ) ; then
        echo "Phase1 initial build exists"
        return
    fi
    mkdir p1
    cd p1
    java -jar $JTB_JAR $MINI_J
    javacc jtb.out.jj
    cd ..
}

function test_p1()
{
    cd p1

    # java Typecheck.java ../tests/Factorial.java

    cd ..
}

#----------------------------------------

# Call main driver function
main
