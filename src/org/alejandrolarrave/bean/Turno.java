package org.alejandrolarrave.bean;

import java.util.Date;

public class Turno {
    private int codTurno;
    private Date fechaTurno;
    private Date fechaCita;
    private int valorCita;
    private int Medico_Especialidad_codMedicoEspecialidad;
    private int ResponsableTurno_codResponsableTurno;
    private int Pacientes_codPaciente;

    public Turno() {
        
    }

    public Turno(int codTurno, Date fechaTurno, Date fechaCita, int valorCita, int Medico_Especialidad_codMedicoEspecialidad, int ResponsableTurno_codResponsableTurno, int Pacientes_codPaciente) {
        this.codTurno = codTurno;
        this.fechaTurno = fechaTurno;
        this.fechaCita = fechaCita;
        this.valorCita = valorCita;
        this.Medico_Especialidad_codMedicoEspecialidad = Medico_Especialidad_codMedicoEspecialidad;
        this.ResponsableTurno_codResponsableTurno = ResponsableTurno_codResponsableTurno;
        this.Pacientes_codPaciente = Pacientes_codPaciente;
    }


    public int getCodTurno() {
        return codTurno;
    }

    public void setCodTurno(int codTurno) {
        this.codTurno = codTurno;
    }

    public Date getFechaTurno() {
        return fechaTurno;
    }

    public void setFechaTurno(Date fechaTurno) {
        this.fechaTurno = fechaTurno;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public int getValorCita() {
        return valorCita;
    }

    public void setValorCita(int valorCita) {
        this.valorCita = valorCita;
    }

    public int getMedico_Especialidad_codMedicoEspecialidad() {
        return Medico_Especialidad_codMedicoEspecialidad;
    }

    public void setMedico_Especialidad_codMedicoEspecialidad(int Medico_Especialidad_codMedicoEspecialidad) {
        this.Medico_Especialidad_codMedicoEspecialidad = Medico_Especialidad_codMedicoEspecialidad;
    }

    public int getResponsableTurno_codResponsableTurno() {
        return ResponsableTurno_codResponsableTurno;
    }

    public void setResponsableTurno_codResponsableTurno(int ResponsableTurno_codResponsableTurno) {
        this.ResponsableTurno_codResponsableTurno = ResponsableTurno_codResponsableTurno;
    }

    public int getPacientes_codPaciente() {
        return Pacientes_codPaciente;
    }

    public void setPacientes_codPaciente(int Pacientes_codPaciente) {
        this.Pacientes_codPaciente = Pacientes_codPaciente;
    }
    
    
}
