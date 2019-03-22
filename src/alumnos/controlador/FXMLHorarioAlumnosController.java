/**
 * FXMLHorarioAlumnosController es la clase que posee los elementos
 * de la ventana, asi como sus respectivas acciones que permiten
 * llevar el control de tablas con los horarios disponibles y los elegidos.
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/03/21
 */

package alumnos.controlador;

import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import alumnos.Alumno;
import alumnos.HorarioAlumno;
import alumnos.HorarioAlumnoDAO;
import alumnos.HorarioMateria;
import alumnos.HorarioMateriaDAO;
import alumnos.IHorarioAlumnoDAO;
import alumnos.IHorarioMateriaDAO;
import alumnos.Materia;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;


public class FXMLHorarioAlumnosController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button agregarButton;

    @FXML
    private TableColumn<HorarioMateria, String> diaTableColumn_disponible;

    @FXML
    private TableColumn<HorarioMateria, String> diaTableColumn_elegido;

    @FXML
    private Button eliminarButton;

    @FXML
    private Button guardarBDButton;

    @FXML
    private TableColumn<HorarioMateria, LocalTime> horaFinTableColumn_disponible;

    @FXML
    private TableColumn<HorarioMateria, LocalTime> horaFinTableColumn_elegido;

    @FXML
    private TableColumn<HorarioMateria, LocalTime> horaInicioTableColumn_disponible;

    @FXML
    private TableColumn<HorarioMateria, LocalTime> horaInicioTableColumn_elegido;

    @FXML
    private TableView<HorarioMateria> horarioDisponibleTableView;

    @FXML
    private TableView<HorarioMateria> horarioElegidoTableView;

    @FXML
    private TableColumn<HorarioMateria, Materia> materiaTableColumn_disponible;

    @FXML
    private TableColumn<HorarioMateria, Materia> materiaTableColumn_elegido;

    @FXML
    private TableColumn<HorarioMateria, String> salonTableColumn_disponible;

    @FXML
    private TableColumn<HorarioMateria, String> salonTableColumn_elegido;

    private Alumno alumno;

    private ObservableList<HorarioMateria> observerHorariosDisponibles = FXCollections.observableArrayList();
    private ObservableList<HorarioMateria> observerHorariosElegidos = FXCollections.observableArrayList();
    private HorarioMateria horarioSelected = null;
    private final ArrayList<HorarioMateria> horariosToInsert = new ArrayList<>();
    private final ArrayList<HorarioMateria> horariosToDelete = new ArrayList<>();

    private final IHorarioMateriaDAO horarioDAO = new HorarioMateriaDAO();
    private final IHorarioAlumnoDAO horarioAlumnoDAO = new HorarioAlumnoDAO();

    @FXML
    void initialize() {
        materiaTableColumn_disponible.setCellValueFactory(new PropertyValueFactory<>("materia"));
        materiaTableColumn_elegido.setCellValueFactory(new PropertyValueFactory<>("materia"));
        salonTableColumn_disponible.setCellValueFactory(new PropertyValueFactory<>("salon"));
        salonTableColumn_elegido.setCellValueFactory(new PropertyValueFactory<>("salon"));
        horaInicioTableColumn_disponible.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        horaInicioTableColumn_elegido.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        horaFinTableColumn_disponible.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
        horaFinTableColumn_elegido.setCellValueFactory(new PropertyValueFactory<>("horaFin"));
        diaTableColumn_disponible.setCellValueFactory(new PropertyValueFactory<>("dia"));
        diaTableColumn_elegido.setCellValueFactory(new PropertyValueFactory<>("dia"));
        horarioDAO.loadHorariosMaterias();
        observerHorariosDisponibles = horarioDAO.getHorariosMaterias();

        horarioDisponibleTableView.setItems(observerHorariosDisponibles);
        horarioElegidoTableView.setItems(observerHorariosElegidos);

        agregarButton.setOnAction(agregarButtonHandler());
        eliminarButton.setOnAction(eliminarButtonHandler());
        guardarBDButton.setOnAction(guardarBDButtonHandler());
    }

    /**
     * Inicializa la ventana con un alumno.
     * 
     * @param alumno alumno a asignar horario
     */
    void initData(Alumno alumno) {
        this.alumno = alumno;
        System.out.println(this.alumno);
    }

    /**
     * Retorna el evento de agregar un horario seleccionado de los
     * disponibles a la tabla de horarios elegidos.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> agregarButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                horarioSelected = horarioDisponibleTableView.getSelectionModel().getSelectedItem();
                if (horarioSelected == null) {
                    new Alert(AlertType.WARNING, "Debes seleccionar un horario primero.").show();
                    return;
                }
                for (HorarioMateria horario : horarioElegidoTableView.getItems()) {
                    if (horario.equals(horarioSelected)) {
                        new Alert(
                            AlertType.WARNING, 
                            "No puedes seleccionar este horario porque coincide con uno de tus elegidos."
                        ).show();
                        return;
                    }
                }
                horarioElegidoTableView.getItems().add(horarioSelected);
                horarioDisponibleTableView.getItems().remove(horarioSelected);
                horariosToInsert.add(horarioSelected);
                horarioSelected = null;
            }
        };
    }

    /**
     * Retorna el evento que elimina un horario de la tabla de elegidos.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> eliminarButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                horarioSelected = horarioElegidoTableView.getSelectionModel().getSelectedItem();
                if (horarioSelected == null) {
                    new Alert(AlertType.WARNING, "Debes seleccionar un horario primero.").show();
                    return;
                }
                horarioDisponibleTableView.getItems().add(horarioSelected);
                horarioElegidoTableView.getItems().remove(horarioSelected);
                horarioSelected = null;
            }
        };
    }

    /**
     * Retorna el evento que guarda en la base de datos los horarios elegidos.
     * 
     * @return el evento
     */
    private EventHandler<ActionEvent> guardarBDButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (HorarioMateria horarioMateria : horariosToInsert) {
                    horarioAlumnoDAO.insertHorarioAlumno(
                        new HorarioAlumno(0, horarioMateria.getId(), alumno.getMatricula())
                    );
                }
                /*for (HorarioMateria horarioMateria : horariosToDelete) {
                    horarioAlumnoDAO.deleteHorarioAlumno(
                        new HorarioAlumno()
                    );
                }*/
                Alert saveAlert = new Alert(AlertType.INFORMATION, "Horarios guardados.");
                saveAlert.show();
                horariosToInsert.clear();
                horariosToDelete.clear();
            }
        };
    }
}
