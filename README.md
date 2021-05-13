# cs362Toolkit

- This is a framework for term projects in an object-oriented design class.

### How to Run

- Go to `Boostrap.java` and run `Bootstrap.main()`
- You should receive a message at the bottom of the terminal that states something of the form of:
  ```
  Server Started
  Creating Gateway coms362.cards.streams.RemoteTableGateway@5be6e01c
  ```
- One UI feature that is missing is the ability to provision and join a new game. For now, everything is controlled by
  query parameters on the URL. The host starts a game like
  this: http://localhost:8080/cards362/?host&player=1&min=2&max=4&game=PU52MP
  - host – indicates the player is the host (initiator) of the game
  - player=1 – the host can also be a player, each player has an id
  - min=2 – in a multiplayer game min indicates the minimum number of players that need to join for the game to being
  - max=4 – the maximum number of players that can join the game
  - game=PU52MP – the game to be played, in this case Pickup 52 Multiplayer
- Additional players can join the game like this: http://localhost:8080/cards362/?player=2

### Authors:

- [Andrew Marek](https://github.com/andmarek)
- [Steven Sheets](https://github.com/Paper-SSheets)
- [Alexis Cordts](https://github.com/alexiscordts)
- [Jared Weiland](https://github.com/jbweiland)
- [Michael Huang](https://github.com/miklyellow520)
- Evan Christensen
