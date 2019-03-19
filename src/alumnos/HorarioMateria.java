/**
 * HorarioMateria es la clase que lleva la relacion de un horario
 * con el de una materia.
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/03/14
 */

package alumnos;

import java.time.LocalTime;

public class HorarioMateria {
    private int id = 0;
    private Materia materia;
    private String salon;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String dia;

    /**
     * Crea un objeto HorarioMateria a partir de sus datos
     * 
     * @param id identificador del horario en la base de datos
     * @param idmateria identificador de la materia en la base de datos
     * @param salon salon en donde se imparte la materia
     * @param horaInicio hora de inicio de la clase
     * @param horaFin hora de fin de la clase
     * @param dia dia en que se imparte la materia
     */
    public HorarioMateria(
        int id, int idmateria, String salon, LocalTime horaInicio, 
        LocalTime horaFin, String dia
    ){
        this.id = id;
        materia = new Materia(idmateria);
        this.salon = salon;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.dia = dia;
    }

    /**
     * Establece un nuevo valor para la materia
     * 
     * @param materia materia
     */
    public void setMateria(Materia materia){
        this.materia = materia;
    }

    /**
     * Establece un nuevo valor para el salon
     * @param salon salon
     */
    public void setSalon(String salon){
        this.salon = salon;
    }
    
    /**
     * Establece un nuevo valor para la hora de inicio
     * 
     * @param horaInicio horaInicio
     */
    public void setHoraInicio(LocalTime horaInicio){
        this.horaInicio = horaInicio;
    }
    
    /**
     * Establece un nuevo valor para la hora de fin
     * 
     * @param horaFin horaFin
     */
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    /**
     * Establece un nuevo valor para el dia
     * 
     * @param dia dia
     */
    public void setDia(String dia) {
        this.dia = dia;
    }

    /**
     * Retorna el identificador del horario
     * 
     * @return identificador del horario
     */
    public int getId(){
        return id;
    }

    /**
     * Retorna la materia a la que está ligado el horario
     * 
     * @return materia perteneciente al horario
     */
    public Materia getMateria(){
        return materia;
    }

    /**
     * Retorna el salon donde se imparte la materia
     * 
     * @return salon donde se imparte la materia
     */
    public String getSalon(){
        return salon;
    }

    /**
     * Retorna la hora de inicio de la clase
     * 
     * @return hora de inicio de la clase
     */
    public LocalTime getHoraInicio(){
        return horaInicio;
    }

    /**
     * Retorna la hora de fin de la clase
     * 
     * @return hora de fin de la clase
     */
    public LocalTime getHoraFin(){
        return horaFin;
    }

    /**
     * Retorna el dia en que se imparte la materia
     * 
     * @return dia en que se imparte la materia
     */
    public String getDia(){
        return dia;
    }
}