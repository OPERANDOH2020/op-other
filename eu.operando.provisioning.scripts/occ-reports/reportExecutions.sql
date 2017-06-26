-- phpMyAdmin SQL Dump
-- version 4.0.10.10
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generato il: Mag 22, 2017 alle 17:00
-- Versione del server: 5.1.73
-- Versione PHP: 5.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `operando_report`
--

CREATE DATABASE IF NOT EXISTS `operando_report` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `operando_report`;
-- --------------------------------------------------------

--
-- Struttura della tabella `t_report_mng_list`
--

DROP TABLE IF EXISTS `t_report_mng_list`;
CREATE TABLE IF NOT EXISTS `t_report_mng_list` (
  `Report` varchar(200) NOT NULL,
  `Description` varchar(200) DEFAULT NULL,
  `Version` varchar(100) NOT NULL,
  `Location` varchar(500) NOT NULL,
  `Parameters` varchar(500) DEFAULT NULL,
  `OSPs` varchar(100) NOT NULL,
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Dump dei dati per la tabella `t_report_mng_list`
--

INSERT INTO `t_report_mng_list` (`Report`, `Description`, `Version`, `Location`, `Parameters`, `OSPs`, `ID`) VALUES
('Volunteer Breakdown Report', 'Breakdown of employment information and volunteering preferences ', '1', 'http://integration.operando.esilab.org:8120/operando/webui/birt/frameset', '__report=pdi/report_operando/VolunteerEmploymentDetailsAndAssistancePreferences.rptdesign', 'Ami', '9');

-- --------------------------------------------------------

--
-- Struttura della tabella `t_report_mng_request`
--

DROP TABLE IF EXISTS `t_report_mng_request`;
CREATE TABLE IF NOT EXISTS `t_report_mng_request` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `InsertDate` datetime NOT NULL,
  `Name` varchar(200) NOT NULL,
  `Email` varchar(200) NOT NULL,
  `Description` varchar(1000) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=20 ;

--
-- Dump dei dati per la tabella `t_report_mng_request`
--

INSERT INTO `t_report_mng_request` (`ID`, `InsertDate`, `Name`, `Email`, `Description`) VALUES
(1, '0000-00-00 00:00:00', 'Giulia', 'giulia@progettidiimpresa.it', 'Prova'),
(2, '0000-00-00 00:00:00', 'Daniele', 'daniele.detecterror@progettidiimpresa.it', 'test'),
(3, '0000-00-00 00:00:00', 'Daneiel', 'daniele@progettidiimpresa.it', 'test'),
(4, '0000-00-00 00:00:00', 'test', 'testtest', 'lalala'),
(19, '0000-00-00 00:00:00', 'test', 'federico.dibernardo@progettidiimpresa.it', 'Richiesta di test di un report'),
(13, '2016-11-01 00:00:00', 'Luigi De Luigi', 'luigi.deluigi@email.it', 'Richiedo la creazione di un report personalizzato per me'),
(14, '2016-10-11 00:00:00', 'Federico De Federichi', 'federico.defederichi@email.it', 'Richiedo la creazione di un report personalizzato per la mamma'),
(15, '2016-12-04 00:00:00', 'Alessandro De Alessandri', 'alessandro.dealessandri@email.it', 'Richiedo la creazione di un report personalizzato per la figlia'),
(18, '2017-02-06 15:39:56', 'Daniele', 'daniele.detecterror@progettidiimpresa.it', 'La mia richiesta mira a richiedere la creazione di un report che risponda alle mie esigenze');

-- --------------------------------------------------------

--
-- Struttura della tabella `t_report_mng_results`
--

DROP TABLE IF EXISTS `t_report_mng_results`;
CREATE TABLE IF NOT EXISTS `t_report_mng_results` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ExecutionDate` datetime NOT NULL,
  `Report` varchar(200) NOT NULL,
  `ReportDescription` varchar(200) NOT NULL,
  `ReportVersion` varchar(100) NOT NULL,
  `OSP` varchar(200) NOT NULL,
  `FileName` varchar(500) NOT NULL,
  `IDReport` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=23 ;

--
-- Dump dei dati per la tabella `t_report_mng_results`
--

INSERT INTO `t_report_mng_results` (`ID`, `ExecutionDate`, `Report`, `ReportDescription`, `ReportVersion`, `OSP`, `FileName`, `IDReport`) VALUES
(23, '2017-07-01 23:00:00', 'Volunteer Breakdown Report', 'Breakdown of employment information and volunteering preferences ', '1.0', 'Ami', 'VolunteerBreakdownReport_20170701.pdf', 9);

-- --------------------------------------------------------

--
-- Struttura della tabella `t_report_mng_schedules`
--

DROP TABLE IF EXISTS `t_report_mng_schedules`;
CREATE TABLE IF NOT EXISTS `t_report_mng_schedules` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `OSPs` varchar(200) NOT NULL,
  `Report` varchar(200) NOT NULL,
  `StartDate` datetime NOT NULL,
  `RepeatEveryNumb` int(11) NOT NULL,
  `RepeatEveryType` varchar(200) NOT NULL,
  `DayOfWeek` varchar(200) NOT NULL,
  `StoragePeriodNumb` int(11) NOT NULL,
  `StoragePeriodType` varchar(200) NOT NULL,
  `DescriptionSchedules` varchar(200) NOT NULL,
  `Description` varchar(200) NOT NULL,
  `Version` varchar(200) NOT NULL,
  `Lastrun` datetime DEFAULT NULL,
  `NextScheduled` datetime NOT NULL,
  `GiornoMese` int(11) NOT NULL,
  `GiornoAnno` date NOT NULL,
  `IDReport` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=229 ;

--
-- Dump dei dati per la tabella `t_report_mng_schedules`
--

INSERT INTO `t_report_mng_schedules` (`ID`, `OSPs`, `Report`, `StartDate`, `RepeatEveryNumb`, `RepeatEveryType`, `DayOfWeek`, `StoragePeriodNumb`, `StoragePeriodType`, `DescriptionSchedules`, `Description`, `Version`, `Lastrun`, `NextScheduled`, `GiornoMese`, `GiornoAnno`, `IDReport`) VALUES
(229, 'Ami', 'Volunteer Breakdown Report', '2017-01-01 23:00:00', 1, 'MONTH(s)', '1', 1, 'MONTH(s)', '', '', '', '2017-07-01 23:00:00', '2017-08-01 23:00:00', 0, '2017-08-01', 0);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
