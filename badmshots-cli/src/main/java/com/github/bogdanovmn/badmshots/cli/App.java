package com.github.bogdanovmn.badmshots.cli;

import com.github.bogdanovmn.badmshots.core.VideoStorage;
import com.github.bogdanovmn.badmshots.core.VideoStorageOverview;
import com.github.bogdanovmn.jaclin.CLI;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    private static final String OPT_STORAGE_DIR = "storage-dir";
    private static final String OPT_OVERVIEW = "overview";

    public static void main(String[] args) throws Exception {
        new CLI("badmshots", "making video shots")
            .withRequiredOptions()
                .strArg(OPT_STORAGE_DIR, "video storage directory")
            .withOptions()
                .flag(OPT_OVERVIEW, "show video storage overview")
            .withRestrictions()
                .atLeastOneShouldBeUsed(OPT_OVERVIEW)
            .withEntryPoint(options -> {
                if (options.has(OPT_OVERVIEW)) {
                    VideoStorageOverview overview = new VideoStorage(options.get(OPT_STORAGE_DIR)).overview();
                    System.out.printf("Storage '%s' overview: %d videos\n", OPT_STORAGE_DIR, overview.getTotalVideos());
                    System.out.println("Player's statistic:");
                    overview.getPlayerOverview().forEach(
                        player -> System.out.printf("  %s: %d\n", player.getName(), player.getTotalVideos())
                    );
                }
            }).run(args);
    }
}
