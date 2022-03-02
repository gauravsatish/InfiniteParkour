package mfw.when.infiniteparkour.parkour_rewrite.jumps;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.security.SecureRandom;

public class BlockJump {

    private static final SecureRandom random = new SecureRandom();

    public static Location jump(Location loc) {
        int height = getHeight();
        int forwardLength = getForwardLength();
        int sideOffset = getSideOffset();

        return loc.add(forwardLength, height, sideOffset);
    }

    private static int getSideOffset() {
        int offset = random.nextInt(7) - 3;
        return offset;
    }

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

    private static int getHeight() {
        int height = random.nextInt(3) - 1;

//        if (height == -1) {
//            int decreaseChance = random.nextInt(100);
//            if (decreaseChance >= 40) {
//                height = 0;
//            }
//        }
        return height;
    }

}
