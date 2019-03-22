/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alumnos;

import alumnos.engine.SQL;
import alumnos.engine.SQLRow;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HorarioAlumnoDAO implements IHorarioAlumnoDAO {
    private ObservableList<HorarioAlumno> horariosAlumno;

    /**
     * Crea un objeto HorarioAlumnoDAO
     */
    public HorarioAlumnoDAO(){
        horariosAlumno = FXCollections.observableArrayList();
    }

    @Override
    public void loadHorariosAlumno(Alumno alumno) {
        horariosAlumno.clear();
        SQL.executeQuery("SELECT * FROM horarioAlumno WHERE matriculaAlumno = ?;", 
            new ArrayList<Object>(){
                { add(alumno.getMatricula()); }
            }, (result) -> {
                for (SQLRow row : result) {
                    horariosAlumno.add(
                        new HorarioAlumno(
                            (int) row.getColumnData("idhorarioAlumno"), 
                            (int) row.getColumnData("idhorarioMateria"), 
                            row.getColumnData("matriculaAlumno").toString()
                        )
                    );
                }
                return true;
            }
        );
    }

    @Override
    public ObservableList<HorarioAlumno> getHorariosAlumno() {
        return horariosAlumno;
    }

    @Override
    public HorarioAlumno insertHorarioAlumno(HorarioAlumno horarioAlumno) {
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(horarioAlumno.getHorarioMateria().getId());
        parameters.add(horarioAlumno.getAlumno().getMatricula());
        // Verificar si existe el horario
        if (horarioAlumno.getId() == 0) {
            // No existe el horario
            SQL.executeUpdate(
                "INSERT INTO horarioAlumno VALUES (NULL, ?, ?)", 
                parameters
            );
        }
        return horarioAlumno;
    }

    @Override
    public HorarioAlumno deleteHorarioAlumno(HorarioAlumno horarioAlumno) {
        SQL.executeUpdate(
            "DELETE FROM horarioAlumno WHERE idhorarioAlumno = ?", 
            new ArrayList<Object>() {{
                add(horarioAlumno.getId());
            }}
        );
        horariosAlumno.remove(horarioAlumno);
        return horarioAlumno;
    }
}
