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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.alejandrolarrave.bean.Area;
import org.alejandrolarrave.db.Conexion;
import org.alejandrolarrave.report.GenerarReporte;
import org.alejandrolarrave.sistema.Principal;


public class AreaController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Area> listaArea;
    @FXML private TextField txtNombre;
    @FXML private TableView tblArea;
    @FXML private TableColumn colCodArea;
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
        tblArea.setItems(getArea());
        colCodArea.setCellValueFactory(new PropertyValueFactory<Area, Integer>("codArea"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Area, String>("nombreArea"));
    }
    
    public ObservableList <Area> getArea(){
        ArrayList<Area> lista = new ArrayList<Area>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarAreas}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Area(resultado.getInt("codArea"),resultado.getString("nombreArea")));                       
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaArea = FXCollections.observableList(lista);
    }
            
    public void seleccionElemento(){
        txtNombre.setText(String.valueOf(((Area)tblArea.getSelectionModel().getSelectedItem()).getNombreArea()));
    }
    
    public Area buscarArea(int codArea){
        Area resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("(call sp_BuscarAreas(?))");
            procedimiento.setInt(1, codArea);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Area(registro.getInt("codArea"),registro.getString("nombreArea"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }    
    
    public void eliminar(){
        switch (tipoDeOperacion){
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
                if(tblArea.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de Eliminarlo?", "Eliminar Teléfono", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarAreas(?)");
                            procedimiento.setInt(1, ((Area)tblArea.getSelectionModel().getSelectedItem()).getCodArea());
                            procedimiento.execute();
                            listaArea.remove(tblArea.getSelectionModel().getSelectedIndex());
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
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblArea.getSelectionModel().getSelectedItem() != null){
                   btnEditar.setText("Actualizar");
                   btnReporte.setText("Reporte");
                   btnAgregar.setDisable(true);
                   btnEliminar.setDisable(true);
                   activarControles();
                   tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un área");
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarAreas(?,?)");
            Area registro = (Area)tblArea.getSelectionModel().getSelectedItem();
            registro.setNombreArea(txtNombre.getText());
            procedimiento.setInt(1, registro.getCodArea());
            procedimiento.setString(2, registro.getNombreArea());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnAgregar.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                agregar();
                desactivarControles();
                limpiarControles();
                btnAgregar.setText("Agregar");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
        }
    }
    
    public void agregar(){
        Area registro = new Area();
        registro.setNombreArea(txtNombre.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarAreas(?)}");
            procedimiento.setString(1, registro.getNombreArea());
            procedimiento.execute();
            listaArea.add(registro);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void imprimirReporte(){
        if(tblArea.getSelectionModel().getSelectedItem()!= null){
            int codArea = (((Area)tblArea.getSelectionModel().getSelectedItem()).getCodArea());
            Map parametros = new HashMap();
            parametros.put("p_codArea", null);
            GenerarReporte.mostrarReporte("reporteAreasBuscar.jasper", "Reporte de Areas", parametros);
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
