package net.mizukilab.pit.util;



/**
 * @author Araykal
 * @since 2025/5/4
 */

public class ProgressBar {
    public static String getProgressBar(double currentExp, double startExp, double nextLevelExp, int barLength) {
        double progress = (currentExp - startExp) / (nextLevelExp - startExp);
        progress = Math.max(0, Math.min(1, progress));
        int filledLength = (int) (progress * barLength);
        return "§b" + "■".repeat(filledLength) + "§7" + "□".repeat(barLength - filledLength);
    }


}
