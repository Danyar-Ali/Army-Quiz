
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArmyQuiz extends Application {

    private Label questionLabel1;
    private Label questionLabel2;
    private Label timerLabel;
    private int correctAnswers = 0;
    private int totalQuestions = 0;
    private String currentQuestion1;
    private String currentQuestion2;
    private int result1;
    private int result2;
    private Random random = new Random();
    private Timeline timeline;
    private int timeRemaining = 60; // Timer set to 60 seconds
    private final int ANSWER_DIFFERENCE_LIMIT = 5;
    private Stage mainStage;
    private int quizStep = 0;
    private List<Question> questionsList = new ArrayList<>();
    private List<Integer> userResponses = new ArrayList<>();
    private List<Boolean> correctnessList = new ArrayList<>();
    private int highestRecord = 0;

    @Override
    public void start(Stage stage) {
        this.mainStage = stage;
        showStartPage();
    }

    private void showStartPage() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        VBox topBox = new VBox(20);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPrefSize(600, 400);

        Button startButton = new Button("Start Quiz");
        startButton.setFont(new Font("Verdana", 30));
        startButton.setTextFill(Color.WHITE);
        startButton.setStyle("-fx-background-color: blue;");
        startButton.setOnAction(e -> startQuiz());
        topBox.getChildren().add(startButton);

        root.setCenter(topBox);

        Scene scene = new Scene(root, 600, 400);
        mainStage.setScene(scene);
        mainStage.setTitle("Army Quiz");
        mainStage.show();
    }

    private void startQuiz() {
        correctAnswers = 0;
        totalQuestions = 0;
        quizStep = 1;

        questionsList.clear();
        userResponses.clear();
        correctnessList.clear();

        BorderPane quizPane = new BorderPane();
        quizPane.setPadding(new Insets(20));

        updatePageContent(quizPane);

        HBox timerBox = new HBox();
        timerBox.setAlignment(Pos.CENTER);
        timerLabel = new Label("Time left: 60 seconds");
        timerLabel.setFont(new Font("Verdana", 16));
        timerLabel.setTextFill(Color.RED);
        timerBox.getChildren().add(timerLabel);
        quizPane.setBottom(timerBox);

        Scene quizScene = new Scene(quizPane, 600, 400);
        mainStage.setScene(quizScene);

        startTimer();
    }

    private void startTimer() {
        timeRemaining = 60;
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateTimer() {
        timeRemaining--;
        timerLabel.setText("Time left: " + timeRemaining + " seconds");

        if (timeRemaining <= 0) {
            timeline.stop();
            endQuiz();
        }
    }

    private void updatePageContent(BorderPane quizPane) {
        quizPane.setCenter(createPageContent());
    }

    private VBox createPageContent() {
        VBox pageContent = new VBox(20);
        pageContent.setAlignment(Pos.CENTER);

        if (quizStep == 1) {
            generateQuestions();
            questionLabel1 = new Label("Equation 1: " + currentQuestion1);
            questionLabel1.setFont(new Font("Verdana", 24));
            questionLabel1.setTextFill(Color.BLUE);

            Button nextButton = new Button("Next Equation");
            nextButton.setOnAction(e -> {
                quizStep = 2;
                updatePageContent((BorderPane) mainStage.getScene().getRoot());
            });

            pageContent.getChildren().addAll(questionLabel1, nextButton);
        } else if (quizStep == 2) {
            questionLabel1.setText("Equation 1: [Hidden]");
            questionLabel1.setFont(new Font("Verdana", 24));
            questionLabel1.setTextFill(Color.RED);
            questionLabel1.setOpacity(0.2);

            questionLabel2 = new Label("Equation 2: " + currentQuestion2);
            questionLabel2.setFont(new Font("Verdana", 24));
            questionLabel2.setTextFill(Color.BLUE);

            Button answerButton = new Button("Answer");
            answerButton.setOnAction(e -> {
                quizStep = 3;
                updatePageContent((BorderPane) mainStage.getScene().getRoot());
            });

            pageContent.getChildren().addAll(questionLabel1, questionLabel2, answerButton);
        } else if (quizStep == 3) {
            Label choicePrompt = new Label("Which result was greater?");
            choicePrompt.setFont(new Font("Verdana", 24));
            choicePrompt.setTextFill(Color.BLUE);   

            Button topButton = new Button("EQ 1");
            Button bottomButton = new Button("EQ 2");
            Button sameButton = new Button("Same answer");

            topButton.setOnAction(e -> checkAnswer(1));
            bottomButton.setOnAction(e -> checkAnswer(2));
            sameButton.setOnAction(e -> checkAnswer(0));

            HBox buttonBox = new HBox(20);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.getChildren().addAll(topButton, bottomButton, sameButton);

            pageContent.getChildren().addAll(choicePrompt, buttonBox);
        }

        return pageContent;
    }

    private void generateQuestions() {
        int num1, num2, num3, num4;
        int tempResult1, tempResult2;
        boolean valid = false;

        int questionType = random.nextInt(10); // Adjusted weights for more control

        while (!valid) {
            if (questionType < 2) {  // Addition (25%) or Subtraction (25%)
                num1 = random.nextInt(90) + 10;
                num2 = random.nextInt(30) + 10;
                num3 = random.nextInt(90) + 10;
                num4 = random.nextInt(30) + 10;

                tempResult1 = num1 + num2;
                tempResult2 = num3 + num4;

                if (Math.abs(tempResult1 - tempResult2) <= ANSWER_DIFFERENCE_LIMIT && tempResult1 != tempResult2) {
                    currentQuestion1 = num1 + " + " + num2;
                    currentQuestion2 = num3 + " + " + num4;
                    valid = true;
                }
            } else if (questionType < 4) {  // Subtraction
                num1 = random.nextInt(90) + 10;
                num2 = random.nextInt(30) + 10;
                num3 = random.nextInt(90) + 10;
                num4 = random.nextInt(30) + 10;

                tempResult1 = num1 - num2;
                tempResult2 = num3 - num4;

                if (Math.abs(tempResult1 - tempResult2) <= ANSWER_DIFFERENCE_LIMIT && tempResult1 != tempResult2) {
                    currentQuestion1 = num1 + " - " + num2;
                    currentQuestion2 = num3 + " - " + num4;
                    valid = true;
                }
            } else if (questionType < 7) {  // Multiplication (30%)
                num1 = random.nextInt(7) + 2;
                num2 = random.nextInt(7) + 2;
                num3 = random.nextInt(7) + 2;
                num4 = random.nextInt(7) + 2;

                tempResult1 = num1 * num2;
                tempResult2 = num3 * num4;

                if (Math.abs(tempResult1 - tempResult2) <= ANSWER_DIFFERENCE_LIMIT) {
                    currentQuestion1 = num1 + " * " + num2;
                    currentQuestion2 = num3 + " * " + num4;
                    valid = true;
                }
            } else {  // Division (30%)
                num1 = random.nextInt(90) + 30;
                num2 = random.nextInt(10) + 2;
                num3 = random.nextInt(90) + 30;
                num4 = random.nextInt(10) + 2;

                tempResult1 = num1 / num2;
                tempResult2 = num3 / num4;

                if (num1 % num2 == 0 && num3 % num4 == 0
                        && Math.abs(tempResult1 - tempResult2) <= ANSWER_DIFFERENCE_LIMIT
                        && tempResult1 != tempResult2) {
                    currentQuestion1 = num1 + " / " + num2;
                    currentQuestion2 = num3 + " / " + num4;
                    valid = true;
                }
            }
        }
    }

    private void checkAnswer(int chosenEquation) {
        totalQuestions++;
        boolean isCorrect = false;

        int answer1 = calculateAnswer(currentQuestion1);
        int answer2 = calculateAnswer(currentQuestion2);

        if (chosenEquation == 1) {
            isCorrect = answer1 > answer2;
        } else if (chosenEquation == 2) {
            isCorrect = answer2 > answer1;
        } else if (chosenEquation == 0) {
            isCorrect = answer1 == answer2;
        }

        correctnessList.add(isCorrect);
        userResponses.add(chosenEquation);

        if (isCorrect) {
            correctAnswers++;
        }

        quizStep = 1; // Return to step 1 to generate new questions
        updatePageContent((BorderPane) mainStage.getScene().getRoot());
    }

    private int calculateAnswer(String question) {
        String[] parts = question.split(" ");
        int num1 = Integer.parseInt(parts[0]);
        int num2 = Integer.parseInt(parts[2]);
        switch (parts[1]) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Invalid operator");
        }
    }

    private void endQuiz() {
        BorderPane endPane = new BorderPane();
        endPane.setPadding(new Insets(20));

        VBox endContent = new VBox(20);
        endContent.setAlignment(Pos.CENTER);

        Label resultLabel = new Label("Quiz Over!");
        resultLabel.setFont(new Font("Verdana", 24));
        resultLabel.setTextFill(Color.BLUE);
        endContent.getChildren().add(resultLabel);

        Label scoreLabel = new Label("You answered " + correctAnswers + " out of " + totalQuestions + " questions correctly.");
        scoreLabel.setFont(new Font("Verdana", 18));
        endContent.getChildren().add(scoreLabel);

        Button redoButton = new Button("Redo Quiz");
        redoButton.setFont(new Font("Verdana", 18));
        redoButton.setTextFill(Color.WHITE);
        redoButton.setStyle("-fx-background-color: green;");
        redoButton.setOnAction(e -> startQuiz());
        endContent.getChildren().add(redoButton);

        endPane.setCenter(endContent);

        Scene endScene = new Scene(endPane, 600, 400);
        mainStage.setScene(endScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
