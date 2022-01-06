import Board.Board;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Game {
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
    }

    Game() {
        Board board = new Board();
        board.addPropertyChangeListener(new GameOverListener());

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(2000, 2000);
        frame.setLayout(new BorderLayout());

        frame.add(new Title(), BorderLayout.NORTH);
        frame.add(board);
        frame.setVisible(true);
    }

    private static class GameOverListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("GameOver".equals(evt.getPropertyName())) {
                System.out.println(evt.getNewValue());
            }
        }
    }
}
