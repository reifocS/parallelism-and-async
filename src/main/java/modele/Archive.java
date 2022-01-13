package modele;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import configuration.JAXRS;
import infrastructure.jaxrs.HyperLien;
import infrastructure.jaxrs.annotations.ReponsesPOSTEnCreated;
import infrastructure.jaxrs.annotations.ReponsesGETNullEn404;

public interface Archive {
	@Path("{id}")
	@ReponsesGETNullEn404
	Livre sousRessource(@PathParam("id") IdentifiantLivre id) ;
	
	@Path("{id}")
	@GET 
	@Produces(JAXRS.TYPE_MEDIA)
	@ReponsesGETNullEn404
	Livre getRepresentation(@PathParam("id") IdentifiantLivre id);
	
	@POST
	@ReponsesPOSTEnCreated
	@Consumes(JAXRS.TYPE_MEDIA)
	@Produces(JAXRS.TYPE_MEDIA)
	HyperLien<Livre> ajouter(Livre l);
}
