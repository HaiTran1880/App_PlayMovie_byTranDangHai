package com.example.movie.define;

import com.example.movie.contact.ItemCategory;
import com.example.movie.contact.Video;

import java.util.ArrayList;

public class Define_Methods {
    public  boolean CHECK (String title, ArrayList<ItemCategory> arrayList){
        for (ItemCategory item:arrayList){
            if (title.equals(item.getName())==true){
                return true;
            }
        }
        return false;
    }
}
