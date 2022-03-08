package mfw.when.infiniteparkour.parkour;

import mfw.when.infiniteparkour.InfiniteParkour;
import mfw.when.infiniteparkour.parkour.jumps.BlockJump;
import mfw.when.infiniteparkour.parkour.jumps.JumpType;
import mfw.when.infiniteparkour.parkour.jumps.LadderJump;
import mfw.when.infiniteparkour.parkour.jumps.NeoJump;
import mfw.when.infiniteparkour.settings.PlayerSettings;
import mfw.when.infiniteparkour.settings.SettingsItem;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.security.SecureRandom;
import java.util.List;


/**
 * Manages the parkour for the player
 */
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
    private final PlayerSettings settings;

    public ParkourManager(@NotNull Player player, @NotNull Slot slot) {
        this.player = player;
        this.slot = slot;

        SlotManager.resetPlayer(player);

        this.SLOT_START_LOC = new Location(player.getWorld(), 0.5, InfiniteParkour.PARKOUR_HEIGHT, slot.getMiddleZ()[1], -90f, 0f);
        this.block = SLOT_START_LOC.getBlock();

        this.settings = new PlayerSettings(player);

        InfiniteParkour.getSessions().put(player, this);
        JumpCounterSystem.addPlayer(player);
    }

    /**
     * Gets a random block type
     *
     * @return A random block type
     */
    public static Block getRandomBlockType() {
        return blocks.get((int) (Math.random() * blocks.size()));
    }

    /**
     * Starts the parkour which does the following things:
     * <ul>
     *     <li>Generates the first three blocks for the player</li>
     *     <li>Starts the process which checks if the player has landed onto a new block and generates a new block</li>
     *     <li>Checks whether the player has failed the parkour and resets the player and blocks</li>
     * </ul>
     */
    public void start() {
        if (!player.isOnline()) return;

        InfiniteParkour.getPlayerScores().remove(player);
        InfiniteParkour.getPlayerScores().put(player, 0);
        JumpCounterSystem.update(player);

        player.getInventory().setItem(8, SettingsItem.getItem());

        this.block = SLOT_START_LOC.getBlock();
        jumpDecider.setCooldown(20);
        for (int i = 0; i < 3; i++) doJump(JumpType.BLOCK);

        process = new BukkitRunnable() {
            @Override
            public void run() {
                for (MetadataValue value : player.getLocation().add(0, -1, 0).getBlock().getMetadata(COUNTER_METADATA_VALUE)) {
                    if (value.asInt() > InfiniteParkour.getPlayerScores().get(player)) {
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.1f, (float) Math.random());
                        for (int i = 0; i <= (value.asInt() - InfiniteParkour.getPlayerScores().get(player)); i++) {
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

    /**
     * Called when the player lands on a new block which updates the score,
     * decrements the special jump cooldown<br>
     * and calls the function that generates the next block
     */
    public void trigger() {
        InfiniteParkour.getPlayerScores().put(player, InfiniteParkour.getPlayerScores().get(player) + 1);
        JumpCounterSystem.update(player);
        jumpDecider.decrementCooldown();
        this.doJump(jumpDecider.getNextJumpType());

    }

    /**
     * Stops the parkour
     *
     * @param restart   whether the player should be reset for a new run
     * @param onDisable whether this is called when the server is being shutdown,
     *                  which removes blocks instantly instead of removing them one by one
     */
    public void stop(@NotNull boolean restart, @NotNull boolean onDisable) {
        this.player.getInventory().setItem(8, new ItemStack(Material.AIR));
        this.slot.getLog().resetBlocks(onDisable);
        this.process.cancel();
        this.block = SLOT_START_LOC.getBlock();
        this.counter = 1;
        this.jumpDecider.reset();
        if (restart) {
            Bukkit.getScheduler().runTaskLater(InfiniteParkour.getPlugin(), () -> start(), 20);
        }
    }

    /**
     * Sets the blocks for the corresponding jump type
     *
     * @param type The type of jump
     */
    private void doJump(@NotNull JumpType type) {
        switch (type) {
            case BLOCK -> {
                block = checkWithinBounds(BlockJump.jump(block.getLocation())).getBlock();
                new SyncBlockChanger(block.getLocation(), getRandomBlockType(), true).run();
                block.setMetadata(COUNTER_METADATA_VALUE, new FixedMetadataValue(InfiniteParkour.getPlugin(), counter));
                counter++;
                slot.getLog().addBlock(block);
                playBlockGenAnimation(block);
            }
            case LADDER -> {

                if (!settings.isLadderJumpsEnabled()) {
                    InfiniteParkour.getPlugin().getLogger().info("LADDER JUMP DISABLED");
                    doJump(JumpType.BLOCK);
                    return;
                }
                for (Location loc : LadderJump.jump(block.getLocation())) {
                    this.slot.getLog().addBlock(loc.getBlock());
                    if (loc.getBlock().getType().equals(Material.LADDER)) {
                    }
                    this.playBlockGenAnimation(loc.getBlock());
                }

                org.bukkit.block.Block endBlock = LadderJump.getEndBlock(block.getLocation()).getBlock();
                endBlock.setMetadata(COUNTER_METADATA_VALUE, new FixedMetadataValue(InfiniteParkour.getPlugin(), counter));
                counter++;

                this.block = endBlock;
            }
            case NEO -> {

                if (!settings.isNeoJumpsEnabled()) {
                    InfiniteParkour.getPlugin().getLogger().info("NEO JUMP DISABLED");
                    doJump(JumpType.BLOCK);
                    return;
                }
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
        }
    }

    /**
     * Plays the block generation "animation" which basically just spawns particles around the block
     *
     * @param theBlock the block to spawn particles at
     */
    private void playBlockGenAnimation(@NotNull org.bukkit.block.Block theBlock) {
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

    /**
     * Checks whether the given location is within the bounds of the slot the player is in
     *
     * @param loc The location to check
     * @return The location if it is within the bounds, otherwise the nearest location within the bounds
     */
    private Location checkWithinBounds(@NotNull Location loc) {
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

    /**
     * Gets the slot the player is currently in
     *
     * @return The slot the player is currently in
     */
    public Slot getSlot() {
        return slot;
    }

    /**
     * Gets the player
     *
     * @return The player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the list of blocks types that a generated block can take
     *
     * @return
     */
    public List<Block> getBlocks() {
        return blocks;
    }

    /**
     * Gets the block counter
     *
     * @return The block counter
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Gets the latest block generated
     *
     * @return The latest block generated
     */
    public org.bukkit.block.Block getBlock() {
        return block;
    }

    /**
     * Gets the process that generates new blocks and resets the player
     *
     * @return The process
     */
    public BukkitTask getProcess() {
        return process;
    }

    public PlayerSettings getSettings() {
        return settings;
    }
}
