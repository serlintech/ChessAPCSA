package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GameHistoryPanel extends JPanel {
    private final DataModel model;
    private final JScrollPane scrollPane;
    private final Dimension PANEL_DIM =new Dimension(100,400);
    GameHistoryPanel(){
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        final JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(PANEL_DIM);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }
    void redo(final Board board,
              final Table.MoveLog moveLog){
        int cRow = 0;
        this.model.clear();
        for(final Move move : moveLog.getMoves()){
            final String moveText = move.toString();
            if(move.getMovePiece().getPieceAlliance().isWhite()){
                this.model.setValueAt(moveText,cRow,0);
            }else if(move.getMovePiece().getPieceAlliance().isBlack()){
                this.model.setValueAt(moveText,cRow,1);
                cRow++;
            }
        }
        if(moveLog.getMoves().size() >0){
            final Move lastMove = moveLog.getMoves().get(moveLog.size()-1);
            final String moveText = lastMove.toString();

            if(lastMove.getMovePiece().getPieceAlliance().isWhite()){
                this.model.setValueAt(moveText+calcCheckAndMateHash(board),cRow,0);
            }else if(lastMove.getMovePiece().getPieceAlliance().isBlack()){
                this.model.setValueAt(moveText+calcCheckAndMateHash(board),cRow-1,1);
            }
        }
        final JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());

    }

    private String calcCheckAndMateHash(final Board board) {
        if(board.getCurrentPlayer().isInCheckMate()){
            return "#";

        }else if(board.getCurrentPlayer().isInCheck()){
            return "+";
        }
        return "";
    }

    private static class DataModel extends DefaultTableModel{
        private final List<Row> values;
        public static final String[] names= {"White","Black"};
        DataModel(){
            this.values= new ArrayList<>();
        }
        public void clear(){
            this.values.clear();
            setRowCount(0);
        }
        @Override
        public int getRowCount(){
            if(this.values==null){
                return 0;
            }
            return this.values.size();
        }
        @Override
        public int getColumnCount(){
            return names.length;
        }
        @Override
        public Object getValueAt(final int row, final int col){
            final Row currentRow=this.values.get(row);
            if(col==0){
                return currentRow.getWhiteMove();
            }
            else if(col==1){
                return currentRow.getBlackMove();
            }return null;
        }
        @Override
        public void setValueAt(final Object aValue, final int row, final int column){
            final Row cRow;
            if(this.values.size() <= row){
                cRow = new Row();
                this.values.add(cRow);
            }else{
                cRow=this.values.get(row);
            }
            if(column==0){
                cRow.setWhiteMove((String) aValue);
                fireTableRowsInserted(row,row);
            }else if(column==1){
                cRow.setBlackMove((String)aValue);
                fireTableCellUpdated(row, column);
            }
        }
        @Override
        public Class<?> getColumnClass(final int column){
            return Move.class;
        }
        @Override
        public String getColumnName(final int column){
            return names[column];
        }

    }
    private static class Row{
        private String whiteMove;
        private String blackMove;

        Row() {

        }
        public String getWhiteMove(){
            return this.whiteMove;
        }
        public String getBlackMove(){
            return this.blackMove;
        }
        public void setWhiteMove(final String move){
            this.whiteMove=move;
        }
        public void setBlackMove(final String move){
            this.blackMove=move;
        }
    }

}
