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
    private ArrayList<Rule> rules;
    private HashMap<Vector2,Block> map;
    protected boolean gameOver;
    public boolean isGameOver(){
        return gameOver;
    }
    protected boolean complete;
    private int[][] matrix8;
    public MapBlock() {
        ArrayList<String> lines = new ArrayList<String>();
        rules = new ArrayList<Rule>();
        matrix8=new int[MAX_WIDTH][MAX_HEIGHT];
        FileHandle handle = Gdx.files.local("Rules");
        String text = handle.readString();
        String wordsArray[] = text.split("\\r?\\n");
        for (String word : wordsArray) {
            lines.add(word);
        }
        for (int i = 0; i < Integer.parseInt(lines.get(0)); i++) {
            rules.add(new Rule(lines.get(i)));
        }

        map=new HashMap<Vector2, Block>();
    }
    public ArrayList<Vector2> searchByColumn(int column,int tokens){
        ArrayList<Vector2> result=new ArrayList<Vector2>();
        for(int i=0;i<MAX_HEIGHT;i++){
            if(matrix8[column][i]==tokens){
                result.add(new Vector2(column,i));
            }
        }
        return result;
    }
    public ArrayList<Vector2> searchByRow(int row,int tokens){
        ArrayList<Vector2> result=new ArrayList<Vector2>();
        for(int i=0;i<MAX_WIDTH;i++){
            if(matrix8[i][row]==tokens){
                result.add(new Vector2(i,row));
            }
        }
        return result;
    }
    public ArrayList<Vector2> searchFull(int tokens){
        ArrayList<Vector2> result=new ArrayList<Vector2>();
        for(int i=0;i<MAX_WIDTH;i++)
            for(int j=0;j<MAX_HEIGHT;j++)
                if(matrix8[i][j]==tokens)
                    result.add(new Vector2(i,j));
        return result;
    }


}
