package modele;

import java.util.Optional;

import javax.ws.rs.container.AsyncResponse;

import infrastructure.jaxrs.HyperLien;
import infrastructure.jaxrs.Outils;

public class ImplementationAppelsAsynchrones {

	// Version asynchrone
	public static void rechercheAsynchroneBibliotheque(Repertoire bib, Livre l, AsyncResponse ar) {
		Outils.afficherInfoTache("recherche aynchrone");
		Optional<HyperLien<Livre>> h = bib.chercher(l);
		ar.resume(h);
		// Fonctionnement d'un appel asynchrone
		// - Le client réalise un appel de la méthode distante. Cet appel renvoie
		// immédiatement une promesse de type Future, qui contient le canal de retour sur lequel la
		// réponse arrivera.
		// - Le serveur reçoit la requête correspondante et la traite. Il envoie la
		// réponse au client via le canal de retour : voir ar.resume.
		// - Le client peut tester si la promesse est réalisée ou non. Elle l'est
		// lorsque la réponse du serveur parvient au client.
	}
}
