package kiet.nguyentuan;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by kiettuannguyen on 21/05/2017.
 */

public class BaseActor3D {
    protected String id;
    protected ModelInstance instance;
    public ModelInstance getInstance(){
        return instance;
    }
    protected final Vector3 position;
    protected final Quaternion rotation;
    protected final Vector3 scale;
    public BaseActor3D(){
        instance=null;
        position=new Vector3(0,0,0);
        rotation=new Quaternion();
        scale=new Vector3(1,1,1);
    }
    public String getId(){
        return id;
    }
    public void setId(String id)
    {
        this.id=id;
    }
    public void setModelInstance(ModelInstance m){
        instance=m;
    }
    public Matrix4 calculateTransform(){
        return new Matrix4(getPosition(),rotation,scale);
    }
    public void act(float dT){
        instance.transform.set(calculateTransform());
    }
    public void draw(ModelBatch batch, Environment environment){
        batch.render(instance,environment);
    }

    public Vector3 getPosition() {
        return position;
    }
    public void setPosition(Vector3 v){
        position.set(v);
    }
    public void setPosition(float x, float y, float z){
        position.set(x,y,z);
    }
    public void addPosition(Vector3 v){
        position.add(v);
    }
    public void addPosition(float x, float y, float z){
        position.set(x,y,z);
    }
    public float getTurnAngle(){
        return rotation.getAngleAround(0,-1,0);
    }
    public void setTurnAngle(float degrees){
        rotation.set(new Quaternion(Vector3.Y,degrees));
    }
    public void turn(float degrees){
        rotation.mul(new Quaternion(Vector3.Y,-degrees));
    }
    public void moveForward(float dist){
        addPosition(rotation.transform(new Vector3(0,0,-1).scl(dist)));
    }
    public void moveUp(float dist){
        addPosition(rotation.transform(new Vector3(0,1,0).scl(dist)));
    }
    public void moveRight(float dist){
        addPosition(rotation.transform(new Vector3(1,0,0).scl(dist)));
    }
    public void setColor(Color c){
        for(Material m:instance.materials)
            m.set(ColorAttribute.createDiffuse(c));
    }
    public BaseActor3D clone(){
        BaseActor3D newbie=new BaseActor3D();
        newbie.copy(this);
        return newbie;
    }
    public void copy(BaseActor3D actor3D){
        this.instance=new ModelInstance(actor3D.instance);
        this.position.set(actor3D.position);
        this.rotation.set(actor3D.rotation);
        this.scale.set(actor3D.scale);
        this.id=actor3D.id;
    }
}
