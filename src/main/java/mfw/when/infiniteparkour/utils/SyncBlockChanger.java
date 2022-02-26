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
    }

    public org.bukkit.block.Block run() {
        Bukkit.getScheduler().runTask(InfiniteParkour.getPlugin(), new Runnable() {
            @Override
            public void run() {

                if (withParticles) {
                    loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc.add(0.5, 0.5, 0.5), 10, 0, 0, 0, loc.getBlock().getType().createBlockData());
                }

                CraftWorld craftWorld = (CraftWorld) loc.getWorld();
                ServerLevel nmsWorld = craftWorld.getHandle();
                LevelChunk nmsChunk = nmsWorld.getChunkIfLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
                BlockPos bp = new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

                BlockState bs = block.defaultBlockState();
                nmsChunk.setBlockState(bp, bs, true, true);
                BlockPos blockPos = new BlockPos(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                ClientboundBlockUpdatePacket blockChange = new ClientboundBlockUpdatePacket(blockPos, block.defaultBlockState());

                for (Player player : loc.getWorld().getPlayers()) {
                    ((CraftPlayer) player).getHandle().connection.send(blockChange);
                }
            }
        });

        return loc.getBlock();
    }
}
