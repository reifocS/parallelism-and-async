package modele;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.client.Client;

import infrastructure.jaxrs.HyperLien;
import infrastructure.jaxrs.Outils;

public class RechercheSynchroneSequentielle extends RechercheSynchroneAbstraite implements AlgorithmeRecherche {
	
	private NomAlgorithme nomAlgorithme;
	

	public RechercheSynchroneSequentielle(String string) {
		// TODO Auto-generated constructor stub
		this.nomAlgorithme = new ImplemNomAlgorithme(string);
	}

	@Override
	public Optional<HyperLien<Livre>> chercher(Livre l, List<HyperLien<Bibliotheque>> bibliotheques, Client client) {
		Outils.afficherInfoTache(nom().getNom());
		Optional<HyperLien<Livre>> found = null;
		for (HyperLien<Bibliotheque> lien : bibliotheques) {

			found = rechercheSync(lien, l, client);

			if (found.isPresent()) {
				return found;
			}
		}
		return found;
	}

	@Override
	public NomAlgorithme nom() {
		return this.nomAlgorithme;
	}

}