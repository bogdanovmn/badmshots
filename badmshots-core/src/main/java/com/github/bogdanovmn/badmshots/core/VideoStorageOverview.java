package com.github.bogdanovmn.badmshots.core;


import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class VideoStorageOverview {
    long totalVideos;
    @Singular("player")
    List<PlayerOverview> playerOverview;
    @Singular("type")
    List<TypeOverview> typeOverview;

    @Value
    @Builder
    static public class PlayerOverview {
        String name;
        long totalVideos;
    }

    @Value
    @Builder
    static public class TypeOverview {
        String type;
        long totalVideos;
    }
}
