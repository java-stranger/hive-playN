# hive-playN
A playN-based (http://playn.io) GUI implementation for hive-core (https://github.com/java-stranger/hive-core).

The goal is to create a functional gui that can be used to play online with others, and mainly to debug your own AI (bots).

## Compiling and running
You need first to compile and install (```mvn install```) hive-core. 
Then use ```mvn package``` to create a jar file that you can run as usual:
```
java -jar target/foobar.jar
```

## Controls
*Subject to changes!*
```
g - Show/hide coordinate grid
Space - Make next (random) move
u - Undo last move
n - New game
y - Change view theme (cycling between existing)
```
