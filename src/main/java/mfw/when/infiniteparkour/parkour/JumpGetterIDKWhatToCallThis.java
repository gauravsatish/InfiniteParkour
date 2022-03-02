package mfw.when.infiniteparkour.parkour;

import java.security.SecureRandom;

public class JumpGetterIDKWhatToCallThis {

    private static final SecureRandom secureRandom = new SecureRandom();

    private int cooldown = 0;

    public JumpType getNextJumpType() {
        int index = secureRandom.nextInt(3);
        if (index == 1 || index == 2) {
            if (cooldown > 0) return JumpType.values()[0];

            if (secureRandom.nextInt(100) <= 60) return JumpType.values()[0];

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
