@echo off
echo Compiling D{0-1} Knapsack Problem Solver...
echo.

if not exist bin mkdir bin

javac -d bin -encoding UTF-8 -sourcepath src/main/java ^
    src/main/java/com/knapsack/model/*.java ^
    src/main/java/com/knapsack/algorithm/*.java ^
    src/main/java/com/knapsack/io/*.java ^
    src/main/java/com/knapsack/util/*.java ^
    src/main/java/com/knapsack/ui/*.java ^
    src/main/java/com/knapsack/*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Compilation successful!
    echo.
    echo To run the application, execute: run.bat
    echo Or run: java -cp bin com.knapsack.ui.MainFrame
) else (
    echo.
    echo Compilation failed!
)
pause
