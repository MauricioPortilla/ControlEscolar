/**
 * FXMLAlumnosController es la clase que posee los elementos
 * de la ventana, asi como sus respectivas acciones que permiten
 * llevar el control de una tabla de alumnos.
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/02/22
 */

package alumnos.controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import alumnos.Alumno;
import alumnos.AlumnoDAO;
import alumnos.IAlumnoDAO;
import alumnos.engine.Engine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXMLAlumnosController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button agregarButton;

    @FXML
    private TableView<Alumno> alumnosTableView;

    @FXML
    private TableColumn<Alumno, String> apellidoMaternoTableColumn;

    @FXML
    private TextField apellidoMaternoTextField;

    @FXML
    private TableColumn<Alumno, String> apellidoPaternoTableColumn;

    @FXML
    private TextField apellidoPaternoTextField;

    @FXML
    private Button editarButton;

    @FXML
    private Button eliminarButton;

    @FXML
    private Button guardarButton;

    @FXML
    private Label label;

    @FXML
    private Button limpiarButton;

    @FXML
    private TableColumn<Alumno, String> matriculaTableColumn;

    @FXML
    private TextField matriculaTextField;

    @FXML
    private TableColumn<Alumno, String> nombreTableColumn;

    @FXML
    private TextField nombreTextField;

    @FXML
    private Button salirButton;

    @FXML
    private Button guardarArchivoButton;

    @FXML
    private Button guardarBDButton;

    @FXML
    private Button cargarArchivoButton;

    @FXML
    private Button cargarBDButton;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem menuItemMaterias;

    @FXML
    private MenuItem horariosMateriasMenuItem;

    @FXML
    private MenuItem asignarHorarioMenuItem;

    private ObservableList<Alumno> observerAlumnos = FXCollections.observableArrayList();
    private Alumno alumnoSelected = null;
    private final ArrayList<Alumno> alumnosToInsert = new ArrayList<>();
    private final ArrayList<Alumno> alumnosToUpdate = new ArrayList<>();
    private final ArrayList<Alumno> alumnosToDelete = new ArrayList<>();

    private final IAlumnoDAO alumnoDAO = new AlumnoDAO();


    @FXML
    void initialize() {
        Engine.createDatabase();
        nombreTableColumn.setCellValueFactory(
            new PropertyValueFactory<Alumno, String>("nombre")
        );
        apellidoPaternoTableColumn.setCellValueFactory(
            new PropertyValueFactory<Alumno, String>("apellidoPaterno")
        );
        apellidoMaternoTableColumn.setCellValueFactory(
            new PropertyValueFactory<Alumno, String>("apellidoMaterno")
        );
        matriculaTableColumn.setCellValueFactory(
            new PropertyValueFactory<Alumno, String>("matricula")
        );
        alumnosTableView.setItems(observerAlumnos);

        agregarButton.setOnAction(agregarButtonHandler());
        editarButton.setOnAction(editarButtonHandler());
        eliminarButton.setOnAction(eliminarButtonHandler());
        limpiarButton.setOnAction(limpiarButtonHandler());
        guardarButton.setOnAction(guardarButtonHandler());
        salirButton.setOnAction(salirButtonHandler());
        guardarArchivoButton.setOnAction(guardarArchivoButtonHandler());
        guardarBDButton.setOnAction(guardarBDButtonHandler());
        cargarBDButton.setOnAction(cargarBDButtonHandler());
        cargarArchivoButton.setOnAction(cargarArchivoButtonHandler());

        menuItemMaterias.setOnAction(materiasFormButtonHandler());
        horariosMateriasMenuItem.setOnAction(horariosMateriasFormButtonHandler());
        asignarHorarioMenuItem.setOnAction(horariosAlumnosFormButtonHandler());

        cargarBDButton.setVisible(false);
        cargarArchivoButton.setVisible(false);
        guardarArchivoButton.setVisible(false);

        cargarBDButton.fire();
    }

    /**
     * Permite agregar un nuevo estudiante a la tabla
     * de estudiantes.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> agregarButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (
                    nombreTextField.getText().isEmpty() ||
                    apellidoPaternoTextField.getText().isEmpty() ||
                    apellidoMaternoTextField.getText().isEmpty() ||
                    matriculaTextField.getText().isEmpty()
                ) {
                    Alert warningAlert = new Alert(
                        AlertType.WARNING, 
                        "Debes completar todos los campos"
                    );
                    warningAlert.show();
                    return;
                }
                Alumno nuevoAlumno = new Alumno(
                    nombreTextField.getText(), 
                    apellidoPaternoTextField.getText(), 
                    apellidoMaternoTextField.getText(), 
                    matriculaTextField.getText()
                );
                observerAlumnos.add(nuevoAlumno);
                alumnosToInsert.add(nuevoAlumno);
                cleanAlumnoForm();
            }
        };
    }

    /**
     * Permite editar un estudiante seleccionado de
     * la tabla de estudiantes, asignando sus datos al
     * formulario del alumno.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> editarButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                alumnoSelected = alumnosTableView.getSelectionModel().getSelectedItem();
                if (alumnoSelected == null) {
                    Alert warningAlert = new Alert(
                        AlertType.WARNING, 
                        "Debes seleccionar un alumno primero."
                    );
                    warningAlert.show();
                    return;
                }
                nombreTextField.setText(alumnoSelected.getNombre());
                apellidoPaternoTextField.setText(alumnoSelected.getApellidoPaterno());
                apellidoMaternoTextField.setText(alumnoSelected.getApellidoMaterno());
                matriculaTextField.setText(alumnoSelected.getMatricula());
                guardarButton.setDisable(false);
            }
        };
    }

    /**
     * Permite eliminar un estudiante seleccionado de la tabla de estudiantes.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> eliminarButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                alumnoSelected = alumnosTableView.getSelectionModel().getSelectedItem();
                if (alumnoSelected == null) {
                    Alert warningAlert = new Alert(
                        AlertType.WARNING, 
                        "Debes seleccionar un alumno primero."
                    );
                    warningAlert.show();
                    return;
                }
                Alert deleteAlert = new Alert(
                    AlertType.CONFIRMATION, 
                    "¿Estás seguro de que deseas eliminar este alumno?"
                );
                if (deleteAlert.showAndWait().get() == ButtonType.OK) {
                    alumnosToDelete.add(alumnoSelected);
                    alumnosTableView.getItems().remove(alumnoSelected);
                }
            }
        };
    }

    /**
     * Limpia el formulario del alumno.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> limpiarButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cleanAlumnoForm();
            }
        };
    }

    /**
     * Permite guardar los cambios del formulario de alumno despues
     * de haber seleccionado un alumno a editar.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> guardarButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                alumnoSelected = alumnosTableView.getSelectionModel().getSelectedItem();
                if (alumnoSelected == null) {
                    Alert warningAlert = new Alert(
                        AlertType.WARNING, 
                        "Debes seleccionar un alumno primero."
                    );
                    warningAlert.show();
                    return;
                }
                
                alumnoSelected.setNombre(nombreTextField.getText());
                alumnoSelected.setApellidoPaterno(apellidoPaternoTextField.getText());
                alumnoSelected.setApellidoMaterno(apellidoMaternoTextField.getText());
                alumnoSelected.setMatricula(matriculaTextField.getText());
                
                alumnosToUpdate.add(alumnoSelected);
                cleanAlumnoForm();
                guardarButton.setDisable(true);

                alumnosTableView.refresh();
            }
        };
    }

    /**
     * Permite salir del programa.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> salirButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                salirButton.getScene().getWindow().hide();
            }
        };
    }

    /**
     * Permite guardar los datos de la tabla de alumnos
     * en un archivo .dat en alguna ruta especificada por el usuario.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> guardarArchivoButtonHandler(){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                        "DAT files (*.dat)", "*.dat"
                    );
                    fileChooser.getExtensionFilters().add(extFilter);
                    File file = fileChooser.showSaveDialog(
                        guardarArchivoButton.getScene().getWindow()
                    );
                    ObjectOutputStream stream = new ObjectOutputStream(
                        new FileOutputStream(file)
                    );
                    stream.writeInt(alumnosTableView.getItems().size());
                    for (int i=0; i < alumnosTableView.getItems().size(); i++) {
                        stream.writeObject(alumnosTableView.getItems().get(i));
                    }
                    stream.close();
                    Alert confirmAlert = new Alert(
                        AlertType.INFORMATION, 
                        "Alumnos guardados."
                    );
                    confirmAlert.show();
                } catch(IOException e) {
                    Alert errorAlert = new Alert(
                        AlertType.ERROR, 
                        "Se produjo un error al guardar el archivo -> " + e.getMessage()
                    );
                    errorAlert.show();
                }
            }
        };
    }

    /**
     * Permite guardar los datos de la tabla de alumnos en la base de datos.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> guardarBDButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (Alumno alumno : alumnosToInsert) {
                    alumnoDAO.insertAlumno(alumno);
                }
                for (Alumno alumno : alumnosToUpdate) {
                    alumnoDAO.updateAlumno(alumno);
                }
                for (Alumno alumno : alumnosToDelete) {
                    alumnoDAO.deleteAlumno(alumno);
                }
                Alert saveAlert = new Alert(
                    AlertType.INFORMATION, 
                    "Alumnos guardados."
                );
                saveAlert.show();
                alumnosToInsert.clear();
                alumnosToUpdate.clear();
                alumnosToDelete.clear();
            }
        };
    }

    /**
     * Permite cargar los datos de los alumnos desde la base de datos.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> cargarBDButtonHandler(){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                alumnosTableView.getItems().clear();
                alumnoDAO.loadAlumnos();
                observerAlumnos = alumnoDAO.getAlumnos();
                alumnosTableView.setItems(observerAlumnos);
                alumnosToInsert.clear();
                alumnosToUpdate.clear();
                alumnosToDelete.clear();
            }
        };
    }

    /**
     * Permite cargar los datos de los alumnos desde un archivo .dat
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> cargarArchivoButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    alumnosToInsert.clear();
                    alumnosToUpdate.clear();
                    alumnosToDelete.clear();
                    alumnosTableView.getItems().clear();

                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                        "DAT files (*.dat)", "*.dat"
                    );
                    fileChooser.getExtensionFilters().add(extFilter);
                    File file = fileChooser.showOpenDialog(
                        cargarArchivoButton.getScene().getWindow()
                    );

                    ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
                    int numberOfAlumnos = stream.readInt();
                    for (int i=1; i <= numberOfAlumnos; i++) {
                        Object alumnoObject = stream.readObject();
                        alumnosTableView.getItems().add((Alumno)alumnoObject);
                        alumnosTableView.getItems().get(i-1).resetStringProperties();
                    }
                    stream.close();

                    Alert confirmAlert = new Alert(AlertType.INFORMATION, "Alumnos cargados.");
                    confirmAlert.show();
                } catch (ClassNotFoundException classException) {
                    Alert errorAlert = new Alert(
                        AlertType.ERROR, 
                        "Se produjo un error al cargar el archivo -> " + classException.getMessage()
                    );
                    errorAlert.show();
                } catch (IOException e) {
                    Alert errorAlert = new Alert(
                        AlertType.ERROR, 
                        "Se produjo un error al cargar el archivo -> " + e.getMessage()
                    );
                    errorAlert.show();
                } catch (Exception ex) {
                    System.out.println(
                        "Se produjo un error al cargar el archivo -> " + ex.getMessage()
                    );
                }
            }
        };
    }

    /**
     * Permite abrir la ventana del control de materias.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> materiasFormButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Parent root;
                try {
                    root = FXMLLoader.load(getClass().getResource(
                        "/alumnos/interfaz/FXMLMaterias.fxml"
                    ));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Control de materias - Mauricio Cruz Portilla");
                    stage.show();
                } catch (IOException ex) {
                    System.out.println("Error al abrir la ventana Materias.");
                }
            }
        };
    }

    /**
     * Permite abrir la ventana del control de horarios de las materias
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> horariosMateriasFormButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Parent root;
                try {
                    root = FXMLLoader.load(getClass().getResource(
                        "/alumnos/interfaz/FXMLHorarioMaterias.fxml"
                    ));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Control de horarios de materias - Mauricio Cruz Portilla");
                    stage.show();
                } catch (IOException ex) {
                    System.out.println("Error al abrir la ventana Horarios de Materias.");
                }
            }
        };
    }

    private EventHandler<ActionEvent> horariosAlumnosFormButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (alumnosTableView.getSelectionModel().getSelectedItem() == null) {
                    new Alert(AlertType.WARNING, "Debes seleccionar un alumno primero.").show();
                    return;
                }
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/alumnos/interfaz/FXMLHorarioAlumnos.fxml"
                    ));
                    Stage stage = new Stage();
                    stage.setScene(new Scene((AnchorPane)loader.load()));
                    stage.setTitle("Control de horarios de alumnos - Mauricio Cruz Portilla");
                    FXMLHorarioAlumnosController controller = loader.
                        <FXMLHorarioAlumnosController>getController();
                    controller.initData(alumnosTableView.getSelectionModel().getSelectedItem());
                    stage.show();
                } catch (IOException ex) {
                    System.out.println("Error al abrir la ventana Horario de Alumno");
                }
            }
        };
    }

    /**
     * Limpia el formulario del alumno.
     */
    private void cleanAlumnoForm(){
        nombreTextField.clear();
        apellidoPaternoTextField.clear();
        apellidoMaternoTextField.clear();
        matriculaTextField.clear();
        alumnoSelected = null;
    }

}
