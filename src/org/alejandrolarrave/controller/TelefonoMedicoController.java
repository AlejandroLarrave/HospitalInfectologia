package org.alejandrolarrave.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.alejandrolarrave.bean.Medico;
import org.alejandrolarrave.bean.TelefonoMedico;
import org.alejandrolarrave.db.Conexion;
import org.alejandrolarrave.report.GenerarReporte;
import org.alejandrolarrave.sistema.Principal;

public class TelefonoMedicoController implements Initializable {
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medico> listaMedico;
    private ObservableList<TelefonoMedico> listaTelefonoMedico;
    @FXML private ComboBox cmbCodMedico;
    @FXML private TextField txtTelefonoPersonal;
    @FXML private TextField txtTelefonoTrabajo;
    @FXML private TableView tblTelefonoMedico;
    @FXML private TableColumn colCodTelefonoMedico;
    @FXML private TableColumn colCodMedico;
    @FXML private TableColumn colTelefonoPersonal;
    @FXML private TableColumn colTelefonoTrabajo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodMedico.setItems(getMedicos());
    }
    
    public void cargarDatos(){
        tblTelefonoMedico.setItems(getTelefonoMedicos());
        colCodTelefonoMedico.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, Integer>("codTelefonoMedico"));
        colCodMedico.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, Integer>("Medicos_codMedico"));
        colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, String>("telefonoPersonal"));
        colTelefonoTrabajo.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, String>("telefonoTrabajo"));
    }
    
    public ObservableList<TelefonoMedico>getTelefonoMedicos(){
        ArrayList<TelefonoMedico> lista = new ArrayList<TelefonoMedico>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarTelefonosMedicos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new TelefonoMedico(resultado.getInt("codTelefonoMedico"),
                                             resultado.getString("telefonoPersonal"),
                                             resultado.getString("telefonoTrabajo"),
                                             resultado.getInt("Medicos_codMedico")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaTelefonoMedico = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Medico>getMedicos(){
              ArrayList<Medico> lista = new ArrayList<Medico>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedicos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Medico(resultado.getInt("codMedico"),resultado.getInt("licenciaMedica"),
                        resultado.getString("nombres"),resultado.getString("apellidos"),
                        resultado.getTime("horaEntrada"),resultado.getTime("horaSalida"),
                        resultado.getInt("turnoMaximo"),resultado.getString("sexo")));
            }            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaMedico = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
        cmbCodMedico.getSelectionModel().select(buscarMedico(((TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getMedicos_codMedico()));
        txtTelefonoPersonal.setText(String.valueOf(((TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getTelefonoPersonal()));
        txtTelefonoTrabajo.setText(String.valueOf(((TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getTelefonoTrabajo()));
    } 
    
    public Medico buscarMedico(int codigoMedico){
        Medico resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedicos(?)}");
            procedimiento.setInt(1, codigoMedico);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Medico (registro.getInt("codMedico"),
                        registro.getInt("licenciaMedica"),registro.getString("nombres"),
                        registro.getString("apellidos"),registro.getTime("horaEntrada"),
                        registro.getTime("horaSalida"),registro.getInt("turnoMaximo"),
                        registro.getString("sexo"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public TelefonoMedico buscarTelefonoMedico(int codigoTelefonoMedico){
        TelefonoMedico resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("(call sp_BuscarTelefonosMedicos(?))");
            procedimiento.setInt(1, codigoTelefonoMedico);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new TelefonoMedico (registro.getInt("codTelefonoMedico"),
                                                registro.getString("telefonoPersonal"),
                                                registro.getString("telefonoTrabajo"),
                                                registro.getInt("codMedico"));
            } 
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NUEVO;
                break;
            default:
                if (tblTelefonoMedico.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de Eliminarlo", "Eliminar Teléfono", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonosMedicos(?)}");
                            procedimiento.setInt(1, ((TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getCodTelefonoMedico());
                            procedimiento.execute();
                            listaTelefonoMedico.remove(tblTelefonoMedico.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }    
        }
    }
    
    public void editar(){
        switch (tipoDeOperacion){
            case NINGUNO:
                if(tblTelefonoMedico.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable (true);
                    btnEliminar.setDisable (true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Teléfono");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarTelefonosMedicos(?,?,?,?)");
            TelefonoMedico registro = (TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem();
            registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
            registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
            registro.setMedicos_codMedico(((Medico)cmbCodMedico.getSelectionModel().getSelectedItem()).getCodMedico());
            procedimiento.setInt(1, registro.getCodTelefonoMedico());
            procedimiento.setString(2, registro.getTelefonoPersonal());
            procedimiento.setString(3, registro.getTelefonoTrabajo());
            procedimiento.setInt(4, registro.getMedicos_codMedico());
            procedimiento.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText ("Cancelar");
                btnEditar.setDisable (true);
                btnReporte.setDisable (true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                agregar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Nuevo");
                btnEliminar.setText ("Eliminar");
                btnEditar.setDisable (false);
                btnReporte.setDisable (false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
        }
    }
    
    public void agregar(){
        TelefonoMedico registro = new TelefonoMedico();
        registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
        registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
        registro.setMedicos_codMedico(((Medico)cmbCodMedico.getSelectionModel().getSelectedItem()).getCodMedico());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonosMedicos(?,?,?)}");
            procedimiento.setString(1, registro.getTelefonoPersonal());
            procedimiento.setString(2, registro.getTelefonoTrabajo());
            procedimiento.setInt(3, registro.getMedicos_codMedico());
            procedimiento.execute();
            listaTelefonoMedico.add(registro);
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void desactivarControles(){
        txtTelefonoPersonal.setEditable(false);
        txtTelefonoTrabajo.setEditable(false);
        cmbCodMedico.setDisable(true);
    }
    
    public void activarControles(){
        txtTelefonoPersonal.setEditable(true);
        txtTelefonoTrabajo.setEditable(true);
        cmbCodMedico.setDisable(false);
    }
    
    public void limpiarControles(){
        txtTelefonoPersonal.setText("");
        txtTelefonoTrabajo.setText("");
        cmbCodMedico.getSelectionModel().clearSelection();
    }
    
    public void imprimirReporte(){
        if (tblTelefonoMedico.getSelectionModel().getSelectedItem() != null){
            int codTelefonoMedico = (((TelefonoMedico)tblTelefonoMedico.getSelectionModel().getSelectedItem()).getCodTelefonoMedico());
            Map parametros = new HashMap();
            parametros.put("p_codTelefonoMedico", null);
            GenerarReporte.mostrarReporte("reporteTelefonoMedicoBuscar.jasper", "Reporte de Teléfonos Médico", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
