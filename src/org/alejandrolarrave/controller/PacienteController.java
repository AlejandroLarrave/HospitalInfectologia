package org.alejandrolarrave.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.alejandrolarrave.bean.Paciente;
import org.alejandrolarrave.db.Conexion;
import org.alejandrolarrave.sistema.Principal;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.alejandrolarrave.report.GenerarReporte;

public class PacienteController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Paciente> listaPaciente;
    @FXML private TextField txtDPI;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEdad;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtOcupacion;
    @FXML private TextField txtSexo;
    private DatePicker dtpFechaNacimiento;
    @FXML private GridPane grpFechaNacimiento;   
    @FXML private TableView tblPacientes;
    @FXML private TableColumn colCodPaciente;
    @FXML private TableColumn colDPI;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colNacimiento;
    @FXML private TableColumn colEdad;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colOcupacion;
    @FXML private TableColumn colSexo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        dtpFechaNacimiento = new DatePicker(Locale.ENGLISH);
        dtpFechaNacimiento.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        dtpFechaNacimiento.getCalendarView().todayButtonTextProperty().set("Today");
        dtpFechaNacimiento.getCalendarView().setShowWeeks(false);
        grpFechaNacimiento.add(dtpFechaNacimiento,0,0);

    }
    
    public void cargarDatos(){
        tblPacientes.setItems(getPaciente());
        colCodPaciente.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("codPaciente"));
        colDPI.setCellValueFactory(new PropertyValueFactory<Paciente, String>("DPI"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Paciente, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Paciente, String>("apellidos"));
        colNacimiento.setCellValueFactory(new PropertyValueFactory<Paciente, Date>("fechaNacimiento"));
        colEdad.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("edad"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("direccion"));
        colOcupacion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("ocupacion"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Paciente, String>("sexo")); 
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
        txtDPI.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDPI()));
        txtNombres.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getNombres()));
        txtApellidos.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getApellidos()));
        dtpFechaNacimiento.selectedDateProperty().set(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaNacimiento());
        txtEdad.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getEdad()));
        txtDireccion.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDireccion()));
        txtOcupacion.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getOcupacion()));
        txtSexo.setText(String.valueOf(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getSexo()));
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
                btnReporte.setDisable(false);
                btnEditar.setDisable(false);
                tipoDeOperaciones = operaciones.NUEVO;
                break;
            default:
                if (tblPacientes.getSelectionModel().getSelectedItem() != null){
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de Eliminarlo", "Eliminar Teléfono", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarTelefonosMedicos(?)");
                        procedimiento.setInt(1, (((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodPaciente()));
                        procedimiento.execute();
                        listaPaciente.remove(tblPacientes.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                }else{
                    JOptionPane.showConfirmDialog(null, "Debe selecionar un elemento");
                }
        }
    }
    
    public void editar(){
        switch (tipoDeOperaciones){
            case NINGUNO:
                if (tblPacientes.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                    
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar a un médico ");
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
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarPacientes(?,?,?,?,?,?,?,?)");
        Paciente registro = (Paciente)tblPacientes.getSelectionModel().getSelectedItem();
        registro.setDPI(txtDPI.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setNombres(txtNombres.getText());
        registro.setFechaNacimiento(dtpFechaNacimiento.getSelectedDate());
        registro.setDireccion(txtDireccion.getText());
        registro.setOcupacion(txtOcupacion.getText());
        registro.setSexo(txtSexo.getText());
        procedimiento.setInt(1, registro.getCodPaciente());
        procedimiento.setString(2, registro.getDPI());
        procedimiento.setString(3, registro.getApellidos());
        procedimiento.setString(4, registro.getNombres());
        procedimiento.setDate(5, new java.sql.Date (registro.getFechaNacimiento().getTime()));
        procedimiento.setString(6, registro.getDireccion());
        procedimiento.setString(7, registro.getOcupacion());
        procedimiento.setString(8, registro.getSexo());
        procedimiento.execute();;       
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
                tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
        }
                
    }
    
    public void agregar(){
        Paciente registro = new Paciente();
        registro.setDPI(txtDPI.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setNombres(txtNombres.getText());
        registro.setFechaNacimiento(dtpFechaNacimiento.getSelectedDate());
        registro.setDireccion(txtDireccion.getText());
        registro.setOcupacion(txtOcupacion.getText());
        registro.setSexo(txtSexo.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarPacientes (?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getDPI());
            procedimiento.setString(2, registro.getApellidos());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setDate(4, new java.sql.Date (registro.getFechaNacimiento().getTime()));
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getOcupacion());
            procedimiento.setString(7, registro.getSexo());
            procedimiento.execute();
            listaPaciente.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }  
    }
  
    public void imprimirReporte(){
        if(tblPacientes.getSelectionModel().getSelectedItem()!= null){
            int codPaciente = (((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getCodPaciente());
            Map parametros = new HashMap();
            parametros.put("p_codPaciente", null);
            GenerarReporte.mostrarReporte("reportePacientesBuscar.jasper", "Reporte de Pacientes", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public void desactivarControles(){
        txtDPI.setEditable(false);
        txtApellidos.setEditable(false);
        txtNombres.setEditable(false);
        grpFechaNacimiento.setDisable(true);
        txtEdad.setEditable(true);
        txtDireccion.setEditable(false);
        txtOcupacion.setEditable(false);
        txtSexo.setEditable(false);
    }
    
    public void activarControles(){
        txtDPI.setEditable(true);
        txtApellidos.setEditable(true);
        txtNombres.setEditable(true);
        grpFechaNacimiento.setDisable(false);
        txtEdad.setEditable(false);
        txtDireccion.setEditable(true);
        txtOcupacion.setEditable(true);
        txtSexo.setEditable(true);
    }
    
    public void limpiarControles(){
        txtDPI.setText("");
        txtApellidos.setText("");
        txtNombres.setText("");
        txtEdad.setText("");
        txtDireccion.setText("");
        txtOcupacion.setText("");
        txtSexo.setText("");
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
