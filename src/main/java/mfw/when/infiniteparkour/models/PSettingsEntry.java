package mfw.when.infiniteparkour.models;

import java.util.UUID;

public class PSettingsEntry {

    private String playerUUID;
    private boolean neoJumpsEnabled;
    private boolean ladderJumpsEnabled;

    public PSettingsEntry(String playerUUID, boolean neoJumpsEnabled, boolean ladderJumpsEnabled) {
        this.playerUUID = playerUUID;
        this.neoJumpsEnabled = neoJumpsEnabled;
        this.ladderJumpsEnabled = ladderJumpsEnabled;
    }

    public String getPlayerID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID.toString();
    }

    public boolean isNeoJumpsEnabled() {
        return neoJumpsEnabled;
    }

    public void setNeoJumpsEnabled(boolean neoJumpsEnabled) {
        this.neoJumpsEnabled = neoJumpsEnabled;
    }

    public boolean isLadderJumpsEnabled() {
        return ladderJumpsEnabled;
    }

    public void setLadderJumpsEnabled(boolean ladderJumpsEnabled) {
        this.ladderJumpsEnabled = ladderJumpsEnabled;
    }
}
