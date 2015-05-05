package com.loltimeline.m1miage.app.data;

/**
 * Created by Thomas on 26/03/2015.
 */
public class Match {
    private long matchId;
    private String winner;
    private int map_id;
    private String queue_type;
    private long match_creation;
    private long match_duration;

    // Dans Participant
    private int champion_id;
    private int spelle1_id;
    private int spell2_id;

    // Dans ParticipanStats
    private long KILLS;
    private long DEATHS;
    private long ASSISTS;

    private long summoner_id;
    private String summoner_name;

    public Match() {
    }

    public Match(long matchId, String winner, int map_id, String queue_type, long match_creation, long match_duration, int champion_id, int spelle1_id, int spell2_id, long KILLS, long DEATHS, long ASSISTS, long summoner_id, String summoner_name) {
        this.matchId = matchId;
        this.winner = winner;
        this.map_id = map_id;
        this.queue_type = queue_type;
        this.match_creation = match_creation;
        this.match_duration = match_duration;
        this.champion_id = champion_id;
        this.spelle1_id = spelle1_id;
        this.spell2_id = spell2_id;
        this.KILLS = KILLS;
        this.DEATHS = DEATHS;
        this.ASSISTS = ASSISTS;
        this.summoner_id = summoner_id;
        this.summoner_name = summoner_name;
    }

    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getMap_id() {
        return map_id;
    }

    public void setMap_id(int map_id) {
        this.map_id = map_id;
    }

    public String getQueue_type() {
        return queue_type;
    }

    public void setQueue_type(String queue_type) {
        this.queue_type = queue_type;
    }

    public long getMatch_creation() {
        return match_creation;
    }

    public void setMatch_creation(long match_creation) {
        this.match_creation = match_creation;
    }

    public long getMatch_duration() {
        return match_duration;
    }

    public void setMatch_duration(long match_duration) {
        this.match_duration = match_duration;
    }

    public int getChampion_id() {
        return champion_id;
    }

    public void setChampion_id(int champion_id) {
        this.champion_id = champion_id;
    }

    public int getSpelle1_id() {
        return spelle1_id;
    }

    public void setSpelle1_id(int spelle1_id) {
        this.spelle1_id = spelle1_id;
    }

    public int getSpell2_id() {
        return spell2_id;
    }

    public void setSpell2_id(int spell2_id) {
        this.spell2_id = spell2_id;
    }

    public long getKILLS() {
        return KILLS;
    }

    public void setKILLS(long KILLS) {
        this.KILLS = KILLS;
    }

    public long getDEATHS() {
        return DEATHS;
    }

    public void setDEATHS(long DEATHS) {
        this.DEATHS = DEATHS;
    }

    public long getASSISTS() {
        return ASSISTS;
    }

    public void setASSISTS(long ASSISTS) {
        this.ASSISTS = ASSISTS;
    }

    public long getSummoner_id() {
        return summoner_id;
    }

    public void setSummoner_id(long summoner_id) {
        this.summoner_id = summoner_id;
    }

    public String getSummoner_name() {
        return summoner_name;
    }

    public void setSummoner_name(String summoner_name) {
        this.summoner_name = summoner_name;
    }
}