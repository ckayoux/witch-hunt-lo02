# DOC

### Overview
    [X] overview.html

### fr.sos.witchhunt
    [x] DisplayMediator
    [x] InputMediator
    [x] PlayerDisplayObservable ~
    [x] PlayerInputObservable
    [x] Visitable
    [x] Visitor

### fr.sos.witchhunt.controller
    [ ]	package-info.java (RFF)
    [X] Tabletop
    [X] ScoreCounter
	[X] Round
	[X] Turn
	[ ] InputController (RFF)
	[ ] DisplayController (RFF)
	[ ] Application (RFF)
	[ ] Game (RFF)

### fr.sos.witchhunt.model
	[X] Identity
	
	[X] Resettable

	[ ] Menu (RFF)

### fr.sos.witchhunt.model.cards
	[ ] package-info.java
	[ ] RumourCard
	[ ] RumourCardsPile
	[ ] IdentityCard
	[ ] Effect
	[ ] WitchEffect
	[ ] HuntEffect
	[ ] Les 12 ? extends RumourCard

### fr.sos.witchhunt.model.players
	[X] Player
	[X] CPUPlayer reste à faire chooseStrategy
	[X] HumanPlayer
	[X] DefenseAction
	[X] TurnAction

### fr.sos.witchhunt.model.cpustrategies
	[X] PlayStrategy
	[X] GropingStrategy
	[X] OffensiveStrategy
	[X] DefensiveStrategy (+ test selectBestCard)
	[ ] CPUStrategy
	[ ] CardValue
	[ ] CardValueMap

### fr.sos.witchhunt.view
	[ ] TOUT (RFF)