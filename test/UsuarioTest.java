import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import java.util.ArrayList;
import java.util.List;

import models.UsuarioBD;
import models.TareaBD;

import org.junit.Test;


public class UsuarioTest 
{
	@Test
	public void comprobarInsercionUsuarioNoRepetido()
	{
		running(fakeApplication(inMemoryDatabase()), ()->{
		
			UsuarioBD user = new UsuarioBD();
			
			user.nombre="Pepe";
			
			List<TareaBD> tareas = new ArrayList();
			
			TareaBD tarea1 = new TareaBD();
			TareaBD tarea2 = new TareaBD();
			
			tarea1.tarea="cocinar";
			tarea1.tipo="personal";
			
			tarea2.tarea="correr";
			tarea2.tipo="deporte";
			
			tareas.add(tarea1);
			tareas.add(tarea2);
			
			user.tareas = tareas;
			
			boolean creado = user.validarCrear(user);
			
			assertTrue(creado);
			
			if (creado)
			{
				UsuarioBD userRetrieve = user.findByNombre(user.nombre);
				
				assertNotNull(userRetrieve);
			}
		});
	}


	@Test
	public void comprobarInsercionUsuarioRepetido()
	{
		running(fakeApplication(inMemoryDatabase()), ()->{
			
			UsuarioBD user = new UsuarioBD();
			
			user.nombre="Pepe";
			
			List<TareaBD> tareas = new ArrayList();
			
			TareaBD tarea1 = new TareaBD();
			TareaBD tarea2 = new TareaBD();
			
			tarea1.tarea="cocinar";
			tarea1.tipo="personal";
			
			tarea2.tarea="correr";
			tarea2.tipo="deporte";
			
			tareas.add(tarea1);
			tareas.add(tarea2);
			
			user.tareas = tareas;
			
			boolean creado = user.validarCrear(user);
			
			assertTrue(creado);
			
			if (creado)
			{
				
				boolean creado2 = user.validarCrear(user);
				assertFalse(creado2);
				
				if (creado2 == false)
				{
					UsuarioBD userRetrieve = user.findByNombre(user.nombre);
					
					assertNotNull(userRetrieve);
				}
				
			}
		});
	}
	
	@Test
	public void comprobarBorradoUsuario()
	{
		running(fakeApplication(inMemoryDatabase()), ()->{
			
			UsuarioBD user = new UsuarioBD();
			
			user.nombre="Pepe";
			
			List<TareaBD> tareas = new ArrayList();
			
			TareaBD tarea1 = new TareaBD();
			TareaBD tarea2 = new TareaBD();
			
			tarea1.tarea="cocinar";
			tarea1.tipo="personal";
			
			tarea2.tarea="correr";
			tarea2.tipo="deporte";
			
			tareas.add(tarea1);
			tareas.add(tarea2);
			
			user.tareas = tareas;
			
			boolean creado = user.validarCrear(user);
			
			assertTrue(creado);
			
			if (creado)
			{
				
				boolean borrado = user.validarBorrar(user);
				assertTrue(borrado);
				
			}
		});
	}

}
