/**
 * AlumnoDAO es la clase que implementa la interfaz
 * IAlumnoDAO y lleva a cabo el control de CRUD de los
 * alumnos en la base de datos.
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/03/11
 */
package alumnos;

import alumnos.engine.SQL;
import alumnos.engine.SQLRow;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import javafx.collections.FXCollections;

public class AlumnoDAO implements IAlumnoDAO {
    private ObservableList<Alumno> alumnos;

    public AlumnoDAO(){
        alumnos = FXCollections.observableArrayList();
    }

    @Override
    public void loadAlumnos(){
        alumnos.clear();
        SQL.executeQuery("SELECT * FROM alumno;", (result) -> {
            for(SQLRow row : result){
                alumnos.add(new Alumno(
                    row.getColumnData("nombre").toString(),
                    row.getColumnData("paterno").toString(),
                    row.getColumnData("materno").toString(),
                    row.getColumnData("matricula").toString()
                ));
            }
            return true;
        });
    }

    @Override
    public ObservableList<Alumno> getAlumnos(){
        return alumnos;
    }

    @Override
    public Alumno getAlumno(String matricula){
        for(Alumno alumno : alumnos){
            if(alumno.getMatricula().equals(matricula)){
                return alumno;
            }
        }
        return null;
    }

    @Override
    public Alumno insertAlumno(Alumno alumno){
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(alumno.getMatricula());
        parameters.add(alumno.getNombre());
        parameters.add(alumno.getApellidoPaterno());
        parameters.add(alumno.getApellidoMaterno());
        // Verificar si existe el alumno
        ArrayList<SQLRow> alumnoData = SQL.executeQuery(
            "SELECT matricula FROM alumno WHERE matricula = ?",
            new ArrayList<Object>(){{
                add(alumno.getMatricula());
            }}, (result) -> {
                // Existe el alumno
                return true;
            }
        );
        if(alumnoData.isEmpty()){
            // No existe el alumno
            SQL.executeUpdate(
                "INSERT INTO alumno VALUES (?, ?, ?, ?)", parameters
            );
        }
        return alumno;
    }

    @Override
    public Alumno updateAlumno(Alumno alumno){
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(alumno.getMatricula());
        parameters.add(alumno.getNombre());
        parameters.add(alumno.getApellidoPaterno());
        parameters.add(alumno.getApellidoMaterno());
        parameters.add(alumno.getMatricula());
        // Verificar si existe el alumno
        ArrayList<SQLRow> alumnoData = SQL.executeQuery(
            "SELECT matricula FROM alumno WHERE matricula = ?",
            new ArrayList<Object>(){{
                add(alumno.getMatricula());
            }}, (result) -> {
                // Existe el alumno
                SQL.executeUpdate(
                    "UPDATE alumno SET matricula = ?, nombre = ?, paterno = ?, "+
                    "materno = ? WHERE matricula = ?", 
                    parameters
                );
                return true;
            }
        );
        return alumno;
    }

    @Override
    public Alumno deleteAlumno(Alumno alumno){
        SQL.executeUpdate(
            "DELETE FROM alumno WHERE matricula = ?", 
            new ArrayList<Object>(){{
                add(alumno.getMatricula());
            }}
        );
        alumnos.remove(alumno);
        return alumno;
    }
}
