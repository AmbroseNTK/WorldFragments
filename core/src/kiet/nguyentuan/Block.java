package kiet.nguyentuan;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;

import java.util.HashMap;

/**
 * Created by kiettuannguyen on 22/05/2017.
 */

public class Block extends BaseActor3D {
    private float target;
    private boolean transforming;
    private boolean using;
    private Vector2 posCross;
    final UBJsonReader jsonReader = new UBJsonReader();
    final G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
    static int countTransforming=0;
    public Block(String id, ModelInstance instance, Vector3 position){
        super();
        setId(id);
        setPosition(position);
        setModelInstance(instance);
        setUsing(true);
    }
    public Block(String id, Vector2 position){
        super();
        setModelInstance(new ModelInstance(modelLoader.loadModel(Gdx.files.getFileHandle(id+ ".g3db", Files.FileType.Internal))));
        setPosCross(position);
        setUsing(true);
        setId(id);
    }
    public void breakX(boolean pos){
        if(pos) {
            if (position.x >= target)
                setTransforming(false);
        }
        else{
            if(position.x<=target)
                setTransforming(false);
        }
    }
    public void breakZ(boolean pos){
        if(pos) {
            if (position.z >= target)
                setTransforming(false);
        }
        else{
            if(position.z<=target)
                setTransforming(false);
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
        if(transforming)
            countTransforming++;
        else
            countTransforming--;
    }

    public boolean isUsing() {
        return using;
    }

    public void setUsing(boolean using) {
        this.using = using;
    }

    public Vector2 getPosCross() {
        return posCross;
    }

    public void setPosCross(Vector2 posCross) {
        this.posCross = posCross;
        position.x=posCross.x*2;
        position.z=posCross.y*-2;
    }
}
