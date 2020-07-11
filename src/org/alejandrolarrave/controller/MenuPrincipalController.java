package org.alejandrolarrave.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.alejandrolarrave.report.GenerarReporte;
import org.alejandrolarrave.sistema.Principal;

public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void ProgramadorView(){
    escenarioPrincipal.ProgramadorView();
    }
    public void MedicosView(){
        escenarioPrincipal.MedicosView();
    }
    public void ventanaPaciente(){
        escenarioPrincipal.ventanaPaciente();
    }
    public void ventanaTurno(){
        escenarioPrincipal.ventanaTurno();
    }
    public void ventanaArea(){
        escenarioPrincipal.ventanaArea();
    }
    public void ventanaCargo(){
        escenarioPrincipal.ventanaCargo();
    }
    public void ventanaTelefonoMedico (){
        escenarioPrincipal.ventanaTelefonoMedico();
    }
    public void ventanaContactoUrgencia(){
        escenarioPrincipal.ventanaContactoUrgencia();
    }
    public void ventanaHorario(){
        escenarioPrincipal.ventanaHorario();
    }
    public void ventanaResponsableTurno(){
        escenarioPrincipal.ventanaResponsableTurno();
    }
    public void ventanaEspecialidades(){
        escenarioPrincipal.ventanaEspecialidades();
    }
    public void ventanaMedicoEspecialidad(){
        escenarioPrincipal.ventanaMedicoEspecialidad();
    }
    public void imprimirReporteMedicos(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteMedicos.jasper", "Reporte de Médicos", parametros);
    }
    public void imprimirReporteTelefonosMedico(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteTelefonosMedicoListar.jasper", "Reporte de Teléfonos Médico", parametros);
    }
    public void imprimirReporteHorarios(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteHorariosListar.jasper", "Reporte de Horarios", parametros);
    }
    public void imprimirReporteEspecialidades(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteEspecialidadesListar.jasper", "Reporte de Especialidades", parametros);
    }
    public void imprimirReporteMedicoEspecialidad(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteMedicoEspecialidadListar.jasper", "Reporte de Médico Especialidad", parametros);
    }
    public void imprimirReportePacientes(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReportePacientesListar.jasper", "Reporte de Pacientes", parametros);
    }
    public void imprimirReporteContactoUrgencia(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteContactoUrgenciaListar.jasper", "Reporte de Contactos Urgencia", parametros);
    }
    public void imprimirReporteAreas(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteAreasListar.jasper", "Reporte de Áreas", parametros);
    }
    public void imprimirReporteCargos(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteCargosListar.jasper", "Reporte de Cargos", parametros);
    }
    public void imprimirReporteResponsableTurno(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteResponsableTurnoListar.jasper", "Reporte de Responsables Turno", parametros);
    }
    public void imprimirReporteTurnos(){
        Map parametros = new HashMap();
        GenerarReporte.mostrarReporte("ReporteTurnosListar.jasper", "Reporte de Turnos", parametros);
    }
}
