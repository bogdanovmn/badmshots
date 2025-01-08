package com.github.bogdanovmn.badmshots.core;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VideoFileTest {

    static class Args implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                Arguments.of(
                    "2024-12-08-t-sposad--kachalovA-elizarevaT-ternovayaT-ternovoyD.mp4",
                    Set.of("kachalovA", "elizarevaT", "ternovayaT", "ternovoyD")
                ),
                Arguments.of(
                    "2024-12-21-lesteh--misha-mashaNO-igor-mashaPE.mp4",
                    Set.of("misha", "mashaNO", "igor", "mashaPE")
                )
            );
        }
    }
    @ParameterizedTest
    @ArgumentsSource(Args.class)
    public void players(String fileName, Set<String> expectedPlayers) {
        VideoFile file = new VideoFile(Path.of(fileName));
        assertEquals(expectedPlayers, file.players());
    }
}