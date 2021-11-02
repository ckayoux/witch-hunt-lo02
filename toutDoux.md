#Avancement :

## ~~done~~ :
#appli
+ ~~Squelette de l'appli~~
#view
+ ~~Menus~~
+ ~~Classes d'affichage et de saisie console MVC~~
#controller
+ ~~Flux du jeu~~ _tours, rounds_
+ ~~Distribution de l'identité~~
+ ~~Distribution des cartes~~
#model
+ ~~Caractéristiques d'un joueur~~
+ ~~Elimination d'un joueur~~
+ ~~Carte AngryMob~~
+ ~~Immunisation d'un joueur (EvilEye)~~
	à revoir quand on aura codé EvilEye, on pourrait plutôt forcer la cible à jouer une action au tour d'après - qui est de choisir un joueur à accuser mais pas l'immunisé sauf si y a que lui d'accusable
## toutDoux :
#appli
+ Commentaires
+ Rangement
+ Gestion des erreurs
+ + de modularité, généralité (changer nombre de joueurs max, min ...)
#view
+ tous les displays qui vont avec les choses à rajouter dans le modèle
#controller
+ _**ScoreCounter**_ et conditions de victoire dans Tabletop
#model
+ ajouter un display pour _Player.**revealIdentity()**_
+ ajouter un display pour _Player.**eliminate**()_, et changer la méthode eliminate pour que ce soit p1.eliminate(p2) plutôt que p2.eliminate(), comme ça on peut display "p2 a été éliminé par p1"
+ ajouter un display pour _Player.**addScore()**_ : si pts > 0 "you gain pts points" sinon si <0 "you loose pts points" sinon nada
+ méthode _Player.**choosePlayerToAccuse()**_ : avec un menu pour HumanPlayer, avec la stratégie choisie pour CPUPlayer
+ méthode _Player.**defend()**_ : *pouvoir jouer un effet witch ou _Player.**revealIdentity()**_
+ fonctionnalité pour qu'un joueur humain puisse afficher ses cartes lors de son tour
+ méthode _Player.**hunt()**_
+ coder les effets de chacune des RumourCards
+ IA (stratégies, attribuer des valeurs aux cartes selon la situation et la stratégie, choisir telle ou telle victime ...)

