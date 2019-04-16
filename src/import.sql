
INSERT INTO `klasa` (`nazwa_klasy`, `rok_szkolny`, `wychowawca`) VALUES
('1a', 2019, 22222222220),
('1b', 2019, 22222222221);
INSERT INTO `nauczyciel` (`pesel`, `imie`, `nazwisko`) VALUES
(22222222220, 'Benton', 'Wildsmit'),
(22222222221, 'Darnall', 'Musson'),
(22222222222, 'Roderic', 'Spensley'),
(22222222223, 'Der', 'Houndsom'),
(22222222224, 'Fairfax', 'Mewton'),
(22222222225, 'Bengt', 'Kubis'),
(22222222226, 'Serge', 'Steagall'),
(22222222227, 'Yanaton', 'Britnell'),
(22222222228, 'Kayne', 'Halpen'),
(22222222229, 'Zebadiah', 'Drake');
INSERT INTO `przedmiot` (`nazwa_przedmiotu`) VALUES
('algebra_liniowa'),
('geometria_analityczna'),
('informatyka_ogolna'),
('jezyk_angielski'),
('jezyk_polski'),
('programowanie_java'),
('programowanie_zespolowe');

INSERT INTO `przedmiot_pomocnicza` (`id`,`nazwa_przedmiotu`, `prowadzacy`) VALUES
('1','algebra_liniowa', '22222222221'),
('2','programowanie_java', '22222222222'),
('3','programowanie_java', '22222222223'),
('4','programowanie_java', '22222222224'),
('5','informatyka_ogolna', '22222222225'),
('6','informatyka_ogolna', '22222222226'),
('7','programowanie_zespolowe', '22222222226'),
('8','algebra_liniowa', '22222222227'),
('9','geometria_analityczna', '22222222228'),
('10','algebra_liniowa', '22222222228'),
('11','jezyk_polski', '22222222229'),
('12','jezyk_angielski', '22222222220');
INSERT INTO `rodzic` (`pesel`, `dziecko`, `imie_ojca`, `nazwisko_ojca`, `imie_matki`, `nazwisko_matki`) VALUES
(11111111110, 32222222220, 'Orin', 'Titmuss', 'Ines', 'Disman'),
(11111111111, 32222222221, 'Chancey', 'Feakins', 'Stacee', 'Strickland'),
(11111111112, 32222222222, 'Harp', 'Daugherty', 'Hedy', 'Pennicard'),
(11111111113, 32222222223, 'Sigfried', 'Tomkies', 'Regine', 'Cirlos'),
(11111111114, 32222222224, 'Pablo', 'Maletratt', 'Sharity', 'Ashfold'),
(11111111115, 32222222225, 'Northrup', 'Morin', 'Robbin', 'Bissill'),
(11111111116, 32222222226, 'Frazier', 'Heller', 'Kristina', 'Raine'),
(11111111117, 32222222227, 'Matthiew', 'Scargle', 'Moyna', 'Frankham'),
(11111111118, 32222222228, 'Rudie', 'Birrel', 'Libbey', 'Gammett'),
(11111111119, 32222222229, 'Aldous', 'Nowakowski', 'Thomasin', 'Moule');
INSERT INTO `uczen` (`pesel`, `nazwa_klasy`, `imie`, `nazwisko`) VALUES
(32222222220, '1b', 'Zenon', 'Szmit'),
(32222222221, '1a', 'Gniewomir', 'Iryd'),
(32222222222, '1a', 'Adrian', 'Bury'),
(32222222223, '1a', 'Krystian', 'Drozd'),
(32222222224, '1a', 'Katarzyna', 'Kusz'),
(32222222225, '1a', 'Marek', 'Klan'),
(32222222226, '1b', 'Arkadiusz', 'Seagall'),
(32222222227, '1b', 'Bartosz', 'Belz'),
(32222222228, '1b', 'Kain', 'Harpel'),
(32222222229, '1b', 'Zbigniew', 'Smok');