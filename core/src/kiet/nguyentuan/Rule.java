package kiet.nguyentuan;

/**
 * Created by kiettuannguyen on 21/05/2017.
 */

public class Rule {
    private String element1;
    private String element2;
    private String result;
    public Rule(String data){
        String[] tokens=data.split(";");
        element1=tokens[0];
        element2=tokens[1];
        result=tokens[2];
    }
    public String checkRule(String ele1,String ele2){
        if((ele1.equals(element1)&&ele2.equals(element2))||(ele2.equals(element1)&&ele1.equals(element2))){
            return result;
        }
        else
            return "NA";
    }
}
