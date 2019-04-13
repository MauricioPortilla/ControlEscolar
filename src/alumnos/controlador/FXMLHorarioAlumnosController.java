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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;


public class FXMLHorarioAlumnosController {

    private static final int MAX_CREDITOS_HORARIO = 350 / 6;

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

    @FXML
    private Label creditosLabel;

    private Alumno alumno;
    private int creditosHorario = 0;

    private ObservableList<HorarioMateria> observerHorariosDisponibles = 
            FXCollections.observableArrayList();
    private ObservableList<HorarioMateria> observerHorariosElegidos = 
            FXCollections.observableArrayList();
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
        horaInicioTableColumn_disponible.setCellValueFactory(
            new PropertyValueFactory<>("horaInicio")
        );
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
        loadAlumnoHorarioBD();
    }

    /**
     * Carga el horario del alumno desde la base de datos.
     */
    private void loadAlumnoHorarioBD() {
        horarioDisponibleTableView.getItems().clear();
        horarioElegidoTableView.getItems().clear();

        horarioAlumnoDAO.loadHorariosAlumno(alumno);
        observerHorariosElegidos = horarioAlumnoDAO.getHorariosAlumnoMateria();
        horarioElegidoTableView.setItems(observerHorariosElegidos);
        
        horarioDAO.loadHorariosMaterias();
        observerHorariosDisponibles = horarioDAO.getHorariosMaterias();
        // Eliminar horarios elegidos de los disponibles y sumar creditos
        ArrayList<HorarioMateria> materiasConsideradas = new ArrayList<>();
        for (HorarioMateria horario : observerHorariosElegidos) {
            boolean horarioFound = false;
            for (HorarioMateria horarioConsiderado : materiasConsideradas) {
                if (horarioConsiderado.getMateria().getNrc() == horario.getMateria().getNrc()) {
                    observerHorariosDisponibles.remove(horario);
                    horarioFound = true;
                    break;
                }
            }
            if (horarioFound) {
                continue;
            }
            creditosHorario += horario.getMateria().getCreditos();
            materiasConsideradas.add(horario);
            observerHorariosDisponibles.remove(horario);
        }
        creditosLabel.setText("Créditos: " + creditosHorario);
        horarioDisponibleTableView.setItems(observerHorariosDisponibles);
        
        horariosToInsert.clear();
        horariosToDelete.clear();
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
                            "No puedes seleccionar este horario porque coincide " +
                            "con uno de tus elegidos."
                        ).show();
                        return;
                    }
                }
                if ((creditosHorario + horarioSelected.getMateria().getCreditos()) > 
                    MAX_CREDITOS_HORARIO) {
                    new Alert(
                        AlertType.WARNING, 
                        "No puedes seleccionar este horario porque ya excediste " +
                        "el máximo de créditos."
                    ).show();
                    return;
                }
                creditosHorario += horarioSelected.getMateria().getCreditos();
                creditosLabel.setText("Créditos: " + creditosHorario);
                for (HorarioMateria horario : horarioDisponibleTableView.getItems()) {
                    if (horario.getMateria().getNrc() == horarioSelected.getMateria().getNrc()) {
                        horariosToInsert.add(horario);
                        horariosToDelete.remove(horario);
                    }
                }
                for (HorarioMateria horario : horariosToInsert) {
                    if (!horarioElegidoTableView.getItems().contains(horario)) {
                        horarioElegidoTableView.getItems().add(horario);
                        horarioDisponibleTableView.getItems().remove(horario);
                    }
                }
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
                creditosHorario -= horarioSelected.getMateria().getCreditos();
                creditosLabel.setText("Créditos: " + creditosHorario);

                for (HorarioMateria horario : horarioElegidoTableView.getItems()) {
                    if (horario.getMateria().getNrc() == horarioSelected.getMateria().getNrc()) {
                        horariosToDelete.add(horario);
                        horariosToInsert.remove(horario);
                    }
                }
                for (HorarioMateria horario : horariosToDelete) {
                    if (!horarioDisponibleTableView.getItems().contains(horario)) {
                        horarioDisponibleTableView.getItems().add(horario);
                        horarioElegidoTableView.getItems().remove(horario);
                    }
                }
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
                    if (horarioAlumnoDAO.getHorariosAlumnoMateria().contains(horarioMateria)) {
                        continue;
                    }
                    horarioAlumnoDAO.insertHorarioAlumno(
                        new HorarioAlumno(0, horarioMateria.getId(), alumno.getMatricula())
                    );
                }
                for (HorarioMateria horarioMateria : horariosToDelete) {
                    if (!horarioAlumnoDAO.getHorariosAlumnoMateria().contains(horarioMateria)) {
                        continue;
                    }
                    horarioAlumnoDAO.deleteHorarioAlumno(
                        new HorarioAlumno(0, horarioMateria.getId(), alumno.getMatricula())
                    );
                }
                new Alert(AlertType.INFORMATION, "Horarios guardados.").show();
                horariosToInsert.clear();
                horariosToDelete.clear();
            }
        };
    }
}
