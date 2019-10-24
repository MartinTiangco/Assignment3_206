package Application.Helpers;

import java.io.File;

/**
 * Question class and its properties for the Active Learning Component (or Quiz)
 * @author Group 25:
 * 			- Martin Tiangco, mtia116
 * 			- Yuansheng Zhang, yzhb120
 */
public class Question {
    private Boolean _correctness = false;
    private File _creationTested;
    private String _correctAnswer;
    private String _questionNumber;
    private String _userAnswer;

    public String getQuestionNumber() {
        return _questionNumber;
    }

    public void setQuestionNumber(String questionNumber) {
        _questionNumber = questionNumber;
    }

    public String getCorrectAnswer() {
        return _correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        _correctAnswer = correctAnswer;
    }

    public String getUserAnswer() {
        return _userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        _userAnswer = userAnswer;
    }

    public Boolean getCorrectness() {
        return _correctness;
    }

    public void setCorrectness(Boolean correctness) {
        _correctness = correctness;
    }

    public File getCreationTested() {
        return _creationTested;
    }

    public void setCreationTested(File creationTested) {
        _creationTested = creationTested;
    }
}
