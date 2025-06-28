package com.sudoko.uicustom.screen;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sudoko.Generator;
import com.sudoko.model.Space;
import com.sudoko.service.BoardService;
import com.sudoko.service.Difficulty;
import com.sudoko.service.NotifierService;
import com.sudoko.uicustom.Input.NumberText;
import com.sudoko.uicustom.button.StartButton;
import com.sudoko.uicustom.button.CheckGameStatusButton;
import com.sudoko.uicustom.button.FinishGameButton;
import com.sudoko.uicustom.button.ResetButton;
import com.sudoko.uicustom.button.DifficultyButton;
import com.sudoko.uicustom.frame.MainFrame;
import com.sudoko.uicustom.panel.MainPanel;
import com.sudoko.uicustom.panel.SudokuSector;

import static com.sudoko.model.GameStatusEnum.INCOMPLETE;
import static com.sudoko.service.EventEnum.CLEAR_SPACE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showConfirmDialog;

public class MainScreen {
    
    private final static Dimension dimension = new Dimension(600, 600);
    private final NotifierService notifierService;
    private final Generator generator;
    private final DifficultyButton difficultyButton;
    private Difficulty selectedDifficulty;
    private BoardService boardService;
    private boolean gameIsActive;
    private List<NumberText> allNumberTexts;

    private JButton startButton;
    private JButton resetButton;
    private JButton checkGameStatusButton;
    private JButton finishGameButton;

    public MainScreen(final Generator generator, final DifficultyButton difficultyButton){
        this.generator = generator;
        this.gameIsActive = false;
        this.selectedDifficulty = Difficulty.EASY;
        this.difficultyButton = difficultyButton;
        this.notifierService = new NotifierService();
        this.allNumberTexts = new ArrayList<>();
    }

    private void generateNewGame(Difficulty difficulty){
        String[] gameStringArray = generator.gameNew(difficulty);
        Map<String, String> newGameConfig = new HashMap<>();
        for (String cellString : gameStringArray){
            String[] parts = cellString.split(";");
            String coords = parts[0];
            String valueAndFixed = parts[1];
            newGameConfig.put(coords, valueAndFixed);
        }
        this.boardService = new BoardService(newGameConfig);
        notifierService.notify(CLEAR_SPACE);
    }

    public void buildMainScreen(){
        JPanel mainPanel = new MainPanel(dimension);
        JFrame mainFrame = new MainFrame(dimension, mainPanel);

        difficultyButton.setExternalActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String difficultyName = e.getActionCommand();
                selectedDifficulty = Difficulty.valueOf(difficultyName);
            }
        });
        generateNewGame(selectedDifficulty);
        rebuildSudokuGrid(mainPanel);
        mainFrame.setVisible(true);
    }

    private void rebuildSudokuGrid(JPanel mainPanel){
        mainPanel.removeAll();
        allNumberTexts.clear();
        for (int r = 0; r < 9; r+=3){
            var endrow = r + 2;
            for (int c = 0; c < 9; c+=3){
                var endcol = c + 2;
                List<Space> spaces = getSpacesFromSector(boardService.getSpaces(), c, endcol, r, endrow);
                JPanel sector = generateSection(spaces);
                mainPanel.add(sector);
            }
        }
        addDifficultyButton(mainPanel);
        addStartButton(mainPanel);
        addResetButton(mainPanel);
        addCheckGameStatusButton(mainPanel);
        addFinishGameButton(mainPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private List<Space> getSpacesFromSector(List<List<Space>> spaces, final int initCol, final int endCol,
                                                                      final int initRow, final int endRow){
        List<Space> spaceSector = new ArrayList<>();
        for(int r = initRow; r <= endRow; r++){
            for (int c = initCol; c <= endCol; c++){
                spaceSector.add(spaces.get(r).get(c));
            }
        }
        return spaceSector;
    }

    private JPanel generateSection(final List<Space> spaces){
        List<NumberText> fields = new ArrayList<>();
        for (Space space : spaces){
            NumberText numberText = new NumberText(space);
            notifierService.subscribe(CLEAR_SPACE, numberText);
            fields.add(numberText);
            allNumberTexts.add(numberText);
        }
        return new SudokuSector(fields);
    }

    private void addStartButton(JPanel maiPanel){
        startButton = new StartButton(e -> {
            var gameStatus = boardService.getStatus();
            if (gameStatus == INCOMPLETE){
                int dialogResult = showConfirmDialog(
                    null,
                    "O jogo já foi iniciado. Deseja realmente iniciar um novo jogo?",
                    "Novo jogo",
                    YES_NO_OPTION,
                    QUESTION_MESSAGE
                );
                if (dialogResult != 0){
                    return;
                }
            }
            generateNewGame(selectedDifficulty);
            gameIsActive = true;
            rebuildSudokuGrid(maiPanel);
            for (NumberText nt : allNumberTexts){
                nt.setGameActive(true);
            }
            resetButton.setEnabled(true);
            checkGameStatusButton.setEnabled(true);
            finishGameButton.setEnabled(true);
        });
        maiPanel.add(startButton);
    }

    private void addDifficultyButton(JPanel mainPanel) {
        mainPanel.add(difficultyButton);
    }

    private void addResetButton(JPanel mainPanel) {
        resetButton = new ResetButton(e -> {
            int dialogResult = showConfirmDialog(
                null,
                "Deseja realmente reiniciar o jogo?",
                "Limpar o jogo",
                YES_NO_OPTION,
                QUESTION_MESSAGE
            );
            if (dialogResult == 0){
                boardService.reset();
                notifierService.notify(CLEAR_SPACE);
            }
        });
        resetButton.setEnabled(false);
        mainPanel.add(resetButton);
    }

    private void addCheckGameStatusButton(JPanel mainPanel) {
        checkGameStatusButton = new CheckGameStatusButton(e -> {
            var hasErrors = boardService.hasErros();
            var gameStatus = boardService.getStatus();
            String message = switch (gameStatus){
                case NON_STARTED -> "O Jogo não foi iniciado";
                case INCOMPLETE -> "O jogo está imcompleto";
                case COMPLETE -> "O jogo esta completo";
            };
            message += hasErrors ? " e contém erros" : " e não contém erros";
            showMessageDialog(null, message);
        });
        checkGameStatusButton.setEnabled(false);
        mainPanel.add(checkGameStatusButton);
    }

    private void addFinishGameButton(JPanel mainPanel) {
        finishGameButton = new FinishGameButton(e -> {
            if (boardService.gameIsFinished()){
                showMessageDialog(null, "Parabéns você concluiu o jogo");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
                gameIsActive = false;
                for (NumberText nt : allNumberTexts){
                    nt.setGameActive(false);
                }
            } else {
                showMessageDialog(null, "Seu jogo tem algum erro, ajuste e tente novamente");
            }
        });
        finishGameButton.setEnabled(false);
        mainPanel.add(finishGameButton);
    }
}