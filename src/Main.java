import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import java.util.List;

public class Main {
    //static JTextField filterText;
    static TableRowSorter<TickerTableModel> sorterLeft;
    static TableRowSorter<TickerTableModel> sorterRight;

    public static void setBorder(JComponent component) {
        component.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.red),component.getBorder()));
    }


    public static class TickerTable {
        JScrollPane scrollPane;
        JTable table;

        private JScrollPane createTable(List<MockTicker> tickers) {


            TickerTableModel model = new TickerTableModel(tickers) ;

            table = new JTable(model);

            JScrollPane scrollPane2 = new JScrollPane(table);
            table.setFillsViewportHeight(true);

            return scrollPane2;
        }

        public TickerTable(List<MockTicker> tickers){
            scrollPane = createTable(tickers);
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
        public TickerTableModel getModel() {return (TickerTableModel) table.getModel();}
    }

    public static class ButtonPane {
        JPanel totalPane = new JPanel();
        JPanel filterPane = new JPanel();
        BoxLayout totalPaneLayout = new BoxLayout(totalPane,BoxLayout.Y_AXIS);
        JTextField searchField;

        JPanel buttonPane = new JPanel();
        public ButtonPane() {
            buttonPane.add(new JButton("OK"));
            buttonPane.add(new JButton("Cancel"));
            buttonPane.add(new JButton("Save..."));
            buttonPane.add(new JButton("Load..."));

            filterPane.add(new JLabel("Search text: "));
            searchField = new JTextField();
            searchField.setColumns(30);
            filterPane.add(searchField);

            totalPane.setLayout(totalPaneLayout);
            totalPane.add(filterPane);
            totalPane.add(buttonPane);
        }
        public JPanel getPane() {
            return totalPane;
        }
        public JTextField getSearchField() {
            return searchField;
        }
    }

    public static class MockTicker {
        private String isin;
        private String name;
        private String marketCode;
        private String cfi;
        private String group;

        MockTicker(String i) {
            isin = "isin " + i;
            name = "name " + i;
            marketCode = "marketCode " + i;
            cfi = "cfi " + i;
            group = "group " + i;
        }

        public String getIsin() {
            return isin;
        }

        public String getName() {
            return name;
        }

        public String getMarketCode() {
            return marketCode;
        }

        public String getCfi() {
            return cfi;
        }

        public String getGroup() {
            return group;
        }
    }
    public static class TickerTableModel extends AbstractTableModel {

        List<MockTicker> tickers;
        public void setData(List<MockTicker> tickers) {
            this.tickers = tickers;
        }
        TickerTableModel(List<MockTicker> tickers) {
            this.tickers = tickers;
        }

        @Override
        public String getColumnName(int column){
            switch(column) {
                case 0:
                    return "Name";
                case 1:
                    return "ISIN";
                case 2:
                    return "CFI";
                case 3:
                    return "Group";
                case 4:
                    return "Market Code";
            }
            return null;
        }

        @Override
        public int getRowCount() {
            return tickers.size();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return tickers.get(rowIndex).getName();
                case 1:
                    return tickers.get(rowIndex).getIsin();
                case 2:
                    return tickers.get(rowIndex).getCfi();
                case 3:
                    return tickers.get(rowIndex).getGroup();
                case 4:
                    return tickers.get(rowIndex).getMarketCode();
            }
            return null;
        }

        public void addRow(MockTicker ticker) {
            tickers.add(ticker);
            this.fireTableDataChanged();
        }
        public MockTicker removeRow(int row) {
            MockTicker ticker = tickers.remove(row);
            this.fireTableDataChanged();
            return ticker;
        }
        public MockTicker getRow(int row) {
            return tickers.get(row);
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

                    TickerTableModel rightModel = (TickerTableModel) right.getModel();
                    TickerTableModel leftModel = (TickerTableModel) left.getModel();
                    int row = left.convertRowIndexToModel(left.getSelectedRow());
                    rightModel.addRow(leftModel.removeRow(row));

                }
            });
            leftButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    TickerTableModel rightModel = (TickerTableModel) right.getModel();
                    TickerTableModel leftModel = (TickerTableModel) left.getModel();
                    int row = right.convertRowIndexToModel(right.getSelectedRow());
                    leftModel.addRow(rightModel.removeRow(row));

                }
            });
        }

        public JPanel getPanel() {
            return buttonPanel;
        }
    }

    public static class TickerTablesPane {
        private JPanel pane;
        TickerTable left, right;

        private void setTableSorter(JTable table) {
            table.setAutoCreateRowSorter(true);
            TableRowSorter<TickerTableModel> sorter = new TableRowSorter<>((TickerTableModel)table.getModel());
            table.setRowSorter(sorter);
        }
        TickerTablesPane(JTextField filterText, List<MockTicker> tickers) {
            List<MockTicker> tickersToFilter = new ArrayList<>();

            pane = new JPanel();
            pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));


            left = new TickerTable(tickers);
            setTableSorter(left.getTable());

            right = new TickerTable(tickersToFilter);
            JTable rightTable = right.getTable();
            rightTable.setAutoCreateRowSorter(true);
            sorterRight = new TableRowSorter<>(right.getModel());
            rightTable.setRowSorter(sorterRight);
            //TODO replace global sorterLeft in newFilter with local sorter from left and right

            pane.add(left.getPane());
            pane.add(new MoveButtonPane(left.getTable(), rightTable).getPanel());
            pane.add(right.getPane());

            filterText.getDocument().addDocumentListener(
                    new DocumentListener() {
                        public void changedUpdate(DocumentEvent e) {
                            newFilter(filterText.getText());
                        }
                        @Override
                        public void insertUpdate(DocumentEvent e) {
                            newFilter(filterText.getText());
                        }
                        public void removeUpdate(DocumentEvent e) {
                            newFilter(filterText.getText());
                        }
                    });
        }

        public JPanel getPane() {
            return pane;
        }
    }

    public static class TickerSelectorDialog {
        TickerSelectorDialog() {
            JDialog dialog = new JDialog();
            dialog.setTitle("Tickers to watch");

            List<MockTicker> tickers = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                tickers.add(new MockTicker(Integer.toString(i)));
            }

            ButtonPane buttonPane = new ButtonPane();

            TickerTablesPane tickerTablesPane = new TickerTablesPane(buttonPane.getSearchField(), tickers);
            //setBorder(pane);

            dialog.setLayout(new BorderLayout());

            dialog.add(tickerTablesPane.getPane(), BorderLayout.CENTER);
            dialog.add(buttonPane.getPane(),BorderLayout.PAGE_END);

            dialog.pack();
            SwingUtilities.invokeLater(() ->
                    dialog.setVisible(true));

            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }
    }
    public static void main(String[] args) {
        new TickerSelectorDialog();
    }

    static private void newFilter(String text) {
        RowFilter<TickerTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(text);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorterLeft.setRowFilter(rf);
        sorterRight.setRowFilter(rf);
    }
    //TODO now refactor
}
