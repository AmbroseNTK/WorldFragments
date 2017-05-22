package kiet.nguyentuan;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.MathUtils;
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
    Block[][] map;
    public Block[][] getMap(){
        return map;
    }
    HashMap<String,Block> listBlock;
    UBJsonReader jsonReader = new UBJsonReader();
    G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
    ArrayList<Rule> ruleList=new ArrayList<Rule>();
    private boolean transforming=false;
    private boolean gameOver=false;
    private int  thePreviousKey=-1;
    final static int UP=0;
    final static int DOWN=1;
    final static int LEFT=2;
    final static int RIGHT=3;
    public boolean isGameOver(){
        return gameOver;
    }
    public MapBlock() {
        map=new Block[8][8];
        listBlock=new HashMap<String, Block>();
        ArrayList<String> lines=new ArrayList<String>();
        FileHandle handle = Gdx.files.local("Rules");
        String text = handle.readString();
        String wordsArray[] = text.split("\\r?\\n");
        for(String word : wordsArray) {
            lines.add(word);
        }
        int i;
        for(i=1;i<= Integer.parseInt(lines.get(0));i++){
            Block actor=new Block(lines.get(i),new ModelInstance(modelLoader.loadModel(Gdx.files.getFileHandle(lines.get(i)+ ".g3db", Files.FileType.Internal))),new Vector3(0,0,0));
            listBlock.put(lines.get(i),actor);
        }
        for(i=i;i<lines.size();i++){
            ruleList.add(new Rule(lines.get(i)));
        }
        for(int j=0;j<3;j++)
        createNewBlock();
    }
    public void createNewBlock(){
       ArrayList<Vector2> emptyBlock=new ArrayList<Vector2>();
       for(int i=0;i<8;i++){
           for(int j=0;j<8;j++){
               if(map[i][j]==null)
                   emptyBlock.add(new Vector2(i,j));
           }
       }
       if(emptyBlock.size()>2) {
           int r1 = MathUtils.random(1, emptyBlock.size()-1);
           map[(int)(emptyBlock.get(r1).x)][(int)(emptyBlock.get(r1).y)]=listBlock.get("water").clone();
           map[(int)(emptyBlock.get(r1).x)][(int)(emptyBlock.get(r1).y)].setPosition(emptyBlock.get(r1).x*2f,0f,emptyBlock.get(r1).y*2f);
           emptyBlock.remove(r1);
           r1=MathUtils.random(1,emptyBlock.size()-1);
           map[(int)(emptyBlock.get(r1).x)][(int)(emptyBlock.get(r1).y)]=listBlock.get("lava").clone();
           map[(int)(emptyBlock.get(r1).x)][(int)(emptyBlock.get(r1).y)].setPosition(emptyBlock.get(r1).x*2f,0f,emptyBlock.get(r1).y*2f);
       }
       else
       {
           gameOver=true;
       }
    }
    public void transformMap(int mode){
        switch (mode){
            case UP:
                if(!transforming) {
                    int[] node =new int[8];
                    for (int i = 0; i < 8; i++) {
                        node[i]=0;
                        for (int j = 0; j <8; j++) {
                            if (map[i][j] != null) {
                                map[i][j].setTarget((j-node[i]) * -2);
                                if(map[i][j].getTarget() !=0) {
                                    map[i][node[i]] = map[i][j].clone();
                                    map[i][node[i]] = null;
                                    node[i]++;
                                }
                            }
                        }
                    }
                  transforming=true;
                }
                thePreviousKey=0;
                break;

        }
        createNewBlock();
    }

    public boolean isTransforming() {
        return transforming;
    }
    public void update(float dT) {
        if(thePreviousKey==0) {
            boolean complete = true;
            for (int i = 0; i < 8; i++) {
                for (int j = 7; j >= 0; j--) {
                    if (map[i][j] != null) {
                        complete = complete & map[i][j].breakZ(false);
                        if (!map[i][j].breakZ(false)) {
                            map[i][j].moveForward(dT * 5f);
                        }
                    }
                }
            }
            if (complete) {
                transforming = false;
                thePreviousKey=-1;
            }
        }
    }
}
