package nu.danielsundberg.yakutia;


public class GameplayerId {

    private static final long serialVersionUID = 1L;

    private long gameId;
    private long playerId;

    public GameplayerId() {
    }

    public GameplayerId(long gameId, long playerId) {
        this.setGameId(gameId);
        this.setPlayerId(playerId);
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getPlayerId() {
        return playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameplayerId that = (GameplayerId) o;

        if (gameId != that.gameId) return false;
        if (playerId != that.playerId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (gameId ^ (gameId >>> 32));
        result = 31 * result + (int) (playerId ^ (playerId >>> 32));
        return result;
    }
}
