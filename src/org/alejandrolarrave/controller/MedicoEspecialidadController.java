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
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.alejandrolarrave.bean.Especialidades;
import org.alejandrolarrave.bean.Horario;
import org.alejandrolarrave.bean.Medico;
import org.alejandrolarrave.bean.MedicoEspecialidad;
import org.alejandrolarrave.db.Conexion;
import org.alejandrolarrave.report.GenerarReporte;
import org.alejandrolarrave.sistema.Principal;

public class MedicoEspecialidadController implements Initializable{
    private enum operaciones {NUEVO,GUARDAR,EDITAR,ACTUALIZAR,ELIMINAR,CANCELAR,NINGUNO}
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medico> listaMedico;
    private ObservableList<Especialidades> listaEspecialidades;
    private ObservableList<Horario> listaHorario;
    private ObservableList<MedicoEspecialidad> listaMedicoEspecialidad;
    @FXML private ComboBox cmbCodMedico;
    @FXML private ComboBox cmbCodEspecialidad;
    @FXML private ComboBox cmbCodHorario;
    @FXML private TableView tblMedicoEspecialidad;
    @FXML private TableColumn colCodMedicoEspecialidad;
    @FXML private TableColumn colCodMedico;
    @FXML private TableColumn colCodEspecialidad;
    @FXML private TableColumn colCodHorario;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodMedico.setItems(getMedicos());
        cmbCodEspecialidad.setItems(getEspecialidades());
        cmbCodHorario.setItems(getHorarios());
    }
    
    public void cargarDatos(){
        tblMedicoEspecialidad.setItems(getMedicoEspecialidad());
        colCodMedicoEspecialidad.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codMedicoEspecialidad"));
        colCodMedico.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("Medicos_codMedico"));
        colCodEspecialidad.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("Especialidades_codEspecialidad"));
        colCodHorario.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("Horarios_codHorario"));
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
    
    public ObservableList<Horario> getHorarios(){
        ArrayList<Horario> lista = new ArrayList<Horario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarHorarios}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Horario(resultado.getInt("codHorario"),resultado.getTime("horarioInicio"),
                                      resultado.getTime("horarioSalida"),resultado.getBoolean("lunes"),
                                      resultado.getBoolean("martes"),resultado.getBoolean("miercoles"),
                                      resultado.getBoolean("jueves"),resultado.getBoolean("viernes")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaHorario = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento(){
        cmbCodMedico.getSelectionModel().select(buscarMedico(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getMedicos_codMedico()));
        cmbCodEspecialidad.getSelectionModel().select(buscarEspecialidades(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getEspecialidades_codEspecialidad()));
        cmbCodHorario.getSelectionModel().select(buscarHorario(((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getHorarios_codHorario()));
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
    
    public Horario buscarHorario(int codHorario){
        Horario resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("(call sp_BuscarHorarios(?))");
            procedimiento.setInt(1, codHorario);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Horario(registro.getInt("codHorario"),registro.getTime("horarioInicio"),registro.getTime("horarioSalida"),
                                        registro.getBoolean("lunes"),registro.getBoolean("martes"),registro.getBoolean("miercoles"),registro.getBoolean("jueves"),registro.getBoolean("viernes"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public MedicoEspecialidad buscarMedicoEspecialidad(int codMedicoEspecialidad){
        MedicoEspecialidad resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("(call sp_BuscarMedico_Especialidad(?))");
            procedimiento.setInt(1, codMedicoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()){
                resultado = new MedicoEspecialidad(registro.getInt("codMedicoEspecialidad"),registro.getInt("Medicos_codMedico"),
                                                   registro.getInt("Especialidades_codEspecialidad"),registro.getInt("Horarios_codHorario"));
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
                if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
                    int resultado = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminarlo?", "Eliminar MedicoEspecialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(resultado == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarMedico_Especialidad(?)");
                            procedimiento.setInt(1, ((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodMedicoEspecialidad());
                            procedimiento.execute();
                            listaMedicoEspecialidad.remove(tblMedicoEspecialidad.getSelectionModel().getSelectedIndex());
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
                if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarMedico_Especialidad(?,?,?,?)");
            MedicoEspecialidad registro = (MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem();
            registro.setMedicos_codMedico(((Medico)cmbCodMedico.getSelectionModel().getSelectedItem()).getCodMedico());
            registro.setEspecialidades_codEspecialidad(((Especialidades)cmbCodEspecialidad.getSelectionModel().getSelectedItem()).getCodEspecialidad());
            registro.setHorarios_codHorario(((Horario)cmbCodHorario.getSelectionModel().getSelectedItem()).getCodHorario());
            procedimiento.setInt(1, registro.getCodMedicoEspecialidad());
            procedimiento.setInt(2, registro.getMedicos_codMedico());
            procedimiento.setInt(3, registro.getEspecialidades_codEspecialidad());
            procedimiento.setInt(4, registro.getHorarios_codHorario());
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
        try{
        MedicoEspecialidad registro = new MedicoEspecialidad();
        registro.setEspecialidades_codEspecialidad(((Especialidades)cmbCodEspecialidad.getSelectionModel().getSelectedItem()).getCodEspecialidad());
        registro.setHorarios_codHorario(((Horario)cmbCodHorario.getSelectionModel().getSelectedItem()).getCodHorario());
        registro.setMedicos_codMedico(((Medico)cmbCodMedico.getSelectionModel().getSelectedItem()).getCodMedico());
        
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedico_Especialidad(?,?,?)}");
            procedimiento.setInt(1, registro.getMedicos_codMedico());
            procedimiento.setInt(2, registro.getEspecialidades_codEspecialidad());
            procedimiento.setInt(3, registro.getHorarios_codHorario());
            procedimiento.execute();
            listaMedicoEspecialidad.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void imprimirReporte(){
        if(tblMedicoEspecialidad.getSelectionModel().getSelectedItem() != null){
            int codMedicoEspecialidad = (((MedicoEspecialidad)tblMedicoEspecialidad.getSelectionModel().getSelectedItem()).getCodMedicoEspecialidad());
            Map parametros = new HashMap();
            parametros.put("p_codMedicoEspecialidad", null);
            GenerarReporte.mostrarReporte("reporteMedicoEspecialidadBuscar.jasper", "Reporte de Médico Especialidad", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public void desactivarControles(){
        cmbCodMedico.setDisable(true);
        cmbCodEspecialidad.setDisable(true);
        cmbCodHorario.setDisable(true);
    }

    public void activarControles(){
        cmbCodMedico.setDisable(false);
        cmbCodEspecialidad.setDisable(false);
        cmbCodHorario.setDisable(false);
    }
    
    public void limpiarControles(){
        cmbCodMedico.getSelectionModel().clearSelection();
        cmbCodEspecialidad.getSelectionModel().clearSelection();
        cmbCodHorario.getSelectionModel().clearSelection();
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
