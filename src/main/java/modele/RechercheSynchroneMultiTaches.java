package modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.client.Client;

import infrastructure.jaxrs.HyperLien;
import infrastructure.jaxrs.Outils;

public class RechercheSynchroneMultiTaches extends RechercheSynchroneAbstraite implements AlgorithmeRecherche {

	private NomAlgorithme nomAlgorithme;
	private ExecutorService exec ;

	public RechercheSynchroneMultiTaches(String nom) {
		this.nomAlgorithme = new ImplemNomAlgorithme(nom);
		this.exec = Executors.newCachedThreadPool();

	}

	@Override
	public Optional<HyperLien<Livre>> chercher(Livre l, List<HyperLien<Bibliotheque>> bibliotheques, Client client) {

		Outils.afficherInfoTache(nom().getNom());

		CountDownLatch countdown = new CountDownLatch(bibliotheques.size());
		AtomicReference<Optional<HyperLien<Livre>>> ret= new AtomicReference<>(Optional.empty());

		for (HyperLien<Bibliotheque> lien : bibliotheques) {

			exec.submit(() -> {

				Optional<HyperLien<Livre>> ressource = rechercheSync(lien, l, client);

				if (!ressource.isPresent()) {
					countdown.countDown();
				} else {
					while (countdown.getCount() > 0) {
						countdown.countDown();
					}
					ret.set(ressource);
				}
			});
		}

		try {
			countdown.await();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret.get();

	}

	@Override
	public NomAlgorithme nom() {
		return this.nomAlgorithme;
	}

}