SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE DATABASE IF NOT EXISTS `dziennik_elektroniczny` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `dziennik_elektroniczny`;

CREATE TABLE `autoryzacja` (
  `pesel` bigint(20) NOT NULL,
  `login` varchar(30) NOT NULL,
  `haslo` varchar(30) NOT NULL,
  `kto` varchar(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `klasa` (
  `nazwa_klasy` varchar(2) NOT NULL,
  `wychowawca` bigint(20) DEFAULT NULL,
  `rok_szkolny` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `klasa` (`nazwa_klasy`, `wychowawca`, `rok_szkolny`) VALUES
('1a', 22222222220, '2019-01-01'),
('1b', 22222222221, '2019-01-01');

CREATE TABLE `nauczyciel` (
  `pesel` bigint(20) NOT NULL,
  `imie` varchar(30) DEFAULT NULL,
  `nazwisko` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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

CREATE TABLE `obecnosc` (
  `id` bigint(20) NOT NULL,
  `przedmiot` bigint(20) DEFAULT NULL,
  `pesel` bigint(20) DEFAULT NULL,
  `data` date DEFAULT NULL,
  `wartosc` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `ocena` (
  `id` int(11) NOT NULL,
  `przedmiot` bigint(20) DEFAULT NULL,
  `pesel` bigint(20) NOT NULL,
  `stopien` varchar(2) DEFAULT NULL,
  `data` date DEFAULT NULL,
  `opis` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `przedmiot` (
  `nazwa_przedmiotu` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `przedmiot` (`nazwa_przedmiotu`) VALUES
('algebra_liniowa'),
('geometria_analityczna'),
('informatyka_ogolna'),
('jezyk_angielski'),
('jezyk_polski'),
('programowanie_java'),
('programowanie_zespolowe');

CREATE TABLE `rodzic` (
  `pesel` bigint(20) NOT NULL,
  `dziecko` bigint(20) DEFAULT NULL,
  `imie_ojca` varchar(30) DEFAULT NULL,
  `nazwisko_ojca` varchar(30) DEFAULT NULL,
  `imie_matki` varchar(30) DEFAULT NULL,
  `nazwisko_matki` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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

CREATE TABLE `sklad_klasy` (
  `id` int(11) NOT NULL,
  `klasa` varchar(2) NOT NULL,
  `uczen` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `sklad_klasy` (`id`, `klasa`, `uczen`) VALUES
(1, '1a', 32222222221),
(2, '1a', 32222222222),
(3, '1a', 32222222223),
(4, '1a', 32222222224),
(5, '1a', 32222222225),
(6, '1b', 32222222226),
(7, '1b', 32222222227),
(8, '1b', 32222222228),
(9, '1b', 32222222229),
(10, '1b', 32222222220);

CREATE TABLE `uczen` (
  `pesel` bigint(20) NOT NULL,
  `nazwa_klasy` varchar(2) DEFAULT NULL,
  `imie` varchar(30) DEFAULT NULL,
  `nazwisko` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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

CREATE TABLE `zajecia` (
  `id` int(11) NOT NULL,
  `klasa` varchar(2) NOT NULL,
  `przedmiot` varchar(30) NOT NULL,
  `prowadzacy` bigint(20) NOT NULL,
  `godzina` time NOT NULL,
  `dzien` varchar(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `zajecia` (`id`, `klasa`, `przedmiot`, `prowadzacy`, `godzina`, `dzien`) VALUES
(1, '1a', 'algebra_liniowa', 22222222221, '08:00:00', 'pon'),
(2, '1a', 'algebra_liniowa', 22222222221, '08:50:00', 'pon'),
(3, '1a', 'programowanie_java', 22222222222, '09:40:00', 'pon'),
(4, '1a', 'programowanie_java', 22222222222, '10:35:00', 'pon'),
(5, '1b', 'programowanie_java', 22222222223, '08:45:00', 'pon'),
(6, '1b', 'programowanie_java', 22222222223, '09:40:00', 'pon'),
(7, '1a', 'informatyka_ogolna', 22222222225, '08:00:00', 'wt'),
(8, '1a', 'informatyka_ogolna', 22222222225, '08:50:00', 'wt'),
(9, '1a', 'programowanie_zespolowe', 22222222226, '09:40:00', 'wt'),
(10, '1a', 'programowanie_zespolowe', 22222222226, '10:35:00', 'wt'),
(11, '1b', 'algebra_liniowa', 22222222221, '08:00:00', 'wt'),
(12, '1b', 'algebra_liniowa', 22222222221, '08:45:00', 'wt'),
(13, '1b', 'programowanie_java', 22222222223, '09:40:00', 'wt'),
(14, '1b', 'programowanie_zespolowe', 22222222226, '10:35:00', 'wt'),
(15, '1b', 'programowanie_zespolowe', 22222222226, '12:00:00', 'wt'),
(16, '1a', 'jezyk_polski', 22222222229, '08:00:00', 'sr'),
(17, '1a', 'jezyk_polski', 22222222229, '08:50:00', 'sr'),
(18, '1a', 'geometria_analityczna', 22222222228, '09:40:00', 'sr'),
(19, '1a', 'geometria_analityczna', 22222222228, '10:35:00', 'sr'),
(20, '1b', 'programowanie_java', 22222222223, '09:40:00', 'sr'),
(21, '1b', 'jezyk_angielski', 22222222220, '10:35:00', 'sr'),
(22, '1b', 'jezyk_polski', 22222222229, '12:00:00', 'sr'),
(23, '1a', 'jezyk_polski', 22222222229, '08:00:00', 'czw'),
(24, '1a', 'jezyk_polski', 22222222229, '08:50:00', 'czw'),
(25, '1a', 'jezyk_polski', 22222222229, '09:40:00', 'czw'),
(26, '1b', 'geometria_analityczna', 22222222228, '08:45:00', 'czw'),
(27, '1b', 'geometria_analityczna', 22222222228, '09:40:00', 'czw'),
(28, '1a', 'programowanie_zespolowe', 22222222226, '10:35:00', 'pt'),
(29, '1a', 'programowanie_zespolowe', 22222222226, '12:00:00', 'pt'),
(30, '1b', 'programowanie_zespolowe', 22222222226, '12:50:00', 'pt'),
(31, '1b', 'programowanie_zespolowe', 22222222226, '13:40:00', 'pt');


ALTER TABLE `autoryzacja`
  ADD PRIMARY KEY (`pesel`);

ALTER TABLE `klasa`
  ADD PRIMARY KEY (`nazwa_klasy`),
  ADD KEY `FKmxovwiw2qfv2shhm5dbbgaphp` (`wychowawca`);

ALTER TABLE `nauczyciel`
  ADD PRIMARY KEY (`pesel`);

ALTER TABLE `obecnosc`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKolviqa2btnk0e71agi851swb7` (`pesel`);

ALTER TABLE `ocena`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK4u4plwm0ilmqvcakhrqcy4dh` (`pesel`);

ALTER TABLE `przedmiot`
  ADD PRIMARY KEY (`nazwa_przedmiotu`);

ALTER TABLE `rodzic`
  ADD PRIMARY KEY (`pesel`),
  ADD KEY `FKhdrr4fj5055xgeu53lqo1kacs` (`dziecko`);

ALTER TABLE `sklad_klasy`
  ADD PRIMARY KEY (`id`),
  ADD KEY `klasa` (`klasa`),
  ADD KEY `uczen` (`uczen`);

ALTER TABLE `uczen`
  ADD PRIMARY KEY (`pesel`),
  ADD KEY `FKhhbcs24yp2d32ecuphf8sit2b` (`nazwa_klasy`);

ALTER TABLE `zajecia`
  ADD PRIMARY KEY (`id`),
  ADD KEY `klasa` (`klasa`),
  ADD KEY `prowadzacy` (`prowadzacy`),
  ADD KEY `przedmiot` (`przedmiot`);


ALTER TABLE `obecnosc`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE `ocena`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `sklad_klasy`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

ALTER TABLE `zajecia`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;


ALTER TABLE `autoryzacja`
  ADD CONSTRAINT `FKrfn0dlip3mtqdjy30k1lvu7lv` FOREIGN KEY (`pesel`) REFERENCES `nauczyciel` (`pesel`),
  ADD CONSTRAINT `autoryzacja_ibfk_1` FOREIGN KEY (`pesel`) REFERENCES `uczen` (`pesel`);

ALTER TABLE `klasa`
  ADD CONSTRAINT `FKmxovwiw2qfv2shhm5dbbgaphp` FOREIGN KEY (`wychowawca`) REFERENCES `nauczyciel` (`pesel`);

ALTER TABLE `obecnosc`
  ADD CONSTRAINT `FKolviqa2btnk0e71agi851swb7` FOREIGN KEY (`pesel`) REFERENCES `uczen` (`pesel`);

ALTER TABLE `ocena`
  ADD CONSTRAINT `FK4u4plwm0ilmqvcakhrqcy4dh` FOREIGN KEY (`pesel`) REFERENCES `uczen` (`pesel`);

ALTER TABLE `rodzic`
  ADD CONSTRAINT `FKhdrr4fj5055xgeu53lqo1kacs` FOREIGN KEY (`dziecko`) REFERENCES `uczen` (`pesel`);

ALTER TABLE `sklad_klasy`
  ADD CONSTRAINT `sklad_klasy_ibfk_1` FOREIGN KEY (`klasa`) REFERENCES `klasa` (`nazwa_klasy`),
  ADD CONSTRAINT `sklad_klasy_ibfk_2` FOREIGN KEY (`uczen`) REFERENCES `uczen` (`pesel`);

ALTER TABLE `uczen`
  ADD CONSTRAINT `FKhhbcs24yp2d32ecuphf8sit2b` FOREIGN KEY (`nazwa_klasy`) REFERENCES `klasa` (`nazwa_klasy`);

ALTER TABLE `zajecia`
  ADD CONSTRAINT `zajecia_ibfk_1` FOREIGN KEY (`klasa`) REFERENCES `klasa` (`nazwa_klasy`),
  ADD CONSTRAINT `zajecia_ibfk_2` FOREIGN KEY (`prowadzacy`) REFERENCES `nauczyciel` (`pesel`),
  ADD CONSTRAINT `zajecia_ibfk_3` FOREIGN KEY (`przedmiot`) REFERENCES `przedmiot` (`nazwa_przedmiotu`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
