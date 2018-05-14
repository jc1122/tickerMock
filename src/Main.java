import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void setBorder(JComponent component) {
        component.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.red),component.getBorder()));
    }


    public static class TickerTable {
        JScrollPane scrollPane;
        JTable table;
        private JScrollPane createTable(Object[][] data) {
            String[] columnNames = {"test1", "test2", "test3"};
            int numRows = 0 ;
            DefaultTableModel model = new DefaultTableModel(numRows, columnNames.length) ;
            model.setColumnIdentifiers(columnNames);
            if(data != null) {
                model.setDataVector(data, columnNames);
            }
            table = new JTable(model);

            JScrollPane scrollPane2 = new JScrollPane(table);
            table.setFillsViewportHeight(true);

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

        public JScrollPane getPane() {
            return scrollPane;
        }
        public JTable getTable() { return table;}
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
        JTable left;
        JTable right;

        MoveButtonPane(JTable leftTable, JTable rightTable) {
            this.left = leftTable;
            this.right = rightTable;

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
            rightButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    Object[] newRow = new Object[left.getColumnCount()];
                    for (int i = 0; i < left.getColumnCount(); i++) {
                        newRow[i] = left.getValueAt(left.getSelectedRow(), i);
                    }
                    DefaultTableModel rightModel = (DefaultTableModel) right.getModel();
                    rightModel.addRow(newRow);
                    DefaultTableModel leftModel = (DefaultTableModel) left.getModel();
                    leftModel.removeRow(left.getSelectedRow());
                }
            });
            leftButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    Object[] newRow = new Object[right.getColumnCount()];
                    for (int i = 0; i < right.getColumnCount(); i++) {
                        newRow[i] = right.getValueAt(right.getSelectedRow(), i);
                    }
                    DefaultTableModel leftModel = (DefaultTableModel) left.getModel();
                    DefaultTableModel rightModel = (DefaultTableModel) right.getModel();
                    leftModel.addRow(newRow);
                    rightModel.removeRow(right.getSelectedRow());
                }
            });
        }

        public JPanel getPanel() {
            return buttonPanel;
        }
    }
    public static void main(String[] args) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Tickers to watch");

        Object[][] data = {
                {"ticker", "name", "value"},
                {"ticker1", "name1", "value"},
                {"ticker2", "name2", "value"},
                {"ticker3", "name3", "value"},
                {"ticker4", "name4", "value"}
        };

        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));

        TickerTable left = new TickerTable(data);
        TickerTable right = new TickerTable(null);
        JTable leftTable = left.getTable();
        JTable rightTable = right.getTable();

        pane.add(left.getPane());
        pane.add(new MoveButtonPane(leftTable, rightTable).getPanel());
        pane.add(right.getPane());
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
