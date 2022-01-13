package modele;

import java.util.List;
import java.util.Optional;
import javax.ws.rs.client.Client;
import infrastructure.jaxrs.HyperLien;
import infrastructure.jaxrs.Outils;

public class RechercheAsynchroneStreamParallele extends RechercheAsynchroneAbstraite {

	private NomAlgorithme nomAlgorithme;

	public RechercheAsynchroneStreamParallele(String string) {
		this.nomAlgorithme = new ImplemNomAlgorithme(string);
	}

	@Override
	public Optional<HyperLien<Livre>> chercher(Livre livre, List<HyperLien<Bibliotheque>> bibliotheques,
			Client client) {
		return bibliotheques.parallelStream().map((hyperlien) -> rechercheAsync(hyperlien, livre, client))
				.map(Outils::remplirPromesse).filter((x) -> x.isPresent()).findAny().orElse(Optional.empty());
	}

	@Override
	public NomAlgorithme nom() {
		return this.nomAlgorithme;
	}

}
