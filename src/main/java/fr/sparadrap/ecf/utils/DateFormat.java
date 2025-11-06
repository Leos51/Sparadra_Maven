package fr.sparadrah.ecf.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormat {
    private static final DateTimeFormatter FORMATTER_DATE_FRENCH = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     *  transforme une date Localdate en type String pour l'affichage suivant le pattern
     * @param date : Localdate
     * @param pattern : String
     * @return String
     */
    public static String formatDate(LocalDate date, String pattern) {
        if (date == null || pattern == null || pattern.isBlank()) {
            throw new IllegalArgumentException("La date et le pattern ne peut pas etre null ou vide");
        }

        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(pattern);
        return date.format(myFormatObj);
    }


    /**
     *  transforme une date saisie par l'utilisateur (String) en LocalDate afin de l'enregistrer dans une instance
     * @param dateStr : String
     * @return : LocalDate
     */
    public static LocalDate parseDateFromString(String dateStr) {
        try {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dateStr, FORMATTER_DATE_FRENCH);
        } catch (DateTimeParseException e) {
            System.err.println("Format de date invalide. Utilisez \"dd/MM/yyyy\".\n" + e.getMessage());
        }
        return null;
    }

}
