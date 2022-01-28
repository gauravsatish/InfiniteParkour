package mfw.when.infiniteparkour.slotsystem;

public class Slot {

    private final int slotNumber;
    private final double slotStartZ;
    private final double slotEndZ;

    public Slot(int slotNumber) {
        this.slotNumber = slotNumber;
        this.slotStartZ = slotNumber * 16;
        this.slotEndZ = ((slotNumber + 1) * 16) - 1;
    }

    public double[] getMiddleZ() {
        double[] midZs = new double[2];
        midZs[0] = slotStartZ + 7;
        midZs[1] = slotStartZ + 8;
        return midZs;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public double getSlotStartZ() {
        return slotStartZ;
    }

    public double getSlotEndZ() {
        return slotEndZ;
    }

}
