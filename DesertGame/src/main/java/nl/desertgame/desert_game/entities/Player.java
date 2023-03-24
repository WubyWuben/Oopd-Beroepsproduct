package nl.desertgame.desert_game.entities;

import com.github.hanyaeger.api.AnchorPoint;
import com.github.hanyaeger.api.Coordinate2D;
import com.github.hanyaeger.api.Size;
import com.github.hanyaeger.api.UpdateExposer;
import com.github.hanyaeger.api.entities.Collided;
import com.github.hanyaeger.api.entities.Collider;
import com.github.hanyaeger.api.entities.Direction;
import com.github.hanyaeger.api.entities.impl.DynamicSpriteEntity;
import com.github.hanyaeger.api.userinput.KeyListener;
import javafx.scene.effect.ColorInput;
import javafx.scene.input.KeyCode;
import nl.desertgame.desert_game.DesertGame;
import nl.desertgame.desert_game.entities.Enemies.Boss;
import nl.desertgame.desert_game.entities.Enemies.Enemy;
import nl.desertgame.desert_game.entities.Enemies.MidBoss;
import nl.desertgame.desert_game.map.tiles.*;
import nl.desertgame.desert_game.screens.*;
import nl.desertgame.desert_game.screens.StartRoom;

import java.util.List;
import java.util.Set;

public class Player extends DynamicSpriteEntity implements KeyListener, Collided, Collider, UpdateExposer {

    private DesertGame desertGame;
    public static int nextScene = 2;

    public static int currentscene = 1;

    public static int previousScene = 0;
    private static int health;
    private static int potions;
    private static int totalHealth;

    public static void setTotalHealth(int totalHealth) {
        Player.totalHealth = totalHealth;
    }
    public static int getTotalHealth(){
        return totalHealth;
    }

    boolean isColliding = false;
    static int direction;

    public static boolean hasKey = true;


    public Player(DesertGame desertgame,Coordinate2D initialLocation) {
        super("sprites/player.png", initialLocation, new Size(32, 32), 2, 2);
        this.desertGame = desertgame;
        setAnchorPoint(AnchorPoint.CENTER_CENTER);
    }
    @Override
    public void onPressedKeysChange(Set<KeyCode> pressedKeys) {
        if (pressedKeys.isEmpty()) {
            setSpeed(0);
            return;
        }
        if (!isColliding) {
            Move(pressedKeys);
        }
        if(isColliding) {
            switch (direction) {
                case 2 -> { //moving up
                    if (!pressedKeys.contains(KeyCode.UP)) {
                        Move(pressedKeys);
                    } else {
                        setSpeed(0);
                        System.out.println("No Up");
                    }
                }
                case 1 -> {
                    if (!pressedKeys.contains(KeyCode.RIGHT)) {
                        Move(pressedKeys);
                    } else {
                        setSpeed(0);
                    }
                }
                case 0 -> {
                    if (!pressedKeys.contains(KeyCode.DOWN)) {
                        Move(pressedKeys);
                    } else {
                        setSpeed(0);
                    }
                }
                case 3 -> {
                    if (!pressedKeys.contains(KeyCode.LEFT)) {
                        Move(pressedKeys);
                    } else {
                        setSpeed(0);
                    }
                }
                default -> {
                }
            }
        }
    }

    void Move(Set<KeyCode> pressedKeys) {
            KeyCode keyPressed = pressedKeys.iterator().next();
            switch (keyPressed) {
                case LEFT -> {
                    moveplayer(3);

                }
                case RIGHT -> {
                    moveplayer(1);

                }
                case UP -> {
                    moveplayer(2);

                }
                case DOWN -> {
                    moveplayer(0);

                }
                case A -> { //was used for testing
                    System.out.println(nextScene);
                    desertGame.setActiveScene(nextScene);
                    setScenes(+1);
                }
                case X -> { //was used for testing
                    doDamage(1);
                }
                case Z -> { //was used for testing
                    healPlayer();
                }
                case C -> {

                    System.out.println(StartRoom.getPlayerLocation());
                }
                default -> setSpeed(0);
            }
    }

    void moveplayer(int m){
        setCurrentFrameIndex(m);
        setMotion(3,m*90);
        if (!isColliding) {
            direction = m;
        }
    }
    @Override
    public void explicitUpdate(long l) {
        if (isColliding) {
        }
        isColliding = false;
    }

    public int getPotions() {
        return potions;
    }

    @Override
    public void setCurrentFrameIndex(int index) {
        super.setCurrentFrameIndex(index);
    }

    private void setScenes(int change) {
       previousScene += change;
       currentscene += change;
       nextScene += change;
       System.out.println(currentscene);
       System.out.println(previousScene);
       System.out.println(nextScene);
    }



    @Override
    public void onCollision(Collider collidingObjects) {
        if (collidingObjects instanceof SolidTile) {
            isColliding = true;
            setMotion(0, 0);
        } else if (collidingObjects instanceof ExitTile) {
            desertGame.setActiveScene(nextScene);
            setScenes(+1);
        } else if (collidingObjects instanceof EntryTile) {
            desertGame.setActiveScene(previousScene);
            setScenes(-1);
        } else if (collidingObjects instanceof Keydoor) {
            if(Player.hasKey) {
                desertGame.setActiveScene(nextScene);
                setScenes(+1);
            } else {
            System.out.println("player has no key");
            }
        } else if (collidingObjects instanceof TopDoor) {
            desertGame.setActiveScene(5);
            currentscene = 5;
        } else if (collidingObjects instanceof BottomDoor) {
            desertGame.setActiveScene(2);
            currentscene = 2;

        } else if( collidingObjects instanceof Object){
            isColliding = true;
            setMotion(0, 0);
        }else if( collidingObjects instanceof Enemy){
            if(collidingObjects instanceof Boss) {
                doDamage(3);
            } else if(collidingObjects instanceof MidBoss){
                doDamage(2);
            } else {
                doDamage(1);
            }


        }
    }


    public void doDamage(int damage) {
        setHealth(-damage);
        updateSceneHearts();
    }
    public void healPlayer() {
        setHealth(1);
        updateSceneHearts();
    }

    public void updateSceneHearts() {
        switch (currentscene) {
            case 1 -> {
                StartRoom.updateHearts();
            }
            case 2 -> {
                EnemyChoiceRoom.updateHearts();
            }
            case 3 -> {
                FakeChestRoom.updateHearts();
            }
            case 4 -> {
                BossRoom.updateHearts();
            }
            case 5 -> {
                MiddleBoss.updateHearts();
            }
        }
    }

    public static void setHealth(int hearts) {
        health = getHealth() + hearts;
    }

    public static void setPotions(int pots){
        potions = pots;
    }

    public static int getHealth() {
        return health;
    }
}
