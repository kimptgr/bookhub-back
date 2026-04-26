INSERT INTO etats (libelle)
VALUES ('DISPONIBLE');
INSERT INTO etats (libelle)
VALUES ('RESERVE');
INSERT INTO etats (libelle)
VALUES ('EMPRUNTE');
INSERT INTO etats (libelle)
VALUES ('INUTILISABLE');

INSERT INTO statuts (libelle)
VALUES ('SUR_LISTE_D_ATTENTE');
INSERT INTO statuts (libelle)
VALUES ('EN_ATTENTE_DE_RETRAIT');
INSERT INTO statuts (libelle)
VALUES ('CLOTUREE');
INSERT INTO statuts (libelle)
VALUES ('ANNULEE');

INSERT INTO genres (libelle)
VALUES ('ROMAN_POLICIER');
INSERT INTO genres (libelle)
VALUES ('ART_POETIQUE');
INSERT INTO genres (libelle)
VALUES ('FANTAISIE');
INSERT INTO genres (libelle)
VALUES ('THEATRE');
INSERT INTO genres (libelle)
VALUES ('ARGUMENTATIF');
INSERT INTO genres (libelle)
VALUES ('GRAPHIQUE');
INSERT INTO genres (libelle)
VALUES ('SCIENCE_FICTION');
INSERT INTO genres (libelle)
VALUES ('NOVELLA');
INSERT INTO genres (libelle)
VALUES ('ROMAN');

insert into utilisateurs (desactive, email, mot_de_passe_chiffre, nom, prenom, role)
VALUES (false, 'mtartine@dej.com', '$2a$10$26.ezViBvLzeEl1XK5v9L.EUtJ4ApAsYdvNLf7WUjF4QmZuO0rak.', 'Tartine', 'Marie',
        'UTILISATEUR');
insert into utilisateurs (desactive, email, mot_de_passe_chiffre, nom, prenom, role)
VALUES (false, 'admin@mtartine', '$2a$10$26.ezViBvLzeEl1XK5v9L.EUtJ4ApAsYdvNLf7WUjF4QmZuO0rak.', 'Tartine', 'Marie',
        'BIBLIOTHECAIRE');
insert into utilisateurs (desactive, email, mot_de_passe_chiffre, nom, prenom, role)
VALUES (true, 'old@elaff', '$2a$10$26.ezViBvLzeEl1XK5v9L.EUtJ4ApAsYdvNLf7WUjF4QmZuO0rak.', 'Old', 'Compte',
        'UTILISATEUR');

INSERT INTO auteurs (nom, prenom)
VALUES ('PERRIN', 'Valérie');
INSERT INTO auteurs (nom, prenom)
VALUES ('VIKTOROVITCH', 'Clément');
INSERT INTO auteurs (nom, prenom)
VALUES ('LOMBÉ', 'Lisette');
INSERT INTO auteurs (nom, prenom)
VALUES ('DESPENTES', 'Virginie');
INSERT INTO auteurs (nom, prenom)
VALUES ('MARTIN', 'George R.R.');
INSERT INTO auteurs (nom, prenom)
VALUES ('BESSON', 'Philippe');
INSERT INTO auteurs (nom, prenom)
VALUES ('GIEBEL', 'Karine');
INSERT INTO auteurs (nom, prenom)
VALUES ('DENJEAN', 'Céline');
INSERT INTO auteurs (nom, prenom)
VALUES ('FOUCHET', 'Lorraine');
INSERT INTO auteurs (nom, prenom)
VALUES ('PUÉRTOLAS', 'Romain');
INSERT INTO auteurs (nom, prenom)
VALUES ('LAPIERRE', 'Alexandra');
INSERT INTO auteurs (nom, prenom)
VALUES ('MARTIN-LUGAND', 'Agnès');
INSERT INTO auteurs (nom, prenom)
VALUES ('CALMEL', 'Mireille');
INSERT INTO auteurs (nom, prenom)
VALUES ('COLLETTE', 'Sandrine');
INSERT INTO auteurs (nom, prenom)
VALUES ('LOEVENBRUCK', 'Henri');
INSERT INTO auteurs (nom, prenom)
VALUES ('GIORDANO', 'Raphaëlle');
INSERT INTO auteurs (nom, prenom)
VALUES ('BUSSI', 'Michel');

INSERT INTO livres (date_de_parution, id_etat, isbn, titre, url_image, synopsis)
VALUES ('2019-04-24', 1, '9782253238027', 'Changer l''eau des fleurs',
        'https://www.babelio.com/couv/CVT_Changer-leau-des-fleurs_5277.jpg',
        'Violette Toussaint est garde-cimetière dans une petite ville de Bourgogne. Les gens de passage et les habitués viennent se confier et se réchauffer dans sa loge. Avec la petite équipe de fossoyeurs et le jeune curé, elle forme une famille décalée. Mais quels événements ont mené Violette dans cet univers où le tragique et le cocasse s’entremêlent ?Après le succès des  Oubliés du dimanche, un nouvel hymne au merveilleux des choses simples.');
INSERT INTO livres_genres (id_livre, id_genre)
VALUES (1, 9);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (1, 1);

INSERT INTO livres (date_de_parution, id_etat, isbn, titre, url_image, synopsis)
VALUES ('2021-10-14', 1, '9782021465877', 'Le pouvoir rhétorique',
        'https://www.babelio.com/couv/CVT_Le-pouvoir-rhetorique_9738.jpg', 'La rhétorique est partout. Dans les discours politiques comme dans les spots publicitaires. Dans les réunions professionnelles comme dans les dîners de famille. Dans les entretiens d’embauche comme dans les rendez-vous galants. Pas un jour ne passe sans que nous ayons à défendre une idée, un projet, un produit ; et à nous protéger contre d’éventuelles fourberies. Que cela nous plaise ou non, convaincre est un pouvoir. À nous d’apprendre à le maîtriser.
Et de savoir y résister.');
INSERT INTO livres_genres (id_livre, id_genre)
VALUES (2, 5);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (2, 2);

INSERT INTO livres (date_de_parution, id_etat, isbn, titre, url_image, synopsis)
VALUES ('2020-07-10', 1, '9782378801670', 'Brûler brûler brûler',
        'https://www.babelio.com/couv/CVT_Bruler-bruler-bruler_1999.jpg', '« Te faire douter.
Te faire avoir peur.
Te faire avoir honte
De ta couleur.
Qui oubliera ?
Qu’à un noir,
On disait tu… »

Antiracistes, féministes, politiques, les mots de Lisette Lombé font battre le pavé et le cœur. Le poing levé, à coups de mots et de collages, elle dénonce les injustices et poursuit le combat de ses aînées, d’Angela Davis à Toni Morrison.');
INSERT INTO livres_genres (id_livre, id_genre)
VALUES (3, 2);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (3, 3);

INSERT INTO livres (date_de_parution, id_etat, isbn, titre, url_image, synopsis)
VALUES ('2007-09-28', 1, '9782253122111', 'King Kong Théorie',
        'https://m.media-amazon.com/images/I/91vTbX7F2kL._SY522_.jpg', 'J’écris de chez les moches, pour les moches, les frigides, les mal baisées, les imbaisables, toutes les exclues du grand marché à la bonne meuf, aussi bien que pour les hommes qui n’ont pas envie d’être protecteurs, ceux qui voudraient l’être mais ne savent pas s’y prendre, ceux qui ne sont pas ambitieux, ni compétitifs, ni bien membrés.
Parce que l’idéal de la femme blanche séduisante qu’on nous brandit tout le temps sous le nez, je crois bien qu’il n’existe pas.
V.D.
En racontant pour la première fois comment elle est devenue Virginie Despentes, l’auteur de Baise-moi conteste les discours bien-pensants sur le viol, la prostitution, la pornographie. Manifeste pour un nouveau féminisme.');
INSERT INTO livres_genres (id_livre, id_genre)
VALUES (4, 5);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (4, 4);


INSERT INTO livres (date_de_parution, id_etat, isbn, titre, url_image, synopsis)
VALUES ('2010-01-20', 1, '9782290019436', 'Le Trône de Fer - Intégrale tome 1 sur 7',
        'https://www.babelio.com/couv/CVT_10390_aj_m_4829.jpg',
        'Après avoir tué le monarque dément Aerys II Targaryen, Robert Baratheon est devenu le nouveau souverain du royaume des Sept Couronnes. Tandis que, en son domaine de Winterfell, son fidèle ami le duc Eddard Stark rend paisiblement la justice. Mais un jour, le roi Robert lui rend visite, porteur de sombres nouvelles : le trône est en péril. Stark, qui s''est toujours tenu éloigné des affaires du pouvoir, doit alors abandonner les terres du Nord pour rejoindre la cour et ses intrigues. L''heure est grave, d''autant qu''au-delà du Mur qui protège le royaume depuis des siècles, rôdent d''étranges créatures.');
INSERT INTO livres_genres (id_livre, id_genre)
VALUES (5, 9);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (5, 5);

INSERT INTO livres (date_de_parution, id_etat, isbn, titre, url_image, synopsis)
VALUES ('2010-01-20', 1, '9782290019443',
        'Le Trône de Fer - Intégrale tome 2 sur 7',
        'https://www.babelio.com/couv/sm_10390_aj_m_3508.jpg',
        'Après avoir tué le monarque dément Aerys II Targaryen, Robert Baratheon est devenu le nouveau souverain du royaume des Sept Couronnes. Tandis que, en son domaine de Winterfell, son fidèle ami le duc Eddard Stark rend paisiblement la justice. Mais un jour, le roi Robert lui rend visite, porteur de sombres nouvelles : le trône est en péril. Stark, qui s''est toujours tenu éloigné des affaires du pouvoir, doit alors abandonner les terres du Nord pour rejoindre la cour et ses intrigues. L''heure est grave, d''autant qu''au-delà du Mur qui protège le royaume depuis des siècles, rôdent d''étranges créatures.');
INSERT INTO livres_genres (id_livre, id_genre)
VALUES (6, 9);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (6, 5);

INSERT INTO livres (date_de_parution, id_etat, isbn, titre, url_image, synopsis)
VALUES ('2010-01-20', 1, '9782290022160',
        'Le Trône de Fer - Intégrale tome 3 sur 7',
        'https://www.babelio.com/couv/sm_30276_1604083.jpg',
        'Après avoir tué le monarque dément Aerys II Targaryen, Robert Baratheon est devenu le nouveau souverain du royaume des Sept Couronnes. Tandis que, en son domaine de Winterfell, son fidèle ami le duc Eddard Stark rend paisiblement la justice. Mais un jour, le roi Robert lui rend visite, porteur de sombres nouvelles : le trône est en péril. Stark, qui s''est toujours tenu éloigné des affaires du pouvoir, doit alors abandonner les terres du Nord pour rejoindre la cour et ses intrigues. L''heure est grave, d''autant qu''au-delà du Mur qui protège le royaume depuis des siècles, rôdent d''étranges créatures.');
INSERT INTO livres_genres (id_livre, id_genre)
VALUES (7, 9);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (7, 5);

INSERT INTO livres (date_de_parution, id_etat, isbn, titre, url_image, synopsis)
VALUES ('2010-01-20', 1, '9782290022177',
        'Le Trône de Fer - Intégrale tome 4 sur 7',
        'https://www.babelio.com/couv/CVT_Le-Trone-de-Fer-Integrale-tome-4_8756.jpg',
        'Après avoir tué le monarque dément Aerys II Targaryen, Robert Baratheon est devenu le nouveau souverain du royaume des Sept Couronnes. Tandis que, en son domaine de Winterfell, son fidèle ami le duc Eddard Stark rend paisiblement la justice. Mais un jour, le roi Robert lui rend visite, porteur de sombres nouvelles : le trône est en péril. Stark, qui s''est toujours tenu éloigné des affaires du pouvoir, doit alors abandonner les terres du Nord pour rejoindre la cour et ses intrigues. L''heure est grave, d''autant qu''au-delà du Mur qui protège le royaume depuis des siècles, rôdent d''étranges créatures.');
INSERT INTO livres_genres (id_livre, id_genre)
VALUES (8, 9);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (8, 5);

INSERT INTO livres (date_de_parution, id_etat, isbn, titre, url_image, synopsis)
VALUES ('2010-01-20', 1, '9782290107096',
        'Le Trône de Fer - Intégrale tome 5 sur 7',
        'https://www.babelio.com/couv/CVT_Le-Trone-de-Fer-Integrale-5--A-Dance-with-Dragons_2175.jpg',
        'Après avoir tué le monarque dément Aerys II Targaryen, Robert Baratheon est devenu le nouveau souverain du royaume des Sept Couronnes. Tandis que, en son domaine de Winterfell, son fidèle ami le duc Eddard Stark rend paisiblement la justice. Mais un jour, le roi Robert lui rend visite, porteur de sombres nouvelles : le trône est en péril. Stark, qui s''est toujours tenu éloigné des affaires du pouvoir, doit alors abandonner les terres du Nord pour rejoindre la cour et ses intrigues. L''heure est grave, d''autant qu''au-delà du Mur qui protège le royaume depuis des siècles, rôdent d''étranges créatures.');
INSERT INTO livres_genres (id_livre, id_genre)
VALUES (9, 9);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (9, 5);

INSERT INTO livres (date_de_parution, id_etat, isbn, titre, url_image, synopsis)
VALUES ('2010-01-20', 1, '9782080295439',
        'Le Trône de Fer - Intégrale tome 6 sur 7',
        'https://www.babelio.com/couv/CVT_Le-Trone-de-Fer-Integrale-6--The-Winds-of-Winter_4786.jpg',
        'Après avoir tué le monarque dément Aerys II Targaryen, Robert Baratheon est devenu le nouveau souverain du royaume des Sept Couronnes. Tandis que, en son domaine de Winterfell, son fidèle ami le duc Eddard Stark rend paisiblement la justice. Mais un jour, le roi Robert lui rend visite, porteur de sombres nouvelles : le trône est en péril. Stark, qui s''est toujours tenu éloigné des affaires du pouvoir, doit alors abandonner les terres du Nord pour rejoindre la cour et ses intrigues. L''heure est grave, d''autant qu''au-delà du Mur qui protège le royaume depuis des siècles, rôdent d''étranges créatures.');
INSERT INTO livres_genres (id_livre, id_genre)
VALUES (10, 9);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (10, 5);

INSERT INTO livres (date_de_parution, id_etat, isbn, titre, url_image, synopsis)
VALUES ('2010-01-20', 4, '2290022160',
        'Le Trône de Fer - Intégrale tome 7 sur 7',
        'https://www.babelio.com/couv/CVT_Le-Trone-de-Fer-Integrale-7--A-Dream-of-Spring_1366.jpg',
        'Après avoir tué le monarque dément Aerys II Targaryen, Robert Baratheon est devenu le nouveau souverain du royaume des Sept Couronnes. Tandis que, en son domaine de Winterfell, son fidèle ami le duc Eddard Stark rend paisiblement la justice. Mais un jour, le roi Robert lui rend visite, porteur de sombres nouvelles : le trône est en péril. Stark, qui s''est toujours tenu éloigné des affaires du pouvoir, doit alors abandonner les terres du Nord pour rejoindre la cour et ses intrigues. L''heure est grave, d''autant qu''au-delà du Mur qui protège le royaume depuis des siècles, rôdent d''étranges créatures.');
INSERT INTO livres_genres (id_livre, id_genre)
VALUES (11, 9);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (11, 5);

INSERT INTO livres (date_de_parution,
                    id_etat,
                    isbn,
                    titre,
                    url_image,
                    synopsis)
VALUES ('2025-11-06',
        1,
        '9782266348942',
        '13 à table !',
        'https://m.media-amazon.com/images/I/41SdgcTkCFL._SX195_.jpg',
        'Recueil de nouvelles inédites réunissant plusieurs auteurs français contemporains. Comme chaque année, le projet 13 à table ! associe le plaisir de lire à une action solidaire, au profit des Restos du Cœur.');


INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (12, 1);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (12, 6);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (12, 7);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (12, 8);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (12, 9);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (12, 10);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (12, 11);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (12, 12);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (12, 13);
INSERT INTO livres_auteurs (id_livre, id_auteur)
VALUES (12, 14);

insert into reservations (date_disponibilite, date_retrait_max, est_supprimee, rang, date_demande_reservation,
                          id_livre, id_statut, id_utilisateur)
values ((CURRENT_DATE - INTERVAL '10 days'), (CURRENT_DATE + interval '4 days'), true, null,
        (CURRENT_DATE - INTERVAL '3 Days'), 6, 3, 1);

insert into reservations (date_disponibilite, date_retrait_max, est_supprimee, rang, date_demande_reservation,
                          id_livre, id_statut, id_utilisateur)
values (CURRENT_DATE, (CURRENT_DATE + INTERVAL '14 days'), false, 1, CURRENT_DATE, 7, 2, 1);

insert into reservations (date_disponibilite, date_retrait_max, est_supprimee, rang, date_demande_reservation,
                          id_livre, id_statut, id_utilisateur)
values (CURRENT_DATE, (CURRENT_DATE + INTERVAL '14 days'), false, 1, CURRENT_DATE, 8, 2, 1);

insert into reservations (date_disponibilite, date_retrait_max, est_supprimee, rang, date_demande_reservation,
                          id_livre, id_statut, id_utilisateur)
values (CURRENT_DATE, (CURRENT_DATE + INTERVAL '14 days'), true, 1, CURRENT_DATE, 10, 2, 1);
