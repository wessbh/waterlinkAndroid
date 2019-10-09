-- phpMyAdmin SQL Dump
-- version 4.8.4
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le :  Dim 31 mars 2019 à 23:23
-- Version du serveur :  10.1.37-MariaDB
-- Version de PHP :  7.3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `waterlink`
--

-- --------------------------------------------------------

--
-- Structure de la table `charges_fixes`
--

CREATE TABLE `charges_fixes` (
  `charges_fixes_sonede` int(10) NOT NULL,
  `tva` int(10) NOT NULL,
  `timbre_fiscale` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `charges_fixes`
--

INSERT INTO `charges_fixes` (`charges_fixes_sonede`, `tva`, `timbre_fiscale`) VALUES
(5900, 19, 60);

-- --------------------------------------------------------

--
-- Structure de la table `consommation`
--

CREATE TABLE `consommation` (
  `id_consommation` int(11) NOT NULL,
  `id_user` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `valeur` float NOT NULL,
  `total` float NOT NULL,
  `prix` float NOT NULL,
  `seuil` int(10) DEFAULT '0',
  `tranche` int(10) DEFAULT '200',
  `alert_sent` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `consommation`
--

INSERT INTO `consommation` (`id_consommation`, `id_user`, `date`, `valeur`, `total`, `prix`, `seuil`, `tranche`, `alert_sent`) VALUES
(1, 556677, '2019-02-12 00:00:00', 37, 227000, 21.103, 25, 450, 2);

-- --------------------------------------------------------

--
-- Structure de la table `consommation_quotidienne`
--

CREATE TABLE `consommation_quotidienne` (
  `id` int(10) NOT NULL,
  `id_user` int(10) DEFAULT NULL,
  `date` datetime NOT NULL,
  `total` int(10) DEFAULT NULL,
  `prix` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `facture`
--

CREATE TABLE `facture` (
  `id` int(10) NOT NULL,
  `id_user` int(10) NOT NULL,
  `prix_onas` float NOT NULL,
  `date_debut` datetime(3) NOT NULL,
  `date_fin` datetime(3) NOT NULL,
  `prix_facture` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `facture`
--

INSERT INTO `facture` (`id`, `id_user`, `prix_onas`, `date_debut`, `date_fin`, `prix_facture`) VALUES
(30, 556677, 178442, '2019-07-01 22:17:22.664', '2019-07-01 22:17:22.664', 525898);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `num_contrat` int(11) NOT NULL,
  `nom` varchar(255) NOT NULL,
  `prenom` varchar(255) NOT NULL,
  `num_tel` int(11) NOT NULL,
  `region` varchar(255) NOT NULL,
  `code_postal` int(11) NOT NULL,
  `mail` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `api_key` varchar(32) DEFAULT NULL,
  `device_token` varchar(255) DEFAULT NULL,
  `date_inscrit` datetime(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`num_contrat`, `nom`, `prenom`, `num_tel`, `region`, `code_postal`, `mail`, `password`, `api_key`, `device_token`, `date_inscrit`) VALUES
(556677, 'bh', 'wess', 56782844, 'nabeul', 8075, 'wess@gmail.com', 'sha1$16f450d9$1$2aa57294b6cc39be489b0c4085954d0a85c21789', 'hoRoD7JTF7WYrF2ibJYWpaOhcroestK8', 'eTNbpuLj1Xs:APA91bEpuCJbgnqk-RRJ_dc0iLMXge82gqHCCR42QwgTlVV74gASj25e2NDysvdhg9Ss7RdbPSaNu4Gfe7Ttr11oHb_6hl_a97EcdQt-cXERNJuB8R2msaaogjZeEow9RnqEQYPvQQ0P', '2018-12-01 07:14:10.000');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `consommation`
--
ALTER TABLE `consommation`
  ADD PRIMARY KEY (`id_consommation`),
  ADD KEY `userId` (`id_user`);

--
-- Index pour la table `consommation_quotidienne`
--
ALTER TABLE `consommation_quotidienne`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_userid` (`id_user`);

--
-- Index pour la table `facture`
--
ALTER TABLE `facture`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_user_id` (`id_user`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`num_contrat`),
  ADD UNIQUE KEY `id` (`num_contrat`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `consommation`
--
ALTER TABLE `consommation`
  MODIFY `id_consommation` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `consommation_quotidienne`
--
ALTER TABLE `consommation_quotidienne`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `facture`
--
ALTER TABLE `facture`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `consommation`
--
ALTER TABLE `consommation`
  ADD CONSTRAINT `userId` FOREIGN KEY (`id_user`) REFERENCES `user` (`num_contrat`);

--
-- Contraintes pour la table `consommation_quotidienne`
--
ALTER TABLE `consommation_quotidienne`
  ADD CONSTRAINT `fk_userid` FOREIGN KEY (`id_user`) REFERENCES `user` (`num_contrat`);

--
-- Contraintes pour la table `facture`
--
ALTER TABLE `facture`
  ADD CONSTRAINT `fk_user_id` FOREIGN KEY (`id_user`) REFERENCES `consommation` (`id_user`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
