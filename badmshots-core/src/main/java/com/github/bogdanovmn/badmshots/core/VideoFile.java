package com.github.bogdanovmn.badmshots.core;


import lombok.Builder;
import lombok.Value;

import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VideoFile {
    private final Path name;
    private final Meta meta;

    public VideoFile(Path path) {
        this.name = path;
        this.meta = Meta.of(path.getFileName().toString());
    }

    Set<String> players() {
        return meta.getPlayers();
    }

    @Value
    @Builder
    private static class Meta {
        /*
            2024-12-08-t-sposad--kachalovA-elizarevaT-ternovayaT-ternovoyD.mp4
            2024-12-21-lesteh--misha-mashaNO-igor-mashaPE.mp4
        */
        private static final Pattern NAME_PATTERN = Pattern.compile(
            "^(?<date>\\d{4}-\\d{2}-\\d{2})(?<isTournament>-t)?(?<club>-\\w+)(?<tCategory>-\\w{2,3})?--(?<players>\\w+-\\w+(-\\w+)?(-\\w+)?)\\.mp4$");

        static Meta of(String fileName) {
            Matcher matcher = NAME_PATTERN.matcher(fileName);
            if (matcher.find()) {
                return Meta.builder()
                    .date(
                        matcher.group("date")
                    )
                    .club(
                        matcher.group("club")
                    )
                    .tournamentCategory(
                        matcher.group("tCategory")
                    )
                    .isTournament(
                        matcher.group("isTournament") != null
                    )
                    .players(
                        Set.of(
                            matcher.group("players").split("-")
                        )
                    )
                .build();

            }
            throw new IllegalArgumentException(String.format("Invalid file name: %s", fileName));
        }

        String date;
        String club;
        String tournamentCategory;
        boolean isTournament;
        Set<String> players;
    }
}
