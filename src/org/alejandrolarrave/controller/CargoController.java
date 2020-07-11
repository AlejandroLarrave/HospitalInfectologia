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
import org.alejandrolarrave.bean.Cargo;
import org.alejandrolarrave.db.Conexion;
import org.alejandrolarrave.report.GenerarReporte;
import org.alejandrolarrave.sistema.Principal;

public class CargoController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, CANCELAR, ACTUALIZAR, NINGUNO}
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Cargo> listaCargo;
    @FXML private TextField txtNombre;
    @FXML private TableView tblCargos;
    @FXML private TableColumn colCodCargo;
    @FXML private TableColumn colNombre;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblCargos.setItems(getCargo());
        colCodCargo.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("codCargo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombreCargo"));
    }
    
    public ObservableList <Cargo> getCargo(){
        ArrayList<Cargo> lista = new ArrayList<Cargo>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCargos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Cargo(resultado.getInt("codCargo"),resultado.getString("nombreCargo")));                       
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaCargo = FXCollections.observableList(lista);
    }
            
    public void seleccionarElemento(){
        txtNombre.setText(String.valueOf(((Cargo)tblCargos.getSelectionModel().getSelectedItem()).getNombreCargo()));
    }
    
    public Cargo buscarCargo(int codCargo){
        Cargo resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("(call sp_BuscarCargos(?))");
            procedimiento.setInt(1, codCargo);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Cargo(registro.getInt("codCargo"),registro.getString("nombreCargo"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void eliminar(){
        switch (tipoDeOperaciones){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperaciones = operaciones.NUEVO;
                break;
            default:
                if(tblCargos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de Eliminarlo?", "Eliminar Cargo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarCargos(?)");
                            procedimiento.setInt(1, ((Cargo)tblCargos.getSelectionModel().getSelectedItem()).getCodCargo());
                            procedimiento.execute();
                            listaCargo.remove(tblCargos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showConfirmDialog(null, "Está seguro de eliminarlo");
            }
        }
    }
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if (tblCargos.getSelectionModel().getSelectedItem() != null){
                   btnEditar.setText("Actualizar");
                   btnReporte.setText("Reporte");
                   btnAgregar.setDisable(true);
                   btnEliminar.setDisable(true);
                   activarControles();
                   tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un cargo");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarCargos(?,?)");
            Cargo registro = (Cargo)tblCargos.getSelectionModel().getSelectedItem();
            registro.setNombreCargo(txtNombre.getText());
            procedimiento.setInt(1, registro.getCodCargo());
            procedimiento.setString(2, registro.getNombreCargo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
    
    public void nuevo(){
        switch (tipoDeOperaciones){
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperaciones = operaciones.GUARDAR;
                break;
            case GUARDAR:
                agregar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                cargarDatos();
                break;
        }
    }
    
    public void agregar(){
        Cargo registro = new Cargo();
        registro.setNombreCargo(txtNombre.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCargos(?)}");
            procedimiento.setString(1, registro.getNombreCargo());
            procedimiento.execute();
            listaCargo.add(registro);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
   
    public void imprimirReporte(){
        if(tblCargos.getSelectionModel().getSelectedItem()!= null){
            int codCargo = (((Cargo)tblCargos.getSelectionModel().getSelectedItem()).getCodCargo());
            Map parametros = new HashMap();
            parametros.put("p_codCargo", null);
            GenerarReporte.mostrarReporte("reporteCargosBuscar.jasper", "Reporte de Cargos", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public void desactivarControles(){
        txtNombre.setEditable(false);
    }
    
    public void activarControles(){
        txtNombre.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNombre.setText("");
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
