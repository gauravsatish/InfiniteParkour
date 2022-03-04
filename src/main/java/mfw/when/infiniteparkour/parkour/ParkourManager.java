package mfw.when.infiniteparkour.parkour;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.parkour.jumps.BlockJump;
import mfw.when.infiniteparkour.parkour.jumps.LadderJump;
import mfw.when.infiniteparkour.parkour.jumps.NeoJump;
import mfw.when.infiniteparkour.slotsystem.Slot;
import mfw.when.infiniteparkour.slotsystem.SlotManager;
import mfw.when.infiniteparkour.utils.SyncBlockChanger;
import mfw.when.infiniteparkour.utils.SyncPlayerTeleport;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.security.SecureRandom;
import java.util.List;

public class ParkourManager {

    private static final String COUNTER_METADATA_VALUE = "counter";
    private static final List<Block> blocks = List.of(Blocks.MOSS_BLOCK, Blocks.MOSSY_COBBLESTONE, Blocks.CRACKED_STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.FLOWERING_AZALEA_LEAVES);
    private final Location SLOT_START_LOC;

    private final Player player;
    private final SecureRandom random = new SecureRandom();
    private final Slot slot;
    private final JumpDecider jumpDecider = new JumpDecider();
    private int counter = 1;
    private org.bukkit.block.Block block;
    private BukkitTask process;

    public ParkourManager(Player player, Slot slot) {
        this.player = player;
        this.slot = slot;

        SlotManager.resetPlayer(player);

        this.SLOT_START_LOC = new Location(player.getWorld(), 0.5, InfiniteParkour.PARKOUR_HEIGHT, slot.getMiddleZ()[1], -90f, 0f);
        this.block = SLOT_START_LOC.getBlock();

        InfiniteParkour.getPlayerParkourManager().put(player, this);
        JumpCounterSystem.addPlayer(player);
    }

    public static Block getRandomBlockType() {
        return blocks.get((int) (Math.random() * blocks.size()));
    }

    public void start() {
        if (!player.isOnline()) return;

        InfiniteParkour.getPlayerJumpCounter().remove(player);
        InfiniteParkour.getPlayerJumpCounter().put(player, 0);

        this.block = SLOT_START_LOC.getBlock();
        jumpDecider.setCooldown(20);
        for (int i = 0; i < 3; i++) doBlockJump();

        process = new BukkitRunnable() {
            @Override
            public void run() {
                for (MetadataValue value : player.getLocation().add(0, -1, 0).getBlock().getMetadata(COUNTER_METADATA_VALUE)) {
                    if (value.asInt() > InfiniteParkour.getPlayerJumpCounter().get(player)) {
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.1f, (float) Math.random());
                        for (int i = 0; i <= (value.asInt() - InfiniteParkour.getPlayerJumpCounter().get(player)); i++) {
                            trigger();
                        }
                    }
                }

                if (player.getVelocity().getY() < -2) {
                    new SyncPlayerTeleport(player, SLOT_START_LOC.clone().add(0, 1, 0)).run();
                    stop(true, false);
                }
            }
        }.runTaskTimerAsynchronously(InfiniteParkour.getPlugin(), 0, 2);
    }

    public void trigger() {
        InfiniteParkour.getPlayerJumpCounter().put(player, InfiniteParkour.getPlayerJumpCounter().get(player) + 1);
        JumpCounterSystem.update(player);
        jumpDecider.decrementCooldown();

        switch (jumpDecider.getNextJumpType()) {
            case BLOCK -> doBlockJump();
            case NEO -> doNeoJump();
            case LADDER -> doLadderJump();
        }
    }

    public void stop(boolean restart, boolean onDisable) {
        this.slot.getLog().resetBlocks(onDisable);
        this.process.cancel();
        this.block = SLOT_START_LOC.getBlock();
        this.counter = 1;
        this.jumpDecider.reset();
        if (restart) {
            Bukkit.getScheduler().runTaskLater(InfiniteParkour.getPlugin(), () -> start(), 20);
        }
    }

    private void doBlockJump() {
        block = checkWithinBounds(BlockJump.jump(block.getLocation())).getBlock();
        new SyncBlockChanger(block.getLocation(), getRandomBlockType(), true).run();

        block.setMetadata(COUNTER_METADATA_VALUE, new FixedMetadataValue(InfiniteParkour.getPlugin(), counter));
        counter++;
        slot.getLog().addBlock(block);

        playBlockGenAnimation(block);
    }

    private void doLadderJump() {
        for (Location loc : LadderJump.jump(block.getLocation())) {
            this.slot.getLog().addBlock(loc.getBlock());
            if (loc.getBlock().getType().equals(Material.LADDER)) {
                InfiniteParkour.getPlugin().getLogger().info("LADDER found in output array");
            }
            this.playBlockGenAnimation(loc.getBlock());
        }

        org.bukkit.block.Block endBlock = LadderJump.getEndBlock(block.getLocation()).getBlock();
        endBlock.setMetadata(COUNTER_METADATA_VALUE, new FixedMetadataValue(InfiniteParkour.getPlugin(), counter));
        counter++;

        this.block = endBlock;

    }

    private void doNeoJump() {
        for (Location loc : NeoJump.jump(block.getLocation())) {
            this.slot.getLog().addBlock(loc.getBlock());
            this.playBlockGenAnimation(loc.getBlock());
        }
        org.bukkit.block.Block endBlock = NeoJump.getEndBlock(block.getLocation()).getBlock();
        for (int i = 0; i >= -2; i--) {
            endBlock.getRelative(i, 0, 0).setMetadata(COUNTER_METADATA_VALUE, new FixedMetadataValue(InfiniteParkour.getPlugin(), counter));
        }
        counter++;
        this.block = endBlock;
    }

    private void playBlockGenAnimation(org.bukkit.block.Block theBlock) {
        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {
                new ParticleBuilder(ParticleEffect.CLOUD, theBlock.getLocation().add(0.5, 0.5, 0.5).add((float) (random.nextDouble(2.0) - 1) / 1.25, (float) (random.nextDouble(2.0) - 1) / 1.25, (float) (random.nextDouble(2.0) - 1) / 1.25)).setSpeed(0.1f).display(player);
                counter++;
                if (counter == 10) {
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(InfiniteParkour.getPlugin(), 0, 1);
    }

    private Location checkWithinBounds(Location loc) {
        if (loc.getBlockZ() >= slot.getMaxZ()) {
            int displaceOffset = (int) (loc.getBlockZ() - slot.getMaxZ());
            if (displaceOffset == 0) {
                loc.add(0, 0, -2);
            } else {
                loc.add(0, 0, -2 * displaceOffset);
            }
        } else if (loc.getBlockZ() <= slot.getMinZ()) {
            int displaceOffset = (int) (slot.getMinZ() - loc.getBlockZ());
            if (displaceOffset == 0) {
                loc.add(0, 0, 2);
            } else {
                loc.add(0, 0, 2 * displaceOffset);
            }
        }

        return loc;
    }

    public Slot getSlot() {
        return slot;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public int getCounter() {
        return counter;
    }

    public org.bukkit.block.Block getBlock() {
        return block;
    }

    public BukkitTask getProcess() {
        return process;
    }
}
