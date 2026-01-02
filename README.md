# Système de Gestion d’Agence Immobilière

## Description
Application Java développée dans le cadre d’un projet universitaire (L2 Informatique)
permettant la gestion complète d’une agence immobilière.

Le système centralise la gestion des :
- biens immobiliers
- clients
- agents
- visites
- contrats

La persistance des données est assurée par la sérialisation des objets Java.

## Fonctionnalités
- Ajout, suppression et recherche des biens immobiliers
- Gestion des clients (coordonnées, recherche par identifiant)
- Gestion des agents immobiliers
- Planification et gestion des visites
- Création et gestion des contrats (vente et location)
- Interface graphique développée avec Java Swing

## Architecture
Le projet suit une architecture MVC :

### Modèle (Model)
- Classes métiers : Bien, Client, Agent, Visite, Contrat
- Sérialisation des données dans des fichiers `.dat`
- Méthodes CRUD

### Contrôleur (Controller)
- Gestion de la logique métier
- Validation des données
- Interaction entre la vue et le modèle

### Vue (View)
- Interfaces graphiques Java Swing
- Formulaires, tableaux et navigation par boutons

## Technologies utilisées
- Java
- Java Swing
- Architecture MVC
- Sérialisation des objets Java

## Exécution
Le projet peut être exécuté avec un IDE Java :
- Visual Studio Code
- IntelliJ IDEA
- Eclipse

## Auteur
Cirine Belkacemi  
Université d’Évry Paris-Saclay – L2 Informatique
