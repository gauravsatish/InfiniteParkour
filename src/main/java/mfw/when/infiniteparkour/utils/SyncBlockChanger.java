package mfw.when.infiniteparkour.utils;

import mfw.when.infiniteparkour.InfiniteParkour;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.Chunk;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.entity.Player;

public class SyncBlockChanger {

    private final Block block;
    private final Location loc;
    private final Material material;

    public SyncBlockChanger(Location loc, Block block, Material material) {
        this.loc = loc;
        this.block = block;
        this.material = material;
    }

    public void run() {
        Bukkit.getScheduler().runTask(InfiniteParkour.getPlugin(), new Runnable() {
            @Override
            public void run() {

                CraftWorld craftWorld = (CraftWorld) loc.getWorld();
                WorldServer nmsWorld = craftWorld.getHandle();
                Chunk nmsChunk = nmsWorld.getChunkIfLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
                BlockPosition bp = new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

                IBlockData bs = block.n();
                nmsChunk.setBlockState(bp, bs, true, true);

//                ProtocolManager manager = ProtocolLibrary.getProtocolManager();
//                PacketContainer packet = manager.createPacket(PacketType.Play.Server.BLOCK_CHANGE);
//                packet.getIntegers().write(0, 1);

                for (Player player : loc.getWorld().getPlayers()) {
                    player.sendBlockChange(loc, material.createBlockData());
                }
            }
        });
    }
}
