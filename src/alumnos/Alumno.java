/**
 * Alumno es la clase que posee los datos de un alumno.
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/02/22
 */

package alumnos;

import alumnos.engine.SQL;
import alumnos.engine.SQLRow;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

public class Alumno implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombreString;
    private String apellidoPaternoString;
    private String apellidoMaternoString;
    private String matriculaString;

    private transient SimpleStringProperty nombre;
    private transient SimpleStringProperty apellidoPaterno;
    private transient SimpleStringProperty apellidoMaterno;
    private transient SimpleStringProperty matricula;
    
    /**
     * Crea un objeto Alumno
     * @param nombre nombre del alumno
     * @param paterno apellido paterno del alumno
     * @param materno apellido materno del alumno
     * @param matricula matricula del alumno
     */
    public Alumno(String nombre, String paterno, String materno, String matricula){
        this.nombre = new SimpleStringProperty(nombre);
        this.apellidoPaterno = new SimpleStringProperty(paterno);
        this.apellidoMaterno = new SimpleStringProperty(materno);
        this.matricula = new SimpleStringProperty(matricula);
        nombreString = nombre;
        apellidoPaternoString = paterno;
        apellidoMaternoString = materno;
        matriculaString = matricula;
    }

    /**
     * Crea un objeto Alumno a partir de su matricula
     * 
     * @param matricula matricula
     */
    public Alumno(String matricula) {
        SQL.executeQuery(
            "SELECT * FROM alumno WHERE matricula = ?", 
            new ArrayList<Object>() {{
                add(matricula);
            }}, (result) -> {
                for (SQLRow row : result) {
                    nombreString = row.getColumnData("nombre").toString();
                    apellidoPaternoString = row.getColumnData("paterno").toString();
                    apellidoMaternoString = row.getColumnData("materno").toString();
                    matriculaString = row.getColumnData("matricula").toString();
                    nombre = new SimpleStringProperty(nombreString);
                    apellidoPaterno = new SimpleStringProperty(apellidoPaternoString);
                    apellidoMaterno = new SimpleStringProperty(apellidoMaternoString);
                    this.matricula = new SimpleStringProperty(matriculaString);
                }
                return true;
            }
        );
    }

    /**
     * Reestablece las propiedades y los valores
     * de los atributos del alumno.
     */
    public void resetStringProperties(){
        this.nombre = new SimpleStringProperty(nombreString);
        this.apellidoPaterno = new SimpleStringProperty(apellidoPaternoString);
        this.apellidoMaterno = new SimpleStringProperty(apellidoMaternoString);
        this.matricula = new SimpleStringProperty(matriculaString);
        nombre.set(nombreString);
        apellidoPaterno.set(apellidoPaternoString);
        apellidoMaterno.set(apellidoMaternoString);
        matricula.set(matriculaString);
    }

    /**
     * Establece un nuevo nombre al alumno.
     * @param nombre el nuevo nombre
     */
    public void setNombre(String nombre){
        this.nombre.set(nombre);
        nombreString = nombre;
    }

    /**
     * Establece un nuevo apellido paterno al alumno.
     * @param paterno el nuevo apellido paterno
     */
    public void setApellidoPaterno(String paterno){
        apellidoPaterno.set(paterno);
        apellidoPaternoString = paterno;
    }

    /**
     * Establece un nuevo apellido materno al alumno.
     * @param materno el nuevo apellido materno.
     */
    public void setApellidoMaterno(String materno){
        apellidoMaterno.set(materno);
        apellidoMaternoString = materno;
    }

    /**
     * Estaablece una nueva matricula al alumno,
     * @param matricula la nueva matricula
     */
    public void setMatricula(String matricula){
        this.matricula.set(matricula);
        matriculaString = matricula;
    }

    /**
     * Retorna el nombre del alumno.
     * @return el nombre del alumno
     */
    public String getNombre(){
        return nombre.get();
    }

    /**
     * Retorna el apellido paterno del alumno.
     * @return el apellido paterno del alumno
     */
    public String getApellidoPaterno(){
        return apellidoPaterno.get();
    }

    /**
     * Retorna el apellido materno del alumno.
     * @return el apellido materno del alumno
     */
    public String getApellidoMaterno(){
        return apellidoMaterno.get();
    }

    /**
     * Retorna la matricula del alumno.
     * @return la matricula del alumno
     */
    public String getMatricula(){
        return matricula.get();
    }
}
