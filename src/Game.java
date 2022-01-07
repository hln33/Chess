import Board.Board;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

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

        private void setTitle(String newTitle) {
            label.setText(newTitle);
        }
    }
    private static class Start extends JPanel implements ActionListener{
        Start() {
            JButton onePlayer = new JButton();
            JButton twoPlayer = new JButton();

            onePlayer.setText("One Player");
            onePlayer.addActionListener(this);
            twoPlayer.setText("Two Player");
            twoPlayer.addActionListener(this);

            add(onePlayer);
            add(twoPlayer);

            setBounds(500, 150, 500, 500);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton)e.getSource();
            boolean PVP = Objects.equals(clickedButton.getText(), "Two Player");

            Board board = new Board(PVP);
            board.addPropertyChangeListener(new GameOverListener());

            frame.remove(this);
            frame.add(title, BorderLayout.NORTH);
            frame.add(board);
            frame.setVisible(true);
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
    private static final JFrame frame = new JFrame();
    Game() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(2000, 2000);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        Start start = new Start();
        frame.add(start);
    }

}
