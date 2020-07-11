package org.alejandrolarrave.sistema;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.alejandrolarrave.controller.AreaController;
import org.alejandrolarrave.controller.CargoController;
import org.alejandrolarrave.controller.ContactoUrgenciaController;
import org.alejandrolarrave.controller.EspecialidadesController;
import org.alejandrolarrave.controller.HorarioController;
import org.alejandrolarrave.controller.MedicoEspecialidadController;
import org.alejandrolarrave.controller.MedicosController;
import org.alejandrolarrave.controller.MenuPrincipalController;
import org.alejandrolarrave.controller.PacienteController;
import org.alejandrolarrave.controller.ProgramadorViewController;
import org.alejandrolarrave.controller.ResponsableTurnoController;
import org.alejandrolarrave.controller.TelefonoMedicoController;
import org.alejandrolarrave.controller.TurnoController;
import org.alejandrolarrave.db.Conexion;


public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/alejandrolarrave/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
   
    @Override
    public void start(Stage escenarioPrincipal) throws SQLException {
        /*Connection conn= Conexion.getInstancia().getConexion();
        Statement stmt = (Statement) conn.createStatement();
        ResultSet rs = (ResultSet) stmt.executeQuery("select * from Medicos"); 
        
        while (rs.next()){
            System.out.println(rs.getInt(1));
            System.out.println(rs.getString(2));
            System.out.println(rs.getString(3));
            System.out.println(rs.getString(4));
        }*/
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("Hospital de Infectología");
        menuPrincipal();
        escenarioPrincipal.show();
    }
    
    public void menuPrincipal(){
        try {
            MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 392,390);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public void ProgramadorView(){
        try {
            ProgramadorViewController ProgramadorView = (ProgramadorViewController)cambiarEscena("ProgramadorView.fxml", 456,300);
            ProgramadorView.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void MedicosView(){
        try {
            MedicosController MedicosView = (MedicosController) cambiarEscena("MedicosView.fxml", 600,400);
            MedicosView.setEscenarioPrincipal(this);
        }catch (Exception e){
        e.printStackTrace();
        }
    }
    
    public void ventanaArea(){
        try {
            AreaController areaController = (AreaController) cambiarEscena("AreasView.fxml",600,400);
            areaController.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaCargo(){
        try {
            CargoController cargoController = (CargoController) cambiarEscena("CargosView.fxml",600,400);
            cargoController.setEscenarioPrincipal(this);          
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaContactoUrgencia(){
        try {
            ContactoUrgenciaController contactoUrgenciaController = (ContactoUrgenciaController) cambiarEscena("ContactoUrgenciaView.fxml",600,400);
            contactoUrgenciaController.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaPaciente(){
        try {
            PacienteController pacienteController = (PacienteController) cambiarEscena("PacientesView.fxml",600,400);
            pacienteController.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTelefonoMedico(){
        try {
            TelefonoMedicoController telefonoMedicoController = (TelefonoMedicoController) cambiarEscena("TelefonosMedicoView.fxml",600,400);
            telefonoMedicoController.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTurno(){
        try {
            TurnoController turnoController = (TurnoController) cambiarEscena ("TurnoView.fxml",600,400);
            turnoController.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaHorario(){
        try{
            HorarioController horarioController = (HorarioController) cambiarEscena("HorariosView.fxml",600,400);
            horarioController.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaResponsableTurno(){
        try{
            ResponsableTurnoController responsableTurnoController = (ResponsableTurnoController) cambiarEscena("ResponsableTurnoView.fxml",600,400);
            responsableTurnoController.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEspecialidades(){
        try{
            EspecialidadesController especialidadesController = (EspecialidadesController) cambiarEscena("EspecialidadesView.fxml",600,400);
            especialidadesController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaMedicoEspecialidad(){
        try{
            MedicoEspecialidadController medicoEspecialidadController = (MedicoEspecialidadController) cambiarEscena ("MedicoEspecialidadView.fxml",600,400);
            medicoEspecialidadController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+ fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+ fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }
            
            
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
