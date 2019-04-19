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

DROP TABLE IF EXISTS `klasa`;
CREATE TABLE IF NOT EXISTS `klasa` (
  `nazwa_klasy` varchar(2) NOT NULL,
  `rok_szkolny` year(4) DEFAULT NULL,
  `wychowawca` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`nazwa_klasy`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `klasa` (`nazwa_klasy`, `rok_szkolny`, `wychowawca`) VALUES
('1a', 2019, 22222222220),
('1b', 2019, 22222222221);

DROP TABLE IF EXISTS `nauczyciel`;
CREATE TABLE IF NOT EXISTS `nauczyciel` (
  `pesel` bigint(11) NOT NULL,
  `imie` varchar(30) DEFAULT NULL,
  `nazwisko` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`pesel`)
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

DROP TABLE IF EXISTS `obecnosc`;
CREATE TABLE IF NOT EXISTS `obecnosc` (
  `pesel` bigint(11) NOT NULL,
  `data` date DEFAULT NULL,
  `wartosc` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`pesel`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `ocena`;
CREATE TABLE IF NOT EXISTS `ocena` (
  `pesel` bigint(11) NOT NULL,
  `przedmiot` varchar(30) DEFAULT NULL,
  `stopien` varchar(2) DEFAULT NULL,
  `data` date DEFAULT NULL,
  `opis` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`pesel`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `przedmiot`;
CREATE TABLE IF NOT EXISTS `przedmiot` (
  `nazwa_przedmiotu` varchar(30) NOT NULL,
  PRIMARY KEY (`nazwa_przedmiotu`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `przedmiot` (`nazwa_przedmiotu`) VALUES
('algebra_liniowa'),
('geometria_analityczna'),
('informatyka_ogolna'),
('jezyk_angielski'),
('jezyk_polski'),
('programowanie_java'),
('programowanie_zespolowe');

DROP TABLE IF EXISTS `przedmiot_pomocnicza`;
CREATE TABLE IF NOT EXISTS `przedmiot_pomocnicza` (
  `id` bigint(11) NOT NULL,
  `nazwa_przedmiotu` varchar(30) NOT NULL,
  `prowadzacy` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

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

DROP TABLE IF EXISTS `rodzic`;
CREATE TABLE IF NOT EXISTS `rodzic` (
  `pesel` bigint(11) NOT NULL,
  `dziecko` bigint(11) DEFAULT NULL,
  `imie_ojca` varchar(30) DEFAULT NULL,
  `nazwisko_ojca` varchar(30) DEFAULT NULL,
  `imie_matki` varchar(30) DEFAULT NULL,
  `nazwisko_matki` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`pesel`)
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

DROP TABLE IF EXISTS `uczen`;
CREATE TABLE IF NOT EXISTS `uczen` (
  `pesel` bigint(11) NOT NULL,
  `nazwa_klasy` varchar(2) DEFAULT NULL,
  `imie` varchar(30) DEFAULT NULL,
  `nazwisko` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`pesel`)
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
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
