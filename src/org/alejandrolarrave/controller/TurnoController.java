package org.alejandrolarrave.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
//import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.alejandrolarrave.bean.Especialidades;
import org.alejandrolarrave.bean.MedicoEspecialidad;
import org.alejandrolarrave.bean.Paciente;
import org.alejandrolarrave.bean.ResponsableTurno;
import org.alejandrolarrave.bean.Turno;
import org.alejandrolarrave.db.Conexion;
import org.alejandrolarrave.report.GenerarReporte;
import org.alejandrolarrave.sistema.Principal;

public class TurnoController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<MedicoEspecialidad> listaMedicoEspecialidad;
    private ObservableList<ResponsableTurno> listaResponsableTurno;
    private ObservableList<Paciente> listaPaciente;
    private ObservableList<Turno> listaTurno;
    @FXML private ComboBox cmbCodEspecialidad;
    @FXML private ComboBox cmbCodResponsableTurno;
    @FXML private ComboBox cmbCodPaciente;
    private DatePicker dtpFechaTurno;
    private DatePicker dtpFechaCita;
    @FXML private GridPane grpFechaTurno;
    @FXML private GridPane grpFechaCita;
    @FXML private TextField txtValorCita;
    @FXML private TableView tblTurno;
    @FXML private TableColumn colCodTurno;
    @FXML private TableColumn colCodMedicoEspecialidad;
    @FXML private TableColumn colCodResponsableTurno;
    @FXML private TableColumn colCodPaciente;
    @FXML private TableColumn colFechaTurno;
    @FXML private TableColumn colFechaCita;
    @FXML private TableColumn colValorCita;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodEspecialidad.setItems(getMedicoEspecialidad());
        cmbCodResponsableTurno.setItems(getResponsableTurno());
        cmbCodPaciente.setItems(getPaciente());
        
      dtpFechaTurno = new DatePicker(Locale.ENGLISH);
      dtpFechaTurno.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
      dtpFechaTurno.getCalendarView().todayButtonTextProperty().set("Today");
      dtpFechaTurno.getCalendarView().setShowWeeks(false);
      grpFechaTurno.add(dtpFechaTurno, 0,0);
      
      dtpFechaCita = new DatePicker(Locale.ENGLISH);
      dtpFechaCita.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
      dtpFechaCita.getCalendarView().todayButtonTextProperty().set("Today");
      dtpFechaCita.getCalendarView().setShowWeeks(false);
      grpFechaCita.add(dtpFechaCita, 0,0);
    }

    public void cargarDatos(){
        tblTurno.setItems(getTurnos());
        colCodTurno.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codTurno"));
        colCodMedicoEspecialidad.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("Medico_Especialidad_codMedicoEspecialidad"));
        colCodResponsableTurno.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("ResponsableTurno_codResponsableTurno"));
        colCodPaciente.setCellValueFactory(new PropertyValueFactory<Turno, Integer> ("Pacientes_codPaciente"));
        colFechaTurno.setCellValueFactory(new PropertyValueFactory<Turno, Date>("fechaTurno"));
        colFechaCita.setCellValueFactory(new PropertyValueFactory<Turno, Date> ("fechaCita"));
        colValorCita.setCellValueFactory(new PropertyValueFactory<Turno, Integer> ("valorCita"));
    }
    
    
    public ObservableList<Turno> getTurnos(){
        ArrayList<Turno> lista = new ArrayList<Turno>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTurno}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Turno(resultado.getInt("codTurno"),resultado.getDate("fechaTurno"),resultado.getDate("fechaCita"),
                                    resultado.getInt("valorCita"),resultado.getInt("Medico_Especialidad_codMedicoEspecialidad"),
                                    resultado.getInt("ResponsableTurno_codResponsableTurno"),resultado.getInt("Pacientes_codPaciente")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTurno = FXCollections.observableList(lista);
    }
    
    public ObservableList<MedicoEspecialidad> getMedicoEspecialidad(){
        ArrayList<MedicoEspecialidad> lista = new ArrayList<MedicoEspecialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedico_Especialidad}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new MedicoEspecialidad(resultado.getInt("codMedicoEspecialidad"),resultado.getInt("Medicos_codMedico"),
                                                 resultado.getInt("Especialidades_codEspecialidad"),resultado.getInt("Horarios_codHorario")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaMedicoEspecialidad = FXCollections.observableList(lista);
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
        txtValorCita.setText(String.valueOf(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getValorCita()));
        dtpFechaTurno.selectedDateProperty().set(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getFechaTurno());
        dtpFechaCita.selectedDateProperty().set(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getFechaCita());
        cmbCodEspecialidad.getSelectionModel().select(buscarEspecialidades(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getMedico_Especialidad_codMedicoEspecialidad()));
        cmbCodResponsableTurno.getSelectionModel().select(buscarResponsableTurno(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getResponsableTurno_codResponsableTurno()));
        cmbCodPaciente.getSelectionModel().select(buscarPaciente(((Turno)tblTurno.getSelectionModel().getSelectedItem()).getPacientes_codPaciente()));
    }
    
    public Turno buscarTurno(int codTurno){
        Turno resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("(call sp_BuscarTurno(?))");
            procedimiento.setInt(1, codTurno);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new Turno(registro.getInt("codTurno"),registro.getDate("fechaCita"),registro.getDate("fechaTurno"),registro.getInt("valorCita"),
                                      registro.getInt("Medico_Especialidad_codMedicoEspecialidad"),registro.getInt("ResponsableTurno_codResponsableTurno"),registro.getInt("Pacientes_codPaciente"));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
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
                if(tblTurno.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminarlo", "Eliminar Turno", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarTurno(?)");
                            procedimiento.setInt(1, ((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodTurno());
                            procedimiento.execute();
                            listaTurno.remove(tblTurno.getSelectionModel().getSelectedIndex());
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
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTurno.getSelectionModel().getSelectedItem() !=null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un turno");
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarHorarios(?,?,?,?,?,?,?)");
            Turno registro = (Turno)tblTurno.getSelectionModel().getSelectedItem();
            registro.setFechaCita(dtpFechaCita.getSelectedDate());
            registro.setFechaTurno(dtpFechaTurno.getSelectedDate());
            registro.setValorCita(Integer.parseInt(txtValorCita.getText()));
            registro.setMedico_Especialidad_codMedicoEspecialidad(((MedicoEspecialidad)cmbCodEspecialidad.getSelectionModel().getSelectedItem()).getCodMedicoEspecialidad());
            registro.setResponsableTurno_codResponsableTurno(((ResponsableTurno)cmbCodResponsableTurno.getSelectionModel().getSelectedItem()).getCodResponsableTurno());
            registro.setPacientes_codPaciente(((Paciente)cmbCodPaciente.getSelectionModel().getSelectedItem()).getCodPaciente());
            procedimiento.setInt(1, registro.getCodTurno());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setDate(3, new java.sql.Date(registro.getFechaTurno().getTime()));  
            procedimiento.setInt(4, registro.getValorCita());
            procedimiento.setInt(5, registro.getMedico_Especialidad_codMedicoEspecialidad());
            procedimiento.setInt(6, registro.getResponsableTurno_codResponsableTurno());
            procedimiento.setInt(7, registro.getPacientes_codPaciente());
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
        Turno registro = new Turno();
        registro.setValorCita(Integer.parseInt(txtValorCita.getText()));
        registro.setMedico_Especialidad_codMedicoEspecialidad(((MedicoEspecialidad)cmbCodEspecialidad.getSelectionModel().getSelectedItem()).getCodMedicoEspecialidad());
        registro.setResponsableTurno_codResponsableTurno(((ResponsableTurno)cmbCodResponsableTurno.getSelectionModel().getSelectedItem()).getCodResponsableTurno());
        registro.setPacientes_codPaciente(((Paciente)cmbCodPaciente.getSelectionModel().getSelectedItem()).getCodPaciente());
        registro.setFechaCita(dtpFechaCita.getSelectedDate());
        registro.setFechaTurno(dtpFechaTurno.getSelectedDate());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTurno(?,?,?,?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date (registro.getFechaTurno().getTime()));
            procedimiento.setDate(2, new java.sql.Date (registro.getFechaCita().getTime()));           
            procedimiento.setInt(3, registro.getValorCita());
            procedimiento.setInt(4, registro.getMedico_Especialidad_codMedicoEspecialidad());
            procedimiento.setInt(5, registro.getResponsableTurno_codResponsableTurno());
            procedimiento.setInt(6, registro.getPacientes_codPaciente());
            procedimiento.execute();
            listaTurno.add(registro);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void imprimirReporte(){
        if(tblTurno.getSelectionModel().getSelectedItem()!= null){
            int codTurno = (((Turno)tblTurno.getSelectionModel().getSelectedItem()).getCodTurno());
            Map parametros = new HashMap();
            parametros.put("p_codTurno", null);
            GenerarReporte.mostrarReporte("reporteTurnoBuscar.jasper", "Reporte de Turno", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public void desactivarControles(){
        txtValorCita.setEditable(false);
        cmbCodEspecialidad.setDisable(true);
        cmbCodResponsableTurno.setDisable(true);
        cmbCodPaciente.setDisable(true);
        dtpFechaTurno.setDisable(true);
        dtpFechaCita.setDisable(true);
    }
    
    public void activarControles(){
        txtValorCita.setEditable(true);
        cmbCodEspecialidad.setDisable(false);
        cmbCodResponsableTurno.setDisable(false);
        cmbCodPaciente.setDisable(false);
        dtpFechaTurno.setDisable(false);
        dtpFechaCita.setDisable(false);
    }
    
    public void limpiarControles(){
        txtValorCita.setText("");
        cmbCodEspecialidad.getSelectionModel().clearSelection();
        cmbCodResponsableTurno.getSelectionModel().clearSelection();
        cmbCodPaciente.getSelectionModel().clearSelection();
    }
    
    private void cancelar(){
        btnAgregar.setText("Agregar");
        btnEliminar.setText("Eliminar");
        btnEditar.setDisable(false);
        btnReporte.setDisable(false);
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

