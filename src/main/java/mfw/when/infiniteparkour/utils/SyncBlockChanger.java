package mfw.when.infiniteparkour.utils;

import mfw.when.infiniteparkour.InfiniteParkour;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SyncBlockChanger {

    private final Block block;
    private final Location loc;
    private final Boolean withParticles;

    public SyncBlockChanger(Location loc, Block block, boolean withParticles) {
        this.loc = loc;
        this.block = block;
        this.withParticles = withParticles;

        if (!loc.getChunk().isLoaded()) loc.getChunk().load();

        Bukkit.getScheduler().runTask(InfiniteParkour.getPlugin(), () -> {
            Location tmpLoc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());

            CraftWorld craftWorld = (CraftWorld) tmpLoc.getWorld();
            ServerLevel nmsWorld = craftWorld.getHandle();
            LevelChunk nmsChunk = nmsWorld.getChunkIfLoaded(tmpLoc.getBlockX() >> 4, tmpLoc.getBlockZ() >> 4);
            BlockPos bp = new BlockPos(tmpLoc.getBlockX(), tmpLoc.getBlockY(), tmpLoc.getBlockZ());

            BlockState bs = block.defaultBlockState();
            nmsChunk.setBlockState(bp, bs, true, true);
            ClientboundBlockUpdatePacket blockChange = new ClientboundBlockUpdatePacket(bp, this.block.defaultBlockState());

            for (Player player : tmpLoc.getWorld().getPlayers()) {
                ((CraftPlayer) player).getHandle().connection.send(blockChange);
            }
        });
    }

    public org.bukkit.block.Block run() {

        if (withParticles) {
            loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc.clone().add(0.5, 0.5, 0.5), 10, 0, 0, 0, loc.getBlock().getType().createBlockData());
        }

        return loc.getBlock();
    }
}
