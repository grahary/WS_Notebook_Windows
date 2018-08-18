/*
 * Created by Cho, Wonsik on 2018-08-01.
 *
 * 프로젝트명: WS Notebook
 * 프로젝트 설명: JDBC, JavaFX를 이용한 메모 관리 프로그램
 */

package kr.pe.kaijer.wsnotebook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

import kr.pe.kaijer.wsnotebook.model.Memo;
import kr.pe.kaijer.wsnotebook.view.MemoReadController;
import kr.pe.kaijer.wsnotebook.view.UserLoginController;
import kr.pe.kaijer.wsnotebook.view.MemoListController;

public class MainApp extends Application {
    private Stage stage;

    public static void main(String args[]) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("MS Notebook");
        showLoginView();
        primaryStage.show();
    }

    /**
     * 로그인 화면 출력
     */
    public void showLoginView() {
        try {
            UserLoginController login = (UserLoginController)replaceSceneContent("UserLogin");
            login.setMainApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 메모 리스트 화면 출력
     *
     * @param loggedUserID     로그인한 사용자의 ID
     */
    public void showMemoListView(String loggedUserID) {
        try {
            MemoListController memoList = (MemoListController)replaceSceneContent("MemoList");
            memoList.setMainApp(this);
            memoList.userLogged(loggedUserID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main Stage 의 뷰 변경 (로그인 <-> 메모 리스트)
     *
     * @param fxml     변경할 뷰의 이름
     */
    private Node replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = MainApp.class.getResourceAsStream("view/" + fxml + ".fxml");

        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(MainApp.class.getResource("view/" + fxml + ".fxml"));
        AnchorPane page;

        try {
            page = (AnchorPane)loader.load(in);
        } finally {
            in.close();
        }

        Scene scene = new Scene(page);
        stage.setScene(scene);

        return (Node)loader.getController();
    }

    private Memo memo;
    private String userID;

    public void setMemo(Memo memo) {
        this.memo = memo;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Modal Window 열기
     *
     * @param fxml     Modal Window 로 열 뷰의 이름
     */
    public void showModalContent(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/" + fxml + ".fxml"));

            Scene scene = new Scene(loader.load());
            Stage modalStage = new Stage();

            modalStage.setTitle("WS Notebook");
            modalStage.setScene(scene);
            modalStage.initModality(Modality.WINDOW_MODAL);
            modalStage.initOwner(stage);
            modalStage.show();

            switch (fxml) {
                case "MemoRead":
                    MemoReadController memoRead = loader.getController();
                    memoRead.setMemoReadView(memo);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}