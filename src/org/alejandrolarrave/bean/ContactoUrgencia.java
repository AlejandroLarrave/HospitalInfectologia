package org.alejandrolarrave.bean;

public class ContactoUrgencia {
    private int codContactoUrgencia;
    private String nombres;
    private String apellidos;
    private String numeroContacto;
    private int Pacientes_codPaciente;

    public ContactoUrgencia() {
    }

    public ContactoUrgencia(int codContactoUrgencia, String nombres, String apellidos, String numeroContacto, int Pacientes_codPaciente) {
        this.codContactoUrgencia = codContactoUrgencia;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.numeroContacto = numeroContacto;
        this.Pacientes_codPaciente = Pacientes_codPaciente;
    }

    public ContactoUrgencia(int aInt, String string, String string0, int aInt0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getCodContactoUrgencia() {
        return codContactoUrgencia;
    }

    public void setCodContactoUrgencia(int codContactoUrgencia) {
        this.codContactoUrgencia = codContactoUrgencia;
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

    public String getNumeroContacto() {
        return numeroContacto;
    }

    public void setNumeroContacto(String numeroContacto) {
        this.numeroContacto = numeroContacto;
    }

    public int getPacientes_codPaciente() {
        return Pacientes_codPaciente;
    }

    public void setPacientes_codPaciente(int Pacientes_codPaciente) {
        this.Pacientes_codPaciente = Pacientes_codPaciente;
    }
    
    public String toString(){
        return getCodContactoUrgencia() + " | "+ getNombres()+" , " + getApellidos();
    }
    
}
