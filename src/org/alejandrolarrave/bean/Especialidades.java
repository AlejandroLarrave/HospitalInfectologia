package org.alejandrolarrave.bean;

public class Especialidades {
    private int codEspecialidad;
    private String nombreEspecialidad;

    public Especialidades() {
    }

    public Especialidades(int codEspecialidad, String nombreEspecialidad) {
        this.codEspecialidad = codEspecialidad;
        this.nombreEspecialidad = nombreEspecialidad;
    }

    public int getCodEspecialidad() {
        return codEspecialidad;
    }

    public void setCodEspecialidad(int codEspecialidad) {
        this.codEspecialidad = codEspecialidad;
    }

    public String getNombreEspecialidad() {
        return nombreEspecialidad;
    }

    public void setNombreEspecialidad(String nombreEspecialidad) {
        this.nombreEspecialidad = nombreEspecialidad;
    }
    
    public String toString(){
        return getCodEspecialidad()+ " | "+getNombreEspecialidad();
    }
    
}
