package org.alejandrolarrave.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
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
import org.alejandrolarrave.bean.Medico;
import org.alejandrolarrave.db.Conexion;
import org.alejandrolarrave.report.GenerarReporte;
import org.alejandrolarrave.sistema.Principal;

public class MedicosController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medico> listaMedico;   
    @FXML private TextField txtLicencia;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtSexo;
    @FXML private TextField txtTurno;
    @FXML private TextField txtEntrada;
    @FXML private TextField txtSalida;
    @FXML private TableView tblMedicos;
    @FXML private TableColumn colcodMedico;
    @FXML private TableColumn colLicencia;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colEntrada;
    @FXML private TableColumn colSalida;
    @FXML private TableColumn colTurno;
    @FXML private TableColumn colSexo;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    @FXML private Button btnReporteGeneral;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblMedicos.setItems(getMedicos());
        colcodMedico.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("codMedico"));
        colLicencia.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("licenciaMedica"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Medico, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Medico, String>("apellidos"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<Medico, Time>("horaEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Medico, Time>("horaSalida"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Medico, String>("sexo"));
        colTurno.setCellValueFactory(new PropertyValueFactory<Medico, String>("turnoMaximo"));
    }
    
    public ObservableList<Medico> getMedicos(){
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
        txtLicencia.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getLicenciaMedica()));
        txtNombres.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getNombres()));
        txtApellidos.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getApellidos()));
        txtEntrada.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraEntrada()));
        txtSalida.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraSalida()));
        txtTurno.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getTurnoMaximo()));
        txtSexo.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getSexo()));
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
                if (tblMedicos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de Eliminarlo?", "Eliminar Médico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarMedicos(?)");
                            procedimiento.setInt(1, ((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getCodMedico());
                            procedimiento.execute();
                            listaMedico.remove(tblMedicos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar a un médico");
                }
        }
    }
        
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblMedicos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnReporteGeneral.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                    
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
                btnReporteGeneral.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar(){
        try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarMedicos(?,?,?,?,?,?,?)");
           Medico registro = (Medico)tblMedicos.getSelectionModel().getSelectedItem();
           registro.setLicenciaMedica(Integer.parseInt(txtLicencia.getText()));
           registro.setNombres(txtNombres.getText());
           registro.setApellidos(txtApellidos.getText());
           registro.setHoraEntrada(Time.valueOf(txtEntrada.getText()));
           registro.setHoraSalida(Time.valueOf(txtSalida.getText()));
           registro.setSexo(txtSexo.getText());
           procedimiento.setInt(1, registro.getCodMedico());
           procedimiento.setInt(2, registro.getLicenciaMedica());
           procedimiento.setString(3, registro.getNombres());
           procedimiento.setString(4, registro.getApellidos());
           procedimiento.setTime(5, registro.getHoraEntrada());
           procedimiento.setTime(6, registro.getHoraSalida());
           procedimiento.setString(7, registro.getSexo());
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
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                btnReporteGeneral.setDisable(true);
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
                btnReporteGeneral.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
        }
    }
    
    public void guardar(){
        Medico registro = new Medico();
        registro.setLicenciaMedica(Integer.parseInt(txtLicencia.getText()));
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setHoraEntrada(Time.valueOf(txtEntrada.getText()));
        registro.setHoraSalida(Time.valueOf(txtSalida.getText()));
        registro.setSexo(txtSexo.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedicos(?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getLicenciaMedica());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setTime(4, registro.getHoraEntrada());
            procedimiento.setTime(5, registro.getHoraSalida());
            procedimiento.setString(6, registro.getSexo());
            procedimiento.execute();
            listaMedico.add(registro);
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    /*public void generarReporte(){
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                tipoDeOperacion = operaciones.ACTUALIZAR;
                break;
            case ACTUALIZAR:
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                tipoDeOperacion = operaciones.NINGUNO;
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(true);
                txtNombres.setEditable(false);
                txtLicencia.setEditable(false);
                txtApellidos.setEditable(false);
                txtEntrada.setEditable(false);
                txtSalida.setEditable(false);
                txtSexo.setEditable(false);
                txtTurno.setEditable(false);
                break;
        }
    }*/
    
    public void imprimirReporte(){
        if(tblMedicos.getSelectionModel().getSelectedItem() != null){
            int codMedico = (((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getCodMedico());
            Map parametros = new HashMap();
            parametros.put("p_codMedico", codMedico);
            GenerarReporte.mostrarReporte("reporteMedicosBuscar.jasper", "Reporte de Médicos", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }    
    
    public void imprimirReporteGeneral(){
        if(tblMedicos.getSelectionModel().getSelectedItem() != null){
            int codMedico = (((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getCodMedico());
            Map parametros = new HashMap();
            parametros.put("p_codMedico", codMedico);
            GenerarReporte.mostrarReporte("reporteGeneral.jasper", "Reporte de General", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }  
    
    public void desactivarControles(){
        txtLicencia.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtEntrada.setEditable(false);
        txtSalida.setEditable(false);
        txtTurno.setEditable(false);
        txtSexo.setEditable(false);       
    }
    
    public void activarControles(){
        txtLicencia.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtEntrada.setEditable(true);
        txtSalida.setEditable(true);
        txtTurno.setEditable(false);
        txtSexo.setEditable(true);
    }
    
    public void limpiarControles(){
        txtLicencia.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEntrada.setText("");
        txtSalida.setText("");
        txtTurno.setText("");
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
