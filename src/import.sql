
INSERT INTO `nauczyciel` (`pesel`, `imie`, `nazwisko`) VALUES(22222222220, 'Benton', 'Wildsmit'),(22222222221, 'Darnall', 'Musson'),(22222222222, 'Roderic', 'Spensley'),(22222222223, 'Der', 'Houndsom'),(22222222224, 'Fairfax', 'Mewton'),(22222222225, 'Bengt', 'Kubis'),(22222222226, 'Serge', 'Steagall'),(22222222227, 'Yanaton', 'Britnell'),(22222222228, 'Kayne', 'Halpen'),(22222222229, 'Zebadiah', 'Drake');
INSERT INTO `klasa` (`nazwa_klasy`, `rok_szkolny`, `wychowawca`) VALUES('1a', '2019-01-01', 22222222220),('1b', '2019-01-01', 22222222221),('1c', '2019-01-01', 22222222222);
INSERT INTO `uczen` (`pesel`, `nazwa_klasy`, `imie`, `nazwisko`) VALUES(32222222220, '1b', 'Zenon', 'Szmit'),(32222222221, '1a', 'Gniewomir', 'Iryd'),(32222222222, '1a', 'Adrian', 'Bury'),(32222222223, '1a', 'Krystian', 'Drozd'),(32222222224, '1a', 'Katarzyna', 'Kusz'),(32222222225, '1a', 'Marek', 'Klan'),(32222222226, '1b', 'Arkadiusz', 'Seagall'),(32222222227, '1b', 'Bartosz', 'Belz'),(32222222228, '1b', 'Kain', 'Harpel'),(32222222229, '1b', 'Zbigniew', 'Smok');
INSERT INTO `rodzic` (`pesel`, `dziecko`, `imie_ojca`, `nazwisko_ojca`, `imie_matki`, `nazwisko_matki`) VALUES(11111111110, 32222222220, 'Orin', 'Titmuss', 'Ines', 'Disman'),(11111111111, 32222222221, 'Chancey', 'Feakins', 'Stacee', 'Strickland'),(11111111112, 32222222222, 'Harp', 'Daugherty', 'Hedy', 'Pennicard'),(11111111113, 32222222223, 'Sigfried', 'Tomkies', 'Regine', 'Cirlos'),(11111111114, 32222222224, 'Pablo', 'Maletratt', 'Sharity', 'Ashfold'),(11111111115, 32222222225, 'Northrup', 'Morin', 'Robbin', 'Bissill'),(11111111116, 32222222226, 'Frazier', 'Heller', 'Kristina', 'Raine'),(11111111117, 32222222227, 'Matthiew', 'Scargle', 'Moyna', 'Frankham'),(11111111118, 32222222228, 'Rudie', 'Birrel', 'Libbey', 'Gammett'),(11111111119, 32222222229, 'Aldous', 'Nowakowski', 'Thomasin', 'Moule');
INSERT INTO `przedmiot` (`nazwa_przedmiotu`) VALUES('algebra_liniowa'),('geometria_analityczna'),('informatyka_ogolna'),('jezyk_angielski'),('jezyk_polski'),('programowanie_java'),('programowanie_zespolowe');
INSERT INTO `sklad_klasy` (`id`, `klasa`, `uczen`) VALUES (NULL, '1a', '32222222221'), (NULL, '1a', '32222222222'), (NULL, '1a', '32222222223'), (NULL, '1a', '32222222224'), (NULL, '1a', '32222222225'), (NULL, '1b', '32222222226'), (NULL, '1b', '32222222227'), (NULL, '1b', '32222222228'), (NULL, '1b', '32222222229'), (NULL, '1b', '32222222220');
INSERT INTO `ocena` (`id`, `przedmiot`, `pesel`, `stopien`, `data`, `opis`) VALUES(1, 'jezyk_angielski', 32222222221, '5', '2019-04-21', 'Piatunia ze sprawdziano'),(2, 'jezyk_angielski', 32222222222, '1', '2019-04-13', NULL),(NULL, 'jezyk_angielski', '32222222223', '-4', '2019-04-13', 'sss'),(NULL, 'jezyk_angielski', '32222222221', '-3', '2019-04-13', NULL);
INSERT INTO `zajecia` (`id`, `klasa`, `przedmiot`, `prowadzacy`, `godzina`, `dzien`) VALUES(1, '1a', 'algebra_liniowa', 22222222221, '08:00:00', 'pon'),(2, '1a', 'algebra_liniowa', 22222222221, '08:50:00', 'pon'),(3, '1a', 'programowanie_java', 22222222222, '09:40:00', 'pon'),(4, '1a', 'programowanie_java', 22222222222, '10:35:00', 'pon'),(5, '1b', 'programowanie_java', 22222222223, '08:45:00', 'pon'),(6, '1b', 'programowanie_java', 22222222223, '09:40:00', 'pon'),(7, '1a', 'informatyka_ogolna', 22222222225, '08:00:00', 'wt'),(8, '1a', 'informatyka_ogolna', 22222222225, '08:50:00', 'wt'),(9, '1a', 'programowanie_zespolowe', 22222222226, '09:40:00', 'wt'),(10, '1a', 'programowanie_zespolowe', 22222222226, '10:35:00', 'wt'),(11, '1b', 'algebra_liniowa', 22222222221, '08:00:00', 'wt'),(12, '1b', 'algebra_liniowa', 22222222221, '08:45:00', 'wt'),(13, '1b', 'programowanie_java', 22222222223, '09:40:00', 'wt'),(14, '1b', 'programowanie_zespolowe', 22222222226, '10:35:00', 'wt'),(15, '1b', 'programowanie_zespolowe', 22222222226, '12:00:00', 'wt'),(16, '1a', 'jezyk_polski', 22222222229, '08:00:00', 'sr'),(17, '1a', 'jezyk_polski', 22222222229, '08:50:00', 'sr'),(18, '1a', 'geometria_analityczna', 22222222228, '09:40:00', 'sr'),(19, '1a', 'geometria_analityczna', 22222222228, '10:35:00', 'sr'),(20, '1b', 'programowanie_java', 22222222223, '09:40:00', 'sr'),(21, '1b', 'jezyk_angielski', 22222222220, '10:35:00', 'sr'),(22, '1b', 'jezyk_polski', 22222222229, '12:00:00', 'sr'),(23, '1a', 'jezyk_polski', 22222222229, '08:00:00', 'czw'),(24, '1a', 'jezyk_polski', 22222222229, '08:50:00', 'czw'),(25, '1a', 'jezyk_polski', 22222222229, '09:40:00', 'czw'),(26, '1b', 'geometria_analityczna', 22222222228, '08:45:00', 'czw'),(27, '1b', 'geometria_analityczna', 22222222228, '09:40:00', 'czw'),(28, '1a', 'programowanie_zespolowe', 22222222226, '10:35:00', 'pt'),(29, '1a', 'programowanie_zespolowe', 22222222226, '12:00:00', 'pt'),(30, '1b', 'programowanie_zespolowe', 22222222226, '12:50:00', 'pt'),(31, '1b', 'programowanie_zespolowe', 22222222226, '13:40:00', 'pt'),(NULL, '1b', 'programowanie_java', 22222222226, '14:25:00', 'pt');
INSERT INTO `autoryzacja` (`pesel`, `login`, `haslo`, `kto`) VALUES ('22222222225', 'user', 'haslo', 'n');