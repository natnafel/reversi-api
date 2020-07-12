package com.cs525.reversi.util.away;

import java.util.UUID;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cs525.reversi.models.Protocol;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.services.GameService;
import com.cs525.reversi.util.Pair;
import com.cs525.reversi.util.away.g2.G2CellLocation;

@Component
public class G2RestAwayGame extends AwayGame<UUID> {
    private final RestTemplate restTemplate;

    public G2RestAwayGame(GameService gameService, RestTemplate restTemplate) {
        super(gameService);
        this.restTemplate = restTemplate;
    }

    @Override
    protected Pair<UUID, CellLocation> startGame(String hostName, int port, boolean makeFirstMove) {
        return new Pair<>(UUID.randomUUID(), null);
    }

    @Override
    protected CellLocation makeAMove(String hostName, int port, UUID identifier, CellLocation cellLocation) {

        int row = -1;
        int col = -1;

        if (cellLocation != null) {
            row = cellLocation.getRow();
            col = cellLocation.getCol();
        }

        G2CellLocation g2CellLocation = restTemplate.exchange(String.format("%s:%s/game/postmove?x=%s&y=%s", hostName, port, row, col),
                HttpMethod.GET, null, G2CellLocation.class).getBody();

        return new CellLocation(g2CellLocation.getX(), g2CellLocation.getY()) ;
    }

    @Override
    public Protocol getProtocol() {
        return Protocol.G2REST;
    }
}
