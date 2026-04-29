package fr.eni.bookhub.utils;

public class TelephoneFormatter {

    public static String normaliser(String telephone) {
        if (telephone == null || telephone.isBlank()) return null;

        // Supprime tout sauf les chiffres le ^ permis d'indiquer tout ce qui n'est pas, ici les chiffres
        String chiffres = telephone.replaceAll("[^0-9]", "");

        // Si ça commence par 33(le + a été supprimer) on supprime le 33 et on vérifie s'il faut ou non ajouter un 0 devant
        if (chiffres.startsWith("33")) {
            chiffres = chiffres.substring(2);

            if (chiffres.startsWith("0") && chiffres.length() == 10) {
                chiffres = chiffres;
            } else {
                chiffres = "0" + chiffres;
            }
        }

        // Vérifie qu'on a bien 10 chiffres
        if (chiffres.length() != 10) {
            throw new IllegalArgumentException("Numéro de téléphone invalide après normalisation : " + telephone);
        }

        return chiffres;
    }
}
