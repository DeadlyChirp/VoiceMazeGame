#!/bin/bash

# Résolution des dépendances
mvn dependency:resolve

# Nettoyage et installation
mvn clean install

# Installation du fichier JAR
mvn install:install-file -Dfile=LIUM_SpkDiarization-4.2.jar -DgroupId=LIUM_SpkDiarization-4.2 -DartifactId=LIUM_4.2 -Dversion=4.2 -Dpackaging=jar

#pour lancer ce fichier, on utilise : sh config.sh
#si besoin, on donne les permissions : chmod +x config.sh
