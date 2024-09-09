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

    // New list to store incorrect answers
    private List<IncorrectAnswer> incorrectAnswers = new ArrayList<>();

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
        incorrectAnswers.clear(); // Clear previous incorrect answers

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
                    result1 = tempResult1;
                    result2 = tempResult2;
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
                    result1 = tempResult1;
                    result2 = tempResult2;
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
                    result1 = tempResult1;
                    result2 = tempResult2;
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
                    result1 = tempResult1;
                    result2 = tempResult2;
                    valid = true;
                }
            }
        }
    }

    private void checkAnswer(int chosenEquation) {
        totalQuestions++;

        boolean isCorrect = false;
        if (chosenEquation == 1 && result1 > result2) {
            correctAnswers++;
            isCorrect = true;
        } else if (chosenEquation == 2 && result2 > result1) {
            correctAnswers++;
            isCorrect = true;
        } else if (chosenEquation == 0 && result1 == result2) {
            correctAnswers++;
            isCorrect = true;
        }

        // Record whether the answer was correct or not
        correctnessList.add(isCorrect);
        userResponses.add(chosenEquation);

        // If incorrect, record the details for review
        if (!isCorrect) {
            incorrectAnswers.add(new IncorrectAnswer(currentQuestion1, currentQuestion2, chosenEquation, result1, result2));
        }

        // Continue to the next question
        quizStep = 1;
        updatePageContent((BorderPane) mainStage.getScene().getRoot());
    }

    private void endQuiz() {
        BorderPane resultsPane = new BorderPane();
        resultsPane.setPadding(new Insets(20));

        VBox resultContent = new VBox(20);
        resultContent.setAlignment(Pos.CENTER);

        Label resultLabel = new Label("Quiz Finished!");
        resultLabel.setFont(new Font("Verdana", 30));
        resultLabel.setTextFill(Color.BLUE);
        resultContent.getChildren().add(resultLabel);

        Label scoreLabel = new Label("Your score: " + correctAnswers + " / " + totalQuestions);
        scoreLabel.setFont(new Font("Verdana", 24));
        scoreLabel.setTextFill(Color.GREEN);
        resultContent.getChildren().add(scoreLabel);

        if (correctAnswers > highestRecord) {
            highestRecord = correctAnswers;
        }
        Label recordLabel = new Label("Your highest score: " + highestRecord);
        recordLabel.setFont(new Font("Verdana", 20));
        resultContent.getChildren().add(recordLabel);

        Button reviewButton = new Button("Review Incorrect Answers");
        reviewButton.setFont(new Font("Verdana", 18));
        reviewButton.setTextFill(Color.WHITE);
        reviewButton.setStyle("-fx-background-color: blue;");
        reviewButton.setOnAction(e -> showReviewPage());
        resultContent.getChildren().add(reviewButton);

        Button retryButton = new Button("Retry Quiz");
        retryButton.setFont(new Font("Verdana", 18));
        retryButton.setTextFill(Color.WHITE);
        retryButton.setStyle("-fx-background-color: green;");
        retryButton.setOnAction(e -> startQuiz());
        resultContent.getChildren().add(retryButton);

        resultsPane.setCenter(resultContent);
        Scene resultScene = new Scene(resultsPane, 600, 400);
        mainStage.setScene(resultScene);
    }

    // Show review page with incorrect answers
    private void showReviewPage() {
        BorderPane reviewPane = new BorderPane();
        reviewPane.setPadding(new Insets(20));

        VBox reviewContent = new VBox(20);
        reviewContent.setAlignment(Pos.CENTER);

        Label reviewLabel = new Label("Incorrect Answers Review");
        reviewLabel.setFont(new Font("Verdana", 24));
        reviewLabel.setTextFill(Color.BLUE);
        reviewContent.getChildren().add(reviewLabel);

        // Display each incorrect answer with color-coded text for correct answers
        for (IncorrectAnswer incorrectAnswer : incorrectAnswers) {
            String userAnswer;
            if (incorrectAnswer.userAnswer == 1) {
                userAnswer = "Equation 1 was chosen";
            } else if (incorrectAnswer.userAnswer == 2) {
                userAnswer = "Equation 2 was chosen";
            } else {
                userAnswer = "Both were chosen as equal";
            }

            // Show the equations
            Label equationLabel1 = new Label("Equation 1: " + incorrectAnswer.equation1);
            Label equationLabel2 = new Label("Equation 2: " + incorrectAnswer.equation2);
            equationLabel1.setFont(new Font("Verdana", 18));
            equationLabel2.setFont(new Font("Verdana", 18));

            // Show user's incorrect choice in red
            Label userLabel = new Label("Your answer: " + userAnswer);
            userLabel.setFont(new Font("Verdana", 16));
            userLabel.setTextFill(Color.RED);

            // Show correct answer in green
            Label correctLabel;
            if (incorrectAnswer.correctAnswer1 > incorrectAnswer.correctAnswer2) {
                correctLabel = new Label("Correct answer: Equation 1 was greater");
            } else if (incorrectAnswer.correctAnswer2 > incorrectAnswer.correctAnswer1) {
                correctLabel = new Label("Correct answer: Equation 2 was greater");
            } else {
                correctLabel = new Label("Correct answer: Both were equal");
            }
            correctLabel.setFont(new Font("Verdana", 16));
            correctLabel.setTextFill(Color.GREEN);

            // Add both user and correct answer to the layout
            VBox questionBox = new VBox(10, equationLabel1, equationLabel2, userLabel, correctLabel);
            questionBox.setAlignment(Pos.CENTER_LEFT);
            reviewContent.getChildren().add(questionBox);
        }

        // Wrap reviewContent inside a ScrollPane to enable scrolling
        ScrollPane scrollPane = new ScrollPane(reviewContent);
        scrollPane.setFitToWidth(true); // Ensures the content fits within the width
        scrollPane.setPadding(new Insets(10));

        // Button to go back to results
        Button backButton = new Button("Back to Results");
        backButton.setFont(new Font("Verdana", 18));
        backButton.setTextFill(Color.WHITE);
        backButton.setStyle("-fx-background-color: blue;");
        backButton.setOnAction(e -> endQuiz()); // Back to the results page
        reviewContent.getChildren().add(backButton);

        reviewPane.setCenter(scrollPane); // Set the scrollable content in the center
        Scene reviewScene = new Scene(reviewPane, 600, 400);
        mainStage.setScene(reviewScene);
    }

    // Define a class to store question and answer details
    class Question {
        String question1, question2;
        int result1, result2;

        public Question(String question1, String question2, int result1, int result2) {
            this.question1 = question1;
            this.question2 = question2;
            this.result1 = result1;
            this.result2 = result2;
        }
    }

    // Define a class to store incorrect answer details for review
    class IncorrectAnswer {
        String equation1, equation2;
        int userAnswer;
        int correctAnswer1, correctAnswer2;

        public IncorrectAnswer(String equation1, String equation2, int userAnswer, int correctAnswer1, int correctAnswer2) {
            this.equation1 = equation1;
            this.equation2 = equation2;
            this.userAnswer = userAnswer;
            this.correctAnswer1 = correctAnswer1;
            this.correctAnswer2 = correctAnswer2;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
