package ws;

import javax.ws.rs.core.Application;
import java.util.Set;

@javax.ws.rs.ApplicationPath("resources")
public class ApplicationConfig extends Application{

	
	public Set<Class<?>> getClasses(){
		
		return getRestResourceClasses();
	}
	private Set<Class<?>> getRestResourceClasses() {
		Set<Class<?>> resource=
				new java.util.HashSet<Class<?>>();
		
		resource.add(ws.Service.class);
		
		return resource;
		
	}
}
