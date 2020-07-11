package org.alejandrolarrave.bean;

public class Area {
    private int codArea;
    private String nombreArea;

    public Area() {
    }

    public Area(int codArea, String nombreArea) {
        this.codArea = codArea;
        this.nombreArea = nombreArea;
    }

    public int getCodArea() {
        return codArea;
    }

    public void setCodArea(int codArea) {
        this.codArea = codArea;
    }

    public String getNombreArea() {
        return nombreArea;
    }

    public void setNombreArea(String nombreArea) {
        this.nombreArea = nombreArea;
    }
    
    public String toString(){
        return getCodArea() + " | " + getNombreArea();
    } 
}
