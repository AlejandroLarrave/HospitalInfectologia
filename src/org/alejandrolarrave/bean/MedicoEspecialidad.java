package org.alejandrolarrave.bean;

public class MedicoEspecialidad {
    private int codMedicoEspecialidad;
    private int Medicos_codMedico;
    private int Especialidades_codEspecialidad;
    private int Horarios_codHorario;

    public MedicoEspecialidad() {
    }

    public MedicoEspecialidad(int codMedicoEspecialidad, int Medicos_codMedico, int Especialidades_codEspecialidad, int Horarios_codHorario) {
        this.codMedicoEspecialidad = codMedicoEspecialidad;
        this.Medicos_codMedico = Medicos_codMedico;
        this.Especialidades_codEspecialidad = Especialidades_codEspecialidad;
        this.Horarios_codHorario = Horarios_codHorario;
    }

    public int getCodMedicoEspecialidad() {
        return codMedicoEspecialidad;
    }

    public void setCodMedicoEspecialidad(int codMedicoEspecialidad) {
        this.codMedicoEspecialidad = codMedicoEspecialidad;
    }

    public int getMedicos_codMedico() {
        return Medicos_codMedico;
    }

    public void setMedicos_codMedico(int Medicos_codMedico) {
        this.Medicos_codMedico = Medicos_codMedico;
    }

    public int getEspecialidades_codEspecialidad() {
        return Especialidades_codEspecialidad;
    }

    public void setEspecialidades_codEspecialidad(int Especialidades_codEspecialidad) {
        this.Especialidades_codEspecialidad = Especialidades_codEspecialidad;
    }

    public int getHorarios_codHorario() {
        return Horarios_codHorario;
    }

    public void setHorarios_codHorario(int Horarios_codHorario) {
        this.Horarios_codHorario = Horarios_codHorario;
    }
    
    public String toString(){
        return getCodMedicoEspecialidad()+" | "+"codMedico"+getMedicos_codMedico();
    }
    
}
