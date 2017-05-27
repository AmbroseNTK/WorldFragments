package kiet.nguyentuan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by kiettuannguyen on 26/05/2017.
 */

public class InputProcessor {

    private int thePreviousKey;
    private boolean locked;
    public static final int UP= Input.Keys.UP;
    public static final int DOWN=Input.Keys.DOWN;
    public static final int LEFT=Input.Keys.LEFT;
    public static final int RIGHT=Input.Keys.RIGHT;

    public int getThePreviousKey() {
        return thePreviousKey;
    }
    public boolean isLocked() {
        return locked;
    }

    private MapBlock mapBlock;
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    public InputProcessor(){

        thePreviousKey=-1;

    }
    public void update() {
        if (!locked) {
            if (Gdx.input.isKeyPressed(UP)) {
                thePreviousKey = UP;
                mapBlock.reCalcMatrix();
            }
            if (Gdx.input.isKeyPressed(DOWN)) {
                thePreviousKey = DOWN;
                mapBlock.reCalcMatrix();
            }
            if (Gdx.input.isKeyPressed(LEFT)) {
                thePreviousKey = LEFT;
                mapBlock.reCalcMatrix();
            }
            if (Gdx.input.isKeyPressed(RIGHT)) {
                thePreviousKey = RIGHT;
                mapBlock.reCalcMatrix();
            }
        }
    }


    public MapBlock getMapBlock() {
        return mapBlock;
    }

    public void setMapBlock(MapBlock mapBlock) {
        this.mapBlock = mapBlock;
    }
}
