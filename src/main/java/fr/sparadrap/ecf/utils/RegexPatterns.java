package fr.sparadrap.ecf.utils;


import java.util.regex.Pattern;

public class RegexPatterns {
    /**
     * Email : format standard RFC 5322 simplifié
     * Exemples valides: user@domain.com, user.name@sub.domain.fr
     */
    public static final String EMAIL_REGEX = "^[\\w.-]+@[\\w.-]+\\.\\w{2,}$";

    /**
     * Nom/Prénom : lettres (avec accents), espaces, tirets, apostrophes
     * Exemples valides: Jean-Pierre, O'Connor, Müller
     */
    public static final String NAME_REGEX = "^[A-Za-zÀ-ÖØ-öø-ÿ\\-\\s]+$";

    /**
     * Téléphone français (fixe et mobile)
     * Formats acceptés:
     * - 0612345678
     * - 06 12 34 56 78
     * - 06.12.34.56.78
     * - +33 6 12 34 56 78
     * - 0033612345678
     */
    public static final String PHONE_REGEX = "^(?:(?:\\+|00)33[\\s.-]?(?:\\(0\\)[\\s.-]?)?|0)[1-9](?:[\\s.-]?\\d{2}){4}$";

    /**
     * Code postal français (5 chiffres, départements 01-98)
     */
    public static final String POSTCODE_REGEX = "^(?:0[1-9]|[1-8]\\d|9[0-8])\\d{3}$";

    /**
     * NIR (Numéro de Sécurité Sociale français)
     * Format: [Sexe 1 chiffre][Année 2 chiffres][Mois 2 chiffres][Département 2 chiffres/2A/2B]
     *         [Commune 3 chiffres][N° ordre 3 chiffres][Clé optionnelle 2 chiffres]
     *
     * Exemples valides:
     * - 175028912345678 (avec clé)
     * - 1750289123456 (sans clé)
     * - 2850578234567
     */
    public static final String NIR_REGEX = "^[12]\\d{2}(0[1-9]|1[0-2]|[2-9]\\d)(\\d{2}|2[AB])\\d{3}\\d{3}(\\d{2})?$";

    /**
     * RPPS (Répertoire Partagé des Professionnels de Santé)
     * 11 chiffres exactement
     */
    public static final String RPPS_REGEX = "^\\d{11}$";

    /**
     * Date format DD/MM/YYYY
     */
    public static final String DATE_PATTERN = "^(0[1-9]|[12][0-9]|3[01])\\/(0[1-9]|1[0-2])\\/([0-9]{4})$";
}
