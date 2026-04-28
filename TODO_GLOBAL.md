# ElectriMada - Plan d'implémentation complet

## Phase 1 : API REST Java (Backend)
- [ ] Mettre à jour `pom.xml` (JSON, CORS, Java 17)
- [ ] Créer `ApiResponse.java` (standard JSON)
- [ ] Créer `CorsFilter.java` (CORS pour React)
- [ ] Créer `DashboardServlet.java`
- [ ] Créer `FoyerServlet.java`
- [ ] Créer `DemandeServlet.java`
- [ ] Créer `AllocationServlet.java`
- [ ] Créer `BatterieServlet.java`
- [ ] Créer `RapportServlet.java`
- [ ] Créer `SyncServlet.java`
- [ ] Mettre à jour `web.xml`

## Phase 2 : Frontend React Web
- [ ] Créer `mon-app-web/` avec Vite + React + TS
- [ ] Configurer proxy API
- [ ] Créer `i18n.ts` (FR / MG)
- [ ] Créer page Dashboard
- [ ] Créer page Demandes
- [ ] Créer page Allocations
- [ ] Créer page Rapports
- [ ] Créer composants réutilisables

## Phase 3 : Frontend React Native (adaptation)
- [ ] Connecter à l'API REST
- [ ] Implémenter offline-first
- [ ] Ajouter i18n FR/MG
- [ ] Créer écrans principaux

## Phase 4 : Tests unitaires Java
- [ ] TasBinaireTest.java
- [ ] SacADosTest.java
- [ ] MoyenneGlissanteTest.java
- [ ] ArbreFenwickTest.java
- [ ] AllocationServiceTest.java

## Phase 5 : Documentation
- [ ] Mettre à jour README.md

