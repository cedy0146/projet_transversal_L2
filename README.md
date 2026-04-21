# ElectriMadaProject

Projet Java Web pour la gestion ElectriMada.

## Structure du projet
```
ElectriMadaProject/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/electrimada/
│   │   │       ├── modele/       <-- Classes POJO (Foyer, Batterie, Rapport, etc.)
│   │   │       ├── dao/          <-- Interfaces et classes FoyerDAO, etc.
│   │   │       ├── service/      <-- Algorithmes (SacADos, MoyenneGlissante, etc.)
│   │   │       └── controleur/   <-- Servlets ou points d'entrée
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── lib/          <-- mysql-connector-java.jar
│   │       │   └── web.xml       <-- Configuration
│   │       └── index.jsp         <-- Page d'accueil
├── target/                       <-- Généré par Maven
├── pom.xml                       <-- Configuration Maven
└── README.md                     <-- Ce fichier
```

## Déploiement
1. `mvn clean compile` pour compiler
2. `mvn package` pour générer le WAR
3. Déployer `target/ElectriMadaProject.war` sur Tomcat

## Base de données
Utilise MySQL. Ajoutez `mysql-connector-java.jar` dans `src/main/webapp/WEB-INF/lib/` si pas géré par Maven.
