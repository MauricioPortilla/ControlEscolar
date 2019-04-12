/**
 * FXMLHorarioMateriasController es la clase que posee los elementos
 * de la ventana, asi como sus respectivas acciones que permiten
 * llevar el control de una tabla de horarios de materias.
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/03/21
 */

package alumnos.controlador;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import alumnos.HorarioMateria;
import alumnos.HorarioMateriaDAO;
import alumnos.IHorarioMateriaDAO;
import alumnos.Materia;
import alumnos.MateriaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLHorarioMateriasController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button agregarButton;

    @FXML
    private Button cargarBDButton;

    @FXML
    private TableColumn<HorarioMateria, String> diaTableColumn;

    @FXML
    private ComboBox<String> diaComboBox;

    @FXML
    private Button editarButton;

    @FXML
    private Button eliminarButton;

    @FXML
    private Button guardarBDButton;

    @FXML
    private Button guardarButton;

    @FXML
    private TableColumn<HorarioMateria, LocalTime> horaFinTableColumn;

    @FXML
    private TextField horaFinTextField;

    @FXML
    private TableColumn<HorarioMateria, LocalTime> horaInicioTableColumn;

    @FXML
    private TextField horaInicioTextField;

    @FXML
    private TableView<HorarioMateria> horariosTableView;

    @FXML
    private Button limpiarButton;

    @FXML
    private ComboBox<Materia> materiaComboBox;

    @FXML
    private TableColumn<HorarioMateria, Materia> materiaTableColumn;

    @FXML
    private TableColumn<HorarioMateria, String> salonTableColumn;

    @FXML
    private TextField salonTextField;

    private ObservableList<HorarioMateria> observerHorarios = FXCollections.observableArrayList();
    private HorarioMateria horarioSelected = null;
    private final ArrayList<HorarioMateria> horariosToInsert = new ArrayList<>();
    private final ArrayList<HorarioMateria> horariosToUpdate = new ArrayList<>();
    private final ArrayList<HorarioMateria> horariosToDelete = new ArrayList<>();

    private final IHorarioMateriaDAO horarioDAO = new HorarioMateriaDAO();

    @FXML
    void initialize() {
        materiaTableColumn.setCellValueFactory(new PropertyValueFactory<>("materia"));
        salonTableColumn.setCellValueFactory(new PropertyValueFactory<>("salon"));
        horaInicioTableColumn.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        horaFinTableColumn.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
        diaTableColumn.setCellValueFactory(new PropertyValueFactory<>("dia"));
        horariosTableView.setItems(observerHorarios);

        ObservableList<String> dias = FXCollections.observableArrayList(
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"
        );
        diaComboBox.setItems(dias);

        MateriaDAO materiaDAO = new MateriaDAO();
        materiaDAO.loadMaterias();
        materiaComboBox.setItems(materiaDAO.getMaterias());

        agregarButton.setOnAction(agregarButtonHandler());
        editarButton.setOnAction(editarButtonHandler());
        eliminarButton.setOnAction(eliminarButtonHandler());
        limpiarButton.setOnAction(limpiarButtonHandler());
        guardarButton.setOnAction(guardarButtonHandler());
        guardarBDButton.setOnAction(guardarBDButtonHandler());
        cargarBDButton.setOnAction(cargarBDButtonHandler());

        cargarBDButton.fire();
        cargarBDButton.setVisible(false);
    }

    /**
     * Permite agregar un nuevo horario a la tabla.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> agregarButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (
                    materiaComboBox.getValue() == null ||
                    salonTextField.getText().isEmpty() ||
                    horaInicioTextField.getText().isEmpty() ||
                    horaFinTextField.getText().isEmpty() ||
                    diaComboBox.getValue() == null
                ) {
                    Alert warningAlert = new Alert(
                        AlertType.WARNING, 
                        "Debes completar todos los campos"
                    );
                    warningAlert.show();
                    return;
                }
                HorarioMateria nuevoHorario = new HorarioMateria(
                    0, materiaComboBox.getValue().getId(), 
                    salonTextField.getText(), 
                    LocalTime.parse(horaInicioTextField.getText()), 
                    LocalTime.parse(horaFinTextField.getText()), 
                    diaComboBox.getValue()
                );
                observerHorarios.add(nuevoHorario);
                horariosToInsert.add(nuevoHorario);
                cleanHorarioForm();
            }
        };
    }

    /**
     * Permite editar un horario seleccionado de la tabla de horarios, asignando
     * sus datos al formulario del horario.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> editarButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                horarioSelected = horariosTableView.getSelectionModel().getSelectedItem();
                if (horarioSelected == null) {
                    Alert warningAlert = new Alert(
                        AlertType.WARNING, 
                        "Debes seleccionar un horario primero."
                    );
                    warningAlert.show();
                    return;
                }
                materiaComboBox.getSelectionModel().select(horarioSelected.getMateria());
                salonTextField.setText(horarioSelected.getSalon());
                horaInicioTextField.setText(horarioSelected.getHoraInicio().toString());
                horaFinTextField.setText(horarioSelected.getHoraFin().toString());
                diaComboBox.getSelectionModel().select(horarioSelected.getDia());
                guardarButton.setDisable(false);
            }
        };
    }

    /**
     * Permite eliminar un horario seleccionada de la tabla de horarios.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> eliminarButtonHandler(){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                horarioSelected = horariosTableView.getSelectionModel().getSelectedItem();
                if (horarioSelected == null) {
                    Alert warningAlert = new Alert(
                        AlertType.WARNING, 
                        "Debes seleccionar un horario primero."
                    );
                    warningAlert.show();
                    return;
                }
                Alert deleteAlert = new Alert(
                    AlertType.CONFIRMATION, 
                    "¿Estás seguro de que deseas eliminar este horario?"
                );
                if (deleteAlert.showAndWait().get() == ButtonType.OK) {
                    horariosToDelete.add(horarioSelected);
                    horariosTableView.getItems().remove(horarioSelected);
                }
            }
        };
    }

    /**
     * Limpia el formulario de la materia.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> limpiarButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cleanHorarioForm();
            }
        };
    }

    /**
     * Permite guardar los cambios del formulario de horario despues
     * de haber seleccionado un horario a editar.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> guardarButtonHandler(){
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (horarioSelected == null) {
                    Alert warningAlert = new Alert(
                        AlertType.WARNING, 
                        "Debes seleccionar un horario primero."
                    );
                    warningAlert.show();
                    return;
                }
                
                try {
                    horarioSelected.setHoraInicio(LocalTime.parse(horaInicioTextField.getText()));
                    horarioSelected.setHoraFin(LocalTime.parse(horaFinTextField.getText()));
                } catch (DateTimeParseException ex) {
                    new Alert(AlertType.WARNING, "Ingresa una hora válida.").show();
                    return;
                }
                horarioSelected.setMateria(materiaComboBox.getSelectionModel().getSelectedItem());
                horarioSelected.setSalon(salonTextField.getText());
                horarioSelected.setDia(diaComboBox.getSelectionModel().getSelectedItem());

                horariosToUpdate.add(horarioSelected);
                cleanHorarioForm();
                guardarButton.setDisable(true);

                horariosTableView.refresh();
            }
        };
    }

    /**
     * Permite guardar los datos de la tabla de horarios en la base de datos.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> guardarBDButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (HorarioMateria horario : horariosToInsert) {
                    horarioDAO.insertHorarioMateria(horario);
                }
                for (HorarioMateria horario : horariosToUpdate) {
                    horarioDAO.updateHorarioMateria(horario);
                }
                for (HorarioMateria horario : horariosToDelete) {
                    horarioDAO.deleteHorarioMateria(horario);
                }
                Alert saveAlert = new Alert(AlertType.INFORMATION, "Horarios guardados.");
                saveAlert.show();
                horariosToInsert.clear();
                horariosToUpdate.clear();
                horariosToDelete.clear();
            }
        };
    }

    /**
     * Permite cargar los datos de los horarios desde la base de datos.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> cargarBDButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                horariosTableView.getItems().clear();
                horarioDAO.loadHorariosMaterias();
                observerHorarios = horarioDAO.getHorariosMaterias();
                horariosTableView.setItems(observerHorarios);
                horariosToInsert.clear();
                horariosToUpdate.clear();
                horariosToDelete.clear();
            }
        };
    }

    /**
     * Limpia el formulario del horario.
     */
    private void cleanHorarioForm() {
        materiaComboBox.getSelectionModel().clearSelection();
        salonTextField.clear();
        horaInicioTextField.clear();
        horaFinTextField.clear();
        diaComboBox.getSelectionModel().clearSelection();
        horarioSelected = null;
    }

}
