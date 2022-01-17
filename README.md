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

_En étudiant le code des interfaces **Bibliotheque** et **Portail** (et de leurs interfaces parentes) ainsi que celui de leurs implémentations, déterminer l'ensemble des requêtes **http** acceptées par ces services. On supposera que la bibliothèque est déployée à l'adresse **BIBLIO** et le portail à l'adresse **PORTAIL**._

#### Repertoire

```java
@PUT
@Produces(TYPE_MEDIA)
@Consumes(TYPE_MEDIA)
@ReponsesPUTOption
// Requête (méthode http + url) : PUT + http://localhost:8080/PROJET/portail/
// Corps :
//    <livre>
//      <titre>Services5.6</titre>
//    </livre>
// Réponses (à spécifier par code) :
// - code 200 :
//    <?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
//    <hyperlienuri="http://localhost:8095/bib5/bibliotheque/6"/>
Optional<HyperLien<Livre>> chercher(Livre l);

@PUT
@ReponsesPUTOption
@Path(JAXRS.SOUSCHEMIN_ASYNC)
@Consumes(JAXRS.TYPE_MEDIA)
@Produces(JAXRS.TYPE_MEDIA)
// Requête (méthode http + url) : PUT + http://localhost:8080/PROJET/portail/async
// Corps :
//    <livre>
//      <titre>Services5.6</titre>
//    </livre>
// Réponses (à spécifier par code) :
// - code 200 :
//    <?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
//    <hyperlienuri="http://localhost:8095/bib5/bibliotheque/6"/>
Future<Optional<HyperLien<Livre>>> chercherAsynchrone(Livre l, @Suspended final AsyncResponse ar);

@GET
@Path(SOUSCHEMIN_CATALOGUE)
@Produces(TYPE_MEDIA)
// Requête (méthode http + url) : GET + http://localhost:8080/PROJET/portail/catalogue
// Corps : XHR does not allow payloads for GET request.
// Réponses (à spécifier par code) :
// - code 200 :
//    <?xml version="1.0" encoding="UTF-8" standalone="yes" ?>
//    <liste>
//      <hyperlienuri="http://localhost:8090/bib0/bibliotheque/0"/>
//      <hyperlienuri="http://localhost:8090/bib0/bibliotheque/1"/>
//      ...
//      <hyperlienuri="http://localhost:8090/bib0/bibliotheque/9"/>
//    </liste>
HyperLiens<Livre> repertorier();
```

#### Archive

```java
@Path("{id}")
@ReponsesGETNullEn404
// Adresse de la sous-ressource : http://localhost:8090/bib0/bibliotheque/0
// Requête sur la sous-ressource (méthode http + url) : GET + http://localhost:8090/bib0/bibliotheque/0
// Corps : XHR does not allow payloads for GET request.
// Réponses (à spécifier par code) :
// - code 200 :
//    <livre>
//      <titre>Services0.0</titre>
//    </livre>
Livre sousRessource(@PathParam("id") IdentifiantLivre id) ;

@GET
@Path("{id}")
@Produces(JAXRS.TYPE_MEDIA)
@ReponsesGETNullEn404
// Requête (méthode http + url) : GET + http://localhost:8090/bib0/bibliotheque/0
// Corps : XHR does not allow payloads for GET request.
// Réponses (à spécifier par code) :
// - code 200 :
//    <livre>
//      <titre>Services0.0</titre>
//    </livre>
Livre getRepresentation(@PathParam("id") IdentifiantLivre id);

@POST
@ReponsesPOSTEnCreated
@Consumes(JAXRS.TYPE_MEDIA)
@Produces(JAXRS.TYPE_MEDIA)
// Requête (méthode http + url) : POST + http://localhost:8090/bib0/bibliotheque
// Corps :
//    <livre>
//      <titre>Le titre de mon livre</titre>
//    </livre>
// Réponses (à spécifier par code) :
// - code 201
HyperLien<Livre> ajouter(Livre l);
```

#### AdminAlgo

```java
@PUT
@Path(JAXRS.SOUSCHEMIN_ALGO_RECHERCHE)
@Consumes(JAXRS.TYPE_MEDIA)
// Requête (méthode http + url) : PUT + http://localhost:8080/PROJET/portail/admin/recherche
// Corps :
//    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
//    <algo nom="recherche sync multi"/>
// Réponses (à spécifier par code) :
// - code 204
void changerAlgorithmeRecherche(NomAlgorithme algo);
```

### Couche services - JAX-XB

_En étudiant les interfaces **NomAlgorithme** et **Livre**, donner le schéma et un exemple de données XML pour un nom d'algorithme et un livre. Répondre dans le **readme**._

Il a été utilisé _Generate a Schema from JAXB classes_.

#### NomAlgorithme

##### Schéma

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema">

  <xs:element name="algo" type="NomAlgorithme"/>

  <xs:complexType name="NomAlgorithme">
    <xs:sequence/>
    <xs:attribute name="nom" type="xs:string"/>
  </xs:complexType>
</xs:schema>
```

##### Exemple

```xml
<algo>
 <nom>NomAlgorithme</nom>
</algo>
```

#### Livre

##### Schéma

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xs:schema version="1.0" xmlns:xs="http://www.w3.org/2001/XMLSchema">

  <xs:element name="livre" type="Livre"/>

  <xs:complexType name="Livre">
    <xs:sequence>
      <xs:element name="titre" type="xs:string" minOccurs="0"/>
    </xs:sequence>
  </xs:complexType>
</xs:schema>
```

##### Exemple

```xml
<livre>
 <titre>Livre</titre>
</livre>
```

### Dimension temporelle

Le scénario suivant a été réalisé avec l'extension Talend Open API, il est disponible au fichier suivant : [./public/Open_API-IMT-Services-TP3.json](https://github.com/reifocS/parallelism-and-async/blob/main/public/Open_API-IMT-Services-TP3.json).

La durée du retour des ces requêtes a ensuite été évaluées avec l'onglet Réseau de la fenêtre de développement propre au navigateur web.

Les mesures ont été réalisées successivement sur un ordinateur à 2 coeurs.

| Path                                          | Method | Status | Size (B) | Time (ms) | Body                                        |
| --------------------------------------------- | ------ | ------ | -------- | --------- | ------------------------------------------- |
| http://localhost:8080/portail/catalogue       | GET    | 200    | 6131     | 403       |                                             |
| http://localhost:8090/bib0/bibliotheque/0     | GET    | 200    | 166      | 151       |                                             |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102      | 48        | `<algo nom="recherche sync seq"/>`          |
| http://localhost:8080/portail/                | PUT    | 200    | 269      | 7610      | `<livre><titre>Services5.6</titre></livre>` |
| http://localhost:8080/portail/async           | PUT    | 200    | 269      | 7670      | `<livre><titre>Services5.6</titre></livre>` |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102      | 26        | `<algo nom="recherche async seq"/>`         |
| http://localhost:8080/portail/                | PUT    | 200    | 269      | 1690      | `<livre><titre>Services5.6</titre></livre>` |
| http://localhost:8080/portail/async           | PUT    | 200    | 269      | 1590      | `<livre><titre>Services5.6</titre></livre>` |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102      | 32        | `<algo nom="recherche sync multi"/>`        |
| http://localhost:8080/portail/                | PUT    | 200    | 269      | 194       | `<livre><titre>Services5.6</titre></livre>` |
| http://localhost:8080/portail/async           | PUT    | 200    | 269      | 668       | `<livre><titre>Services5.6</titre></livre>` |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102      | 6         | `<algo nom="recherche async multi"/>`       |
| http://localhost:8080/portail/                | PUT    | 200    | 269      | 405       | `<livre><titre>Services5.6</titre></livre>` |
| http://localhost:8080/portail/async           | PUT    | 200    | 269      | 1780      | `<livre><titre>Services5.6</titre></livre>` |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102      | 1         | `<algo nom="recherche sync stream 8"/>`     |
| http://localhost:8080/portail/                | PUT    | 200    | 269      | 148       | `<livre><titre>Services5.6</titre></livre>` |
| http://localhost:8080/portail/async           | PUT    | 200    | 269      | 164       | `<livre><titre>Services5.6</titre></livre>` |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102      | 7         | `<algo nom="recherche async stream 8"/>`    |
| http://localhost:8080/portail/                | PUT    | 200    | 269      | 2230      | `<livre><titre>Services5.6</titre></livre>` |
| http://localhost:8080/portail/async           | PUT    | 200    | 269      | 1600      | `<livre><titre>Services5.6</titre></livre>` |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102      | 20        | `<algo nom="recherche sync stream rx"/>`    |
| http://localhost:8080/portail/                | PUT    | 200    | 269      | 7570      | `<livre><titre>Services5.6</titre></livre>` |
| http://localhost:8080/portail/async           | PUT    | 200    | 269      | 7570      | `<livre><titre>Services5.6</titre></livre>` |
| http://localhost:8080/portail/admin/recherche | PUT    | 204    | 102      | 16        | `<algo nom="recherche async stream rx"/>`   |
| http://localhost:8080/portail/                | PUT    | 200    | 269      | 7610      | `<livre><titre>Services5.6</titre></livre>` |
| http://localhost:8080/portail/async           | PUT    | 200    | 269      | 7600      | `<livre><titre>Services5.6</titre></livre>` |
