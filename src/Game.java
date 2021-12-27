import Board.Board;
import javax.swing.*;
import java.awt.*;

public class Game {
    JFrame frame = new JFrame();

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

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(2000, 2000);
        frame.setLayout(new BorderLayout());

        frame.add(new Title(), BorderLayout.NORTH);
        frame.add(board);
        frame.setVisible(true);
    }
}
