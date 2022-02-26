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
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Ladder;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ParkourManager {
    private final ArrayList<net.minecraft.world.level.block.Block> blocks = new ArrayList<>();

    private final Random random = new Random();
    private final Player player;
    private final Slot slot;
    private final JumpGetterIDKWhatToCallThis jumpGetterIDKWhatToCallThis = new JumpGetterIDKWhatToCallThis();
    //    private Block targetBlock;
//    private Block secondBlock;
//    private Block thirdBlock;
    private Block endBlock;
    private BukkitTask process;
    private int counter = 1;

    public ParkourManager(Player player, Slot slot) {

        blocks.add(Blocks.MOSS_BLOCK);
        blocks.add(Blocks.MOSSY_STONE_BRICKS);
        blocks.add(Blocks.INFESTED_CRACKED_STONE_BRICKS);
        blocks.add(Blocks.MOSSY_COBBLESTONE);
        blocks.add(Blocks.FLOWERING_AZALEA_LEAVES);

        this.player = player;
        this.slot = slot;

        SlotManager.resetPlayer(player);

        InfiniteParkour.getPlayerParkourManager().put(player, this);
    }

    public Slot getSlot() {
        return slot;
    }

    public JumpGetterIDKWhatToCallThis getJumpGetterIDKWhatToCallThis() {
        return jumpGetterIDKWhatToCallThis;
    }

    public BukkitTask getProcess() {
        return process;
    }

    private void playBlockGenAnimation(Block block) {
        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                new ParticleBuilder(ParticleEffect.CLOUD, block.getLocation().add(0.5, 0.5, 0.5).add((float) (random.nextDouble(2.0) - 1) / 1.25, (float) (random.nextDouble(2.0) - 1) / 1.25, (float) (random.nextDouble(2.0) - 1) / 1.25))
                        .setSpeed(0.1f)
                        .display(player);
                counter++;
                if (counter == 10) {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(InfiniteParkour.getPlugin(), 0, 1);
    }


    // PARKOURING STUFF

    public void startParkourProcess() {

        if (!player.isOnline()) return;

        InfiniteParkour.getPlayerJumpCounter().remove(player);
        InfiniteParkour.getPlayerJumpCounter().put(player, 0);

//        targetBlock = blockJump(new Location(player.getWorld(), 0.5, InfiniteParkour.PARKOUR_HEIGHT, slot.getMiddleZ()[1]));
//        new SyncBlockChanger(targetBlock.getLocation(), blocks.get((int) (Math.random() * blocks.size())), false).run();
//        secondBlock = blockJump(targetBlock.getLocation());
//        new SyncBlockChanger(secondBlock.getLocation(), blocks.get((int) (Math.random() * blocks.size())), false).run();
//        thirdBlock = blockJump(secondBlock.getLocation());
//        new SyncBlockChanger(thirdBlock.getLocation(), blocks.get((int) (Math.random() * blocks.size())), false).run();
//        endBlock = blockJump(thirdBlock.getLocation());
//        new SyncBlockChanger(endBlock.getLocation(), blocks.get((int) (Math.random() * blocks.size())), false).run();

        endBlock = blockJump(new Location(player.getWorld(), 0.5, InfiniteParkour.PARKOUR_HEIGHT, slot.getMiddleZ()[1]));
        new SyncBlockChanger(endBlock.getLocation(), blocks.get((int) (Math.random() * blocks.size())), false).run();
        jumpGetterIDKWhatToCallThis.setCooldown(20);
        for (int i = 1; i <= 3; i++) {
            endBlock = blockJump(endBlock.getLocation());
            new SyncBlockChanger(endBlock.getLocation(), blocks.get((int) (Math.random() * blocks.size())), false).run();
        }

        endBlock.setMetadata("no_decay", new FixedMetadataValue(InfiniteParkour.getPlugin(), true));


        process = new BukkitRunnable() {
            @Override
            public void run() {
                for (MetadataValue value : player.getLocation().add(0, -1, 0).getBlock().getMetadata("counter")) {
                    if (value.asInt() > InfiniteParkour.getPlayerJumpCounter().get(player)) {
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.1f, (float) Math.random());
                        for (int i = 0; i <= (value.asInt() - InfiniteParkour.getPlayerJumpCounter().get(player)); i++) {
                            InfiniteParkour.getPlayerJumpCounter().put(player, InfiniteParkour.getPlayerJumpCounter().get(player) + 1);
                            JumpCounterSystem.update(player);

                            jumpGetterIDKWhatToCallThis.decrementCooldown();

                            getNextJump();
                        }
                    }
                }

                if (player.getVelocity().getY() < -2) {
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

    private void getNextJump() {
        JumpType jumpType = jumpGetterIDKWhatToCallThis.getNextJumpType();
        switch (jumpType) {
            case BLOCK:
                endBlock = getNextBlockJump();
                break;
            case NEO:
                endBlock = neoJump(endBlock);
                break;
            case LADDER:
                endBlock = ladderJump(endBlock);
        }
    }

    private Block getNextBlockJump() {
        Collections.shuffle(blocks);
        Block block = blockJump(endBlock.getLocation());
        new SyncBlockChanger(block.getLocation(), blocks.get(0), false).run();
        return block;
    }

    public void stopParkourProcess(boolean onDisable) {
        slot.getLog().resetBlocks(onDisable);
        process.cancel();
        endBlock = null;
        counter = 1;
        jumpGetterIDKWhatToCallThis.reset();
    }


    // BLOCK JUMP METHODS

    private Block blockJump(Location loc) {
        int height = generateHeight();
        int forwardLength = generateForwardLength();
        int offset = generateSidewaysOffset();

        Block block = loc.add(forwardLength, height, offset).getBlock();

        if (block.getLocation().getBlockZ() >= slot.getMaxZ()) {
            int displaceOffset = (int) (block.getLocation().getBlockZ() - slot.getMaxZ());
            if (displaceOffset == 0) {
                block = block.getLocation().add(0, 0, -2).getBlock();
            } else {
                block = block.getLocation().add(0, 0, -2 * displaceOffset).getBlock();
            }
        } else if (block.getLocation().getBlockZ() <= slot.getMinZ()) {
            int displaceOffset = (int) (slot.getMinZ() - block.getLocation().getBlockZ());
            if (displaceOffset == 0) {
                block = block.getLocation().add(0, 0, 2).getBlock();
            } else {
                block = block.getLocation().add(0, 0, 2 * displaceOffset).getBlock();
            }
        }

        slot.getLog().addBlock(block);

        block.setMetadata("counter", new FixedMetadataValue(InfiniteParkour.getPlugin(), counter));
        counter++;

        playBlockGenAnimation(block);
        return block;
    }

    private int generateSidewaysOffset() {
        int offset = random.nextInt(7) - 3;
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


    // LADDER JUMP METHODS

    public Block ladderJump(@NotNull Block startBlock) {

        slot.getLog().addBlock(new SyncBlockChanger(startBlock.getLocation().add(3, 1, 0), Blocks.MOSS_BLOCK, false).run());
        slot.getLog().addBlock(new SyncBlockChanger(startBlock.getLocation().add(3, 2, 0), Blocks.MOSS_BLOCK, false).run());
        slot.getLog().addBlock(new SyncBlockChanger(startBlock.getLocation().add(3, 3, 0), Blocks.MOSS_BLOCK, false).run());
        slot.getLog().addBlock(new SyncBlockChanger(startBlock.getLocation().add(3, 4, 0), Blocks.MOSS_BLOCK, false).run());

        Bukkit.getScheduler().runTask(InfiniteParkour.getPlugin(), () -> {
            Block ladder1Block = startBlock.getRelative(2, 1, 0);
            ladder1Block.setType(Material.LADDER);
            Ladder ladder1 = (Ladder) ladder1Block.getBlockData();
            ladder1.setFacing(BlockFace.WEST);
            ladder1Block.setBlockData(ladder1);
            slot.getLog().addBlock(ladder1Block);

            Block ladder2Block = startBlock.getRelative(3, 2, 1);
            ladder2Block.setType(Material.LADDER);
            Ladder ladder2 = (Ladder) ladder2Block.getBlockData();
            ladder2.setFacing(BlockFace.SOUTH);
            ladder2Block.setBlockData(ladder2);
            slot.getLog().addBlock(ladder2Block);

            Block ladder3block = startBlock.getRelative(4, 3, 0);
            ladder3block.setType(Material.LADDER);
            Ladder ladder3 = (Ladder) ladder3block.getBlockData();
            ladder3.setFacing(BlockFace.EAST);
            ladder3block.setBlockData(ladder3);
            slot.getLog().addBlock(ladder3block);
        });

        Block end = startBlock.getRelative(3, 4, 0);

        end.setMetadata("counter", new FixedMetadataValue(InfiniteParkour.getPlugin(), counter));
        counter++;

        return end;
    }


    // NEO JUMP METHODS

    public Block neoJump(@NotNull Block startBlock) {

        slot.getLog().addBlock(new SyncBlockChanger(startBlock.getLocation().add(1, 0, 0), Blocks.MOSS_BLOCK, false).run());
        slot.getLog().addBlock(new SyncBlockChanger(startBlock.getLocation().add(2, 0, 0), Blocks.MOSS_BLOCK, false).run());
        slot.getLog().addBlock(new SyncBlockChanger(startBlock.getLocation().add(3, 0, 0), Blocks.MOSS_BLOCK, false).run());
        slot.getLog().addBlock(new SyncBlockChanger(startBlock.getLocation().add(4, 0, 0), Blocks.MOSS_BLOCK, false).run());
        slot.getLog().addBlock(new SyncBlockChanger(startBlock.getLocation().add(5, 0, 0), Blocks.MOSS_BLOCK, false).run());
        slot.getLog().addBlock(new SyncBlockChanger(startBlock.getLocation().add(6, 0, 0), Blocks.MOSS_BLOCK, false).run());
        slot.getLog().addBlock(new SyncBlockChanger(startBlock.getLocation().add(7, 0, 0), Blocks.MOSS_BLOCK, false).run());

        slot.getLog().addBlock(new SyncBlockChanger(startBlock.getLocation().add(4, 1, 0), Blocks.MOSS_BLOCK, false).run());
        slot.getLog().addBlock(new SyncBlockChanger(startBlock.getLocation().add(4, 2, 0), Blocks.MOSSY_STONE_BRICKS, false).run());
        slot.getLog().addBlock(new SyncBlockChanger(startBlock.getLocation().add(4, 3, 0), Blocks.MOSSY_COBBLESTONE, false).run());

        Block end = startBlock.getRelative(7, 0, 0);
        end.setMetadata("counter", new FixedMetadataValue(InfiniteParkour.getPlugin(), counter));
        startBlock.getRelative(6, 0, 0).setMetadata("counter", new FixedMetadataValue(InfiniteParkour.getPlugin(), counter));
        startBlock.getRelative(5, 0, 0).setMetadata("counter", new FixedMetadataValue(InfiniteParkour.getPlugin(), counter));
        counter++;

        return end;
    }

}
