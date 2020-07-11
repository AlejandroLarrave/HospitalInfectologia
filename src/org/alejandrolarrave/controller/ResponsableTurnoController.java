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
import org.alejandrolarrave.bean.Area;
import org.alejandrolarrave.bean.Cargo;
import org.alejandrolarrave.bean.ResponsableTurno;
import org.alejandrolarrave.db.Conexion;
import org.alejandrolarrave.report.GenerarReporte;
import org.alejandrolarrave.sistema.Principal;

public class ResponsableTurnoController implements Initializable {
    private enum operaciones {NUEVO,GUARDAR,EDITAR,ACTUALIZAR,ELIMINAR,CANCELAR,NINGUNO}
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Area> listaArea;
    private ObservableList<Cargo> listaCargo;
    private ObservableList<ResponsableTurno> listaResponsableTurno;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox cmbCodCargo;
    @FXML private ComboBox cmbCodArea;
    @FXML private TableView tblResponsableTurno;
    @FXML private TableColumn colCodResponsable;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colCodCargo;
    @FXML private TableColumn colCodArea;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodCargo.setItems(getCargo());
        cmbCodArea.setItems(getArea());
    }

    public void cargarDatos(){
        tblResponsableTurno.setItems(getResponsableTurno());
        colCodResponsable.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codResponsableTurno"));
        colNombres.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("nombreResponsable"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String> ("apellidosResponsable"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String> ("telefonoPersonal"));
        colCodCargo.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer> ("Cargos_codCargo"));
        colCodArea.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer> ("Areas_codArea"));
    }
    
    public ObservableList<ResponsableTurno> getResponsableTurno(){
        ArrayList<ResponsableTurno> lista = new ArrayList<ResponsableTurno>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarResponsableTurno}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new ResponsableTurno(resultado.getInt("codResponsableTurno"),resultado.getString("nombreResponsable"),
                                               resultado.getString("apellidosResponsable"),resultado.getString("telefonoPersonal"),
                                               resultado.getInt("Areas_codArea"),resultado.getInt("Cargos_codCargo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaResponsableTurno = FXCollections.observableList(lista);
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
    
    public void SeleccionarElemento(){
        txtNombres.setText(String.valueOf(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getNombreResponsable()));
        txtApellidos.setText(String.valueOf(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getApellidosResponsable()));
        txtTelefono.setText(String.valueOf(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getTelefonoPersonal()));
        cmbCodArea.getSelectionModel().select(buscarArea(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getAreas_codArea()));
        cmbCodCargo.getSelectionModel().select(buscarCargo(((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCargos_codCargo()));
    }
    
    public ResponsableTurno buscarResponsableTurno(int codResponsableTurno){
        ResponsableTurno resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("(call sp_BuscarResponsableTurno(?))");
            procedimiento.setInt(1, codResponsableTurno);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new ResponsableTurno(registro.getInt("codResponsableTurno"),registro.getString("nombreResponsable"),registro.getString("apellidosResponsable"),
                                                 registro.getString("telefonoPersonal"),registro.getInt("Areas_codArea"),registro.getInt("Cargos_codCargo"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
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
                if(tblResponsableTurno.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminarlo", "Eliminar ResponsableTurno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarResponsableTurno(?)");
                            procedimiento.setInt(1, ((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodResponsableTurno());
                            procedimiento.execute();
                            listaResponsableTurno.remove(tblResponsableTurno.getSelectionModel().getSelectedIndex());
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
                if (tblResponsableTurno.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Responsable del turno");
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarResponsableTurno(?,?,?,?,?,?)");
            ResponsableTurno registro = (ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem();
            registro.setNombreResponsable(txtNombres.getText());
            registro.setApellidosResponsable(txtApellidos.getText());
            registro.setTelefonoPersonal(txtTelefono.getText());
            registro.setAreas_codArea(((Area)cmbCodArea.getSelectionModel().getSelectedItem()).getCodArea());
            registro.setCargos_codCargo(((Cargo)cmbCodCargo.getSelectionModel().getSelectedItem()).getCodCargo());
            procedimiento.setInt(1, registro.getCodResponsableTurno());
            procedimiento.setString(2, registro.getNombreResponsable());
            procedimiento.setString(3, registro.getApellidosResponsable());
            procedimiento.setString(4, registro.getTelefonoPersonal());
            procedimiento.setInt(5, registro.getAreas_codArea());
            procedimiento.setInt(6, registro.getCargos_codCargo());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
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
        ResponsableTurno registro = new ResponsableTurno();
        registro.setNombreResponsable(txtNombres.getText());
        registro.setApellidosResponsable(txtApellidos.getText());
        registro.setTelefonoPersonal(txtTelefono.getText());
        registro.setAreas_codArea(((Area)cmbCodArea.getSelectionModel().getSelectedItem()).getCodArea());
        registro.setCargos_codCargo(((Cargo)cmbCodCargo.getSelectionModel().getSelectedItem()).getCodCargo());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarResponsableTurno(?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreResponsable());
            procedimiento.setString(2, registro.getApellidosResponsable());
            procedimiento.setString(3, registro.getTelefonoPersonal());
            procedimiento.setInt(4, registro.getAreas_codArea());
            procedimiento.setInt(5, registro.getCargos_codCargo());
            procedimiento.execute();
            listaResponsableTurno.add(registro);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void imprimirReporte(){
        if(tblResponsableTurno.getSelectionModel().getSelectedItem()!= null){
            int codResponsableTurno = (((ResponsableTurno)tblResponsableTurno.getSelectionModel().getSelectedItem()).getCodResponsableTurno());
            Map parametros = new HashMap();
            parametros.put("p_codResponsableTurno", null);
            GenerarReporte.mostrarReporte("reporteResponsableTurnoBuscar.jasper", "Reporte de Responsable Turno", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public void desactivarControles(){
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtTelefono.setEditable(false);
        cmbCodCargo.setDisable(true);
        cmbCodArea.setDisable(true);
    }
    
    public void activarControles(){
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtTelefono.setEditable(true);
        cmbCodCargo.setDisable(false);
        cmbCodArea.setDisable(false);
    }
    
    public void limpiarControles(){
        txtNombres.setText("");
        txtApellidos.setText("");
        txtTelefono.setText("");
        cmbCodCargo.getSelectionModel().clearSelection();
        cmbCodArea.getSelectionModel().clearSelection();
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
