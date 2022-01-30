package mfw.when.infiniteparkour.utils;

import mfw.when.infiniteparkour.InfiniteParkour;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class SyncBlockChanger {

    private Block block;
    private Material material;

    public SyncBlockChanger(Block block, Material material) {
        this.block = block;
        this.material = material;
    }

    public void run() {
        Bukkit.getScheduler().runTask(InfiniteParkour.getPlugin(), new Runnable() {
            @Override
            public void run() {
                block.setType(material);
            }
        });
    }
}
