/**
 * Alumnos es un programa que permite llevar un control
 * de alumnos, almacenando sus datos en una base de datos o
 * en un archivo binario.
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/02/22
 */

package alumnos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Alumnos extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(
            "/alumnos/interfaz/FXMLAlumnos.fxml"
        ));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("Control de alumnos - Mauricio Cruz Portilla");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
