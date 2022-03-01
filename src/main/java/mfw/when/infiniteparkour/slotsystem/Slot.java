package mfw.when.infiniteparkour.slotsystem;

import mfw.when.infiniteparkour.parkour_rewrite.ParkourManager_REWRITE;

public class Slot {

    private final int slotNumber;
    private final double slotStartZ;
    private final double slotEndZ;
    private final double[] midBlockZ = new double[2];
    private final SlotLog log = new SlotLog();

    public Slot(int slotNumber) {
        this.slotNumber = slotNumber;
        this.slotStartZ = slotNumber * 32;
        this.slotEndZ = slotStartZ + 15;
        midBlockZ[0] = slotStartZ + 7;
        midBlockZ[1] = slotStartZ + 8;
    }

    public double[] getMiddleZ() {
        double[] midZs = new double[2];
        midZs[0] = slotStartZ + 7;
        midZs[1] = slotStartZ + 8;
        return midZs;
    }

    public SlotLog getLog() {
        return log;
    }

    public void attachParkourMGR(ParkourManager_REWRITE parkourManager) {
        SlotManager.getParkourMGRs().put(this, parkourManager);
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public double getMinZ() {
        return slotStartZ;
    }

    public double getMaxZ() {
        return slotEndZ;
    }

}
