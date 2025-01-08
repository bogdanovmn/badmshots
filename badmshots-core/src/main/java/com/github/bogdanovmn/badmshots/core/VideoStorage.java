package com.github.bogdanovmn.badmshots.core;

import com.github.bogdanovmn.badmshots.core.VideoStorageOverview.PlayerOverview;
import com.github.bogdanovmn.common.core.StringCounter;
import com.github.bogdanovmn.common.files.Directory;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VideoStorage {
    private final String path;
    private Set<VideoFile> videos;

    private synchronized void init() throws IOException {
        if (videos == null) {
            videos = new Directory(path)
                .filesWithExtRecursively("mp4").stream()
                    .map(VideoFile::new)
                    .collect(Collectors.toSet());
        }
    }
    public VideoStorageOverview overview() throws IOException {
        init();
        VideoStorageOverview.VideoStorageOverviewBuilder result = VideoStorageOverview.builder()
            .totalVideos(videos.size());

        StringCounter playerCounter = new StringCounter();
        for (VideoFile video : videos) {
            if (video.isNormalGame()) {
                video.players().forEach(playerCounter::increment);
            }
        }

        return result.playerOverview(
            playerCounter.keys().stream()
            .map(
                playerName -> PlayerOverview.builder()
                    .name(playerName)
                    .totalVideos(playerCounter.get(playerName))
                .build()
            ).sorted(Comparator.comparingLong(PlayerOverview::getTotalVideos))
                .collect(Collectors.toList())
        ).build();
    }
}
