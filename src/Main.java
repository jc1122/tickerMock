import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Main {

    public static void setBorder(JComponent component) {
        component.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.red),component.getBorder()));
    }


    public static class TickerTable {
        JScrollPane scrollPane;
        private JScrollPane createTable(Object[][] data) {
            String[] columnNames = {"test1", "test2", "test3"};
            int numRows = 0 ;
            DefaultTableModel model = new DefaultTableModel(numRows, columnNames.length) ;
            model.setColumnIdentifiers(columnNames);
            if(data != null) {
                model.setDataVector(data, columnNames);
            }
            JTable table2 = new JTable(model);

            JScrollPane scrollPane2 = new JScrollPane(table2);
            table2.setFillsViewportHeight(true);

            return scrollPane2;
        }

        public TickerTable(Object[][] data){
            scrollPane = createTable(data);
            Border innerBorder = BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Tickers to choose from:"),
                    BorderFactory.createEmptyBorder(5,5,5,5)
            );
            Border completeBorder = BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5,5,5,5),
                    innerBorder);
            scrollPane.setBorder(completeBorder);
            //setBorder(scrollPane);
        }

        public JScrollPane getTable() {
            return scrollPane;
        }
    }

    public static class ButtonPane {
        JPanel buttonPane = new JPanel();
        public ButtonPane() {
            buttonPane.add(new JButton("OK"));
            buttonPane.add(new JButton("Cancel"));
            buttonPane.add(new JButton("Save..."));
            buttonPane.add(new JButton("Load..."));
        }
        public JPanel getPane() {
            return buttonPane;
        }
    }

    public static class MoveButtonPane {
        JPanel buttonPanel = new JPanel();
        MoveButtonPane() {
            JButton rightButton = new JButton(">");
            JButton leftButton = new JButton("<");
            rightButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.PAGE_AXIS));
            buttonPanel.add(rightButton);
            buttonPanel.add(Box.createRigidArea(new Dimension((int)(leftButton.getPreferredSize().getWidth()*1.61),
                    (int)leftButton.getPreferredSize().getHeight())));
            buttonPanel.add(leftButton);
            //setBorder(buttonPanel);
        }

        public JPanel getPanel() {
            return buttonPanel;
        }
    }
    public static void main(String[] args) {
        JDialog dialog = new JDialog();


        Object[][] data = {
                {"ticker", "name", "value"},
                {"ticker1", "name1", "value"},
                {"ticker2", "name2", "value"},
                {"ticker3", "name3", "value"},
                {"ticker4", "name4", "value"}
        };

        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));

        pane.add(new TickerTable(data).getTable());
        pane.add(new MoveButtonPane().getPanel());
        pane.add(new TickerTable(null).getTable());
        //setBorder(pane);
        dialog.setLayout(new BorderLayout());
        dialog.add(pane, BorderLayout.CENTER);

        dialog.add(new ButtonPane().getPane(),BorderLayout.PAGE_END);
        dialog.pack();
        SwingUtilities.invokeLater(() ->
                dialog.setVisible(true));

        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
