package models;

import java.util.List;

import javax.persistence.*;
import javax.validation.Valid;

import play.data.validation.Constraints.Required;

import com.avaje.ebean.Model;

@Entity
public class UsuarioBD extends Model
{
	@Id
	@SequenceGenerator( name = "userSeq", allocationSize = 10000, initialValue = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "userSeq" )
    public Long id;
	
	@Required
	@Nombre
	public String nombre;
	
	public static final Find<Long,UsuarioBD> find =  new Find<Long,UsuarioBD>(){};
	
	public static UsuarioBD findByNombre(String nombre) 
	{		
		return find.where().eq("nombre", nombre).findUnique();
	}
	
	public static List<UsuarioBD> listar()
	{
		return find.all();
	}
	
	public static boolean validarCrear(UsuarioBD user) 
	{		
		if(find.where().eq("nombre", user.nombre).findUnique() == null)
		{
			user.save();
			return true;
		}
		return false;
	}
	
	public static boolean validarBorrar(UsuarioBD user) 
	{		
		if(find.where().eq("nombre", user.nombre).findUnique() != null)
		{
			user.delete();
			return true;
		}
		return false;
	}
	
	/*Relacion 1-1*/
	/*@OneToOne(cascade=CascadeType.ALL)
	public Cuenta cuenta;*/
	
	/*Relacion 1-N*/
	@Valid
	@OneToMany(cascade=CascadeType.ALL, mappedBy="usuario")
	public List<TareaBD> tareas;
	
	public Long getId()
	{
		return id;
	}
	
	public String getNombre()
	{
		return nombre;
	}
	
	public List<TareaBD> getTareas() 
	{
		return tareas;
	}

}
