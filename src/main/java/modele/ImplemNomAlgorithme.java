package modele;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "algo")
public class ImplemNomAlgorithme implements NomAlgorithme {

	private String nom;

	public ImplemNomAlgorithme() {
		this.nom = null;
	}

	public ImplemNomAlgorithme(String nomAlgorithme) {
		this.nom = nomAlgorithme;
	}

	@Override
	@XmlAttribute(name = "nom")
	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NomAlgorithme))
			return false;
		NomAlgorithme arg = (NomAlgorithme) obj;
		return this.getNom().equals(arg.getNom());
	}

	@Override
	public int hashCode() {
		return this.getNom().hashCode();
	}

	@Override
	public String toString() {
		return this.nom;
	}

}
