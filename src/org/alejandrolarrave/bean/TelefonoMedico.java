package org.alejandrolarrave.bean;

public class TelefonoMedico {
    private int codTelefonoMedico;
    private String telefonoPersonal;
    private String telefonoTrabajo;
    private int Medicos_codMedico;

    public TelefonoMedico() {
    }

    public TelefonoMedico(int codTelefonoMedico, String telefonoPersonal, String telefonoTrabajo, int Medicos_codMedico) {
        this.codTelefonoMedico = codTelefonoMedico;
        this.telefonoPersonal = telefonoPersonal;
        this.telefonoTrabajo = telefonoTrabajo;
        this.Medicos_codMedico = Medicos_codMedico;
    }

    public int getCodTelefonoMedico() {
        return codTelefonoMedico;
    }

    public void setCodTelefonoMedico(int codTelefonoMedico) {
        this.codTelefonoMedico = codTelefonoMedico;
    }

    public String getTelefonoPersonal() {
        return telefonoPersonal;
    }

    public void setTelefonoPersonal(String telefonoPersonal) {
        this.telefonoPersonal = telefonoPersonal;
    }

    public String getTelefonoTrabajo() {
        return telefonoTrabajo;
    }

    public void setTelefonoTrabajo(String telefonoTrabajo) {
        this.telefonoTrabajo = telefonoTrabajo;
    }

    public int getMedicos_codMedico() {
        return Medicos_codMedico;
    }

    public void setMedicos_codMedico(int Medicos_codMedico) {
        this.Medicos_codMedico = Medicos_codMedico;
    }
    
    
}
