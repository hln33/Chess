import Board.Board;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class Game implements ActionListener {
    boolean PVP;

    private static class Title extends JPanel {
        JLabel label = new JLabel();

        Title() {
            label.setFont(new Font("Ink Free", Font.BOLD, 75));
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setText("CHESS");
            label.setOpaque(true);
            label.setBackground(Color.CYAN);
            label.setForeground(Color.MAGENTA);

            setBackground(Color.BLACK);
            add(label);
        }

        private void setTitle(String newTitle) {
            label.setText(newTitle);
        }
    }

    private static class Start extends JPanel {
        JButton onePlayer = new JButton();
        JButton twoPlayer = new JButton();

        Start(Game game) {
            onePlayer.setText("One Player");
            onePlayer.addActionListener(game);
            twoPlayer.setText("Two Player");
            twoPlayer.addActionListener(game);

            add(onePlayer);
            add(twoPlayer);

            setBounds(500, 150, 500, 500);
        }
    }

    private static class GameOverListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("GameOver".equals(evt.getPropertyName())) {
                System.out.println(evt.getNewValue());
                title.setTitle((String)evt.getNewValue());
            }
        }
    }

    private static final Title title = new Title();
    private final Start start = new Start(this);
    private final JFrame frame = new JFrame();
    Game() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(2000, 2000);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        frame.add(start);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton)e.getSource();
        Board board = new Board();
        board.addPropertyChangeListener(new GameOverListener());

        PVP = Objects.equals(clickedButton.getText(), "Two Player");
        frame.remove(start);
        frame.add(title, BorderLayout.NORTH);
        frame.add(board);
        frame.setVisible(true);
    }
}
