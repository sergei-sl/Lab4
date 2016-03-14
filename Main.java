import java.sql.*;
import java.util.Scanner;

import javafx.application.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.geometry.Insets;

public class Main extends Application {
    public static void main(String[] args){
        dl=new DatabaseLogic();
        try {
            launch(args);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;

        //Button 1
        Label label1 = new Label("Connect to database:");
        error = new Label("");
        GridPane.setConstraints(error, 0, 0);

        Label urlLabel = new Label("Server url:");
        GridPane.setConstraints(urlLabel, 0, 0);

        //Name Input
        TextField urlInput = new TextField("jdbc:postgresql://localhost");
        GridPane.setConstraints(urlInput, 1, 0);

        Label portLabel = new Label("Port");
        GridPane.setConstraints(urlLabel, 0, 0);

        //Name Input
        TextField portInput = new TextField("5432");
        GridPane.setConstraints(urlInput, 1, 0);


        //Name Label - constrains use (child, column, row)
        Label nameLabel = new Label("Username:");
        GridPane.setConstraints(nameLabel, 0, 0);

        //Name Input
        TextField nameInput = new TextField("postgres");
        GridPane.setConstraints(nameInput, 1, 0);

        //Password Label
        Label passLabel = new Label("Password:");
        GridPane.setConstraints(passLabel, 0, 1);

        //Password Input
        TextField passInput = new TextField("224244");
        passInput.setPromptText("password");
        GridPane.setConstraints(passInput, 1, 1);

        Button buttonConnect = new Button("Connect");
        buttonConnect.setOnAction(event ->
                {
                    try {
                        dl.connect(urlInput.getText(), portInput.getText(), nameInput.getText(), passInput.getText());
                        dl.init();
                    }
                    catch (SQLException ex){
                        System.out.println("no");
                    }
                    if (dl.isConnected()) {
                        error.setText("");
                        window.setScene(sceneMain);
                    }
                    else{
                        error.setText("Can't connect");
                    }
                });

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label1, urlLabel, urlInput, portLabel, portInput, nameLabel, nameInput, passLabel, passInput, buttonConnect, error);

        sceneConnect = new Scene(layout, 400, 400);

        makeMain();
        make1();
        make3();
        make4();
        make5();
        make6();
        make7();


        window.setScene(sceneConnect);
        window.setTitle("Kostyl application for DB");
        window.show();
    }

    private void makeMain(){

        Button buttonConnect = new Button("Return to connection");
        buttonConnect.setOnAction(event -> {
            try {
                dl.disconnect();
            }
            catch (SQLException ex){

            }
            if (!dl.isConnected()) {
                window.setScene(sceneConnect);
            }
        });

        Button b1 = new Button("Create database");
        b1.setOnAction(event->{

            System.out.println("hoh");
            window.setScene(scene1);
        });

        Button b2 = new Button("Remove database");
        b2.setOnAction(event->{
            if (!dl.hasDB()){
                MessageBox.display( "Database " + dl.getName() + " is already deleted");
            }
            boolean answer=ConfirmBox.display("Are you sur you want to delete "+dl.getName()+" database?");
            if (answer) {
                try {
                    dl.deleteDB();
                    MessageBox.display( "Database " + dl.getName() + " has been deleted");
                } catch (SQLException e) {
                    MessageBox.display( "Error occurred");
                }
            }
        });
        Button b3 = new Button("Clear table");
        b3.setOnAction(event->{
            window.setScene(scene3);
        });
        Button b4 = new Button("Insert new data");
        b4.setOnAction(event->{
            window.setScene(scene4);
        });
        Button b5 = new Button("Search");
        b5.setOnAction(event->{
            window.setScene(scene5);
        });

        Button b6 = new Button("Change record");
        b6.setOnAction(event->{
            window.setScene(scene6);
        });

        Button b7 = new Button("Remove pilot");
        b7.setOnAction(event->{
            window.setScene(scene7);
        });

        VBox layout = new VBox(20);
        layout.getChildren().addAll(b1, b2, b3, b4, b5, b6, b7, buttonConnect, error);
        sceneMain = new Scene(layout, 800, 600);
    }

    private void make1(){
        Label nameLabel = new Label("Databasse name:");
        GridPane.setConstraints(nameLabel, 0, 0);

        //Name Input
        TextField nameInput = new TextField("newDB");
        GridPane.setConstraints(nameInput, 1, 0);

        Button make = new Button("ok");
        make.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                create(nameInput.getText());
            }
        } );
        Button goBack = new Button("Go back to menu");
        goBack.setOnAction(event ->{
            error.setText("");
            window.setScene(sceneMain);
        });
        VBox layout = new VBox(20);
        layout.getChildren().addAll(nameLabel, nameInput, make, goBack, error);
        scene1 = new Scene(layout, 800, 600);

    }
    private void create(String n)     {

        msg = "wait for DB to create";
        Task <Void> task = new Task<Void>() {
            @Override public Void call() throws InterruptedException {
                updateMessage(msg);
                try {
                    msg = dl.createDB(n);
                } catch (Exception ex) {
                    msg = "can't create";
                    ex.printStackTrace();
                }
                return null;
            }
        };

        error.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(e -> {
            error.textProperty().unbind();
            // this message will be seen.
            error.setText(msg);
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }


    private void make3(){

    }

    private void make4(){

    }

    private void make5(){

    }

    private void make6(){

    }

    private void make7(){

    }

    boolean connected;
    static DatabaseLogic dl;
    Label error;
    String msg;
    Stage window;
    Scene sceneConnect, sceneMain, scene1, scene3, scene4, scene5, scene6, scene7;
}
