package modele;

import java.util.Optional;
import java.util.concurrent.Future;
import infrastructure.jaxrs.HyperLien;
import infrastructure.langage.Types;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;

import configuration.JAXRS;

public abstract class RechercheAsynchroneAbstraite implements AlgorithmeRecherche {
		
	protected Future<Optional<HyperLien<Livre>>> rechercheAsync(HyperLien<Bibliotheque> h, Livre l, Client client) {
		WebTarget cible = client.target(h.getUri());
		return cible
				.path(JAXRS.SOUSCHEMIN_ASYNC)
				.request()
				.accept(JAXRS.TYPE_MEDIATYPE)
				.async()
				.put(Entity.entity(l, JAXRS.TYPE_MEDIATYPE), Types.typeRetourChercherAsync());
	}

	protected Future<Optional<HyperLien<Livre>>> rechercheAsyncAvecRappel(
			HyperLien<Bibliotheque> h,
			Livre l,
			Client client,  
			InvocationCallback<Optional<HyperLien<Livre>>> retour) {
		WebTarget cible = client.target(h.getUri());
		return cible
				.path(JAXRS.SOUSCHEMIN_ASYNC)
				.request()
				.accept(JAXRS.TYPE_MEDIATYPE)
				.async()
				.put(Entity.entity(l, JAXRS.TYPE_MEDIATYPE), retour);
	}
 
}