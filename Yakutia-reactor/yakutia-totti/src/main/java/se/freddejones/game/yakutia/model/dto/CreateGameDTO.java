package se.freddejones.game.yakutia.model.dto;

import java.util.List;

public class CreateGameDTO {

    private String gameName;
    private Long createdByPlayerId;
    List<InvitedPlayer> invites;
//    private List<String> invites;

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

    public List<InvitedPlayer> getInvites() {
        return invites;
    }

    public void setInvites(List<InvitedPlayer> invites) {
        this.invites = invites;
    }


//    public List<String> getInvites() {
//        return invites;
//    }
//
//    public void setInvites(List<String> invites) {
//        this.invites = invites;
//    }

    @Override
    public String toString() {
        return "CreateGameDTO{" +
                "gameName='" + gameName + '\'' +
                ", createdByPlayerId=" + createdByPlayerId +
                ", invites=" + invites +
                '}';
    }
}
