package pjv.chess.board;

import pjv.chess.pieces.ChessPiece;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile {

    int tileCoord;

    private static Map<Integer, EmptyTile> EMPTY_TILES = createAllEmptyTiles();

    private static Map<Integer, EmptyTile> createAllEmptyTiles() {

        Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for(int i = 0; i < Utils.TILE_COUNT; i++){
            emptyTileMap.put(i, new EmptyTile(i));
        }

        return emptyTileMap;
    }

    /**
     * Creates new Tile
     * @param tileCoord
     * @param piece
     * @return new Tile
     */
    public static Tile createTile(int tileCoord, ChessPiece piece){
        if(piece != null){
            return new OccupiedTile(tileCoord, piece);
        } else {
            return EMPTY_TILES.get(tileCoord);
        }
    }

    private Tile(int tileCoord) {
        this.tileCoord = tileCoord;
    }

    /**
     * Checks if Tile is empty
     * @return true/false
     */
    public abstract boolean isEmpty();

    public abstract ChessPiece getPiece();

    public int getTileCoordinates(){ return this.tileCoord;}

    /**
     * Extensions of Tile class for easier hadling
     */
    public static class EmptyTile extends Tile{

        EmptyTile(int coord) {
            super(coord);
        }

        @Override
        public boolean isEmpty(){
            return true;
        }

        @Override
        public ChessPiece getPiece(){
            return null;
        }

        @Override
        public String toString(){ return "-"; }
    }

    public static class OccupiedTile extends Tile{
        private ChessPiece onTile;

        OccupiedTile(int tileCoord, ChessPiece onTile){
            super(tileCoord);
            this.onTile = onTile;
        }

        @Override
        public boolean isEmpty(){
            return false;
        }

        @Override
        public ChessPiece getPiece(){
            return this.onTile;
        }

        @Override
        public String toString(){
            return this.onTile.getPieceAlliance() == true ? this.onTile.toString().toUpperCase() : this.onTile.toString().toLowerCase();
        }

    }

}
