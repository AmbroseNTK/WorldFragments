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
import com.badlogic.gdx.utils.UBJsonReader;

import java.util.ArrayList;
import java.util.HashMap;

public class MapBlock {
    public final int MAX_WIDTH=8;
    public final int MAX_HEIGHT=8;
    protected int[][] map;
    protected HashMap<String, Block> blockDictonary;
    protected ArrayList<Rule> rules;
    private ArrayList<Block> blockList;
    final UBJsonReader jsonReader = new UBJsonReader();
    final G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
    protected boolean gameOver;
    public boolean isGameOver(){
        return gameOver;
    }

    public MapBlock(){

        map=new int[MAX_WIDTH][MAX_HEIGHT];
        gameOver=false;
        blockDictonary=new HashMap<String, Block>();
        rules=new ArrayList<Rule>();
        setBlockList(new ArrayList<Block>());

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
    public void initNewBlock(){
        ArrayList<Vector2> empty=checkMap(0);
        cleanBlockList();
        if(empty.size()!=1) {
            String[] blockNames = {"water", "lava"};
            for (int i = 0; i < blockNames.length; i++) {
                Vector2 emptyPos=empty.get(MathUtils.random(0,empty.size()-1));
                map[(int)(emptyPos.x)][(int)(emptyPos.y)]=1;
                Block newBlock=blockDictonary.get(blockNames[i]).clone();
                newBlock.setPosition(emptyPos.x*2,0,emptyPos.y*-2);
                newBlock.setUsing(true);
                getBlockList().add(newBlock);
                empty.remove(emptyPos);
            }
        }
        else
            gameOver=true;
    }

    public void cleanBlockList(){
        for(Block b: getBlockList())
            if(!b.isUsing())
                getBlockList().remove(b);
    }

    public void update(float dT){

    }
    public void render(ModelBatch batch, Environment environment){
        for(Block b:blockList){
            batch.render(b.getInstance(),environment);
        }
    }
    public ArrayList<Block> getBlockList() {
        return blockList;
    }

    public void setBlockList(ArrayList<Block> blockList) {
        this.blockList = blockList;
    }
}
