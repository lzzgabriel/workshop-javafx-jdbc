package dev.lzzgabriel.workshop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

  private static Scene scene;
  
  private static String dbUrl;
  private static final String dbName = "course-javafx";
  private static final String dbDataDir = "./data/db";

  @Override
  public void start(Stage stage) throws IOException {
    var scrollPane = (ScrollPane) loadFXML("MainView");

    scrollPane.setFitToHeight(true);
    scrollPane.setFitToWidth(true);

    scene = new Scene(scrollPane, 640, 480);
    stage.setScene(scene);
    stage.setTitle("Sample JavaFX App");
    stage.show();
  }

  public static Scene getScene() {
    return scene;
  }

  static void setRoot(String fxml) throws IOException {
    scene.setRoot(loadFXML(fxml));
  }

  private static Parent loadFXML(String fxml) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    return fxmlLoader.load();
  }

  public static void main(String[] args) {

    DB db = null;
    try {
      DBConfigurationBuilder configBuilder = DBConfigurationBuilder.newBuilder();
      configBuilder.setPort(3306);
      configBuilder.setDataDir(dbDataDir);
      db = DB.newEmbeddedDB(configBuilder.build());
      
      db.start();
      
      db.createDB(dbName);
      dbUrl = configBuilder.getURL(dbName);
      
      Properties properties = new Properties();
      properties.setProperty("dburl", dbUrl);
      
      File file = new File("./db.properties");
      file.createNewFile();
      
      try (OutputStream outputStream = new FileOutputStream(file)) {
        properties.store(outputStream, "Properites saved to file");
      }
      
      try (InputStream is = new FileInputStream(Path.of(dbDataDir, "../db.sql").toFile())) {
        db.source(is, dbName);
      }
      
      launch();
      
      file.delete();
    } catch (ManagedProcessException e) {
      e.printStackTrace();
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      stopDb(db);
    }
  }
  
  private static void stopDb(DB db) {
    if (db == null) return;
    try {
      try(Connection connection = DriverManager.getConnection(dbUrl)) {
        connection.createStatement().execute("DROP DATABASE `" + dbName + "`");
      }
      db.stop();
    } catch (ManagedProcessException e) {
      e.printStackTrace();
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
  }
  
}