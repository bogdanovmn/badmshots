package com.github.bogdanovmn.badmshots.core;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class VideoFile {
    @EqualsAndHashCode.Include
    private final Path name;
    private final Meta meta;

    public enum Type { SINGLE, DOUBLE, INDIVIDUAL, TRAINING, UNKNOWN }

    public VideoFile(Path path) {
        this.name = path;
        this.meta = Meta.of(path.getFileName().toString());
    }

    Set<String> players() {
        return meta.getPlayers();
    }

    boolean isNormalGame() {
        return meta.getType() == Type.SINGLE || meta.getType() == Type.DOUBLE;
    }

    @Value
    @Builder
    private static class Meta {
        /*
            2024-12-08-t-sposad--kachalovA-elizarevaT-ternovayaT-ternovoyD.mp4
            2024-12-21-lesteh--misha-mashaNO-igor-mashaPE.mp4
        */
        private static final Pattern NAME_PATTERN = Pattern.compile(
            "^(?<date>\\d{4}-\\d{2}-\\d{2})(?<isTournament>-t)?(?<club>-\\w+)(?<tCategory>-\\w{2,3})?--(?<players>[A-Za-z]+(-[A-Za-z]+){1,3})\\.mp4$");

        static Meta of(String fileName) {
            Matcher matcher = NAME_PATTERN.matcher(fileName);
            if (matcher.find()) {
                List<String> players = List.of(
                    matcher.group("players").split("-")
                );
                return Meta.builder()
                    .type(
                        switch(players.size()) {
                            case 2 -> Type.SINGLE;
                            case 3 -> Type.TRAINING;
                            case 4 -> Type.DOUBLE;
                            default -> Type.UNKNOWN;
                        }
                    )
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
                        Set.copyOf(players)
                    )
                .build();

            } else {
                return Meta.builder()
                    .type(
                        fileName.contains("-indi-")
                            ? Type.INDIVIDUAL
                            : Type.UNKNOWN
                    )
                .build();
            }
        }

        Type type;
        String date;
        String club;
        String tournamentCategory;
        boolean isTournament;
        Set<String> players;
    }
}
