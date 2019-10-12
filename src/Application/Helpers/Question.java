package Application.Helpers;

import java.io.File;

public class Question {

    private String questionNumber;
    private String correctAnswer;
    private String userAnswer;
    private Boolean correctness = false;
    private File creationTested;

    public String getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(String questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public Boolean getCorrectness() {
        return correctness;
    }

    public void setCorrectness(Boolean correctness) {
        this.correctness = correctness;
    }

    public File getCreationTested() {
        return creationTested;
    }

    public void setCreationTested(File creationTested) {
        this.creationTested = creationTested;
    }
}
