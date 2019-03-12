package com.example.midtermapp;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class DataObj implements Serializable, Comparable<DataObj> {

    String track, artist, album, trackURL;
    Date date;

    @Override
    public int compareTo(DataObj o) {
        return 0;
    }
}

class CompareByMention implements Comparator<DataObj> {
    @Override
    public int compare(DataObj o1, DataObj o2) {

/*            if (o1.price>o2.price){
                return 1;
            }else if (o1.price<o2.price){
                return -1;
            }else{
                return 0;
            }*/
        return 0;
    }
}