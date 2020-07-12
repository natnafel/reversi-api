package com.cs525.reversi.util.away.g1;

import java.util.UUID;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cs525.reversi.models.Protocol;
import com.cs525.reversi.req.CellLocation;
import com.cs525.reversi.services.GameService;
import com.cs525.reversi.util.Pair;
import com.cs525.reversi.util.away.AwayGame;

@Component
public class G1RestAwayGame extends AwayGame<UUID> {
	private final RestTemplate restTemplate;

	public G1RestAwayGame(GameService gameService, RestTemplate restTemplate) {
		super(gameService);
		this.restTemplate = restTemplate;
	}

	@Override
	protected Pair<UUID, CellLocation> startGame(String hostName, int port, boolean makeFirstMove) {
		restTemplate.exchange(String.format("%s/register/%s", hostName, "Team 3"), HttpMethod.POST, null,
				G1CellLocation.class);

		return new Pair<>(UUID.randomUUID(), null);
	}

	@Override
	protected CellLocation makeAMove(String hostName, int port, UUID identifier, CellLocation cellLocation) {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		G1CellLocation g1CellLocation = restTemplate
				.postForEntity(String.format("%s/play", hostName, cellLocation.getRow(), cellLocation.getCol()),
						cellLocation, G1CellLocation.class)
				.getBody();
		return new CellLocation(g1CellLocation.getRow(), g1CellLocation.getCol());
	}

	@Override
	public Protocol getProtocol() {
		return Protocol.G1REST;
	}
}