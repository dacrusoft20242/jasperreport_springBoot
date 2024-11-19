package co.edu.usco.pw.jasperreport_springBoot.controller;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.edu.usco.pw.jasperreport_springBoot.model.Empleado;
import co.edu.usco.pw.jasperreport_springBoot.service.EmpleadoService;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Controller
public class EmpleadoController {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private ResourceLoader resourceLoader;

	@GetMapping("/empleados")
	public String listarEmpleados(Model model) {
		List<Empleado> empleados = empleadoService.listarEmpleados();
		model.addAttribute("empleados", empleados);
		return "empleados";
	}

	@GetMapping("/empleados/nuevo")
	public String mostrarFormularioEmpleado(Model model) {
		model.addAttribute("empleado", new Empleado());
		return "formulario-empleado";
	}

	@PostMapping("/empleados")
	public String guardarEmpleado(@ModelAttribute("empleado") Empleado empleado) {
		empleadoService.guardarEmpleado(empleado);
		return "redirect:/empleados";
	}

	@GetMapping("/empleados/editar/{id}")
	public String mostrarFormularioEditarEmpleado(@PathVariable("id") Long id, Model model) {
		Empleado empleado = empleadoService.obtenerEmpleado(id);
		model.addAttribute("empleado", empleado);
		return "formulario-empleado";
	}

	@GetMapping("/empleados/eliminar/{id}")
	public String eliminarEmpleado(@PathVariable("id") Long id) {
		empleadoService.eliminarEmpleado(id);
		return "redirect:/empleados";
	}

	@GetMapping("/empleados/reporte")
	//public void generarReporte(HttpServletResponse response) throws Exception {
	public void generarReporte(@RequestParam("puesto") String puesto, HttpServletResponse response) throws Exception {

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=\"reporte_empleados.pdf\"");

		JasperReport reporte = JasperCompileManager
				.compileReport(resourceLoader.getResource("classpath:reporte_empleados.jrxml").getInputStream());
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("REPORT_TITTLE", "Reporte de Empleados");
		//parametros.put("IMAGEN_PATH", "src/main/resources/universidad-surcolombiana-vp.png");
		parametros.put("PUESTO", puesto);
						
		try(Connection conexion = dataSource.getConnection()){
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parametros, conexion);
			
			OutputStream salida = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jasperPrint, salida);
			
			salida.flush();
			salida.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al generar el reporte: " + e.getMessage());
		}
		

	}
}


















