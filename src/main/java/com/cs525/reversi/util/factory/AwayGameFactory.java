package com.cs525.reversi.util.factory;

import com.cs525.reversi.exception.ProtocolNotSupportedException;
import com.cs525.reversi.models.Protocol;
import com.cs525.reversi.util.away.AwayGame;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AwayGameFactory {
    private final List<AwayGame<?>> awayGames;
    @Lazy
    public AwayGameFactory(List<AwayGame<?>> awayGames) {
        this.awayGames = awayGames;
    }

    public AwayGame<?> getAwayGame(Protocol protocol) {
        return awayGames.stream()
                .filter(awayGame -> awayGame.getProtocol() == protocol)
                .findAny()
                .orElseThrow(() -> new ProtocolNotSupportedException(protocol));
    }
}
