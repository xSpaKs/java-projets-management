# Gestion des Employés et Projets

## Description du Projet
Ce projet est une application Java avec interface graphique développée pour une petite entreprise souhaitant gérer efficacement les informations sur ses employés et les projets auxquels ils participent. L'application permet de réaliser les opérations suivantes :

- Gestion des Employés : Ajouter, consulter, modifier et supprimer des employés.
- Gestion des Projets : Ajouter, consulter, modifier et supprimer des projets, ainsi qu'assigner des employés à ces projets.
  
## Fonctionnalités

### Module Employés
- Ajouter un Employé : Saisie des informations telles que le nom, le prénom, le poste, l'email et le téléphone.
- Consulter la Liste des Employés : Affichage de tous les employés enregistrés.
- Modifier un Employé : Mise à jour des informations d'un employé existant.
- Supprimer un Employé : Suppression d'un employé de la base de données.

### Module Projets
- Ajouter un Projet : Saisie du titre, de la description, de la date de début et de fin du projet.
- Assigner des Employés à un Projet : Gestion de la relation plusieurs-à-plusieurs entre employés et projets.
- Consulter la Liste des Projets : Affichage de tous les projets avec les employés affectés.
- Modifier un Projet : Mise à jour des informations d'un projet existant.
- Supprimer un Projet : Suppression d'un projet de la base de données.

## Technologies Utilisées

- Langage de Programmation : Java
- Interface Graphique : Swing
- Base de Données : MySQL
- Connexion à la Base de Données : JDBC


## Structure de la Base de Données
La base de données est composée des tables suivantes :

- Employés : Contient les informations sur les employés.
- Projets : Contient les informations sur les projets.
- Employés_Projets : Table intermédiaire gérant la relation plusieurs-à-plusieurs entre les employés et les projets.

## Prérequis
Java SE : Assurez-vous d'avoir la dernière version du JDK installée.
MySQL : Installez MySQL Server et configurez une base de données pour l'application.
Pilote JDBC MySQL : Téléchargez le connecteur JDBC pour MySQL et ajoutez-le au classpath de votre projet.


## Installation et Configuration

### Cloner le Répertoire du Projet :

- git clone https://github.com/votre-utilisateur/gestion-employes-projets.git
- cd gestion-employes-projets

### Configurer la Base de Données :

- Créez une base de données nommée gestion_entreprise.
- Importez le schéma de la base de données à partir du fichier schema.sql fourni.

### Configurer les Paramètres de Connexion :

- Ouvrez le fichier DatabaseConnection.java
- Modifiez les valeurs pour correspondre à votre configuration MySQL :

  - private static final String URL = ""
  - private static final String USER = "";
  - private static final String PASSWORD = "";

### Compiler et Exécuter l'Application :

Compilez le projet en utilisant votre IDE préféré ou en ligne de commande.
Exécutez l'application pour lancer l'interface graphique.


### Utilisation
Gestion des Employés : Naviguez vers le module "Employés" pour ajouter, consulter, modifier ou supprimer des employés.
Gestion des Projets : Accédez au module "Projets" pour gérer les projets et assigner des employés.
