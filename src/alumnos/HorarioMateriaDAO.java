/**
 * HorarioMateriaDAO es la clase que implementa la interfaz
 * IHorarioMateriaDAO y lleva a cabo el control de CRUD de los
 * horarios de las materias en la base de datos.
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/03/16
 */
package alumnos;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;

import alumnos.engine.SQL;
import alumnos.engine.SQLRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HorarioMateriaDAO implements IHorarioMateriaDAO {
    private ObservableList<HorarioMateria> horariosMaterias;

    /**
     * Crea un objeto HorarioMateriaDAO
     */
    public HorarioMateriaDAO(){
        horariosMaterias = FXCollections.observableArrayList();
    }

    @Override
    public void loadHorariosMaterias() {
        horariosMaterias.clear();
        SQL.executeQuery("SELECT * FROM horarioMateria;", (result) -> {
            for (SQLRow row : result) {
                horariosMaterias.add(
                    new HorarioMateria(
                        (int)row.getColumnData("idhorario"), 
                        (int)row.getColumnData("idmateria"), 
                        row.getColumnData("salon").toString(), 
                        ((Time)row.getColumnData("horaInicio")).toLocalTime(), 
                        ((Time)row.getColumnData("horaFin")).toLocalTime(), 
                        row.getColumnData("dia").toString()
                    )
                );
            }
            return true;
        });
    }

    @Override
    public ObservableList<HorarioMateria> getHorariosMaterias() {
        return horariosMaterias;
    }

    @Override
    public HorarioMateria insertHorarioMateria(HorarioMateria horarioMateria) {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(horarioMateria.getMateria().getId());
        parameters.add(horarioMateria.getSalon());
        parameters.add(horarioMateria.getHoraInicio());
        parameters.add(horarioMateria.getHoraFin());
        parameters.add(horarioMateria.getDia());
        // Verificar si existe el horario
        if (horarioMateria.getId() == 0) {
            // No existe el horario
            SQL.executeUpdate(
                "INSERT INTO horarioMateria VALUES (NULL, ?, ?, ?, ?, ?)", 
                parameters
            );
        }
        return horarioMateria;
    }

    @Override
    public HorarioMateria updateHorarioMateria(HorarioMateria horarioMateria) {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(horarioMateria.getMateria().getId());
        parameters.add(horarioMateria.getSalon());
        parameters.add(horarioMateria.getHoraInicio());
        parameters.add(horarioMateria.getHoraFin());
        parameters.add(horarioMateria.getDia());
        parameters.add(horarioMateria.getId());
        // Verificar si existe el horario
        SQL.executeQuery(
            "SELECT idhorario FROM horarioMateria WHERE idhorario = ?", 
            new ArrayList<Object>() {{
                add(horarioMateria.getId());
            }}, (result) -> {
                // Existe el horario
                SQL.executeUpdate(
                    "UPDATE horarioMateria SET idmateria = ?, salon = ?, " + 
                    "horaInicio = ?, horaFin = ?, dia = ? WHERE idhorario = ?", 
                    parameters
                );
                return true;
            }
        );
        return horarioMateria;
    }

    @Override
    public HorarioMateria deleteHorarioMateria(HorarioMateria horarioMateria) {
        SQL.executeUpdate(
            "DELETE FROM horarioMateria WHERE idhorario = ?", 
            new ArrayList<Object>() {{
                add(horarioMateria.getId());
            }}
        );
        horariosMaterias.remove(horarioMateria);
        return horarioMateria;
    }
}
