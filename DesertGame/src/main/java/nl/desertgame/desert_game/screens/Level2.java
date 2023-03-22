package nl.desertgame.desert_game.screens;

import com.github.hanyaeger.api.scenes.DynamicScene;
import com.github.hanyaeger.api.scenes.TileMapContainer;
import javafx.scene.paint.Color;
import nl.desertgame.desert_game.DesertGame;
import nl.desertgame.desert_game.map.RoomMap;
import nl.desertgame.desert_game.map.StartMap;

public class Level2 extends DynamicScene implements TileMapContainer {

    private DesertGame desertGame;

    public Level2(DesertGame desertGame) {
        this.desertGame = desertGame;
    }
    @Override
    public void setupTileMaps(){
        addTileMap(new RoomMap());
    }

    @Override
    public void setupScene() {
        setBackgroundColor(Color.BLACK);
    }

    @Override
    public void setupEntities() {

    }
}
