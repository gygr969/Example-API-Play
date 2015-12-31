package controllers;

import play.*;
import play.mvc.*;
import models.TareaBD;
import play.data.*;

import views.html.*;

public class Application extends Controller 
{

    public Result index() 
    {
    	return redirect(routes.Usuario.obtenerTodos());
    }

}
