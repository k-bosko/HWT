package hwt.menu;

import hwt.Parameters;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class MenuPanel implements Menu {
  private final JFrame frame;
  private final JButton startGameBtn;
  private final JButton restartGameBtn;
  private final JComboBox<Integer> numRowsComboBox;
  private final JComboBox<Integer> numColsComboBox;
  private final JRadioButton wrappingMazeRbtn;
  private final JRadioButton nonWrappingMazeRbtn;
  private final JRadioButton newMazeRbtn;
  private final JRadioButton seededMazeRbtn;

  private final JComboBox<Integer> numArrowsComboBox;
  private final JComboBox<Integer> numSuperBatsComboBox;
  private final JComboBox<Integer> numPitsComboBox;
  private final JRadioButton textGameType;
  private final JRadioButton guiGameType;


  public MenuPanel(){
    frame = new JFrame();
    frame.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
    frame.setSize(450, 750);

    JPanel mainPanel = new JPanel();
    GridLayout grid = new GridLayout(9, 1);
    mainPanel.setLayout(grid);
    frame.add(mainPanel);

    JPanel fakePanel = new JPanel();
    mainPanel.add(fakePanel);

    JPanel lead = new JPanel();
    lead.setLayout(new FlowLayout(FlowLayout.CENTER));
    JLabel wumpusIcon = new JLabel(new ImageIcon("./img/wumpus.png"), JLabel.CENTER);
    lead.add(wumpusIcon);

    JLabel gameLabel = new JLabel("    Hunt The Wumpus", JLabel.CENTER);
    gameLabel.setFont(new Font("Serif", Font.BOLD, 24));
    lead.add(gameLabel);
    mainPanel.add(lead);


    JLabel mazeLabel = new JLabel("Set maze options: ", SwingConstants.CENTER);
    mazeLabel.setFont(new Font("Serif", Font.BOLD, 14));
    mainPanel.add(mazeLabel);

    //maze options
    JPanel mazeOptions = new JPanel();
    GridLayout optionsGrid = new GridLayout(4,2);
    mazeOptions.setLayout(optionsGrid);

    JLabel numRowsLabel = new JLabel("Number of rows:", SwingConstants.RIGHT);
    numRowsComboBox = new JComboBox(Parameters.NUM_ROWS_ARR);
    mazeOptions.add(numRowsLabel);
    mazeOptions.add(numRowsComboBox);
    numRowsComboBox.setSelectedItem(4);

    JLabel numColsLabel = new JLabel("Number of columns:", SwingConstants.RIGHT);
    numColsComboBox = new JComboBox(Parameters.NUM_COLS_ARR);
    numColsComboBox.setSelectedItem(6);

    mazeOptions.add(numColsLabel);
    mazeOptions.add(numColsComboBox);


    nonWrappingMazeRbtn = new JRadioButton("Non-wrapping maze", true);
    nonWrappingMazeRbtn.setHorizontalAlignment(JRadioButton.RIGHT);
    wrappingMazeRbtn = new JRadioButton("Wrapping maze", false);

    //Group the radio buttons.
    ButtonGroup groupMazeType = new ButtonGroup();
    groupMazeType.add(nonWrappingMazeRbtn);
    groupMazeType.add(wrappingMazeRbtn);

    mazeOptions.add(nonWrappingMazeRbtn);
    mazeOptions.add(wrappingMazeRbtn);

    newMazeRbtn = new JRadioButton("New maze", false);
    newMazeRbtn.setHorizontalAlignment(JRadioButton.RIGHT);
    seededMazeRbtn = new JRadioButton("Same maze", true);

    //Group the radio buttons.
    ButtonGroup groupMazeSeed = new ButtonGroup();
    groupMazeSeed.add(newMazeRbtn);
    groupMazeSeed.add(seededMazeRbtn);

    mazeOptions.add(newMazeRbtn);
    mazeOptions.add(seededMazeRbtn);
    mainPanel.add(mazeOptions);

    JLabel difficultyLabel = new JLabel("Set difficulty level: ", SwingConstants.CENTER);
    difficultyLabel.setFont(new Font("Serif", Font.BOLD, 14));
    mainPanel.add(difficultyLabel);


    JPanel difficultyOptions = new JPanel();
    difficultyOptions.setLayout(new GridLayout(4, 2));


    JLabel numPitsLabel = new JLabel("Number of caves with pits:", SwingConstants.RIGHT);
    numPitsComboBox = new JComboBox(Parameters.NUM_PITS_ARR);
    numPitsComboBox.setSelectedItem(2);

    difficultyOptions.add(numPitsLabel);
    difficultyOptions.add(numPitsComboBox);

    JLabel numSuperbatsLabel = new JLabel("Number of caves with superbats:", SwingConstants.RIGHT);
    numSuperBatsComboBox = new JComboBox(Parameters.NUM_SUPERBATS_ARR);
    numSuperBatsComboBox.setSelectedItem(4);

    difficultyOptions.add(numSuperbatsLabel);
    difficultyOptions.add(numSuperBatsComboBox);

    JLabel numArrowsLabel = new JLabel("Number of arrows:", SwingConstants.RIGHT);
    numArrowsComboBox = new JComboBox(Parameters.NUM_ARROWS_ARR);
    numArrowsComboBox.setSelectedItem(2);

    difficultyOptions.add(numArrowsLabel);
    difficultyOptions.add(numArrowsComboBox);

    textGameType = new JRadioButton("TEXT game", false);
    textGameType.setHorizontalAlignment(JRadioButton.RIGHT);
    guiGameType = new JRadioButton("GUI game", true);

    //Group the radio buttons.
    ButtonGroup groupGameType = new ButtonGroup();
    groupGameType.add(textGameType);
    groupGameType.add(guiGameType);

    difficultyOptions.add(textGameType);
    difficultyOptions.add(guiGameType);

    mainPanel.add(difficultyOptions);

    JLabel commandsLabel = new JLabel("How to play: ", SwingConstants.CENTER);
    commandsLabel.setFont(new Font("Serif", Font.BOLD, 14));
    mainPanel.add(commandsLabel);

    JPanel commands = new JPanel();
    commands.setLayout(new GridLayout(4,1));

    JLabel arrowsText = new JLabel("Use keyboard arrows or mouse click for navigation", SwingConstants.CENTER);
    commands.add(arrowsText);

    JLabel shootText = new JLabel("Press \"s\" to enter the shoot mode", SwingConstants.CENTER);
    commands.add(shootText);

    JLabel shootExitText = new JLabel("Press \"ESC\" to exit the shoot mode", SwingConstants.CENTER);
    commands.add(shootExitText);

    JLabel shotText = new JLabel("Press \"ENTER\" to shoot", SwingConstants.CENTER);
    commands.add(shotText);

    mainPanel.add(commands);

    JPanel buttons = new JPanel();
    buttons.setLayout(new GridLayout(1, 2));

    startGameBtn = new JButton("Start Game");
    startGameBtn.setPreferredSize(new Dimension(40, 20));

    restartGameBtn = new JButton("Restart Game");
    restartGameBtn.setPreferredSize(new Dimension(40, 20));

    buttons.add(startGameBtn);
    buttons.add(restartGameBtn);
    mainPanel.add(buttons);
  }

  public void show() {
    frame.setVisible(true);
  }

  public void hide() {
    frame.setVisible(false);
  }

  public JButton getStartGameBtn() {
    return this.startGameBtn;
  }
  public JButton getRestartGameBtn() {
    return this.restartGameBtn;
  }

  public int getNumRows() {
    int idx = this.numRowsComboBox.getSelectedIndex();
    return Parameters.NUM_ROWS_ARR[idx];
  }

  public int getNumCols() {
    int idx = this.numColsComboBox.getSelectedIndex();
    return Parameters.NUM_COLS_ARR[idx];
  }

  public boolean isNonWrappingMaze() {
    return this.nonWrappingMazeRbtn.isSelected();
  }

  public boolean isWrappingMaze() {
    return this.wrappingMazeRbtn.isSelected();
  }

  public boolean isNewMaze() {
    return this.newMazeRbtn.isSelected();
  }

  public boolean isSameMaze() {
    return this.seededMazeRbtn.isSelected();
  }

  public boolean isTextGame() {
    return this.textGameType.isSelected();
  }

  public boolean isGuiGame() {
    return this.guiGameType.isSelected();
  }

  public int getNumPits() {
    int idx = this.numPitsComboBox.getSelectedIndex();
    return Parameters.NUM_ARROWS_ARR[idx];
  }

  public int getNumBats() {
    int idx = this.numSuperBatsComboBox.getSelectedIndex();
    return Parameters.NUM_SUPERBATS_ARR[idx];
  }

  public int getNumArrows() {
    int idx = this.numArrowsComboBox.getSelectedIndex();
    return Parameters.NUM_ARROWS_ARR[idx];
  }


}
