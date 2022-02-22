package mfw.when.infiniteparkour.parkour;

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
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.awt.*;
import java.util.Random;

public class ParkourManager {

    private final net.minecraft.world.level.block.Block NMS_TARGET_BLOCK = Blocks.INFESTED_CRACKED_STONE_BRICKS;
    private final net.minecraft.world.level.block.Block NMS_SECOND_BLOCK = Blocks.MOSSY_COBBLESTONE;
    private final net.minecraft.world.level.block.Block NMS_THIRD_BLOCK = Blocks.FLOWERING_AZALEA_LEAVES;
    private final net.minecraft.world.level.block.Block NMS_PASSED_BLOCK = Blocks.MOSS_BLOCK;
    private final Material TARGET_BLOCK_MATERIAL = Material.INFESTED_CRACKED_STONE_BRICKS;
    private final Material SECOND_BLOCK_MATERIAL = Material.MOSSY_COBBLESTONE;
    private final Material PASSED_BLOCK_MATERIAL = Material.MOSS_BLOCK;
    private final Material THIRD_BLOCK_MATERIAL = Material.FLOWERING_AZALEA_LEAVES;

    private final Random random = new Random();
    private final Player player;
    private final Slot slot;
    private Block targetBlock;
    private Block secondBlock;
    private Block thirdBlock;
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

        targetBlock = getNextBlock(new Location(player.getWorld(), 0.5, InfiniteParkour.PARKOUR_HEIGHT, slot.getMiddleZ()[1]));
        new SyncBlockChanger(targetBlock.getLocation(), NMS_TARGET_BLOCK, false).run();
        secondBlock = getNextBlock(targetBlock.getLocation());
        new SyncBlockChanger(secondBlock.getLocation(), NMS_SECOND_BLOCK, false).run();
        thirdBlock = getNextBlock(secondBlock.getLocation());
        new SyncBlockChanger(thirdBlock.getLocation(), NMS_THIRD_BLOCK, false).run();


        process = new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getLocation().add(0, -1, 0).getBlock().getType().equals(TARGET_BLOCK_MATERIAL)) {

                    InfiniteParkour.getPlayerJumpCounter().put(player, InfiniteParkour.getPlayerJumpCounter().get(player) + 1);
                    JumpCounterSystem.update(player);

                    new SyncBlockChanger(thirdBlock.getLocation(), NMS_SECOND_BLOCK, false).run();
                    new SyncBlockChanger(targetBlock.getLocation(), NMS_PASSED_BLOCK, false).run();
                    new SyncBlockChanger(secondBlock.getLocation(), NMS_TARGET_BLOCK, false).run();
                    targetBlock = secondBlock;
                    secondBlock = thirdBlock;
                    thirdBlock = getNextBlock(thirdBlock.getLocation());
                    new SyncBlockChanger(thirdBlock.getLocation(), NMS_THIRD_BLOCK, false).run();
                    playBlockGenAnimation(thirdBlock);
                }

                if (player.getVelocity().getY() < -2) {
                    InfiniteParkour.getPlugin().getLogger().info("called reset");
                    new SyncPlayerTeleport(player, new Location(player.getWorld(), 0.5, InfiniteParkour.PARKOUR_HEIGHT + 1, slot.getMiddleZ()[1], -90f, 0f)).run();
                    resetPlayer();
                }
            }
        }.runTaskTimerAsynchronously(InfiniteParkour.getPlugin(), 0, 2);

        InfiniteParkour.getVelocityTrackerProcesses().put(player, process);

        JumpCounterSystem.addPlayer(player);

    }

    private void resetPlayer() {
        Bukkit.getScheduler().runTask(InfiniteParkour.getPlugin(), () -> {
            stopParkourProcess(false);
            Bukkit.getScheduler().runTaskLater(InfiniteParkour.getPlugin(), () -> startParkourProcess(), 20);
        });
    }

    public void stopParkourProcess(boolean onDisable) {
        slot.getLog().resetBlocks(onDisable);
        process.cancel();
        targetBlock = null;
        secondBlock = null;
    }

    private void playBlockGenAnimation(Block block) {
        new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                new ParticleBuilder(ParticleEffect.FALLING_SPORE_BLOSSOM, block.getLocation().add(0.5, 0.5, 0.5).add((float) (random.nextDouble(2.0) - 1) / 1.25, (float) (random.nextDouble(2.0) - 1) / 1.25, (float) (random.nextDouble(2.0) - 1) / 1.25))
                        .setSpeed(0.1f)
                        .setColor(new Color(135, 206, 250))
                        .display(player);
                counter++;
                if (counter == 10) {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(InfiniteParkour.getPlugin(), 0, 1);
    }

    private Block getNextBlock(Location loc) {
        int height = generateHeight();
        int forwardLength = generateForwardLength();
        int offset = generateSidewaysOffset(height);

        Block block = loc.add(forwardLength, height, offset).getBlock();

        if (block.getLocation().getBlockZ() >= slot.getMaxZ()) {
            int displaceOffset = (int) (block.getLocation().getBlockZ() - slot.getMaxZ());
            if (displaceOffset == 0) {
                block = block.getLocation().add(0, 0, -1).getBlock();
            } else {
                block = block.getLocation().add(0, 0, -2 * displaceOffset).getBlock();
            }
            InfiniteParkour.getPlugin().getLogger().info("out of bounds block gen");
        } else if (block.getLocation().getBlockZ() <= slot.getMinZ()) {
            int displaceOffset = (int) (slot.getMinZ() - block.getLocation().getBlockZ());
            if (displaceOffset == 0) {
                block = block.getLocation().add(0, 0, 1).getBlock();
            } else {
                block = block.getLocation().add(0, 0, 2 * displaceOffset).getBlock();
            }
            InfiniteParkour.getPlugin().getLogger().info("out of bounds block gen");
        }

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
