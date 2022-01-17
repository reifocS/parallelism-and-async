# parallelism-and-async

TP3 - Recherche efficace

## Auteurs

Escoffier Vincent & Jallais Adrien

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

*En étudiant le code des interfaces **Bibliotheque** et **Portail** (et de leurs interfaces parentes) ainsi que celui de leurs implémentations, déterminer l'ensemble des requêtes **http** acceptées par ces services. On supposera que la bibliothèque est déployée à l'adresse **BIBLIO** et le portail à l'adresse **PORTAIL**. Placer votre réponse dans un fichier **readme**.*

#### Repertoire

```
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
```

#### Archive 

```
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
```

#### AdminAlgo

```
@PUT
@Path(JAXRS.SOUSCHEMIN_ALGO_RECHERCHE)
@Consumes(JAXRS.TYPE_MEDIA)
// Requête (méthode http + url) : 
// Corps : 
// Réponses (à spécifier par code) :
// - code : 
void changerAlgorithmeRecherche(NomAlgorithme algo);
```

### Couche services - JAX-XB

*En étudiant les interfaces **NomAlgorithme** et **Livre**, donner le schéma et un exemple de données XML pour un nom d'algorithme et un livre. Répondre dans le **readme**.*

#### NomAlgorithme

##### Schéma

```
```

##### Exemple

```
```

#### Livre

##### Schéma

```
```

##### Exemple

```
```

### Dimension temporelle

Mesures réalisées à la suite sur un ordinateur à 2 coeurs.

| Path                                          | Method | Status | Size   | Time (ms) |
| --------------------------------------------- | ------ | ------ | ------ | --------- |
| http://localhost:8080/portail/catalogue       | GET    | 200    | 6.2 kB | 393       |
| http://localhost:8090/bib0/bibliotheque/0     | GET    | 200    | 166 B  | 351       |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102 B  | 14        |
| http://localhost:8080/portail/                | PUT    | 200    | 269 B  | 7570      |
| http://localhost:8080/portail/async           | PUT    | 200    | 269 B  | 7570      |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102 B  | 17        |
| http://localhost:8080/portail/                | PUT    | 200    | 269 B  | 1600      |
| http://localhost:8080/portail/async           | PUT    | 200    | 269 B  | 16400     |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102 B  | 18        |
| http://localhost:8080/portail/                | PUT    | 200    | 269 B  | 241       |
| http://localhost:8080/portail/async           | PUT    | 200    | 269 B  | 212       |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102 B  | 266       |
| http://localhost:8080/portail/                | PUT    | 200    | 269 B  | 200       |
| http://localhost:8080/portail/async           | PUT    | 200    | 269 B  | 399       |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102 B  | 10        |
| http://localhost:8080/portail/                | PUT    | 200    | 269 B  | 170       |
| http://localhost:8080/portail/async           | PUT    | 200    | 269 B  | 560       |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102 B  | 200       |
| http://localhost:8080/portail/                | PUT    | 200    | 269 B  | 1520      |
| http://localhost:8080/portail/async           | PUT    | 200    | 269 B  | 1640      |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102 B  | 11        |
| http://localhost:8080/portail/                | PUT    | 200    | 269 B  | 1540      |
| http://localhost:8080/portail/async           | PUT    | 200    | 269 B  | 1520      |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102 B  | 31        |
| http://localhost:8080/portail/                | PUT    | 200    | 269 B  | 1550      |
| http://localhost:8080/portail/async           | PUT    | 200    | 269 B  | 1550      |




