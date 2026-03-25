@echo off
echo Compiling D{0-1} Knapsack Problem Solver...
echo.

if not exist bin mkdir bin

javac -d bin -encoding UTF-8 -sourcepath src/main/java src/main/java/com/knapsack/model/*.java
javac -d bin -encoding UTF-8 -sourcepath src/main/java src/main/java/com/knapsack/algorithm/*.java
javac -d bin -encoding UTF-8 -sourcepath src/main/java src/main/java/com/knapsack/io/*.java
javac -d bin -encoding UTF-8 -sourcepath src/main/java src/main/java/com/knapsack/util/*.java
javac -d bin -encoding UTF-8 -sourcepath src/main/java src/main/java/com/knapsack/ui/*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Compilation successful!
    echo.
    echo Running the application...
    java -cp bin com.knapsack.ui.MainFrame
) else (
    echo.
    echo Compilation failed!
    pause
)
