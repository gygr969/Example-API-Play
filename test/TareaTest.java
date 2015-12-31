import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

import java.util.ArrayList;
import java.util.List;

import models.TareaBD;
import models.UsuarioBD;

import org.junit.Test;


public class TareaTest 
{
	@Test
	public void comprobarInsercionTareaUsuarioExistente()
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
				List<TareaBD> tareas2 = new ArrayList();
				
				TareaBD tarea3 = new TareaBD();
				TareaBD tarea4 = new TareaBD();
				
				tarea3.tarea="comer";
				tarea3.tipo="personal";
				
				tarea4.tarea="andar";
				tarea4.tipo="deporte";
				
				tareas2.add(tarea3);
				tareas2.add(tarea4);
				
				TareaBD execute = new TareaBD();
				
				boolean insertado = execute.validarInsertar(user, tareas2);
				
				assertTrue(insertado);
				
			}
		});
	}
	
	public void comprobarInsercionTareaUsuarioNoExistente()
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

			TareaBD execute = new TareaBD();
			
			boolean insertado = execute.validarInsertar(user, tareas);
			
			assertFalse(insertado);
			
		});
	}
	
	@Test
	public void comprobarBorradoTarea()
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
				TareaBD tarea = new TareaBD();
				boolean borrado = tarea.validarBorrar(user, tarea1);
				assertTrue(borrado);
				
			}
		});
	}

}
