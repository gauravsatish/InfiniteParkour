package mfw.when.infiniteparkour.parkour;

import mfw.when.infiniteparkour.parkour.jumps.JumpType;

import java.util.Random;

public class JumpDecider {

    private static final Random random = new Random();

    private int cooldown = 0;

    public JumpType getNextJumpType() {
        int index = random.nextInt(3);
        if (index == 1 || index == 2) {
            if (cooldown > 0) return JumpType.values()[0];

            if (random.nextInt(100) <= 70) return JumpType.values()[0];

            cooldown = 15;
        }
        return JumpType.values()[index];
    }


    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void decrementCooldown() {
        if (cooldown > 0) {
            cooldown--;
        }
    }

    public void reset() {
        cooldown = 0;
    }
}
