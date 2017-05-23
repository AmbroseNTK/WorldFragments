package kiet.nguyentuan;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by kiettuannguyen on 22/05/2017.
 */

public class Block extends BaseActor3D {
    private float target;
    private boolean transforming;
    private boolean using;
    public Block(String id, ModelInstance instance, Vector3 position){
        super();
        setId(id);
        setPosition(position);
        setModelInstance(instance);
        transforming=false;
        setUsing(true);
    }
    public boolean breakX(boolean pos){
        if(pos) {
            if (position.x >= target)
                return true;
            return false;
        }
        else{
            if(position.x<=target)
                return true;
            return false;
        }
    }
    public boolean breakZ(boolean pos){
        if(pos) {
            if (position.z >= target)
                return true;
            return false;
        }
        else{
            if(position.z<=target)
                return true;
            return false;
        }
    }
    public Block clone(G3dModelLoader modelLoader){
        Block newbie =new Block(getId(),new ModelInstance(modelLoader.loadModel(Gdx.files.getFileHandle(getId()+ ".g3db", Files.FileType.Internal))),getPosition());
        newbie.setTarget(this.target);
        newbie.setUsing(isUsing());
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

    public boolean isUsing() {
        return using;
    }

    public void setUsing(boolean using) {
        this.using = using;
    }
}
