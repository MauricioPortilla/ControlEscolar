/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alumnos;

public class HorarioAlumno {
    private int id;
    private HorarioMateria horarioMateria;
    private Alumno alumno;

    /**
     * Crea un horario para un alumno
     * 
     * @param id id del horario
     * @param idHorarioMateria id del horario de la materia
     * @param matriculaAlumno matricula del alumno
     */
    public HorarioAlumno(int id, int idHorarioMateria, String matriculaAlumno) {
        this.id = id;
        horarioMateria = new HorarioMateria(idHorarioMateria);
        alumno = new Alumno(matriculaAlumno);
    }

    /**
     * Retorna el id del horario
     * 
     * @return el id del horario
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna el horario de la materia
     * 
     * @return el horario de la materia
     */
    public HorarioMateria getHorarioMateria() {
        return horarioMateria;
    }

    /**
     * Retorna el alumno
     * 
     * @return el alumno
     */
    public Alumno getAlumno() {
        return alumno;
    }
}
