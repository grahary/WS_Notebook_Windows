/*
 * User.java
 *
 * Created by Cho, Wonsik on 2018-08-22.
 */

package kr.pe.kaijer.wsnotebook.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private StringProperty id;
    private StringProperty email;
    private StringProperty pw;
    private IntegerProperty hintQuestion;
    private StringProperty hintAnswer;

    public User() {
        this.id = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.pw = new SimpleStringProperty();
        this.hintQuestion = new SimpleIntegerProperty();
        this.hintAnswer = new SimpleStringProperty();
    }

    // id
    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty idProperty() {
        return id;
    }

    // email
    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    // pw
    public String getPw() {
        return pw.get();
    }

    public void setPw(String pw) {
        this.pw.set(pw);
    }

    public StringProperty pwProperty() {
        return pw;
    }

    // hintQuestion
    public int getHintQuestion() {
        return hintQuestion.get();
    }

    public void setHintQuestion(int hintQuestion) {
        this.hintQuestion.set(hintQuestion);
    }

    public IntegerProperty hintQuestionProperty() {
        return hintQuestion;
    }

    // hintAnswer
    public String getHintAnswer() {
        return hintAnswer.get();
    }

    public void setHintAnswer(String hintAnswer) {
        this.hintAnswer.set(hintAnswer);
    }

    public StringProperty hintAnswerProperty() {
        return hintAnswer;
    }
}
