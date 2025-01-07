package com.github.bogdanovmn.badmshots.core;

import com.github.bogdanovmn.badmshots.core.VideoStorageOverview.PlayerOverview;
import com.github.bogdanovmn.common.core.StringCounter;
import com.github.bogdanovmn.common.files.Directory;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class VideoStorage {
    private final String path;
    private List<VideoFile> videos;

    private synchronized void init() throws IOException {
        if (videos == null) {
            videos = new Directory(path)
                .filesWithExtRecursively("mp4").stream()
                    .map(VideoFile::new)
                    .toList();
        }
    }
    public VideoStorageOverview overview() throws IOException {
        init();
        VideoStorageOverview.VideoStorageOverviewBuilder result = VideoStorageOverview.builder()
            .totalVideos(videos.size());

        StringCounter playerCounter = new StringCounter();
        for (VideoFile video : videos) {
            video.players().forEach(playerCounter::increment);
        }

        return result.playerOverview(
            playerCounter.keys().stream()
                .collect(
                    toMap(
                        k -> k,
                        k -> PlayerOverview.builder().totalVideos(playerCounter.get(k)).build()
                    )
                )
            )
        .build();
    }
}
