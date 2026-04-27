package fr.eni.bookhub.interfaces;

import java.util.List;

public interface LivreAssociations {

    List<Long> auteurs();
    List<Long> genres();
    Long idEtat();
}
