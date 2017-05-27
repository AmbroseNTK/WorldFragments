package kiet.nguyentuan;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ObjectMap;

import java.security.Key;
import java.util.ArrayList;

public class MainActivity extends ApplicationAdapter {

	MapBlock mapBlock;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	private ModelBatch modelBatch;
	private Environment environment;
	public Environment environment2;
	private Block test;
	private Block testOther;
	private InputProcessor inputProcessor;

	@Override
	public void create () {

		test=new Block("hut",new Vector3(0,0,0));
		testOther=new Block("house",new Vector3(0,0,-6));
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		environment.add(new DirectionalLight().set(1,1,1,1,-1,1));

		environment2 = new Environment();
		environment2.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
		environment2.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 5f, 5f, 5f));

		modelBatch=new ModelBatch();
		inputProcessor=new InputProcessor();
		inputProcessor.setLocked(false);

		mapBlock=new MapBlock();
		mapBlock.setInputProcessor(inputProcessor);
		mapBlock.createNewBlock();

		inputProcessor.setMapBlock(mapBlock);

	}

	@Override
	public void render () {

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		inputProcessor.update();


		camController.update();
		modelBatch.begin(cam);
		mapBlock.update(Gdx.graphics.getDeltaTime(), modelBatch,environment);
		modelBatch.end();

	}
	@Override
	public void resize (int width, int height) {
		float vFoV = 50f * ((float)height/(float)width);

		cam = new PerspectiveCamera(vFoV, width, height);
		cam.position.set(0f, 0f, 10f);
		cam.up.set(Vector3.Y);
		cam.lookAt(0f, 0f, 0f);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		if (camController == null) {
			camController = new CameraInputController(cam);

			InputMultiplexer mx = new InputMultiplexer();
			mx.addProcessor(camController);
			Gdx.input.setInputProcessor(mx);

		}
		else {
			camController.camera = cam;
		}
	}


	@Override
	public void dispose () {

	}

}
