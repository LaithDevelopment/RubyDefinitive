package com.mojang.minecraft.level;

import java.util.Random;

public class PerlinNoiseFilter {
    Random random;
    int seed;
    int levels;
    int fuzz;
    
    public PerlinNoiseFilter(final int levels) {
        this.random = new Random();
        this.seed = this.random.nextInt();
        this.levels = 0;
        this.fuzz = 16;
        this.levels = levels;
    }
    
    public int[] read(final int width, final int height) {
        final Random random = new Random();
        final int[] tmp = new int[width * height];
        final int level = this.levels;
        for (int step = width >> level, y = 0; y < height; y += step) {
            for (int x = 0; x < width; x += step) {
                tmp[x + y * width] = (random.nextInt(256) - 128) * this.fuzz;
            }
        }
        for (int step = width >> level; step > 1; step /= 2) {
            final int val = 256 * (step << level);
            final int ss = step / 2;
            for (int y2 = 0; y2 < height; y2 += step) {
                for (int x2 = 0; x2 < width; x2 += step) {
                    final int ul = tmp[(x2 + 0) % width + (y2 + 0) % height * width];
                    final int ur = tmp[(x2 + step) % width + (y2 + 0) % height * width];
                    final int dl = tmp[(x2 + 0) % width + (y2 + step) % height * width];
                    final int dr = tmp[(x2 + step) % width + (y2 + step) % height * width];
                    final int m = (ul + dl + ur + dr) / 4 + random.nextInt(val * 2) - val;
                    tmp[x2 + ss + (y2 + ss) * width] = m;
                }
            }
            for (int y2 = 0; y2 < height; y2 += step) {
                for (int x2 = 0; x2 < width; x2 += step) {
                    final int c = tmp[x2 + y2 * width];
                    final int r = tmp[(x2 + step) % width + y2 * width];
                    final int d = tmp[x2 + (y2 + step) % width * width];
                    final int mu = tmp[(x2 + ss & width - 1) + (y2 + ss - step & height - 1) * width];
                    final int ml = tmp[(x2 + ss - step & width - 1) + (y2 + ss & height - 1) * width];
                    final int i = tmp[(x2 + ss) % width + (y2 + ss) % height * width];
                    final int u = (c + r + i + mu) / 4 + random.nextInt(val * 2) - val;
                    final int l = (c + d + i + ml) / 4 + random.nextInt(val * 2) - val;
                    tmp[x2 + ss + y2 * width] = u;
                    tmp[x2 + (y2 + ss) * width] = l;
                }
            }
        }
        final int[] result = new int[width * height];
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                result[x + y * width] = tmp[x % width + y % height * width] / 1024 + 128;
            }
        }
        return result;
    }
}
