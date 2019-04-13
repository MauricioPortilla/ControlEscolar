package alumnos.controlador;

import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import alumnos.Alumno;
import alumnos.HorarioAlumno;
import alumnos.HorarioAlumnoDAO;
import alumnos.IHorarioAlumnoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLHorarioAlumnoController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<HorarioSemana> horarioTableView;

    @FXML
    private TableColumn<HorarioSemana, String> lunesTableColumn;
    
    @FXML
    private TableColumn<HorarioSemana, String> martesTableColumn;

    @FXML
    private TableColumn<HorarioSemana, String> juevesTableColumn;

    @FXML
    private TableColumn<HorarioSemana, String> miercolesTableColumn;

    @FXML
    private TableColumn<HorarioSemana, String> sabadoTableColumn;

    @FXML
    private TableColumn<HorarioSemana, String> viernesTableColumn;

    private Alumno alumno;
    private IHorarioAlumnoDAO horarioAlumnoDAO = new HorarioAlumnoDAO();
    private ObservableList<HorarioSemana> observerHorariosTable = 
            FXCollections.observableArrayList();

    private static final int NUM_WEEK_DAYS = 6;
    private static final ArrayList<String> WEEK_DAYS = new ArrayList<String>() {{
        add("Lunes");
        add("Martes");
        add("Miércoles");
        add("Jueves");
        add("Viernes");
        add("Sábado");
    }};

    @FXML
    void initialize() {
        lunesTableColumn.setCellValueFactory(new PropertyValueFactory<>("lunes"));
        martesTableColumn.setCellValueFactory(new PropertyValueFactory<>("martes"));
        miercolesTableColumn.setCellValueFactory(new PropertyValueFactory<>("miercoles"));
        juevesTableColumn.setCellValueFactory(new PropertyValueFactory<>("jueves"));
        viernesTableColumn.setCellValueFactory(new PropertyValueFactory<>("viernes"));
        sabadoTableColumn.setCellValueFactory(new PropertyValueFactory<>("sabado"));
    }

    /**
     * Inicializa la ventana tomando como base el alumno ingresado.
     * 
     * @param alumno alumno a consultar
     */
    void initData(Alumno alumno) {
        this.alumno = alumno;
        loadHorarioAlumnos();
    }
    
    /**
     * Carga los horarios del alumno
     */
    private void loadHorarioAlumnos() {
        horarioAlumnoDAO.loadHorariosAlumno(alumno);
        ObservableList<HorarioAlumno> horarioAlumno = horarioAlumnoDAO.getHorariosAlumno();
        String[] horasOrdenadas = new String[horarioAlumno.size()];
        // Ordenar horas de menor a mayor
        for (int i = 0; i < horarioAlumno.size(); i++) {
            horasOrdenadas[i] = horarioAlumno.get(i).getHorarioMateria().getHoraInicio().toString();
        }
        Arrays.sort(horasOrdenadas);
        ArrayList<String> horasConsideradas = new ArrayList<>();
        for (String hora : horasOrdenadas) {
            if (horasConsideradas.contains(hora)) {
                continue;
            }
            int contadorDia = 0;
            String[] materiaDia = new String[6];
            for (String day : WEEK_DAYS) {
                for (HorarioAlumno horario : horarioAlumno) {
                    if (horario.getHorarioMateria().getHoraInicio().toString().equals(hora)) {
                        if (horario.getHorarioMateria().getDia().equals(day)) {
                            materiaDia[contadorDia] = horario.getHorarioMateria().toString();
                            break;
                        }
                    }
                }
                contadorDia++;
            }
            observerHorariosTable.add(new HorarioSemana(
                materiaDia[0], materiaDia[1], materiaDia[2], 
                materiaDia[3], materiaDia[4], materiaDia[5]
            ));
            horasConsideradas.add(hora);
        }
        horarioTableView.setItems(observerHorariosTable);
    }

    public class HorarioSemana {
        private String lunes;
        private String martes;
        private String miercoles;
        private String jueves;
        private String viernes;
        private String sabado;

        public HorarioSemana(
            String lunes, String martes, String miercoles, String jueves, 
            String viernes, String sabado
        ) {
            this.lunes = lunes;
            this.martes = martes;
            this.miercoles = miercoles;
            this.jueves = jueves;
            this.viernes = viernes;
            this.sabado = sabado;
        }

        public String getLunes() {
            return lunes;
        }

        public String getMartes() {
            return martes;
        }

        public String getMiercoles() {
            return miercoles;
        }

        public String getJueves() {
            return jueves;
        }

        public String getViernes() {
            return viernes;
        }

        public String getSabado() {
            return sabado;
        }

        public String[] getSemana() {
            return new String[] { lunes, martes, miercoles, jueves, viernes, sabado };
        }
    }

}
