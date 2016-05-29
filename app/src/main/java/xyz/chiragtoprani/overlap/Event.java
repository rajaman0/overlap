package xyz.chiragtoprani.overlap;

/**
 * Created by chirag on 5/28/16.
 */
public class Event {

    //start time in milliseconds
    public long start;
    public long end;
    public String title;
    public String description;
    public int duration = -1;
    public String location;

    public String toString(){
        return "Title: " + title + ", startTime: " + start;
    }
}
