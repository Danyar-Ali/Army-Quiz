Army IQ Test Quiz

Overview

The Army IQ Test Quiz is a timed, Java-based quiz app that challenges users with random arithmetic problems. You have 60 seconds to determine which of two equations has the greater result.

Features:

Randomized Equations: Each question includes random addition, subtraction, multiplication, or division problems.

60-Second Timer: The quiz ends when the timer runs out, and your score is displayed.

Score Summary: See how many correct answers you provided after the quiz ends.


Requirements:

Java 8 or later
JavaFX SDK

How to Run:

Using an IDE
Download the project and open it in an IDE like IntelliJ or Eclipse.
Configure JavaFX in your IDE.
Run the ArmyQuiz.java class.

Set PATH_TO_FX to the download location of this file.
Note this doesn't have to be in your jdk-22 file.
https://gluonhq.com/products/javafx/ 

Powershell (x86):
PS D:\PenTesting\Code> cd D:\PenTesting\Code\

PS D:\PenTesting\Code> set PATH_TO_FX 'C:\Program Files\Java\jdk-22\javafx-sdk-22.0.2\lib'

PS D:\PenTesting\Code> java --module-path $PATH_TO_FX --add-modules javafx.controls ArmyQuiz
