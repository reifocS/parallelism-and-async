# parallelism-and-async

TP3 - Recherche efficace

## Introduction 

Le but du TP est de proposer plusieurs implémentations des méthodes de recherche, en recourant au parallélisme et à la communication asynchrone, et de les comparer.

## Description

### Acteurs

Une bibliothèque stocke des livres et fournit un service de recherche de livres. Un portail référence des bibliothèques et fournit un service de recherche de livres, en redirigeant les requêtes vers les bibliothèques. 

### Intégration

<p><img alt="Modèle Objet - Schéma récapitulatif" src="https://grall.name/teaching/services/2022/medias/libraries.png" width="500"></p>

Le modèle Objet, fourni, contient les interfaces principales suivantes, pour une partie présente dans ce schéma.

- **Repertoire**, décrivant les services de recherche et de production de catalogue
- **Archive**, décrivant les services permettant de gérer les sous-ressources livresques d'une bibliothèque
- **Bibliotheque**, héritière de **Repertoire** et **Archive**, pour décrire une bibliothèque contenant des livres répertoriés
- **AdminAlgo**, interface d'administration permettant de choisir l'algorithme de recherche dans les bibliothèques référencées par un portail
- **Portail**, héritière de **Repertoire** et **AdminAlgo**, pour décrire un portail référençant des bibliothèques
- **Livre**, décrivant le type de données représentant les livres des bibliothèques
- **IdentifiantLivre**, décrivant le type de données permettant d'identifier les livres
- **AlgorithmeRecherche** et **NomAlgorithme** décrivant un algorithme de recherche et son nom, utilisé pour l'administration
## Résultats

### Couche services - JAX-RS

```
- Repertoire {

	@PUT
	@Produces(TYPE_MEDIA)
	@Consumes(TYPE_MEDIA)
	@ReponsesPUTOption
	// Requête (méthode http + url) : 
	// Corps : 
	// Réponses (à spécifier par code) :
	// - code : 
	Optional<HyperLien<Livre>> chercher(Livre l);


	@PUT
	@ReponsesPUTOption
	@Path(JAXRS.SOUSCHEMIN_ASYNC)
	@Consumes(JAXRS.TYPE_MEDIA)
	@Produces(JAXRS.TYPE_MEDIA)
	// Requête (méthode http + url) : 
	// Corps : 
	// Réponses (à spécifier par code) :
	// - code : 
	Future<Optional<HyperLien<Livre>>> chercherAsynchrone(Livre l, @Suspended final AsyncResponse ar);

	@GET
	@Path(SOUSCHEMIN_CATALOGUE)
	@Produces(TYPE_MEDIA)
	// Requête (méthode http + url) : 
	// Corps : 
	// Réponses (à spécifier par code) :
	// - code : 
	HyperLiens<Livre> repertorier();

- Archive 
	@Path("{id}")
	@ReponsesGETNullEn404
	// Adresse de la sous-ressource : 
	// Requête sur la sous-ressource (méthode http + url) : 
	// Corps : 
	// Réponses (à spécifier par code) :
	// - code : 
	Livre sousRessource(@PathParam("id") IdentifiantLivre id) ;

	@Path("{id}")
	@GET 
	@Produces(JAXRS.TYPE_MEDIA)
	@ReponsesGETNullEn404
	// Requête (méthode http + url) : 
	// Corps : 
	// Réponses (à spécifier par code) :
	// - code : 
	Livre getRepresentation(@PathParam("id") IdentifiantLivre id);

	@POST
	@ReponsesPOSTEnCreated
	@Consumes(JAXRS.TYPE_MEDIA)
	@Produces(JAXRS.TYPE_MEDIA)
	// Requête (méthode http + url) : 
	// Corps : 
	// Réponses (à spécifier par code) :
	// - code : 
	HyperLien<Livre> ajouter(Livre l);
}

- AdminAlgo
	@PUT
	@Path(JAXRS.SOUSCHEMIN_ALGO_RECHERCHE)
	@Consumes(JAXRS.TYPE_MEDIA)
	// Requête (méthode http + url) : 
	// Corps : 
	// Réponses (à spécifier par code) :
	// - code : 
	void changerAlgorithmeRecherche(NomAlgorithme algo);

```

### Couche services - JAX-RS

