package com.cs525.reversi.util.away.g2;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cs525.reversi.models.Protocol;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.services.GameService;
import com.cs525.reversi.util.Pair;
import com.cs525.reversi.util.away.AwayGame;

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

		G2CellLocation g2CellLocation = restTemplate.getForEntity(String.format("%s:%s/game/postmove?x=%s&y=%s", hostName,
				port, cellLocation.getRow(), cellLocation.getCol()), G2CellLocation.class)
				.getBody();

		return new CellLocation(g2CellLocation.getX(), g2CellLocation.getY());
	}

	@Override
	public Protocol getProtocol() {
		return Protocol.G2REST;
	}
}
