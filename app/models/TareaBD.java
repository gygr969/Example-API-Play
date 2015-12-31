package models;

import java.util.*;

import com.avaje.ebean.Model;

import play.data.validation.Constraints.*;

import javax.persistence.*;

@Entity
public class TareaBD extends Model
{	
	@Id
	@SequenceGenerator( name = "tareaSeq",  allocationSize = 10000, initialValue = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "tareaSeq" )
	public long id;
	
	@Required
	@NombreTarea
	public String tarea;
	
	@Required
	@Tipo
	public String tipo;
	
	@ManyToOne
	public UsuarioBD usuario;
	
	public static final Find<Long,TareaBD> find =  new Find<Long,TareaBD>(){};
	public static final Find<Long,UsuarioBD> find2 =  new Find<Long,UsuarioBD>(){};
	
	public static List<TareaBD> listar()
	{
		return find.all();
	}
	
	public static TareaBD findByTareaUsuario(String tarea, UsuarioBD user) 
	{		
		return find.where().eq("tarea", tarea).eq("usuario", user).findUnique();
	}
	
	public static List<TareaBD> findByNombre(String tarea) 
	{		
		return find.where().eq("tarea", tarea).findList();
	}
	
	public static List<TareaBD> findByTipo(String tipo) 
	{		
		return find.where().eq("tipo", tipo).findList();
	}
	
	public static List<TareaBD> findByNombreTipo(String tarea,String tipo) 
	{		
		return find.where().eq("tarea", tarea).eq("tipo", tipo).findList();
	}
	
	public static List<TareaBD> findByUsuario(String usuario) 
	{		
		return find.where().eq("usuario.nombre", usuario).findList();
	}
	
	public static TareaBD findById(String id) 
	{		
		return find.where().eq("id", id).findUnique();
	}
	
	public static boolean validarInsertar(UsuarioBD user, List<TareaBD> tareas) 
	{
		if(find2.where().eq("nombre", user.nombre).findUnique() != null)
		{
			for (TareaBD tarea: tareas)
			{
				tarea.setUsuario(user);
				tarea.save();
			}
			return true;
		}
		
		return false;
		
	}
	
	public static boolean validarBorrar(UsuarioBD user, TareaBD tarea) 
	{		
		if(find.where().eq("tarea", tarea.tarea).eq("usuario", user).findUnique() != null)
		{
			tarea.delete();
			return true;
		}
		return false;
	}

	public void setUsuario(UsuarioBD user) 
	{
		this.usuario = user;
	}

}
