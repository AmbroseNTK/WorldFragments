package kiet.nguyentuan;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StreamUtils;
import com.badlogic.gdx.utils.UBJsonReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

public class MapBlock {
    private int[][] matrix;
    private HashMap<Vector2,Block> listBlock;
    private boolean gameOver;
    private InputProcessor inputProcessor;

    public static final int MAX_WIDTH=8;
    public static final int MAX_HEIGHT=8;

    public MapBlock(){
        gameOver=false;
        matrix=new int[MAX_WIDTH][MAX_HEIGHT];
        listBlock=new HashMap<Vector2, Block>();
    }

    public void createNewBlock(){
        ArrayList<Vector2> emptyList=checkToken(0);
        String[] initBlock={"water","lava"};
        if(emptyList.size()>1) {
            for (String s : initBlock) {
                Vector2 pos = emptyList.get(MathUtils.random(0, emptyList.size() - 1));
                Block newBlock = new Block(s, new Vector3(pos.x * 2, 0, pos.y * -2));
                listBlock.put(pos, newBlock);
                matrix[(int)(pos.x)][(int)(pos.y)]=1;
                emptyList.remove(pos);
            }

        }
        else
            gameOver=true;
        calcMoveZone();
    }
    public void calcMoveZone(){
        for(Vector2 v:listBlock.keySet()){
            int currentX=(int)(v.x);
            int currentY=(int)(v.y);
            int[] metric=new int[4];
            for(int i=0;i<currentX;i++)
                if(matrix[i][currentY]==0)
                    metric[0]++;
            for(int i=currentX;i<MAX_WIDTH;i++)
                if(matrix[i][currentY]==0)
                    metric[1]++;
            for(int j=0;j<currentY;j++)
                if(matrix[currentX][j]==0)
                    metric[2]++;
            for(int j=currentY;j<MAX_HEIGHT;j++)
                if(matrix[currentX][j]==0)
                    metric[3]++;
            Rectangle bounding=new Rectangle((currentX-metric[0])*2,(currentY-metric[2])*2,(metric[0]+metric[1]+1)*2,(metric[2]+metric[3]+1)*2);
            listBlock.get(v).setMoveZone(bounding);
        }
    }
    public void reCalcMatrix(){
        matrix=new int[MAX_WIDTH][MAX_HEIGHT];
        HashMap<Vector2,Block> newBlockList=new HashMap<Vector2, Block>();
        for(Vector2 v:listBlock.keySet()){
            int currentX=(int)(listBlock.get(v).getPosition().x/2.0f);
            int currentY=(int)(listBlock.get(v).getPosition().z/-2.0f);
            newBlockList.put(new Vector2(currentX,currentY),listBlock.get(v));
            matrix[currentX][currentY]=1;
        }
        listBlock=newBlockList;
        calcMoveZone();
    }
    public ArrayList<Vector2> checkToken(int token){
        ArrayList<Vector2> result=new ArrayList<Vector2>();
        for(int i=0;i<MAX_WIDTH;i++)
            for(int j=0;j<MAX_HEIGHT;j++)
                if(matrix[i][j]==token)
                    result.add(new Vector2(i,j));
        return result;

    }

    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public void setInputProcessor(InputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }
    public void update(float dT, ModelBatch batch, Environment environment){
        for(Block test:listBlock.values()) {
            switch (inputProcessor.getThePreviousKey()) {
                case InputProcessor.UP:
                    if (!test.collisionwWithUP()) {
                        test.moveForward(Gdx.graphics.getDeltaTime() * 10.0f);
                        inputProcessor.setLocked(true);
                    } else {
                        inputProcessor.setLocked(false);

                    }
                    break;
                case InputProcessor.DOWN:
                    if (!test.collisionwWithDOWN()) {
                        test.moveForward(Gdx.graphics.getDeltaTime() * -10.0f);
                        inputProcessor.setLocked(true);
                    } else {
                        inputProcessor.setLocked(false);

                    }
                    break;
                case InputProcessor.LEFT:
                    if (!test.collisionwWithLEFT()) {
                        test.moveRight(Gdx.graphics.getDeltaTime() * -10.0f);
                        inputProcessor.setLocked(true);
                    } else {
                        inputProcessor.setLocked(false);

                    }
                    break;
                case InputProcessor.RIGHT:
                    if (!test.collisionwWithRIGHT()) {
                        test.moveRight(Gdx.graphics.getDeltaTime() * 10.0f);
                        inputProcessor.setLocked(true);
                    } else {
                        inputProcessor.setLocked(false);

                    }
                    break;

            }
            test.act(dT);
            batch.render(test.getInstance(),environment);
        }
    }
}
