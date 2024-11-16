package co.edu.usco.pw.jasperreport_springBoot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import co.edu.usco.pw.jasperreport_springBoot.model.Empleado;
import co.edu.usco.pw.jasperreport_springBoot.repository.EmpleadoRepository;

@Service
public class EmpleadoService {

	private final EmpleadoRepository empleadoRepository;

	public EmpleadoService(EmpleadoRepository empleadoRepository) {
		this.empleadoRepository = empleadoRepository;
	}

	public List<Empleado> listarEmpleados() {
		return empleadoRepository.findAll();
	}

	public Empleado guardarEmpleado(Empleado empleado) {
		return empleadoRepository.save(empleado);
	}

	public Empleado obtenerEmpleado(Long id) {
		return empleadoRepository.findById(id).orElse(null);
	}

	public void eliminarEmpleado(Long id) {
		empleadoRepository.deleteById(id);
	}
}
