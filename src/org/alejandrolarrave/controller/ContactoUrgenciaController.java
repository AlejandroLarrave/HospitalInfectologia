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
import org.alejandrolarrave.bean.ContactoUrgencia;
import org.alejandrolarrave.bean.Paciente;
import org.alejandrolarrave.db.Conexion;
import org.alejandrolarrave.report.GenerarReporte;
import org.alejandrolarrave.sistema.Principal;


public class ContactoUrgenciaController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR,CANCELAR, NINGUNO}
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Paciente> listaPaciente;
    private ObservableList<ContactoUrgencia> listaContactoUrgencia;
    @FXML private ComboBox cmbCodPaciente;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtNumeroContacto;
    @FXML private TableView tblContactoUrgencia;
    @FXML private TableColumn colCodContactoUrgencia;
    @FXML private TableColumn colCodPaciente;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colNumeroContacto;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodPaciente.setItems(getPaciente());
    }
    
    public void cargarDatos(){
        tblContactoUrgencia.setItems(getContactoUrgencia());
        colCodContactoUrgencia.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("codContactoUrgencia"));
        colCodPaciente.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("Pacientes_codPaciente"));
        colNombres.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String> ("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String> ("apellidos"));
        colNumeroContacto.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String> ("numeroContacto"));              
    }
    
    public ObservableList<ContactoUrgencia> getContactoUrgencia(){
        ArrayList <ContactoUrgencia> lista = new ArrayList<ContactoUrgencia>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarContactoUrgencia");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new ContactoUrgencia(resultado.getInt("codContactoUrgencia"),resultado.getString("nombres"),
                                               resultado.getString("apellidos"),resultado.getString("numeroContacto"),
                                               resultado.getInt("Pacientes_codPaciente")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }return listaContactoUrgencia = FXCollections.observableList(lista);
    }
    
    public ObservableList<Paciente> getPaciente(){
    ArrayList <Paciente> lista = new ArrayList<Paciente>();
    try{
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarPacientes}");
        ResultSet resultado = procedimiento.executeQuery();
        while (resultado.next()){
            lista.add(new Paciente(resultado.getInt("codPaciente"),resultado.getString("DPI"),resultado.getString("apellidos"),
                                   resultado.getString("nombres"),resultado.getDate("fechaNacimiento"),resultado.getInt("edad"),
                                   resultado.getString("direccion"),resultado.getString("ocupacion"),resultado.getString("sexo")));
                        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaPaciente = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
        txtNombres.setText(String.valueOf(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getNombres()));
        txtApellidos.setText(String.valueOf(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getApellidos()));
        txtNumeroContacto.setText(String.valueOf(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getNumeroContacto()));
        cmbCodPaciente.getSelectionModel().select(buscarPaciente(((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getPacientes_codPaciente()));
    }
    
    public ContactoUrgencia buscarContactoUrgencia(int codigoContactoUrgencia){
        ContactoUrgencia resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("(call sp_BuscarContactoUrgencia(?))");
            procedimiento.setInt(1, codigoContactoUrgencia);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new ContactoUrgencia(registro.getInt("codContactoUrgencia"),
                                                  registro.getString("nombres"),
                                                  registro.getString("apellidos"),
                                                  registro.getInt("Pacientes_codPaciente"));
            }   
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Paciente buscarPaciente(int codPaciente){
        Paciente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("(call sp_BuscarPacientes(?))");
            procedimiento.setInt(1, codPaciente);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Paciente(registro.getInt("codPaciente"),registro.getString("DPI"),registro.getString("apellidos"),
                                        registro.getString("nombres"),registro.getDate("fechaNacimiento"),registro.getInt("edad"),registro.getString("direccion"),
                                        registro.getString("ocupacion"),registro.getString("sexo"));
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
                if (tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminarlo", "Eliminar contacto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarContactoUrgencia(?)");
                            procedimiento.setInt(1, ((Paciente)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodPaciente());
                            procedimiento.execute();
                            listaContactoUrgencia.remove(tblContactoUrgencia.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }                       
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }
    
    public void editar(){
                switch (tipoDeOperaciones){
            case NINGUNO:
                if(tblContactoUrgencia.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable (true);
                    btnEliminar.setDisable (true);
                    activarControles();
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar a un contacto");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarContactoUrgencia(?,?,?,?,?)");
            ContactoUrgencia registro = (ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem();
            registro.setNombres(txtNombres.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setNumeroContacto(txtNumeroContacto.getText());
            procedimiento.setInt(1, registro.getCodContactoUrgencia());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getNumeroContacto());   
            procedimiento.setInt(5, registro.getPacientes_codPaciente());
            procedimiento.execute();
        }catch (Exception e){
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
                limpiarControles();
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
        ContactoUrgencia registro = new ContactoUrgencia();
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setNumeroContacto(txtNumeroContacto.getText());
        registro.setPacientes_codPaciente(((Paciente)cmbCodPaciente.getSelectionModel().getSelectedItem()).getCodPaciente());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarContactoUrgencia(?,?,?,?)}");
            procedimiento.setString(1, registro.getNombres());
            procedimiento.setString(2, registro.getApellidos());
            procedimiento.setString(3, registro.getNumeroContacto());
            procedimiento.setInt(4, registro.getPacientes_codPaciente());
            procedimiento.execute();
            listaContactoUrgencia.add(registro);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void imprimirReporte(){
        if(tblContactoUrgencia.getSelectionModel().getSelectedItem()!= null){
            int codContactoUrgencia = (((ContactoUrgencia)tblContactoUrgencia.getSelectionModel().getSelectedItem()).getCodContactoUrgencia());
            Map parametros = new HashMap();
            parametros.put("p_codContactoUrgencia", null);
            GenerarReporte.mostrarReporte("reporteContactoUrgencia.jasper", "Reporte de Contacto Urgencia", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public void desactivarControles(){
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtNumeroContacto.setEditable(false);
        cmbCodPaciente.setDisable(true);   
    }
    
    public void activarControles(){
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtNumeroContacto.setEditable(true);
        cmbCodPaciente.setDisable(false);
    }
    
    public void limpiarControles(){
        txtNombres.setText("");
        txtApellidos.setText("");
        txtNumeroContacto.setText("");
        cmbCodPaciente.getSelectionModel().clearSelection();
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
