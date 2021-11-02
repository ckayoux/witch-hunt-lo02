# Avancement :

## ~~done~~ :
### appli
+ ~~Squelette de l'appli~~
### view
+ ~~Menus~~ _faire Menu<T> qui renvoie un objet de type \<T\> quand on fait un choix plutôt que toujours un entier ?_
+ ~~Classes d'affichage et de saisie console MVC~~
### controller
+ ~~Flux du jeu~~ _tours, rounds_
+ ~~Distribution de l'identité~~
+ ~~Distribution des cartes~~
### model
+ ~~Caractéristiques d'un joueur~~
+ ~~accusation d'un joueur~~ _ reste à choisir un joueur à accuser avec Strategy.**choosePlayerToAccuse()** pour chaque stratégie_
+ ~~défense d'un joueur~~ _redéfinir Strategy.**chooseDefenseAction()** pour chaque stratégie du joueur CPU. pour le moment, révèle systématiquement son identité_
+ ~~Elimination d'un joueur~~
+ ~~Carte AngryMob~~
+ ~~Immunisation d'un joueur (EvilEye)~~
	à revoir quand on aura codé EvilEye, on pourrait plutôt forcer la cible à jouer une action au tour d'après - qui est de choisir un joueur à accuser mais pas l'immunisé sauf si y a que lui d'accusable
## toutDoux :
### appli
+ Commentaires
+ Rangement
+ Gestion des erreurs
+ \+ de modularité, généralité (changer nombre de joueurs max, min ...)	
### view
+ tous les displays qui vont avec les choses à rajouter dans le modèle
### controller
+ _**ScoreCounter**_ et conditions de victoire dans Tabletop
### model
+ méthode _CPUPlayer.**choosePlayerToAccuse()**_, exemple pour la stratégie ExploringStrategy
+ fonctionnalité pour qu'un joueur humain puisse afficher ses cartes lors de son tour
+ méthode _Player.**hunt()**_
+ coder les effets de chacune des RumourCards
+ IA (stratégies, attribuer des valeurs aux cartes selon la situation et la stratégie, choisir telle ou telle victime ...)

