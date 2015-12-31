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

public class Tarea extends Controller
{
	
	public Result insertar()
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
    		System.out.println("Mensaje consola: usuario no existe");
    		return badRequest(Messages.get("notexist"));
    	}
    	
    	else
    	{
    		List<TareaBD> tareas = user.getTareas();
    		user = user.findByNombre(user.nombre);
    		for (TareaBD tarea: tareas)
    		{
    			tarea.setUsuario(user);
    			tarea.save();
    		}
    		System.out.println("Mensaje consola: tareas de usuario añadidas");
    		
    	    
    	    return ok(Messages.get(Messages.get("insertedtask")));
    	}
    }
	
	public Result borrar()
    {
		String tareaNombre = request().getQueryString("tarea");
		String nombre = request().getQueryString("nombre");
		if(nombre != null && tareaNombre != null)
		{
			UsuarioBD user = new UsuarioBD();
			user.nombre = nombre;
			
			if(user.findByNombre(user.nombre) != null)
	    	{
				user = user.findByNombre(user.nombre);
				TareaBD tarea = new TareaBD();
				
				if(tarea.findByTareaUsuario(tareaNombre, user) != null)
				{
					tarea = tarea.findByTareaUsuario(tareaNombre, user);
					tarea.delete();
					return ok(Messages.get("deletedtask"));
				}
				return notFound(Messages.get("notfoundtask"));
	    	}
			return notFound(Messages.get("notfound"));
		}
		return badRequest(Messages.get("paramname"));
    }
	
	public Result obtenerTodas()
    {
    	List<TareaBD> tareas = TareaBD.listar();
    	
    	response().setHeader("Task-Count", String.valueOf(tareas.size()));
    	
    	if (request().accepts("application/xml")) 
		{
		    return ok(views.xml.tareas.render(tareas));
		}
		else if (request().accepts("application/json")) 
		{
	    	ObjectNode result = Json.newObject();
	    	ArrayNode jsonArray = Json.newArray();
	    	for(TareaBD tarea: tareas)
	    	{
	    		ObjectNode tareaJSON = Json.newObject();
	    		tareaJSON.put("id", tarea.id);
	    		tareaJSON.put("tarea", tarea.tarea);
	    		tareaJSON.put("tipo", tarea.tipo);
	    		tareaJSON.put("idUsuario", tarea.usuario.id);
		        jsonArray.add(tareaJSON);
	    	}
	    	result.putPOJO("tareas", jsonArray);
	    	
	    	return ok(result);
		}
    	
    	return notFound(Messages.get("notfoundtasks"));
    }
	
	public Result obtener()
    {
		String tareaNombre = request().getQueryString("tarea");
		if(tareaNombre != null)
		{
			TareaBD tarea = new TareaBD();
			
			if(tarea.findByNombre(tareaNombre) != null)
			{
				List<TareaBD> tareas = tarea.findByNombre(tareaNombre);
				response().setHeader("Task-Count", String.valueOf(tareas.size()));
				if(tareas.size() > 0)
				{
					if (request().accepts("application/xml")) 
					{
					    return ok(views.xml.tareas.render(tareas));
					}
					else if (request().accepts("application/json")) 
					{
				    	ObjectNode result = Json.newObject();
				    	ArrayNode jsonArray = Json.newArray();
				    	for(TareaBD tarea2: tareas)
				    	{
				    		ObjectNode tareaJSON = Json.newObject();
				    		tareaJSON.put("id", tarea2.id);
				    		tareaJSON.put("tarea", tarea2.tarea);
				    		tareaJSON.put("tipo", tarea2.tipo);
				    		tareaJSON.put("idUsuario", tarea2.usuario.id);
					        jsonArray.add(tareaJSON);
				    	}
				    	result.putPOJO("tareas", jsonArray);
				    	return ok(result);
					}
				}
			}
			return notFound(Messages.get("notfoundtasks"));
		}
		return badRequest(Messages.get("paramtask"));
    }
	
	public Result obtenerPorTipo()
    {
		String tipo = request().getQueryString("tipo");
		if(tipo != null)
		{
			TareaBD tarea = new TareaBD();
			
			if(tarea.findByTipo(tipo) != null)
			{
				List<TareaBD> tareas = tarea.findByTipo(tipo);
				response().setHeader("Task-Count", String.valueOf(tareas.size()));
				
				if(tareas.size() > 0)
				{
					if (request().accepts("application/xml")) 
					{
					    return ok(views.xml.tareas.render(tareas));
					}
					else if (request().accepts("application/json")) 
					{
				    	ObjectNode result = Json.newObject();
				    	ArrayNode jsonArray = Json.newArray();
				    	for(TareaBD tarea2: tareas)
				    	{
				    		ObjectNode tareaJSON = Json.newObject();
				    		tareaJSON.put("id", tarea2.id);
				    		tareaJSON.put("tarea", tarea2.tarea);
				    		tareaJSON.put("tipo", tarea2.tipo);
				    		tareaJSON.put("idUsuario", tarea2.usuario.id);
					        jsonArray.add(tareaJSON);
				    	}
				    	result.putPOJO("tareas", jsonArray);
				    	return ok(result);
					}
				}
			}
			return notFound(Messages.get("notfoundtasks"));
		}
		return badRequest(Messages.get("paramtype"));
    }
	
	public Result obtenerPorTareaTipo()
    {
		String tareaNombre = request().getQueryString("tarea");
		String tipo = request().getQueryString("tipo");
		if(tareaNombre != null && tipo != null)
		{
			TareaBD tarea = new TareaBD();
			
			if(tarea.findByNombreTipo(tareaNombre,tipo) != null)
			{
				List<TareaBD> tareas = tarea.findByNombreTipo(tareaNombre,tipo);
				response().setHeader("Task-Count", String.valueOf(tareas.size()));
				
				if(tareas.size() > 0)
				{
					if (request().accepts("application/xml")) 
					{
					    return ok(views.xml.tareas.render(tareas));
					}
					else if (request().accepts("application/json")) 
					{
				    	ObjectNode result = Json.newObject();
				    	ArrayNode jsonArray = Json.newArray();
				    	for(TareaBD tarea2: tareas)
				    	{
				    		ObjectNode tareaJSON = Json.newObject();
				    		tareaJSON.put("id", tarea2.id);
				    		tareaJSON.put("tarea", tarea2.tarea);
				    		tareaJSON.put("tipo", tarea2.tipo);
				    		tareaJSON.put("idUsuario", tarea2.usuario.id);
					        jsonArray.add(tareaJSON);
				    	}
				    	result.putPOJO("tareas", jsonArray);
				    	return ok(result);
					}
				}
			}
			return notFound(Messages.get("notfoundtasks"));
		}
		return badRequest(Messages.get("paramnametype"));
    }
	
	public Result obtenerPorUsuario()
    {
		String usuario = request().getQueryString("usuario");
		if(usuario != null)
		{
			TareaBD tarea = new TareaBD();
			
			if(tarea.findByUsuario(usuario) != null)
			{
				List<TareaBD> tareas = tarea.findByUsuario(usuario);
				response().setHeader("Task-Count", String.valueOf(tareas.size()));
				
				if(tareas.size() > 0)
				{
					if (request().accepts("application/xml")) 
					{
					    return ok(views.xml.tareas.render(tareas));
					}
					else if (request().accepts("application/json")) 
					{
				    	ObjectNode result = Json.newObject();
				    	ArrayNode jsonArray = Json.newArray();
				    	for(TareaBD tarea2: tareas)
				    	{
				    		ObjectNode tareaJSON = Json.newObject();
				    		tareaJSON.put("id", tarea2.id);
				    		tareaJSON.put("tarea", tarea2.tarea);
				    		tareaJSON.put("tipo", tarea2.tipo);
				    		tareaJSON.put("idUsuario", tarea2.usuario.id);
					        jsonArray.add(tareaJSON);
				    	}
				    	result.putPOJO("tareas", jsonArray);
				    	return ok(result);
					}
				}
			}
			return notFound(Messages.get("notfoundtasks"));
		}
		return badRequest(Messages.get("paramuser"));
    }
	
	public Result obtenerPorId()
    {
		String id = request().getQueryString("id");
		if(id != null)
		{
			Long idLong = Long.parseLong(id);
			
			TareaBD tarea = new TareaBD();
			tarea.id = idLong;
			
			TareaBD bdTarea = (TareaBD) Cache.get("tarea-"+tarea.id);
	    	if (bdTarea == null) {
	    		bdTarea = tarea.findById(id);
	    		Cache.set("usuario-"+tarea.id, bdTarea);
	    	}
			
			if(bdTarea != null)
			{
				if (request().accepts("application/xml")) 
				{
				    return ok(views.xml.tarea.render(bdTarea));
				}
				else if (request().accepts("application/json")) 
				{
			    	ObjectNode result = Json.newObject();
			    	result.put("id", bdTarea.id);
			    	result.put("tarea", bdTarea.tarea);
			    	result.put("tipo", bdTarea.tipo);
			    	result.put("idUsuario", bdTarea.usuario.id);
			    	return ok(result);
				}
			}
			return notFound(Messages.get("notfoundtasks"));
		}
		return badRequest(Messages.get("paramid"));
    }
}
