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
import org.alejandrolarrave.bean.Especialidades;
import org.alejandrolarrave.db.Conexion;
import org.alejandrolarrave.report.GenerarReporte;
import org.alejandrolarrave.sistema.Principal;

public class EspecialidadesController implements Initializable{
    private enum operaciones{NUEVO,GUARDAR,EDITAR,ACTUALIZAR,ELIMINAR,CANCELAR,NINGUNO}
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Especialidades> listaEspecialidades;
    @FXML private TextField txtNombre;
    @FXML private TableView tblEspecialidades;
    @FXML private TableColumn colCodEspecialidad;
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
        tblEspecialidades.setItems(getEspecialidades());
        colCodEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidades, Integer>("codEspecialidad"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Especialidades, String> ("nombreEspecialidad"));
    }
    
    public ObservableList<Especialidades> getEspecialidades(){
        ArrayList<Especialidades> lista = new ArrayList<Especialidades>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEspecialidades}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Especialidades(resultado.getInt("codEspecialidad"),resultado.getString("nombreEspecialidad")));
            }           
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEspecialidades = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
        txtNombre.setText(String.valueOf(((Especialidades)tblEspecialidades.getSelectionModel().getSelectedItem()).getNombreEspecialidad()));
    }   
    
    public Especialidades buscarEspecialidades(int codEspecialidad){
        Especialidades resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("(call sp_BuscarEspecialidades(?))");
            procedimiento.setInt(1, codEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Especialidades(registro.getInt("codEspecialidad"),registro.getString("nombreEspecialidad"));
            }
        }catch(Exception e){
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
                if(tblEspecialidades.getSelectionModel().getSelectedItem()!= null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminarlo", "Eliminar Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarEspecialidades(?)");
                            procedimiento.setInt(1, ((Especialidades)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodEspecialidad());
                            procedimiento.execute();
                            listaEspecialidades.remove(tblEspecialidades.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }
    
    public void editar(){
        switch (tipoDeOperacion){
            case NINGUNO:
                if (tblEspecialidades.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Cancelar");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarEspecialidades(?,?)");
            Especialidades registro = (Especialidades)tblEspecialidades.getSelectionModel().getSelectedItem();
            registro.setNombreEspecialidad(txtNombre.getText());
            procedimiento.setInt(1, registro.getCodEspecialidad());
            procedimiento.setString(2, registro.getNombreEspecialidad());
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
                guardar();
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
    
    public void guardar(){
        Especialidades registro = new Especialidades();
        registro.setNombreEspecialidad(txtNombre.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEspecialidades(?)}");
            procedimiento.setString(1, registro.getNombreEspecialidad());
            procedimiento.execute();
            listaEspecialidades.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void imprimirReporte(){
        if(tblEspecialidades.getSelectionModel().getSelectedItem() !=null){
            int codEspecialidad = (((Especialidades)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodEspecialidad());
            Map parametros = new HashMap();
            parametros.put("p_codEspecialidad", null);
            GenerarReporte.mostrarReporte("reporteEspecialidadBuscar.jasper", "Reporte de Especialidades", parametros);
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
