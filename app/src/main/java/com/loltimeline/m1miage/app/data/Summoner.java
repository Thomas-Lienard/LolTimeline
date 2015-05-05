package com.loltimeline.m1miage.app.data;

/**
 * Created by Thomas on 01/04/2015.
 */
public class Summoner {
    private long summonerId;
    private String name;
    private long lvl;
    private String icon;


    public Summoner() {
    }


    public Summoner(long summonerId, String name, long lvl, String icon) {
        super();
        this.summonerId = summonerId;
        this.name = name;
        this.lvl = lvl;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getLvl() {
        return lvl;
    }

    public void setLvl(long lvl) {
        this.lvl = lvl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }
}