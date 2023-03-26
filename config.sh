#!/bin/bash


# Installation du fichier JAR
mvn install:install-file -Dfile=LIUM_SpkDiarization-8.4.1.jar -DgroupId=lium_spkdiarization -DartifactId=LIUM_4.2 -Dversion=4.2 -Dpackaging=jar

# Résolution des dépendances
mvn dependency:resolve

# Nettoyage et installation
mvn clean install

#pour lancer ce fichier, on utilise : sh config.sh
#si besoin, on donne les permissions : chmod +x config.sh
