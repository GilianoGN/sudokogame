package com.sudoko.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sudoko.model.Board;
import com.sudoko.model.GameStatusEnum;
import com.sudoko.model.Space;

public class BoardService {
    
    private final static int BOARD_LIMIT = 9;
    private final Board board;

    public BoardService(final Map<String, String> gameConfig){
        this.board = new Board(initBoard(gameConfig));
    }

    public List<List<Space>> getSpaces(){
        return board.getSpaces();
    }

    public void reset(){
        board.reset();
    }

    public boolean hasErros(){
        return board.hasErros();
    }

    public GameStatusEnum getStatus(){
        return board.getStatus();
    }

    public boolean gameIsFinished(){
        return board.gameIsFinished();
    }

    private List<List<Space>> initBoard(final Map<String, String> gameConfig){
        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++){
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++){
                String positionConfig = gameConfig.get(String.format("%s,%s", i, j));
                Integer expected = Integer.parseInt(positionConfig.split(",")[0]);
                Boolean fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                var currentSpace = new Space(expected, fixed);
                spaces.get(i).add(currentSpace);
            }
        }
        return spaces;
    }
}