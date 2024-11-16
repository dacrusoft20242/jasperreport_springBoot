package co.edu.usco.pw.jasperreport_springBoot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import co.edu.usco.pw.jasperreport_springBoot.model.Empleado;
import co.edu.usco.pw.jasperreport_springBoot.service.EmpleadoService;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class EmpleadoController {

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
	public void generarReporte(HttpServletResponse response) throws Exception {
	}
}
