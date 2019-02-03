#!/bin/bash

# Mini Java Compiler build and test script
# Modular procedural shell script with each phase separated
# each subroutine executes from the base directory

# PHASE 1 - Type Checking
# PHASE 2 - Intermediate Code Generation
# PHASE 3 - Register Allocation
# PHASE 4 - Activation Records and Instruction Selection

#============================================================
# MAIN DRIVER ROUTINE
#============================================================

# STEPS OF MAIN
# 1. if no build exists make one
# 2. run preliminary type check on custom file
# 3. loop over every actual test file
# 4. rm .class files && cleanup for test script
# 5. make the .tgz file and use official run script on it
# 6. rm everything that gets auto generated by the script

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
    # do_p 1
    do_p 2
    p_wipe
}


function do_p()
{
    case $1 in
        1)
            p_init1  p1 Typecheck.java
            p_check1 p1 Typecheck $TJAVA $TEST1
            p_clean1 p1
            p_test1
            ;;
        2)
            p_init1  p2 J2V.java
            p_check1 p2 J2V $TJAVA $TEST2
            p_clean1 p2
            # p_test2
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

# remove all the auto generated stuff from above
# should be a universal hecleanup helper
function p_wipe()
{
    echo "5. wiping out the extras"; echo
    rm -rf hw*
    rm *logfile.txt
    rm -rf tests/Phase1Tester/Output
    rm -rf tests/Phase2Tester/Output
    rm -rf tests/Phase3Tester/Output
    rm -rf tests/Phase4Tester/Output
}

#============================================================
# PHASE 1 && 2 SHARED BUILD TESTER
#============================================================

# INITIALIZE PROJECT
# Create skeleton MiniJava parser and generate syntax tree classes and visitor classes, etc...
# arg1 = directory, arg2 = java main file
function p_init1()
{
    echo
    if ! [ -d $1 ] ; then
        echo "0. Creating initial build"; echo
        mkdir $1
        cd $1
        java -jar $JTB_JAR $MINI_J
        javacc jtb.out.jj
        rm jtb.out.jj
        touch $2
        exit 0 # done on init
    fi

    echo "0. Build exists"; echo
    cd $1
    echo "1. Compiling"; echo
    javac $2
    cd ..
}

# CUSTOM && MANUAL TEST
# testing type checking on a custom file
# arg1 = folder, arg 2 = java main class, arg3 = customtest, arg4 = multifile test
function p_check1()
{
    LOG1="../custom_logfile.txt"
    LOG2="../manual_logfile.txt"

    cd $1

    echo "2. Checking "$3; echo
    echo "See custom_logfile.txt for trace"; echo
    java $2 < $3 > $LOG1

    echo "3. Manually testing all the Test cases"; echo
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

# REMOVE CLASS FILES
# remove any .class files from the build
# also used because cant submit .class files in the tarball
function p_clean1()
{
    cd $1;         rm *.class
    cd visitor;    rm *.class; cd ..
    cd visitor2;   rm *.class; cd ..
    cd syntaxtree; rm *.class; cd ..
    cd struct;     rm *.class; cd ..
    cd toolbox;    rm *.class; cd ..
    cd ..
}


# TEST PHASE 1
# run the grading script with all the included test cases
# it expects a tar file named "hw1.tgz" to be used with the "run" script
# also it will reject tar files that are too big so must be less than 65kB in size
function p_test1()
{
    if [ -e hw1  ] ; then
        echo "Deleteing old hw1 folder"
        rm -rf hw1
    fi

    if [ -e hw1.tgz  ] ; then
        echo "Deleteing old hw1 tarball"
        rm -rf hw1.tgz
    fi

    echo "Making new tar file for submission"; echo

    mkdir hw1
    mkdir hw1/visitor2 # only want some of the files so no recurse copy (see below)
    mkdir hw1/struct
    mkdir hw1/toolbox

    cp p1/struct/*  hw1/struct
    cp p1/toolbox/* hw1/toolbox
    cp p1/Typecheck.java hw1                            # <--- main file
    cp p1/visitor2/DFStackVisitor.java hw1/visitor2
    cp p1/visitor2/DFStackVisitor2.java hw1/visitor2
    cp p1/visitor2/DFTypeCheckVisitor.java hw1/visitor2 # <--- visitor file

    tar zcvf hw1.tgz hw1 > /dev/null

    echo "4. Running Phase1Tester Script"; echo

    cd tests/Phase1Tester                               # <--- number

    source run SelfTestCases ../../hw1.tgz              # <--- number

    echo;

    cd ../..
}

#============================================================
# DRIVER - Run the main driver routine for the harnesses
#============================================================

main
