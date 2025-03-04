package model;

public class QuizSubmissionDetails {
    private int id;
    private int quizSubmissionID;
    private int questionID;
    private int answerID;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuizSubmissionID() {
        return quizSubmissionID;
    }

    public void setQuizSubmissionID(int quizSubmissionID) {
        this.quizSubmissionID = quizSubmissionID;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getAnswerID() {
        return answerID;
    }

    public void setAnswerID(int answerID) {
        this.answerID = answerID;
    }
}
