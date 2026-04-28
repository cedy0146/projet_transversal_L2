package com.electrimada.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Table de Hachage (HashMap) custom
 */
public class TableHachage<K, V> {

    private static final int CAPACITE_INITIALE = 16;
    private static final double FACTEUR_CHARGE = 0.75;
    private Node<K, V>[] table;
    private int taille;
    private int capacite;

    @SuppressWarnings("unchecked")
    public TableHachage() {
        this.capacite = CAPACITE_INITIALE;
        this.table = new Node[capacite];
        this.taille = 0;
    }

    private int hash(K cle) {
        return (cle == null) ? 0 : Math.abs(cle.hashCode() % capacite);
    }

    public void put(K cle, V valeur) {
        if (cle == null) return;
        int index = hash(cle);
        Node<K, V> nouveau = new Node<>(cle, valeur);
        if (table[index] == null) {
            table[index] = nouveau;
        } else {
            Node<K, V> courant = table[index];
            while (courant != null) {
                if (courant.cle.equals(cle)) {
                    courant.valeur = valeur;
                    return;
                }
                if (courant.suivant == null) break;
                courant = courant.suivant;
            }
            courant.suivant = nouveau;
        }
        taille++;
        if ((double) taille / capacite > FACTEUR_CHARGE) {
            redimensionner();
        }
    }

    public V get(K cle) {
        if (cle == null) return null;
        int index = hash(cle);
        Node<K, V> courant = table[index];
        while (courant != null) {
            if (courant.cle.equals(cle)) {
                return courant.valeur;
            }
            courant = courant.suivant;
        }
        return null;
    }

    public boolean containsKey(K cle) {
        return get(cle) != null;
    }

    public V remove(K cle) {
        if (cle == null) return null;
        int index = hash(cle);
        Node<K, V> courant = table[index];
        Node<K, V> precedent = null;
        while (courant != null) {
            if (courant.cle.equals(cle)) {
                V valeur = courant.valeur;
                if (precedent == null) {
                    table[index] = courant.suivant;
                } else {
                    precedent.suivant = courant.suivant;
                }
                taille--;
                return valeur;
            }
            precedent = courant;
            courant = courant.suivant;
        }
        return null;
    }

    public int size() {
        return taille;
    }

    public boolean isEmpty() {
        return taille == 0;
    }

    public List<K> keySet() {
        List<K> cles = new ArrayList<>();
        for (Node<K, V> node : table) {
            Node<K, V> courant = node;
            while (courant != null) {
                cles.add(courant.cle);
                courant = courant.suivant;
            }
        }
        return cles;
    }

    @SuppressWarnings("unchecked")
    private void redimensionner() {
        Node<K, V>[] ancienneTable = table;
        capacite *= 2;
        table = new Node[capacite];
        taille = 0;
        for (Node<K, V> node : ancienneTable) {
            Node<K, V> courant = node;
            while (courant != null) {
                put(courant.cle, courant.valeur);
                courant = courant.suivant;
            }
        }
    }

    private static class Node<K, V> {
        K cle;
        V valeur;
        Node<K, V> suivant;
        Node(K cle, V valeur) {
            this.cle = cle;
            this.valeur = valeur;
        }
    }
}
