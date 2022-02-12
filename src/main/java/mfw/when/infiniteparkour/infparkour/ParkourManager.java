package mfw.when.infiniteparkour.infparkour;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.slotsystem.Slot;
import mfw.when.infiniteparkour.slotsystem.SlotManager;
import mfw.when.infiniteparkour.utils.SyncBlockChanger;
import mfw.when.infiniteparkour.utils.SyncPlayerTeleport;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class ParkourManager {

    private final net.minecraft.world.level.block.Block NMS_TARGET_BLOCK = Blocks.EMERALD_BLOCK;
    private final net.minecraft.world.level.block.Block NMS_SECOND_BLOCK = Blocks.WAXED_COPPER_BLOCK;
    private final net.minecraft.world.level.block.Block NMS_PASSED_BLOCK = Blocks.GOLD_BLOCK;
    private final Material TARGET_BLOCK_MATERIAL = Material.EMERALD_BLOCK;
    private final Material SECOND_BLOCK_MATERIAL = Material.WAXED_COPPER_BLOCK;
    private final Material PASSED_BLOCK_MATERIAL = Material.GOLD_BLOCK;

    private final Random random = new Random();
    private final Player player;
    private final Slot slot;
    private Block targetBlock;
    private Block secondBlock;
    private BukkitTask process;

    public ParkourManager(Player player, Slot slot) {
        this.player = player;
        this.slot = slot;

        SlotManager.resetPlayer(player);

        InfiniteParkour.getPlayerParkourManager().put(player, this);
        InfiniteParkour.getPlugin().getLogger().info("added to hashmap");
    }

    public Slot getSlot() {
        return slot;
    }

    public BukkitTask getProcess() {
        return process;
    }

    public void startParkourProcess() {

        if (InfiniteParkour.getPlayerJumpCounter().containsKey(player)) {
            InfiniteParkour.getPlayerJumpCounter().remove(player);
        }

        InfiniteParkour.getPlayerJumpCounter().put(player, 0);

        new SyncPlayerTeleport(player, new Location(player.getWorld(), 0.5, InfiniteParkour.PARKOUR_HEIGHT + 1, slot.getMiddleZ()[1], -90f, 0f)).run();

        targetBlock = getNextBlock(new Location(player.getWorld(), 0.5, InfiniteParkour.PARKOUR_HEIGHT, slot.getMiddleZ()[1]));
        new SyncBlockChanger(targetBlock.getLocation(), NMS_TARGET_BLOCK, false).run();
        secondBlock = getNextBlock(targetBlock.getLocation());
        new SyncBlockChanger(secondBlock.getLocation(), NMS_SECOND_BLOCK, false).run();

        process = new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getLocation().add(0, -1, 0).getBlock().getType().equals(TARGET_BLOCK_MATERIAL)) {

                    InfiniteParkour.getPlayerJumpCounter().put(player, InfiniteParkour.getPlayerJumpCounter().get(player) + 1);
                    JumpCounterSystem.update(player);

                    new SyncBlockChanger(targetBlock.getLocation(), NMS_PASSED_BLOCK, false).run();
                    new SyncBlockChanger(secondBlock.getLocation(), NMS_TARGET_BLOCK, false).run();
                    targetBlock = secondBlock;
                    secondBlock = getNextBlock(secondBlock.getLocation());
                    new SyncBlockChanger(secondBlock.getLocation(), NMS_SECOND_BLOCK, false).run();
                }

                if (player.getVelocity().getY() < -2) {
                    InfiniteParkour.getPlugin().getLogger().info("called reset");
                    resetPlayer();
                }
            }
        }.runTaskTimerAsynchronously(InfiniteParkour.getPlugin(), 0, 2);
    }

    private void resetPlayer() {
        Bukkit.getScheduler().runTask(InfiniteParkour.getPlugin(), new Runnable() {
            @Override
            public void run() {
                slot.getLog().resetBlocks();
                process.cancel();
                targetBlock = null;
                secondBlock = null;
                startParkourProcess();
            }
        });
    }

    private Block getNextBlock(Location loc) {
        int height = generateHeight();
        int forwardLength = generateForwardLength();
        int offset = generateSidewaysOffset(height);

        Block block = loc.add(forwardLength, height, offset).getBlock();
        slot.getLog().addBlock(block);
        return block;

    }

    private int generateSidewaysOffset(int height) {
        int offset = random.nextInt(7) - 3;

//        if (height == 1 && (offset == 3 || offset == -3)) {
//            if (offset == 3) {
//                offset = 2;
//            } else {
//                offset = -2;
//            }
//        }
        return offset;
    }

    private int generateForwardLength() {
        int fdLength = 2 + random.nextInt(2);

        if (fdLength == 2) {
            int increaseChance = random.nextInt(100);
            if (increaseChance >= 30) {
                fdLength = 3;
            }
        }
        return fdLength;
    }

    private int generateHeight() {
        int height = random.nextInt(3) - 1;

        if (height == -1) {
            int decreaseChance = random.nextInt(100);
            if (decreaseChance >= 40) {
                height = 0;
            }
        }
        return height;
    }
}
