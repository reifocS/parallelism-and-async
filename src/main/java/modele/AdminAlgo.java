package modele;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import configuration.JAXRS;

public interface AdminAlgo {
	@PUT
	@Path(JAXRS.SOUSCHEMIN_ALGO_RECHERCHE)
	@Consumes(JAXRS.TYPE_MEDIA)
	void changerAlgorithmeRecherche(NomAlgorithme algo);
}
