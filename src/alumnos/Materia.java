/**
 * Materia es la clase que posee los datos de una materia.
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/03/14
 */

package alumnos;

import java.util.ArrayList;

import alumnos.engine.SQL;
import alumnos.engine.SQLRow;

public class Materia {
    private int id = 0;
    private int nrc;
    private String nombre;
    private int creditos;
    private int horasTeoricas;
    private int horasPracticas;
    
    /**
     * Crea un objeto Materia a partir de sus datos.
     * @param id identificador de la materia en la base de datos
     * @param nrc NRC de la materia
     * @param nombre nombre de la materia
     * @param creditos creditos de la materia
     * @param horasTeoricas horas teoricas de la materia
     * @param horasPracticas horas practicas de la materia
     */
    public Materia(
        int id, int nrc, String nombre, int creditos, 
        int horasTeoricas, int horasPracticas
    ){
        this.id = id;
        this.nrc = nrc;
        this.nombre = nombre;
        this.creditos = creditos;
        this.horasTeoricas = horasTeoricas;
        this.horasPracticas = horasPracticas;
    }

    /**
     * Crea un objeto Materia a partir de su identificador.
     * @param id identificador de la materia en la base de datos
     */
    public Materia(int id){
        SQL.executeQuery(
            "SELECT * FROM materia WHERE idmateria = ?", 
            new ArrayList<Object>(){{
                add(id);
            }}, (result) -> {
                for(SQLRow row : result){
                    this.id = (int)row.getColumnData("idmateria");
                    nrc = (int)row.getColumnData("nrc");
                    nombre = row.getColumnData("nombre").toString();
                    creditos = (int)row.getColumnData("creditos");
                    horasTeoricas = (int)row.getColumnData("horasTeoricas");
                    horasPracticas = (int)row.getColumnData("horasPracticas");
                }
                return true;
            }
        );
    }

    /**
     * Establece el NRC de la materia
     * @param nrc nuevo NRC de la materia
     */
    public void setNrc(int nrc){
        this.nrc = nrc;
    }

    /**
     * Establece el nombre de la materia
     * @param nombre nuevo nombre de la materia
     */
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    /**
     * Establece los creditos de la materia
     * @param creditos nuevos creditos de la materia
     */
    public void setCreditos(int creditos){
        this.creditos = creditos;
    }

    /**
     * Establece las horas teoricas de la materia
     * @param horasTeoricas nuevas horas teoricas de la materia
     */
    public void setHorasTeoricas(int horasTeoricas){
        this.horasTeoricas = horasTeoricas;
    }

    /**
     * Establece las horas practicas de la materia
     * @param horasPracticas nuevas horas practicas de la materia
     */
    public void setHorasPracticas(int horasPracticas){
        this.horasPracticas = horasPracticas;
    }

    /**
     * Retorna el identificador de la materia
     * @return identificador de la materia
     */
    public int getId(){
        return id;
    }

    /**
     * Retorna el NRC de la materia
     * @return NRC de la materia
     */
    public int getNrc(){
        return nrc;
    }

    /**
     * Retorna el nombre de la materia
     * @return nombre de la materia
     */
    public String getNombre(){
        return nombre;
    }

    /**
     * Retorna los creditos de la materia
     * @return creditos de la materia
     */
    public int getCreditos(){
        return creditos;
    }

    /**
     * Retorna las horas teoricas de la materia
     * @return horas teoricas de la materia
     */
    public int getHorasTeoricas(){
        return horasTeoricas;
    }

    /**
     * Retorna las horas practicas de la materia
     * @return horas practicas de la materia
     */
    public int getHorasPracticas(){
        return horasPracticas;
    }

    @Override
    public String toString(){
        return nombre + "("+ nrc +")";
    }
}
