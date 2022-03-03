package mfw.when.infiniteparkour.parkour.jumps;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;

/**
 * Calculates the new block to be generated based on given conditions.
 */
public class BlockJump {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Gets the new block location based on given conditions.
     * @param loc the current block location from which the new block location is given.
     * @return The location of the new block generated
     */
    public static Location jump(@NotNull Location loc) {
        int height = getHeight();
        int forwardLength = getForwardLength();
        int sideOffset = getSideOffset();

        return loc.add(forwardLength, height, sideOffset);
    }

    /**
     * Gets the offset of the new block location (on the Z axis).
     * @return offset - the integer offset on the Z axis.
     */
    private static int getSideOffset() {
        return random.nextInt(7) - 3;
    }

    /**
     * Gets the length of the new block location (on the X axis).
     * @return length - the integer offset on the X axis.
     */
    private static int getForwardLength() {
        int fdLength = 2 + random.nextInt(2);

        if (fdLength == 2) {
            int increaseChance = random.nextInt(100);
            if (increaseChance >= 30) {
                fdLength = 3;
            }
        }
        return fdLength;
    }

    /**
     * Gets the height of the new block location (on the Y axis).
     * @return height - the integer offset on the Y axis.
     */
    private static int getHeight() {
        return random.nextInt(3) - 1;
    }

}
