/*
 * Memo.java
 *
 * Created by Cho, Wonsik on 2018-08-16.
 */

package kr.pe.kaijer.wsnotebook.model;

import javafx.beans.property.*;

public class Memo {
    private IntegerProperty idx;
    private StringProperty title;
    private StringProperty userID;
    private StringProperty writeDate;
    private StringProperty content;
    private StringProperty tag;
    private BooleanProperty isEncrypt;
    private StringProperty encryptPW;

    public Memo() {
        this.idx = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.userID = new SimpleStringProperty();
        this.writeDate = new SimpleStringProperty();
        this.content = new SimpleStringProperty();
        this.tag = new SimpleStringProperty();
        this.isEncrypt = new SimpleBooleanProperty();
        this.encryptPW = new SimpleStringProperty();
    }

    // idx
    public int getIdx() {
        return idx.get();
    }

    public void setIdx(int idx) {
        this.idx.set(idx);
    }

    public IntegerProperty idxProperty() {
        return idx;
    }

    // title
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    // userID
    public String getUserID() {
        return userID.get();
    }

    public void setUserID(String userID) {
        this.userID.set(userID);
    }

    public StringProperty userIDProperty() {
        return userID;
    }

    // writeDate
    public String getWriteDate() {
        return writeDate.get();
    }

    public void setWriteDate(String writeDate) {
        this.writeDate.set(writeDate);
    }

    public StringProperty writeDateProperty() {
        return writeDate;
    }

    // content
    public String getContent() {
        return content.get();
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public StringProperty contentProperty() {
        return content;
    }

    // tag
    public String getTag() {
        return tag.get();
    }

    public void setTag(String tag) {
        this.tag.set(tag);
    }

    public StringProperty tagProperty() {
        return tag;
    }

    // isEncrypt
    public Boolean getIsEncrypt() {
        return isEncrypt.get();
    }

    public void setIsEncrypt(boolean isEncrypt) {
        this.isEncrypt.set(isEncrypt);
    }

    public BooleanProperty isEncryptProperty() {
        return isEncrypt;
    }

    // EncryptPW
    public String getEncryptPW() {
        return encryptPW.get();
    }

    public void setEncryptPW(String encryptPW) {
        this.encryptPW.set(encryptPW);
    }

    public StringProperty encryptPWProperty() {
        return encryptPW;
    }
}
