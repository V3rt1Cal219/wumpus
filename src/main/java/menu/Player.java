package menu;

public class Player {
    private String name;
    private char[][] world;
    private int heroY;
    private int heroX;
    private static int spawnX;
    private static int spawnY;
    private static char direction;
    private static int arrows;
    private boolean hasGold;

    public Player(String name, GameEngine world) {
        this.name = name;
        this.world = world.getWorld();
        this.direction = 'N'; // Starting direction  W-west  N- North  E-East  S-South
        initializeHeroPosition();
    }

    public void initializeHeroPosition() {
        // Spawnpoint
        do {
            heroY = (int) (Math.random() * (GameEngine.getSize())) + 1;
            heroX = (int) (Math.random() * (GameEngine.getSize())) + 1;
        } while (world[heroY][heroX] == 'W');

        spawnX = heroY;
        spawnY = heroX;
        world[heroY][heroX] = 'H';
    }

    public void performMove(char move) {
        // Hero movement
        world[heroY][heroX] = ' ';

        int prevHeroX = heroY;
        int prevHeroY = heroX;

        Player player = this;

        switch (move) {
            case 'Q':
            case 'q':
                System.out.println("Game over. Thanks for playing!");
                System.exit(0);
                break;
            case 'R':
            case 'r':
                player.turnRight();
                break;
            case 'L':
            case 'l':
                player.turnLeft();
                break;
            case 'E':
            case 'e':
                player.shootArrow();
                break;
            case 'W':
            case 'w':
                player.moveForward();
                break;
            default:
                System.out.println("Invalid move. Try again.");
        }

        // Hit registration
        if (world[heroY][heroX] == 'U') {
            System.out.println("Wumpus clown got you, you dead.");
            System.exit(0);
        } else if (world[heroY][heroX] == 'G') {
            System.out.println("You are rich , " + name + "! You found all the gold(s)!");

            // Mark that the hero has the gold
            hasGold = true;
        } else if (world[heroY][heroX] == 'P') {
            System.out.println("You stuck in a backroom. The darkside got you. There is no escape from there.");
            arrows--;

            // Check if the hero is out of arrows
            if (arrows <= 0) {
                System.out.println("Out of arrows! Game over.");
                System.exit(0);
            }

            // Respawn the pit at the hero's previous position
            world[prevHeroX][prevHeroY] = 'P';

            // Check if the hero is out of arrows after falling into a pit
            if (arrows <= 0) {
                System.out.println("Out of arrows! Game over.");
                System.exit(0);
            }
        }

        // Update the hero's position
        world[heroY][heroX] = 'H';

        // Check if the hero is back to the starting position with the gold
        if (hasGold && heroY == spawnX && heroX == spawnY) {
            System.out.println("You win, " + name + ".");
            System.exit(0);
        }
    }



    private void moveForward() {
        // Clear the current position
        world[heroY][heroX] = ' ';

        // Calculate the new position based on the current direction
        int newHeroY = heroY;
        int newHeroX = heroX;

        switch (direction) {
            case 'N':
                newHeroY--;
                break;
            case 'E':
                newHeroX++;
                break;
            case 'S':
                newHeroY++;
                break;
            case 'W':
                newHeroX--;
                break;
            default:
                break;
        }

        // Check if the new position is within the boundaries
        if (newHeroY >= 0 && newHeroY < GameEngine.getSize() && newHeroX >= 0 && newHeroX < GameEngine.getSize()) {
            // Check if the new position is not a wall
            if (world[newHeroY][newHeroX] != 'W') {
                // Update the hero's position
                heroY = newHeroY;
                heroX = newHeroX;
            } else {
                System.out.println("You can not go through the wall.");
            }
        } else {
            System.out.println("You can not go outside the map");
        }

        // Update the world with the new hero position
        world[heroY][heroX] = 'H';
    }



    public void turnLeft() {
        // Movement left turn
        switch (direction) {
            case 'N':
                direction = 'W';
                break;
            case 'E':
                direction = 'N';
                break;
            case 'S':
                direction = 'E';
                break;
            case 'W':
                direction = 'S';
                break;
            default:
                break;
        }
        System.out.println("Direction: " + direction);
    }

    public void turnRight() {
        // Movement right turn
        switch (direction) {
            case 'N':
                direction = 'E';
                break;
            case 'E':
                direction = 'S';
                break;
            case 'S':
                direction = 'W';
                break;
            case 'W':
                direction = 'N';
                break;
            default:
                break;
        }
        System.out.println("Direction: " + direction);
    }

    private void turnAround() {
        // 2x left turn
        turnLeft();
        turnLeft();
    }

    public void shootArrow() {
        // Minus the arrow counter if it need
        if (arrows > 0) {
            arrows--;

            System.out.println("You shot an arrow! Arrows left: " + arrows);

            // Hit registration
            int arrowX = heroY;
            int arrowY = heroX;

            while (true) {
                switch (direction) {
                    case 'N':
                        arrowX--;
                        break;
                    case 'E':
                        arrowY++;
                        break;
                    case 'S':
                        arrowX++;
                        break;
                    case 'W':
                        arrowY--;
                        break;
                    default:
                        break;
                }

                // Arrow hit the W border
                if (arrowX <= 0 || arrowX >= GameEngine.getSize() || arrowY <= 0 || arrowY >= GameEngine.getSize()) {
                    System.out.println("You missed the wumpus and shoot across the wall. Nice aim bro.");
                    break;
                }
                if (world[arrowX][arrowY] == 'U') {
                    System.out.println("Nice aim, You killed the Wumpus.");
                    world[arrowX][arrowY] = 'X';
                    break;
                }
            }
        } else {
            System.out.println("Out of arrows! You can no longer shoot.");
        }
    }

    public String getName() {
        return name;
    }

    public char[][] getWorld() {
        return world;
    }

    public int getHeroY() {
        return heroY;
    }

    public int getHeroX() {
        return heroX;
    }

    public static char getDirection() {
        return direction;
    }

    public static int getArrows() {
        return arrows;
    }

    public boolean isHasGold() {
        return hasGold;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorld(char[][] world) {
        this.world = world;
    }

    public void setHeroY(int heroY) {
        this.heroY = heroY;
    }

    public void setHeroX(int heroX) {
        this.heroX = heroX;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public static void setArrows(int arrows) {
        Player.arrows = arrows;
    }

    public void setHasGold(boolean hasGold) {
        this.hasGold = hasGold;
    }

    public static int getSpawnX() {
        return spawnX;
    }

    public static void setSpawnX(int spawnX) {
        Player.spawnX = spawnX;
    }

    public static int getSpawnY() {
        return spawnY;
    }

    public static void setSpawnY(int spawnY) {
        Player.spawnY = spawnY;
    }
}