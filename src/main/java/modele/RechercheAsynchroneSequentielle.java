package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.client.Client;

import infrastructure.jaxrs.HyperLien;
import infrastructure.jaxrs.Outils;

public class RechercheAsynchroneSequentielle extends RechercheAsynchroneAbstraite {

	private NomAlgorithme nomAlgorithme;

	public RechercheAsynchroneSequentielle(String nomAlgorithme) {
		this.nomAlgorithme = new ImplemNomAlgorithme(nomAlgorithme);
	}

	@Override
	public Optional<HyperLien<Livre>> chercher(Livre l, List<HyperLien<Bibliotheque>> bibliotheques, Client client) {
		Outils.afficherInfoTache(nom().getNom());

		List<Future<Optional<HyperLien<Livre>>>> promesses = new ArrayList<>();
		for (HyperLien<Bibliotheque> h : bibliotheques) {
			promesses.add(rechercheAsync(h, l, client));
		}
		Optional<HyperLien<Livre>> ret;
		try {
			for (Future<Optional<HyperLien<Livre>>> promesse : promesses) {
				ret = promesse.get();
				if (ret.isPresent()) {
					return ret;
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	@Override
	public NomAlgorithme nom() {
		// TODO Auto-generated method stub
		return this.nomAlgorithme;
	}

}