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

CREATE TABLE `klasa` (
  `nazwa_klasy` varchar(2) NOT NULL,
  `rok_szkolny` year(4) DEFAULT NULL,
  `wychowawca` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `nauczyciel` (
  `pesel` int(11) NOT NULL,
  `nazwa_klasy` varchar(2) DEFAULT NULL,
  `imie` varchar(30) DEFAULT NULL,
  `nazwisko` varchar(30) DEFAULT NULL,
  `czyDyrektor` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `obecnosc` (
  `pesel` int(11) NOT NULL,
  `data` date DEFAULT NULL,
  `wartosc` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `ocena` (
  `pesel` int(11) NOT NULL,
  `przedmiot` varchar(30) DEFAULT NULL,
  `stopien` varchar(2) DEFAULT NULL,
  `data` date DEFAULT NULL,
  `opis` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `przedmiot` (
  `nazwa_przedmiotu` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `rodzic` (
  `pesel` int(11) NOT NULL,
  `dziecko` int(11) DEFAULT NULL,
  `imie` varchar(30) DEFAULT NULL,
  `nazwisko` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `uczen` (
  `pesel` int(11) NOT NULL,
  `nazwa_klasy` varchar(2) DEFAULT NULL,
  `imie` varchar(30) DEFAULT NULL,
  `nazwisko` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


ALTER TABLE `klasa`
  ADD PRIMARY KEY (`nazwa_klasy`);

ALTER TABLE `nauczyciel`
  ADD PRIMARY KEY (`pesel`);

ALTER TABLE `obecnosc`
  ADD PRIMARY KEY (`pesel`);

ALTER TABLE `ocena`
  ADD PRIMARY KEY (`pesel`);

ALTER TABLE `przedmiot`
  ADD PRIMARY KEY (`nazwa_przedmiotu`);

ALTER TABLE `rodzic`
  ADD PRIMARY KEY (`pesel`);

ALTER TABLE `uczen`
  ADD PRIMARY KEY (`pesel`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;