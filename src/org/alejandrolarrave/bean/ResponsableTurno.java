package org.alejandrolarrave.bean;

public class ResponsableTurno {
    private int codResponsableTurno;
    private String nombreResponsable;
    private String apellidosResponsable;
    private String telefonoPersonal;
    private int Areas_codArea;
    private int Cargos_codCargo;

    public ResponsableTurno() {
    }

    public ResponsableTurno(int codResponsableTurno, String nombreResponsable, String apellidosResponsable, String telefonoPersonal, int Areas_codArea, int Cargos_codCargo) {
        this.codResponsableTurno = codResponsableTurno;
        this.nombreResponsable = nombreResponsable;
        this.apellidosResponsable = apellidosResponsable;
        this.telefonoPersonal = telefonoPersonal;
        this.Areas_codArea = Areas_codArea;
        this.Cargos_codCargo = Cargos_codCargo;
    }

    public int getCodResponsableTurno() {
        return codResponsableTurno;
    }

    public void setCodResponsableTurno(int codResponsableTurno) {
        this.codResponsableTurno = codResponsableTurno;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public String getApellidosResponsable() {
        return apellidosResponsable;
    }

    public void setApellidosResponsable(String apellidosResponsable) {
        this.apellidosResponsable = apellidosResponsable;
    }

    public String getTelefonoPersonal() {
        return telefonoPersonal;
    }

    public void setTelefonoPersonal(String telefonoPersonal) {
        this.telefonoPersonal = telefonoPersonal;
    }

    public int getAreas_codArea() {
        return Areas_codArea;
    }

    public void setAreas_codArea(int Areas_codArea) {
        this.Areas_codArea = Areas_codArea;
    }

    public int getCargos_codCargo() {
        return Cargos_codCargo;
    }

    public void setCargos_codCargo(int Cargos_codCargo) {
        this.Cargos_codCargo = Cargos_codCargo;
    }
    public String toString(){
        return getCodResponsableTurno()+" | "+getNombreResponsable()+" , "+getApellidosResponsable();
    }
    
}
