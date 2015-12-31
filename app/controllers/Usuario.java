package controllers;

import play.*;
import play.mvc.*;
import models.TareaBD;
import models.UsuarioBD;
import play.data.*;
import views.html.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.*;

import play.cache.Cache;
import play.data.Form;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

public class Usuario extends Controller 
{
	public Result crear()
    {
    	/*Cabeceras Content-Type application/json*/
    	/*Relación 1-N {"nombre":"manolito","tareas":[{"tarea":"estudiar","tipo":"profesional"},{"tarea":"comer","tipo":"personal"}]}*/
    	Form<UsuarioBD> form = Form.form(UsuarioBD.class).bindFromRequest();
    	
    	if (form.hasErrors()) 
    	{
    		return badRequest(form.errorsAsJson());
    	}
    	
    	UsuarioBD user = form.get();
    	
    	if(user.findByNombre(user.nombre) == null)
    	{
    		user.save();
    		System.out.println("Mensaje consola: usuario insertado en la BD");
    		return ok(Messages.get("inserted"));
    	}
    	System.out.println("Mensaje consola: usuario duplicado");
    	return badRequest(Messages.get("duplicated"));
    }
	
	public Result obtenerTodos()
    {
    	List<UsuarioBD> users = UsuarioBD.listar();
    	
    	response().setHeader("User-Count", String.valueOf(users.size()));
    	
    	if (request().accepts("application/xml")) 
		{
		    return ok(views.xml.usuarios.render(users));
		}
		else if (request().accepts("application/json")) 
		{
	    	ObjectNode result = Json.newObject();
	    	ArrayNode jsonArray = Json.newArray();
	    	for(UsuarioBD user: users)
	    	{
	    		ObjectNode userJSON = Json.newObject();
	    		userJSON.put("id", user.id);
	    		userJSON.put("nombre", user.nombre);
		        jsonArray.add(userJSON);
		        
		        ArrayNode jsonArray2 = Json.newArray();
		        List<TareaBD> tareas = user.getTareas();
		        
		        for (TareaBD tarea: tareas) {
			    	ObjectNode tareaJSON = Json.newObject();
			    	tareaJSON.put("idTarea", tarea.id);
			    	tareaJSON.put("tarea", tarea.tarea);
			    	tareaJSON.put("tipo", tarea.tipo);
			        jsonArray2.add(tareaJSON);
			    }
			    userJSON.putPOJO("tareas", jsonArray2); 
	    	}
	    	result.putPOJO("usuarios", jsonArray);
	    	
	    	return ok(result);
		}
    	
    	return notFound(Messages.get("notfounds"));
    }
	
	/*http://localhost:9000/usuario/?nombre=perico*/
	public Result obtenerUsuario()
    {
		String nombre = request().getQueryString("nombre");
		if(nombre != null)
		{
			UsuarioBD user = new UsuarioBD();
			user.nombre = nombre;
			
			UsuarioBD bdUser = (UsuarioBD) Cache.get("usuario-"+user.nombre);
	    	if (bdUser == null) {
	    		bdUser = user.findByNombre(user.nombre);
	    		Cache.set("usuario-"+user.nombre, bdUser);
	    	}
			
			if(bdUser != null)
	    	{
				System.out.println("Mensaje consola: usuario encontrado");
				
				if (request().accepts("application/xml")) 
				{
					List<TareaBD> tareas = user.getTareas();
				    response().setHeader("Task-Count", String.valueOf(tareas.size()));
				    return ok(views.xml.usuario.render(bdUser));
				}
				else if (request().accepts("application/json")) 
				{
					ObjectNode result = Json.newObject();
					result.put("id", bdUser.id);
					result.put("nombre", bdUser.nombre);
					
					List<TareaBD> tareas = bdUser.getTareas();
					response().setHeader("Task-Count", String.valueOf(tareas.size()));
					ArrayNode jsonArray = Json.newArray();
					for(TareaBD tarea: tareas)
					{
						ObjectNode tareaJSON = Json.newObject();
				    	tareaJSON.put("idTarea", tarea.id);
				    	tareaJSON.put("tarea", tarea.tarea);
				    	tareaJSON.put("tipo", tarea.tipo);
				        jsonArray.add(tareaJSON);
					}
					result.putPOJO("tareas", jsonArray);
					return ok(result);
				}
				
	    	}
			return notFound(Messages.get("notfound"));
		}
		return badRequest(Messages.get("paramname"));
    }
	
	public Result borrar()
    {
		String nombre = request().getQueryString("nombre");
		if(nombre != null)
		{
			UsuarioBD user = new UsuarioBD();
			user.nombre = nombre;
			
			if(user.findByNombre(user.nombre) != null)
	    	{
				user = user.findByNombre(user.nombre);
				user.delete();
				
				return ok("User deleted");
	    	}
			return notFound(Messages.get("notfound"));
		}
		return badRequest(Messages.get("paramname"));
		
    }
}
