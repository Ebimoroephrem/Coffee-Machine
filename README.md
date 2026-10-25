# Coffee Machine 

## Présentation

Coffee Machine est un projet Java qui simule le fonctionnement d'une machine à café. Il a été développé en cinq itérations afin d'ajouter progressivement la gestion des boissons, des paiements, des ventes et des ruptures de stock.

## Fonctionnalités réalisées

- Itération 1 — Commandes : préparation des boissons et gestion des messages.
- Itération 2 — Paiements : vérification du montant payé et signalement du montant insuffisant.
- Itération 3 — Boissons : ajout du jus d'orange et gestion des boissons extra-chaudes.
- Itération 4 — Rapport des ventes : comptage des boissons vendues et calcul des recettes.
- Itération 5 — Ruptures de stock : vérification de la disponibilité, affichage d'un message et appel du service de notification par e-mail.

## Technologies utilisées

- Java
- Maven
- JUnit 5
- Mockito

## Prérequis

- Java JDK installé.
- Maven installé, ou le wrapper Maven fourni avec le projet.

## Installation

Cloner le dépôt :

```bash
git clone https://github.com/Ebimoroephrem/coffee-machine.git
cd coffee-machine
```

## Exécution des tests

À la racine du projet, exécuter :

bash
mvn test


### Résultat des tests

Dernier résultat obtenu :

text
Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS


Les 20 tests passent avec succès dans la version testée.

## Étapes de réalisation

1. Mise en place de la logique de commande et du protocole de communication avec la machine.
2. Ajout de la vérification du paiement.
3. Prise en charge du jus d'orange et des boissons extra-chaudes.
4. Création du rapport des ventes.
5. Ajout du contrôle des stocks et de la notification en cas de rupture.

Chaque fonctionnalité a été accompagnée de tests unitaires afin de vérifier son comportement et de limiter les régressions.

 ### Difficultés rencontrées

- Respecter le format exact des commandes envoyées à la machine.
- Gérer les commandes sans sucre et les boissons extra-chaudes.
- Vérifier le montant payé avant de préparer une boisson.
- Enregistrer uniquement les ventes valides.
- Tester le comportement en cas de rupture de stock avec Mockito.
- Vérifier que les modifications ne cassent pas les fonctionnalités précédentes.



