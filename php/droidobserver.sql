-- phpMyAdmin SQL Dump
-- version 3.3.9.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Czas wygenerowania: 08 Lis 2011, 13:03
-- Wersja serwera: 5.0.84
-- Wersja PHP: 5.2.17

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Baza danych: `tsm`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla  `files`
--

CREATE TABLE IF NOT EXISTS `files` (
  `id` int(11) NOT NULL auto_increment,
  `patient_id` int(11) NOT NULL,
  `filename` varchar(30) NOT NULL,
  `date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Zrzut danych tabeli `files`
--


-- --------------------------------------------------------

--
-- Struktura tabeli dla  `locations`
--

CREATE TABLE IF NOT EXISTS `locations` (
  `id` int(11) NOT NULL auto_increment,
  `patient_id` int(11) NOT NULL,
  `latitude` varchar(20) NOT NULL,
  `longitude` varchar(20) NOT NULL,
  `date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Zrzut danych tabeli `locations`
--


-- --------------------------------------------------------

--
-- Struktura tabeli dla  `patients`
--

CREATE TABLE IF NOT EXISTS `patients` (
  `id` int(11) NOT NULL auto_increment,
  `mobile` varchar(13) default NULL,
  `name` varchar(30) NOT NULL,
  `surname` varchar(30) default NULL,
  `disease` varchar(50) default NULL,
  `last_ip` varchar(50) default NULL,
  `email` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Zrzut danych tabeli `patients`
--

