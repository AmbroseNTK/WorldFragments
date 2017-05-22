package kiet.nguyentuan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by kiettuannguyen on 21/05/2017.
 */

public class Stage3D {
    private Environment environment;
    private PerspectiveCamera camera;
    public PerspectiveCamera getCamera(){
        return camera;
    }
    private final ModelBatch batch;
    private ArrayList<BaseActor3D> actorList;
    public Stage3D(){
        environment=new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight,0.7f,0.7f,0.7f,1));
        DirectionalLight light=new DirectionalLight();
        Color lightColor= new Color(0.9f,0.9f,0.9f,1);
        Vector3 lightVector=new Vector3(-1.0f,-0.75f,-0.25f);
        light.set(lightColor,lightVector);
        environment.add(light);
        camera=new PerspectiveCamera(67, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.position.set(10f,10f,10f);
        camera.lookAt(0,0,0);
        camera.near=0.01f;
        camera.far=100f;
        camera.update();
        batch=new ModelBatch();
        actorList=new ArrayList<BaseActor3D>();
    }
    public void act(float dT){
        camera.update();
        for(BaseActor3D b : actorList){
            b.act(dT);
        }

    }
    public void draw(){
        batch.begin(camera);
        for(BaseActor3D b:actorList)
            b.draw(batch,environment);
        batch.end();
    }
    public void addActor(BaseActor3D baseActor3D){
        actorList.add(baseActor3D);
    }
    public void removeActor(BaseActor3D baseActor3D){
        actorList.remove(baseActor3D);
    }
    public void setCameraPosition(float x, float y, float z){
        camera.position.set(x,y,z);
    }
    public void setCameraPosition(Vector3 v){
        camera.position.set(v);
    }
    public void moveCamera(float x, float y, float z){
        camera.position.add(x,y,z);
    }
    public void moveCamera(Vector3 v){
        camera.position.add(v);
    }
    public void moveCameraForward(float dist){
        Vector3 forward=new Vector3(camera.direction.x,0,camera.direction.z).nor();
        moveCamera(forward.scl(dist));
    }
    public void moveCameraRight(float dist){
        Vector3 right=new Vector3(camera.direction.z,0,-camera.direction.x).nor();
        moveCamera(right.scl(dist));
    }
    public void moveCameraUp(float dist){
        moveCamera(0,dist,0);
    }
    public void turnCamera(float angle){
        camera.rotate(Vector3.Y,-angle);
    }
    public void tiltCamera(float angle) {
        Vector3 right = new Vector3(camera.direction.z, 0, -camera.direction.x);
        camera.direction.rotate(right,angle);
    }
    public void setCameraDircetion(Vector3 v){
        camera.lookAt(v);
        camera.up.set(0,1,0);
    }
    public void setCameraDirection(float x, float y, float z){
        setCameraDircetion(new Vector3(x,y,z));
    }
}
