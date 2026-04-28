package com.electrimada.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Tas Binaire (Binary Heap) - Max Heap
 * Structure de donnees avancee pour la file de priorite des demandes d'energie.
 * 
 * Pourquoi c'est adapte ?
 * - Complexite : O(1) pour acceder au max, O(log n) pour insertion/suppression
 * - Contrainte 2035 : Beaucoup plus efficace qu'une liste triee, economise la batterie
 *   du telephone lors du traitement de centaines de demandes.
 */
public class TasBinaire<T extends Comparable<T>> {
    
    private List<T> heap;
    
    public TasBinaire() {
        this.heap = new ArrayList<>();
    }
    
    /**
     * Retourne l'element de plus haute priorite (racine) en O(1)
     */
    public T peek() {
        if (heap.isEmpty()) return null;
        return heap.get(0);
    }
    
    /**
     * Extrait et retourne l'element de plus haute priorite en O(log n)
     */
    public T extractMax() {
        if (heap.isEmpty()) return null;
        
        T max = heap.get(0);
        T last = heap.remove(heap.size() - 1);
        
        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }
        
        return max;
    }
    
    /**
     * Insere un nouvel element en O(log n)
     */
    public void insert(T element) {
        heap.add(element);
        heapifyUp(heap.size() - 1);
    }
    
    /**
     * Retourne le nombre d'elements
     */
    public int size() {
        return heap.size();
    }
    
    /**
     * Verifie si le tas est vide
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }
    
    /**
     * Retourne une copie de la liste interne (pour iteration)
     */
    public List<T> getAll() {
        return new ArrayList<>(heap);
    }
    
    // --- Methodes privees ---
    
    private void heapifyUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(parentIndex)) > 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }
    
    private void heapifyDown(int index) {
        int size = heap.size();
        
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int largest = index;
            
            if (leftChild < size && heap.get(leftChild).compareTo(heap.get(largest)) > 0) {
                largest = leftChild;
            }
            
            if (rightChild < size && heap.get(rightChild).compareTo(heap.get(largest)) > 0) {
                largest = rightChild;
            }
            
            if (largest != index) {
                swap(index, largest);
                index = largest;
            } else {
                break;
            }
        }
    }
    
    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
    
    // --- Test main ---
    public static void main(String[] args) {
        TasBinaire<Integer> tas = new TasBinaire<>();
        
        System.out.println("=== Test Tas Binaire ===");
        int[] valeurs = {15, 10, 20, 8, 12, 25, 5};
        
        System.out.println("Insertion : ");
        for (int v : valeurs) {
            tas.insert(v);
            System.out.print(v + " ");
        }
        System.out.println("\nTaille : " + tas.size());
        
        System.out.println("\nExtraction max (ordre decroissant) :");
        while (!tas.isEmpty()) {
            System.out.print(tas.extractMax() + " ");
        }
        System.out.println("\nTas vide : " + tas.isEmpty());
    }
}

