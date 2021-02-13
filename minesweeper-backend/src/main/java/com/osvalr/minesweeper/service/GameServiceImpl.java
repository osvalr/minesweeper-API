package com.osvalr.minesweeper.service;

import com.osvalr.minesweeper.controller.dto.GameResponse;
import com.osvalr.minesweeper.domain.Game;
import com.osvalr.minesweeper.domain.GameState;
import com.osvalr.minesweeper.domain.User;
import com.osvalr.minesweeper.exception.GameExplodedException;
import com.osvalr.minesweeper.exception.GameFinishedException;
import com.osvalr.minesweeper.exception.PositionOutOfBounds;
import com.osvalr.minesweeper.repository.GameRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    @Inject
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public GameResponse create(User user, int size, double mines) {
        Game game = new Game(size, mines);
        game.setUser(user);
        game.setState(GameState.IN_PROGRESS);
        gameRepository.save(game);
        return new GameResponse(game.getId(), game.getStartTime().getTime());
    }

    @Override
    public Optional<Game> getGameById(@Nonnull Long gameId) {
        return gameRepository.findById(gameId);
    }

    private void validateBounds(int expectedSize, int x, int y) {
        if (x < 1 || x > expectedSize
                || y < 1 || y > expectedSize) {
            throw new PositionOutOfBounds("X or Y is out of bounds");
        }
    }

    @Override
    public void flagPosition(@Nonnull Game game, int row, int col) {
        game.toggleFlag(row, col);
    }

    @Override
    public void openPosition(@Nonnull Game game, int row, int col) {
        try {
            game.openPosition(row, col);
        } catch (GameExplodedException e) {
            game.setEndTime(new Date());
            game.setState(GameState.EXPLODED);
            throw e;
        } catch (GameFinishedException e) {
            game.setEndTime(new Date());
            game.setState(GameState.WIN);
            throw e;
        } finally {
            gameRepository.save(game);
        }
    }

    @Override
    public Optional<List<Game>> getAllOpenGamesByUserId(@Nonnull Long id) {
        return gameRepository.findByUserId(id);
    }
}
