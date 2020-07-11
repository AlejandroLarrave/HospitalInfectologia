package org.alejandrolarrave.bean;

import java.sql.Time;

public class Medico {
    private int codMedico;
    private int licenciaMedica;
    private String nombres;
    private String apellidos;
    private Time horaEntrada;
    private Time horaSalida;
    private int turnoMaximo;
    private String sexo;

    public Medico() {
    }

    public Medico(int codMedico, int licenciaMedica, String nombres, String apellidos, Time horaEntrada, Time horaSalida, int turnoMaximo, String sexo) {
        this.codMedico = codMedico;
        this.licenciaMedica = licenciaMedica;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.turnoMaximo = turnoMaximo;
        this.sexo = sexo;
    }

    public int getCodMedico() {
        return codMedico;
    }

    public void setCodMedico(int codMedico) {
        this.codMedico = codMedico;
    }

    public int getLicenciaMedica() {
        return licenciaMedica;
    }

    public void setLicenciaMedica(int licenciaMedica) {
        this.licenciaMedica = licenciaMedica;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Time getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Time horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public Time getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Time horaSalida) {
        this.horaSalida = horaSalida;
    }

    public int getTurnoMaximo() {
        return turnoMaximo;
    }

    public void setTurnoMaximo(int turnoMaximo) {
        this.turnoMaximo = turnoMaximo;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    public String toString(){
        return getCodMedico() + " | " + getNombres() + ", " +getApellidos();
    }
}
