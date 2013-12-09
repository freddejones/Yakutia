package se.freddejones.game.yakutia.model.dto;

public class CreateGameDTO {

    private String gameName;
    private Long createdByPlayerId;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Long getCreatedByPlayerId() {
        return createdByPlayerId;
    }

    public void setCreatedByPlayerId(Long createdByPlayerId) {
        this.createdByPlayerId = createdByPlayerId;
    }

    @Override
    public String toString() {
        return "CreateGameDTO{" +
                "gameName='" + gameName + '\'' +
                ", createdByPlayerId=" + createdByPlayerId +
                '}';
    }

}
