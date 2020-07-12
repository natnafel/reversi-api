package com.cs525.reversi.util.away;

import com.cs525.reversi.models.Protocol;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.req.GameSideDesicion;
import com.cs525.reversi.req.NewGame;
import com.cs525.reversi.resp.NewGameAndMoveResp;
import com.cs525.reversi.services.GameService;
import com.cs525.reversi.util.Pair;
import com.cs525.reversi.util.moderator.GameModerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.logging.Logger;

@Component
public class G3RestAwayGame extends AwayGame<UUID> {
    private final RestTemplate restTemplate;

    private static final String newGamePath = "api/games";
    private static final Logger logger = Logger.getLogger(G3RestAwayGame.class.getSimpleName());

    @Value("${reversi.default-player.username}")
    private String defaultUsername;

    public G3RestAwayGame(GameService gameService, RestTemplate restTemplate) {
        super(gameService);
        this.restTemplate = restTemplate;
    }

    @Override
    protected Pair<UUID, CellLocation> startGame(String hostName, int port, boolean makeFirstMove) {
        NewGame newGameRequest;
        if (makeFirstMove) {
            newGameRequest = new NewGame(defaultUsername, GameSideDesicion.AWAY);
        } else {
            newGameRequest = new NewGame(defaultUsername, GameSideDesicion.HOME);
        }

        NewGameAndMoveResp response =  restTemplate.exchange(String.format("%s:%s/%s", hostName, port, newGamePath), HttpMethod.POST,
                new HttpEntity<>(newGameRequest), NewGameAndMoveResp.class).getBody();

        if (response == null) {
            logger.warning(String.format("Unexpected Response. New game request at %s:%s/%s responded with null.", hostName, port, newGamePath));
            return null;
        }
        return new Pair<>(response.getGameId(), response.getHomeNewPiece());
    }

    @Override
    protected CellLocation makeAMove(String hostName, int port, UUID identifier, CellLocation cellLocation) {
        if (cellLocation == null){
            cellLocation = new CellLocation(-1, -1);
        }
        NewGameAndMoveResp response =  restTemplate.exchange(String.format("%s:%s/api/games/%s/moves", hostName, port, identifier), HttpMethod.POST,
                new HttpEntity<>(cellLocation), NewGameAndMoveResp.class).getBody();
        return response.getHomeNewPiece();
    }

    @Override
    public Protocol getProtocol() {
        return Protocol.G3REST;
    }
}
