MultiScreenGame
===============

This project is a multi-screen game created for Android and PC. The goal of this project was to discover how to use
networking inside of a game, through both service discovery and actual event transmission. 

When starting the application, players would each have the version of the game on their own Android tablet and then share
one computer monitor (similar to how the Wii U is set up.) Each player connects to the PC lobby, and then start he game when 
ready. When the game starts, each player has a 3D view of the world on their own Android device, and a 2D shared view of 
the world from the PC. The ultimate goal of the game is to race in the maze to be the first to collect 3 stars hidden througout.
Clients automatically detect the Lobby through the use of Bonjour ZeroConfig, and use RabbitMQ to communicate. All messaging is done with JSON serialization, so that it can be portable between client types.

MSAndroidClient
===============
This is the Android client project file.


MSPCClient
===============
This is the PC client version of the game.



Requirements
===============
Must have RabbitMQ installed, and open the project in eclipse. RabbitMQ directory must be configured in MSPCClient config file.
