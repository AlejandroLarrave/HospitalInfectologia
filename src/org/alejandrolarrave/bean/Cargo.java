package org.alejandrolarrave.bean;

public class Cargo {
    private int codCargo;
    private String nombreCargo;

    public Cargo() {
    }

    public Cargo(int codCargo, String nombreCargo) {
        this.codCargo = codCargo;
        this.nombreCargo = nombreCargo;
    }

    public int getCodCargo() {
        return codCargo;
    }

    public void setCodCargo(int codCargo) {
        this.codCargo = codCargo;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }
    
    public String toString(){
        return getCodCargo()+ " | "+ getNombreCargo();
    }
    
}
