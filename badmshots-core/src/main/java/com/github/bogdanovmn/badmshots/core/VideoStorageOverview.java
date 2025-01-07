package com.github.bogdanovmn.badmshots.core;


import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class VideoStorageOverview {
    long totalVideos;
    @Singular("player")
    Map<String, PlayerOverview> playerOverview;
    @Singular("type")
    Map<String, TypeOverview> typeOverview;

    @Value
    @Builder
    static public class PlayerOverview {
        long totalVideos;
    }

    @Value
    @Builder
    static public class TypeOverview {
        long totalVideos;
    }
}
