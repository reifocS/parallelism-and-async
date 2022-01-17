package modele;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.client.Client;

import infrastructure.jaxrs.HyperLien;
import infrastructure.jaxrs.Outils;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RechercheAsynchroneStreamRx extends RechercheAsynchroneAbstraite {

	private NomAlgorithme nomAlgorithme;

	public RechercheAsynchroneStreamRx(String nomAlgorithme) {
		this.nomAlgorithme = new ImplemNomAlgorithme(nomAlgorithme);
	}

	@Override
	public Optional<HyperLien<Livre>> chercher(Livre l, List<HyperLien<Bibliotheque>> bibliotheques, Client client) {

		Outils.afficherInfoTache(nom().getNom());

		return Observable.fromIterable(bibliotheques)
				.flatMap(h -> Observable.fromFuture(this.rechercheAsync(h, l, client))).subscribeOn(Schedulers.io())
				.filter((x) -> x.isPresent()).blockingFirst(Optional.empty());
	}

	@Override
	public NomAlgorithme nom() {
		// TODO Auto-generated method stub
		return nomAlgorithme;
	}

}