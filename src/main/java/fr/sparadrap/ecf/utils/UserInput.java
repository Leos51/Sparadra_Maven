package fr.sparadrap.ecf.utils;

import java.util.Scanner;

public class UserInput {
    private final static Scanner sc = new Scanner(System.in);


    /**
     * enregistre la saisie utilisateur dans une variable
     * @param msg : message predefini : String
     * @return La valeur saisie :  String
     */
    public static String getStringValue(String msg){
        System.out.println(msg);
        String str = "";
        while (str.isBlank()) {
            str = sc.nextLine();
        }
        return str;
    }

    /**
     * Permet d'enregistrer la saisie Utilisateur dans une variable
     * @param msg  message predefini : String
     * @return La valeur saisie : int
     */
    public static int getIntValue(String msg) {
        String str = getStringValue(msg);
        try{
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            System.err.println("Erreur : entrée invalide. Veuillez saisir un entier.");
            return -1;
        }
    }

    /**
     * Met la 1ere lettre en majuscule et le reste en minuscule
     * @param word Message predefini
     * @return String
     */
    public static String capitalize(String word) {
        word = word.toLowerCase();
        return word.substring(0, 1).toUpperCase() + word.substring(1);

    }

    /**
     * Permet de sortir de l'application
     */
    public static void exitApp() {
        System.out.println("Au revoir");
        System.exit(0);
    }
}
