#!/bin/bash

# Mini Java Compiler build and test script
# Modular procedural shell script with each phase separated
# each subroutine executes from the base directory

# PHASE 1 - Type Checking (DONE)
# PHASE 2 - Intermediate Code Generation
# PHASE 3 - Register Allocation
# PHASE 4 - Activation Records and Instruction Selection

#============================================================
# MAIN DRIVER ROUTINE
#============================================================

# GLOBALS - stuff
JTB_JAR="../stuff/jtb.jar"          # Tree Builder
MINI_J="../stuff/minijava.jj"       # Tree Builder Input
VAPOR_I="../stuff/vapor.jar"        # Vapor Interpreter
VAPOR_P="../stuff/vapor-parser.jar" # Vapor Parser
MARS="../stuff/mars.jar"            # MIPS Interpreter

# GLOBALS - tests
TJAVA="../tests/tester.java"
TEST1="../tests/Phase1Tester/SelfTestCases/*"
TEST2="../tests/Phase2Tester/SelfTestCases/*.java"

# Driver for all the test harnesses, main is called at bottom
# each phase has a specific test harness procedure, test harnesses are commented out as needed
function main()
{
    # do_p  1
    do_p  2
    p_wipe
}

# STEPS OF do_p()
# 1. if no build exists make one
# 2. run preliminary tests on custom file
# 3. loop over every actual test file
# 4. rm .class files && cleanup for test script
# 5. make the .tgz file and use official run script on it
# 6. rm everything that gets auto generated by the script
function do_p()
{
    case $1 in
        1)
            p_init   p1  Typecheck.java
            p_check  p1  Typecheck  $TJAVA  $TEST1
            p_log
            p_clean  p1
            p_test   p1  Typecheck.java  DFTypeCheckVisitor.java  hw1  tests/Phase1Tester
            ;;
        2)
            p_init   p2  J2V.java
            p_vapor  p2  J2V  $TJAVA  $TEST2
            # p_log
            p_clean  p2
            p_test   p2  J2V.java  DFVaporVisitor.java  hw2  tests/Phase2Tester
            ;;
        3)
            echo "phase3"
            ;;
        4)
            echo "phase4"
            ;;
        *)  echo "invalid option"
            exit 1
            ;;
    esac

    return
}

#============================================================
# PHASE 1 && 2 SHARED BUILD TESTER
#============================================================

# INITIALIZE PROJECT
# Create skeleton MiniJava parser and generate syntax tree classes and visitor classes, etc...
# arg1 = directory, arg2 = java main file
function p_init()
{
    echo
    if ! [ -d $1 ] ; then
        echo "Creating initial build"; echo
        mkdir $1
        cd $1
        java -jar $JTB_JAR $MINI_J
        javacc jtb.out.jj
        rm jtb.out.jj
        touch $2
        exit 0 # done on init
    fi

    echo "Build exists"; echo
    cd $1
    echo "Compiling"; echo
    javac $2
    cd ..
}

# print the log files
function p_log()
{
    echo "Printing Logs"; echo
    echo "custom_logfile.txt"; echo
    cat custom_logfile.txt; echo
    echo "manual_logfile.txt"
    cat manual_logfile.txt
    echo
}

# REMOVE CLASS FILES
# remove any .class files from the build
# also used because cant submit .class files in the tarball
# arg1 = directory to clean
function p_clean()
{
    cd $1;         rm *.class
    cd visitor;    rm *.class; cd ..
    cd visitor2;   rm *.class; cd ..
    cd syntaxtree; rm *.class; cd ..
    cd struct;     rm *.class; cd ..
    cd toolbox;    rm *.class; cd ..
    cd ..
}

# remove all the auto generated stuff from above
# should be a universal cleanup helper
function p_wipe()
{
    echo "wiping out the extras"; echo
    rm -rf hw*
    rm *logfile.*
    rm -rf tests/Phase1Tester/Output
    rm -rf tests/Phase2Tester/Output
    rm -rf tests/Phase3Tester/Output
    rm -rf tests/Phase4Tester/Output
}

# PHASE 1 typecheck
# CUSTOM && MANUAL TEST - TYPECHECKER
# testing type checking on a custom file
# arg1 = folder, arg 2 = java main class, arg3 = customtest, arg4 = multifile test
function p_check()
{
    LOG1="../custom_logfile.txt"
    LOG2="../manual_logfile.txt"

    cd $1

    echo "Checking "$3; echo
    echo "See custom_logfile.txt for trace"; echo
    java $2 < $3 &> $LOG1

    echo "Manually testing all the Test cases"; echo
    echo "See manual_logfile.txt for trace"; echo

    echo "" > $LOG2
    for FILE in $4 ;
    do
        echo "CHECKING: "$FILE >> $LOG2
        if ! java $2 < $FILE >> $LOG2; then
            continue
        fi
        echo "" >> $LOG2
    done

    cd ..
}

# PHASE 2 vapor
# CUSTOM && MANUAL TEST - VAPOR GENERATOR
# testing code generation on a custom file
# arg1 = folder, arg 2 = java main class, arg3 = customtest, arg4 = multifile test
function p_vapor()
{
    LOG1="../custom_logfile.txt"
    LOG2="../manual_logfile.txt"

    cd $1

    echo "Code Generate "$3; echo
    echo "See custom_logfile.txt for trace"; echo
    java $2 < $3 &> $LOG1
    echo "code print out"; echo
    cat $LOG1
    echo "Code test "$3; echo
    java -jar $VAPOR_I run $LOG1; echo

    # echo "Manually testing all the Test cases"; echo
    # echo "See manual_logfile.txt for trace"; echo
    #
    # echo "" > $LOG2
    # for FILE in $4 ;
    # do
    #     echo "Code Generate: "$FILE >> $LOG2
    #     if ! java $2 < $FILE >> $LOG2; then
    #         continue
    #     fi
    #     echo "" >> $LOG2
    # done

    cd ..
}

# TEST With Grading Script
# run the grading script with all the included test cases
# it expects a tar file named "hw[1-4].tgz" to be used with the "run" script
# args: $1 = code directory, $2 = main file, $3 = visitor file, $4 = hw name, $5 = testcases
function p_test()
{
    if [ -e $4 ] ; then
        echo "Deleteing old folder"
        rm -rf $4
    fi

    if [ -e $4.tgz ] ; then
        echo "Deleteing old tarball"
        rm -rf $4.tgz
    fi

    echo "Making new tar file for submission"; echo

    mkdir $4
    mkdir $4/visitor2
    mkdir $4/struct
    mkdir $4/toolbox

    cp $1/struct/*   $4/struct
    cp $1/toolbox/*  $4/toolbox
    cp $1/$2  $4                            # <--- main file
    cp $1/visitor2/DFStackVisitor.java   $4/visitor2
    cp $1/visitor2/DFStackVisitor2.java  $4/visitor2
    cp $1/visitor2/$3  $4/visitor2          # <--- visitor file

    tar zcvf $4.tgz $4 > /dev/null

    echo "Running Tester Script"; echo

    cd $5                                   # <--- test directory

    source run SelfTestCases ../../$4.tgz

    echo;

    cd ../..
}

#============================================================
# DRIVER - Run the main driver routine for the harnesses
#============================================================

main
