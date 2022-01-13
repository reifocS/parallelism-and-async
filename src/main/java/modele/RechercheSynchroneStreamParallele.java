package modele;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.ws.rs.client.Client;

import infrastructure.jaxrs.HyperLien;

public class RechercheSynchroneStreamParallele extends RechercheSynchroneAbstraite implements AlgorithmeRecherche {

	private NomAlgorithme nomAlgorithme;

	public RechercheSynchroneStreamParallele(String string) {
		this.nomAlgorithme = new ImplemNomAlgorithme(string);
	}

	@Override
	public Optional<HyperLien<Livre>> chercher(Livre livre, List<HyperLien<Bibliotheque>> bibliotheques,
			Client client) {
		Stream<Optional<HyperLien<Livre>>> res = bibliotheques.parallelStream()
				.map((hyperlien) -> this.rechercheSync(hyperlien, livre, client));
		res = res.filter(hyperlien -> (hyperlien).isPresent());
		//findAny comme le flux sur lequel on travaille n'a pas d'ordre de rencontre défini 
		return res.findAny().orElse(Optional.empty());
	}

	@Override
	public NomAlgorithme nom() {
		return nomAlgorithme;
	}

}