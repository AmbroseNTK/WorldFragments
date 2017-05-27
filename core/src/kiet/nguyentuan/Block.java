package kiet.nguyentuan;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;

public class Block extends BaseActor3D {
    private float target;
    private UBJsonReader jsonReader = new UBJsonReader();
    private G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
    private Rectangle boundingUP,boundingDOWN,boundingLEFT,boundingRIGHT;
    private Rectangle moveZone;
    private boolean canMove;
    public boolean isCanMove(){
        return canMove;
    }
    public Block(String id, Vector3 position){
        super();
        setId(id);
        setPosition(position);
        setModelInstance(new ModelInstance(modelLoader.loadModel(Gdx.files.getFileHandle(id+".g3db", Files.FileType.Internal))));
    }

    public Block clone(){
        Block newbie =new Block(getId(),getPosition());
        newbie.setTarget(this.target);
        return newbie;
    }
    public boolean collisionwWithUP(){
        return getBoundingBox().overlaps(boundingUP);
    }
    public boolean collisionwWithDOWN(){
        return getBoundingBox().overlaps(boundingDOWN);
    }
    public boolean collisionwWithLEFT(){
        return getBoundingBox().overlaps(boundingLEFT);
    }
    public boolean collisionwWithRIGHT(){
        return getBoundingBox().overlaps(boundingRIGHT);
    }
    public boolean collisionWithOther(Block other){
        return getBoundingBox().overlaps(other.getBoundingBox());
    }
    public Rectangle getBoundingBox(){
        return new Rectangle(getPosition().x,-1*getPosition().z,2,2);
    }
    public float getTarget() {
        return target;
    }

    public void setTarget(float target) {
        this.target = target;
    }

    public Rectangle getMoveZone() {
        return moveZone;
    }

    public void setMoveZone(Rectangle moveZone) {
        this.moveZone = moveZone;
        boundingUP=new Rectangle(moveZone.x,moveZone.y+moveZone.getHeight(),moveZone.getWidth(),0);
        boundingDOWN=new Rectangle(moveZone.x,moveZone.y,moveZone.getWidth(),0);
        boundingLEFT=new Rectangle(moveZone.x,moveZone.y,0,moveZone.getHeight());
        boundingRIGHT=new Rectangle(moveZone.x+moveZone.getWidth(),moveZone.y,0,moveZone.getHeight());
    }

    @Override
    public void act(float dT) {
            super.act(dT);
    }
}
