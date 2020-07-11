package org.alejandrolarrave.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.alejandrolarrave.bean.Horario;
import org.alejandrolarrave.db.Conexion;
import org.alejandrolarrave.report.GenerarReporte;
import org.alejandrolarrave.sistema.Principal;

public class HorarioController implements Initializable{
    private enum operaciones {NUEVO, GUARDAR, EDITAR, ACTUALIZAR, ELIMINAR, CANCELAR, NINGUNO}
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Horario> listaHorario;
    @FXML private TextField txtHorarioInicio;
    @FXML private TextField txtHorarioSalida;
    @FXML private CheckBox ckbLunes;
    @FXML private CheckBox ckbMartes;
    @FXML private CheckBox ckbMiercoles;
    @FXML private CheckBox ckbJueves;
    @FXML private CheckBox ckbViernes;
    @FXML private TableView tblHorarios;
    @FXML private TableColumn colCodHorario;
    @FXML private TableColumn colHorarioInicio;
    @FXML private TableColumn colHorarioSalida;
    @FXML private TableColumn colLunes;
    @FXML private TableColumn colMartes;
    @FXML private TableColumn colMiercoles;
    @FXML private TableColumn colJueves;
    @FXML private TableColumn colViernes;
    @FXML private Button btnAgregar;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        
    }

    public void cargarDatos(){
        tblHorarios.setItems(getHorarios());
        colCodHorario.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("codHorario"));
        colHorarioInicio.setCellValueFactory(new PropertyValueFactory<Horario, Time>("horarioInicio"));
        colHorarioSalida.setCellValueFactory(new PropertyValueFactory<Horario, Time>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horario, Integer>("viernes"));
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
        txtHorarioInicio.setText(String.valueOf(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioInicio()));
        txtHorarioSalida.setText(String.valueOf(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida()));
        ckbLunes.setText(String.valueOf(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getLunes()));
        ckbMartes.setText(String.valueOf(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getMartes()));
        ckbMiercoles.setText(String.valueOf(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getMiercoles()));
        ckbJueves.setText(String.valueOf(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getJueves()));
        ckbViernes.setText(String.valueOf(((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getViernes()));
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
                if(tblHorarios.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de querer eliminarlo", "Eliminar Horario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarHorarios(?)");
                        procedimiento.setInt(1, ((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getCodHorario());
                        procedimiento.execute();
                        listaHorario.remove(tblHorarios.getSelectionModel().getSelectedIndex());
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
        switch (tipoDeOperacion){
            case NINGUNO:
                if(tblHorarios.getSelectionModel().getSelectedItem() !=null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnAgregar.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un horario");
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarHorarios(?,?,?,?,?,?,?,?)");
            Horario registro = (Horario)tblHorarios.getSelectionModel().getSelectedItem();
            registro.setHorarioInicio(Time.valueOf(txtHorarioInicio.getText()));
            registro.setHorarioSalida(Time.valueOf(txtHorarioSalida.getText()));
            registro.setLunes(Boolean.valueOf(ckbLunes.getText()));
            registro.setMartes(Boolean.valueOf(ckbMartes.getText()));
            registro.setMiercoles(Boolean.valueOf(ckbMiercoles.getText()));
            registro.setJueves(Boolean.valueOf(ckbJueves.getText()));
            registro.setViernes(Boolean.valueOf(ckbViernes.getText()));
            procedimiento.setInt(1, registro.getCodHorario());
            procedimiento.setTime(2, registro.getHorarioInicio());
            procedimiento.setTime(3, registro.getHorarioSalida());
            procedimiento.setBoolean(4, registro.getLunes());
            procedimiento.setBoolean(5, registro.getMartes());
            procedimiento.setBoolean(6, registro.getMiercoles());
            procedimiento.setBoolean(7, registro.getJueves());
            procedimiento.setBoolean(8, registro.getViernes());
            procedimiento.execute();
        }catch (Exception e){
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
        Horario registro = new Horario();
        registro.setHorarioInicio(Time.valueOf(txtHorarioInicio.getText()));
        registro.setHorarioSalida(Time.valueOf(txtHorarioSalida.getText()));
        registro.setLunes(Boolean.valueOf(ckbLunes.getText()));
        registro.setMartes(Boolean.valueOf(ckbMartes.getText()));
        registro.setMiercoles(Boolean.valueOf(ckbMiercoles.getText()));
        registro.setJueves(Boolean.valueOf(ckbJueves.getText()));
        registro.setViernes(Boolean.valueOf(ckbViernes.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarHorarios(?,?,?,?,?,?,?)}");
            procedimiento.setTime(1, registro.getHorarioInicio());
            procedimiento.setTime(2, registro.getHorarioSalida());
            procedimiento.setBoolean(3, registro.getLunes());
            procedimiento.setBoolean(4, registro.getMartes());
            procedimiento.setBoolean(5, registro.getMiercoles());
            procedimiento.setBoolean(6, registro.getJueves());
            procedimiento.setBoolean(7, registro.getViernes());
            procedimiento.execute();
            listaHorario.add(registro);
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void imprimirReporte(){
        if(tblHorarios.getSelectionModel().getSelectedItem() != null){
            int codHorario = (((Horario)tblHorarios.getSelectionModel().getSelectedItem()).getCodHorario());
            Map parametros = new HashMap();
            parametros.put("p_codHorario", null);
            GenerarReporte.mostrarReporte("reporteHorariosBuscar.jrxml", "Reporte de Horarios", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    public void desactivarControles(){
        txtHorarioInicio.setEditable(false);
        txtHorarioSalida.setEditable(false);
        ckbLunes.setDisable(true);
        ckbMartes.setDisable(true);
        ckbMiercoles.setDisable(true);
        ckbJueves.setDisable(true);
        ckbViernes.setDisable(true);
    }
    
    public void activarControles(){
        txtHorarioInicio.setEditable(true);
        txtHorarioSalida.setEditable(true);
        ckbLunes.setDisable(false);
        ckbMartes.setDisable(false);
        ckbMiercoles.setDisable(false);
        ckbJueves.setDisable(false);
        ckbViernes.setDisable(false);
    }
    
    public void limpiarControles(){
        txtHorarioInicio.setText("");
        txtHorarioSalida.setText("");
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
