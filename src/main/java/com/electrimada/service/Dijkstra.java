package com.electrimada.service;

import java.util.*;

/**
 * Algorithme de Dijkstra
 * Famille : Graphes / Reseaux (Optionnelle mais recommandee)
 *
 * But : Modeliser le reseau electrique du village sous forme de graphe
 * et calculer le chemin le plus efficace (moins de perte d'energie)
 * entre les panneaux solaires et les foyers eloignes.
 *
 * Modelisation :
 * - Noeuds = points du reseau (panneaux, batteries, foyers, postes de distribution)
 * - Aretes = cables avec poids = perte d'energie (ou resistance)
 * - Objectif : minimiser la perte totale entre source et destination
 *
 * Complexite : O((V + E) log V) avec un tas binaire, ou V = noeuds, E = aretes
 */
public class Dijkstra {

    /**
     * Représente un noeud dans le graphe du reseau electrique
     */
    public static class Noeud {
        public String id;
        public String nom;
        public String type; // "PANNEAU", "BATTERIE", "FOYER", "POSTE"

        public Noeud(String id, String nom, String type) {
            this.id = id;
            this.nom = nom;
            this.type = type;
        }

        @Override
        public String toString() {
            return nom + " (" + type + ")";
        }
    }

    /**
     * Représente une arete (cable) entre deux noeuds
     */
    public static class Arete {
        public String source;
        public String destination;
        public double perte; // perte d'energie en % ou en kWh
        public double distance; // distance en metres

        public Arete(String source, String destination, double perte, double distance) {
            this.source = source;
            this.destination = destination;
            this.perte = perte;
            this.distance = distance;
        }
    }

    /**
     * Resultat du plus court chemin
     */
    public static class ResultatChemin {
        public List<String> chemin; // Liste des IDs de noeuds
        public double perteTotale;
        public double distanceTotale;

        public ResultatChemin() {
            this.chemin = new ArrayList<>();
            this.perteTotale = 0.0;
            this.distanceTotale = 0.0;
        }
    }

    // Graphe represente comme liste d'adjacence
    private Map<String, List<Arete>> graphe;
    private Map<String, Noeud> noeuds;

    public Dijkstra() {
        this.graphe = new HashMap<>();
        this.noeuds = new HashMap<>();
    }

    public void ajouterNoeud(Noeud noeud) {
        noeuds.put(noeud.id, noeud);
        graphe.putIfAbsent(noeud.id, new ArrayList<>());
    }

    public void ajouterArete(Arete arete) {
        graphe.putIfAbsent(arete.source, new ArrayList<>());
        graphe.putIfAbsent(arete.destination, new ArrayList<>());
        graphe.get(arete.source).add(arete);
        // Graphe non oriente : ajouter aussi l'arete inverse
        Arete inverse = new Arete(arete.destination, arete.source, arete.perte, arete.distance);
        graphe.get(arete.destination).add(inverse);
    }

    /**
     * Execute l'algorithme de Dijkstra pour trouver le chemin avec le moins de perte
     * entre la source et la destination.
     */
    public ResultatChemin trouverMeilleurChemin(String idSource, String idDestination) {
        if (!noeuds.containsKey(idSource) || !noeuds.containsKey(idDestination)) {
            return new ResultatChemin();
        }

        // Distances minimales initialisees a l'infini
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> predecesseurs = new HashMap<>();
        for (String id : noeuds.keySet()) {
            distances.put(id, Double.MAX_VALUE);
        }
        distances.put(idSource, 0.0);

        // Tas de priorite : (distance, noeudId)
        PriorityQueue<Map.Entry<String, Double>> pq = new PriorityQueue<>(
            Comparator.comparingDouble(Map.Entry::getValue)
        );
        pq.offer(new AbstractMap.SimpleEntry<>(idSource, 0.0));

        while (!pq.isEmpty()) {
            Map.Entry<String, Double> courant = pq.poll();
            String idCourant = courant.getKey();
            double distCourante = courant.getValue();

            if (distCourante > distances.get(idCourant)) continue;

            for (Arete arete : graphe.getOrDefault(idCourant, new ArrayList<>())) {
                double nouvelleDist = distCourante + arete.perte;

                if (nouvelleDist < distances.get(arete.destination)) {
                    distances.put(arete.destination, nouvelleDist);
                    predecesseurs.put(arete.destination, idCourant);
                    pq.offer(new AbstractMap.SimpleEntry<>(arete.destination, nouvelleDist));
                }
            }
        }

        // Reconstruction du chemin
        return reconstruireChemin(idSource, idDestination, distances, predecesseurs);
    }

    private ResultatChemin reconstruireChemin(String source, String destination,
                                              Map<String, Double> distances,
                                              Map<String, String> predecesseurs) {
        ResultatChemin resultat = new ResultatChemin();

        if (distances.get(destination) == Double.MAX_VALUE) {
            return resultat; // Pas de chemin trouve
        }

        resultat.perteTotale = distances.get(destination);

        // Remonter les predecesseurs
        List<String> cheminInverse = new ArrayList<>();
        String courant = destination;
        while (courant != null) {
            cheminInverse.add(courant);
            courant = predecesseurs.get(courant);
        }
        Collections.reverse(cheminInverse);
        resultat.chemin = cheminInverse;

        // Calculer la distance totale
        for (int i = 0; i < cheminInverse.size() - 1; i++) {
            String from = cheminInverse.get(i);
            String to = cheminInverse.get(i + 1);
            for (Arete a : graphe.get(from)) {
                if (a.destination.equals(to)) {
                    resultat.distanceTotale += a.distance;
                    break;
                }
            }
        }

        return resultat;
    }

    public static void main(String[] args) {
        System.out.println("=== Test Dijkstra (Reseau Electrique Village) ===");

        Dijkstra reseau = new Dijkstra();

        // Noeuds
        reseau.ajouterNoeud(new Noeud("P1", "Panneaux Solaires", "PANNEAU"));
        reseau.ajouterNoeud(new Noeud("B1", "Batterie Centrale", "BATTERIE"));
        reseau.ajouterNoeud(new Noeud("D1", "Poste Distribution Nord", "POSTE"));
        reseau.ajouterNoeud(new Noeud("D2", "Poste Distribution Sud", "POSTE"));
        reseau.ajouterNoeud(new Noeud("F1", "Foyer Razafy", "FOYER"));
        reseau.ajouterNoeud(new Noeud("F2", "Foyer Rakoto", "FOYER"));
        reseau.ajouterNoeud(new Noeud("F3", "Foyer Manou", "FOYER"));

        // Aretes : perte en %, distance en metres
        reseau.ajouterArete(new Arete("P1", "B1", 2.0, 50));
        reseau.ajouterArete(new Arete("B1", "D1", 3.0, 100));
        reseau.ajouterArete(new Arete("B1", "D2", 4.0, 150));
        reseau.ajouterArete(new Arete("D1", "F1", 1.5, 80));
        reseau.ajouterArete(new Arete("D1", "F2", 2.0, 120));
        reseau.ajouterArete(new Arete("D2", "F2", 1.0, 90));
        reseau.ajouterArete(new Arete("D2", "F3", 2.5, 110));

        // Trouver le meilleur chemin vers F2
        ResultatChemin chemin = reseau.trouverMeilleurChemin("P1", "F2");

        System.out.println("Chemin optimal P1 -> F2 :");
        for (String id : chemin.chemin) {
            System.out.println("  -> " + reseau.noeuds.get(id));
        }
        System.out.println("Perte totale : " + String.format("%.1f", chemin.perteTotale) + "%");
        System.out.println("Distance totale : " + String.format("%.0f", chemin.distanceTotale) + "m");

        // Comparer avec un autre foyer
        ResultatChemin cheminF3 = reseau.trouverMeilleurChemin("P1", "F3");
        System.out.println("\nChemin optimal P1 -> F3 :");
        for (String id : cheminF3.chemin) {
            System.out.println("  -> " + reseau.noeuds.get(id));
        }
        System.out.println("Perte totale : " + String.format("%.1f", cheminF3.perteTotale) + "%");
    }
}
