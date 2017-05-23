package kiet.nguyentuan;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.UBJsonReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapBlock {
    public int MAX_WIDTH=8;
    public int MAX_HEIGHT=8;
    public float TRANSFORM_SPEED=10.0f;

    public final static int UP=0;
    public final static int DOWN=1;
    public final static int LEFT=2;
    public final static int RIGHT=3;
    private boolean transforming;
    private int thePreviousKey;
    protected int[][] map;
    protected HashMap<String, Block> blockDictonary;
    protected ArrayList<Rule> rules;
    private HashMap<Vector2,Block> blockList;
    final UBJsonReader jsonReader = new UBJsonReader();
    final G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
    protected boolean gameOver;
    public boolean isGameOver(){
        return gameOver;
    }
    protected boolean complete;
    public MapBlock(){

        map=new int[MAX_WIDTH][MAX_HEIGHT];
        gameOver=false;
        blockDictonary=new HashMap<String, Block>();
        rules=new ArrayList<Rule>();
        blockList=new HashMap<Vector2, Block>();
        transforming=false;
        thePreviousKey=-1;
        complete=false;

        for(int i=0;i<MAX_WIDTH;i++)
            for(int j=0;j<MAX_HEIGHT;j++)
                map[i][j]=0;

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
            blockDictonary.put(lines.get(i),actor);
        }
        for(i=i;i<lines.size();i++){
            rules.add(new Rule(lines.get(i)));
        }
    }
    public ArrayList<Vector2> checkMap(int tokens){
        ArrayList<Vector2> collect=new ArrayList<Vector2>();
        for(int i=0;i<MAX_WIDTH;i++)
            for(int j=0;j<MAX_HEIGHT;j++)
                if(map[i][j]==tokens)
                    collect.add(new Vector2(i,j));
        return collect;
    }
    public ArrayList<Vector2> checkRow(int tokens,int row) {
        ArrayList<Vector2> collect=new ArrayList<Vector2>();
        for(int i=0;i<MAX_WIDTH;i++)
            if(map[i][row]==tokens)
                collect.add(new Vector2(i,row));
        return collect;
    }
    public ArrayList<Vector2> checkColumn(int tokens,int column) {
        ArrayList<Vector2> collect=new ArrayList<Vector2>();
        for(int j=0;j<MAX_HEIGHT;j++)
            if(map[column][j]==tokens)
                collect.add(new Vector2(column,j));
        return collect;
    }
    public void initNewBlock(){
        ArrayList<Vector2> empty=checkMap(0);
        cleanBlockList();
        if(empty.size()!=1) {
            String[] blockNames = {"water", "lava"};
            Block newBlock;
            for (int i = 0; i < blockNames.length; i++) {
                Vector2 emptyPos=empty.get(MathUtils.random(0,empty.size()-1));
                map[(int)(emptyPos.x)][(int)(emptyPos.y)]=1;
                newBlock=blockDictonary.get(blockNames[i]).clone(modelLoader);
                newBlock.setPosition(emptyPos.x*2,0,emptyPos.y*-2);
                newBlock.setUsing(true);
                blockList.put(emptyPos,newBlock);
                empty.remove(emptyPos);
            }
        }
        else
            gameOver=true;
    }
    public void checkAnimation(){

    }
    public void cleanBlockList(){
        for(Block b: getBlockList().values())
            if(!b.isUsing())
                blockList.remove(b);
    }

    public void update(float dT){
        int count=0;
        for(Block b:blockList.values()){
                if (isTransforming()) {
                    switch (thePreviousKey) {
                        case UP:
                            if(!b.breakZ(false)) {
                                b.moveForward(TRANSFORM_SPEED * dT);
                            }
                            else
                                count++;
                            break;
                    }
                }
            b.act(dT);
        }
        if(count==blockList.size()) {
            transforming = false;
            for(Block b:blockList.values())
                b.setTransforming(false);
            initNewBlock();
        }
    }

    public void transformMap(int mode){
        transforming=true;
        complete=false;
        switch (mode){
            case UP:/*
                for(int i=0;i<MAX_WIDTH;i++) {
                    ArrayList<Vector2> search=checkColumn(1,i);
                    if(search.size()>0) {
                        for (int j = 0; j < MAX_HEIGHT; j++) {
                            if (blockList.containsKey(new Vector2(i, j))) {
                                Block newBlock = blockList.get(new Vector2(i, j));
                                newBlock.setTarget((MAX_HEIGHT - search.size()) * -2);
                                newBlock.setTransforming(true);
                                newBlock.setUsing(true);
                                map[i][j] = 0;
                                map[i][MAX_HEIGHT - search.size()] = 1;
                                blockList.remove(new Vector2(i, j));
                                blockList.put(new Vector2(i, MAX_HEIGHT - search.size()), newBlock);
                                search.remove(new Vector2(i, j));
                            }
                        }
                    }
                }*/
                int[] node=new int[MAX_WIDTH];
                HashMap<Vector2,Block> tempBlockList=new HashMap<Vector2, Block>();
                for(Vector2 v:blockList.keySet()){
                    if(v.y!=7) {
                        blockList.get(v).setTarget(blockList.get(v).position.z + (MAX_HEIGHT - 1 - v.y - node[(int) (v.x)]) * -2);
                        tempBlockList.put(new Vector2(v.x,MAX_HEIGHT-node[(int)(v.x)]),blockList.get(v));
                        node[(int) (v.x)]++;
                    }
                }
                blockList = tempBlockList;
                thePreviousKey=UP;
                break;
            case DOWN:
                break;
            case LEFT:
                break;
            case RIGHT:
                break;
        }
    }
    public void render(ModelBatch batch, Environment environment){
        for(Block b:blockList.values()){
            batch.render(b.getInstance(),environment);
        }
    }
    public HashMap<Vector2,Block> getBlockList() {
        return blockList;
    }

    public void setBlockList(HashMap<Vector2,Block> blockList) {
        this.blockList = blockList;
    }

    public boolean isTransforming() {
        return transforming;
    }

    public int getThePreviousKey() {
        return thePreviousKey;
    }

    public void setThePreviousKey(int thePreviousKey) {
        this.thePreviousKey = thePreviousKey;
    }
}
