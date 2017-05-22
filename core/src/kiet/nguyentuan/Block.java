package kiet.nguyentuan;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by kiettuannguyen on 22/05/2017.
 */

public class Block extends BaseActor3D {
    private float target;
    private boolean transforming=false;
    public Block(String id, ModelInstance instance, Vector3 position){
        super();
        setId(id);
        setPosition(position);
        setModelInstance(instance);
    }
    public boolean breakX(boolean pos){
        if(pos) {
            if (position.x > target)
                return true;
            return false;
        }
        else{
            if(position.x<target)
                return true;
            return false;
        }
    }
    public boolean breakZ(boolean pos){
        if(pos) {
            if (position.z > target)
                return true;
            return false;
        }
        else{
            if(position.z<target)
                return true;
            return false;
        }
    }
    public Block clone(){
        Block newbie =new Block(getId(),getInstance(),getPosition());
        newbie.setTarget(this.target);
        return newbie;
    }

    public float getTarget() {
        return target;
    }

    public void setTarget(float target) {
        this.target = target;
    }

    public boolean isTransforming() {
        return transforming;
    }

    public void setTransforming(boolean transforming) {
        this.transforming = transforming;
    }
}
