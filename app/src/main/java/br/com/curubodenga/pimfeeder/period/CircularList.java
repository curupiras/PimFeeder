package br.com.curubodenga.pimfeeder.period;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by curup on 20/10/2017.
 */
public class CircularList {
    private List<Integer> circularList;
    private int indice;

    CircularList(){
        circularList = new ArrayList<Integer>();
        indice = 0;
    }

    public void add(Integer element){
        circularList.add(element);
    }

    public Integer getNext(){


        if(indice == circularList.size()-1){
            indice=0;
        }else{
            indice++;
        }

        return circularList.get(indice);
    }

    public Integer getPrev(){

        if(indice == 0){
            indice=circularList.size()-1;
        }else{
            indice--;
        }

        return circularList.get(indice);
    }

    public Integer get(int i){
        return circularList.get(i);
    }

}
