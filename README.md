# <p align="center">Lost Voice -  2022-SB2-G2-Lost_Voices</p>
  
Voici le répertoire git de notre projet de programmation de L2 en Java. En effet, nous avons eu le sujet SB2 qui consiste en la création d'un labyrinthe qui se dirige à l'aide d'une détection vocal du genre et des commandes. Vous verrez plus loin que le jeu possède plusieurs facettes intéréssantes.


## 🛠️ Installation
Nous avons utilisé Maven pour faire fonctionner notre projet. L'idée était de pouvoir charger toute nos dépendances rapidement et en une seule ligne de commande sans passer par des étapes inutiles. 
C'est pour cela que nous avons utilisé Maven. Pour bien initialiser notre répertoire Maven, il faudra lancer cette commande : 

```bash
sh config.sh
```
Cette commande permet de lancer les lignes de commande qui sont les suivantes :

```bash
# Installation du fichier JAR
mvn install:install-file -Dfile=LIUM_SpkDiarization-8.4.1.jar -DgroupId=lium_spkdiarization -DartifactId=LIUM_4.2 -Dversion=4.2 -Dpackaging=jar

# Résolution des dépendances
mvn dependency:resolve

# Nettoyage et installation
mvn clean install
```
        
Chacune réalise une tâche importante qui permet le bon fonctionnement du projet. 


## 🧐 Lancement     
Pour lancer le jeu, il faut compiler le fichier StartMenu.java à l'aide d'un IDE ou alors de la commande suivante : 

```bash
javac StartMenu.java
java StartMenu
```
Aussi, pour lancer le jeu, vous pouvez utiliser l'executable que vous allez obtenir après avoir lancer le fichier .sh. Pour cela vous devrez lui donner les droits d'executions et donc faire les commandes suivantes : 

```bash
chmod +x target/VocalMaze-1.0-SNAPSHOT.jar
mvn clean package
java -cp target/VocalMaze-1.0-SNAPSHOT.jar com.VocalMaze.Menus.StartMenu
```


## 🙇 Prérequis      
- Maven
- Java 17 (ou plus)
- Une connexion internet (rapide de préférence)
        