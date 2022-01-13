package modele;

import java.util.Optional;
import java.util.concurrent.Future;

import infrastructure.jaxrs.HyperLien;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.InvocationCallback;

public abstract class RechercheAsynchroneAbstraite implements AlgorithmeRecherche {

	abstract protected Future<Optional<HyperLien<Livre>>> rechercheAsync(HyperLien<Bibliotheque> h, Livre l, Client client);

	abstract protected Future<Optional<HyperLien<Livre>>> rechercheAsyncAvecRappel(
			HyperLien<Bibliotheque> h, Livre l, Client client,  
			InvocationCallback<Optional<HyperLien<Livre>>> retour);
}
