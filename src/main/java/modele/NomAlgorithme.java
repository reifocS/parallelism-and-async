package modele;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="algo")
public interface NomAlgorithme {
	String getNom();
}
